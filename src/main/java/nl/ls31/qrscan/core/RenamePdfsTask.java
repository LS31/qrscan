package nl.ls31.qrscan.core;

import nl.ls31.qrscan.model.PdfScanResult;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This task performs the thing mentioned in ScanPdfsTask. However, after all PDFs are scanned for QR codes, the PDFs are
 * then moved to the output directory and renamed.
 *
 * <p>
 * For example, if a PDF file had the QR code '001', it is now renamed to 001_1.pdf and moved to a sub directory 001 in
 * the output directory (i.e. outputDir/001/001.pdf). The suffix '_1' is to avoid collisions with renaming (e.g. two
 * files had the QR code '001'). The next file with QR code '001' will be saved as 001_2.pdf, etc, all in the same sub
 * directory /001/.
 * </p>
 *
 * <p>
 * If no QR code was found, no renaming or moving of that file is attempted.
 * </p>
 *
 * <p>
 * A CSV log file is created, including the old and new file paths.
 * </p>
 *
 * <p>
 * Empty directories in the input directory are not deleted.
 * </p>
 *
 * @author Lars Steggink
 */
public class RenamePdfsTask extends ScanPdfsTask {

    final static private String LSEP = System.lineSeparator();
    private final Path outputDir;

    /**
     * This task performs the thing mentioned in ScanPdfsTask. However, after all PDFs are scanned for QR codes, the
     * PDFs are then moved to the output directory and renamed.
     *
     * @param inputDir            input directory with PDF files
     * @param outputDir           main output directory for renamed PDF files
     * @param qrCodePage          page where QR codes are expected in each PDF
     * @param writeFileAttributes whether to write a custom file attribute after a QR code has been recognised
     * @param useFileAttributes   whether to use the custom file attribute to get stored QR codes instead of (slow)
     *                            scanning
     * @param openLogFile         whether to open the CSV log file at the end
     */
    public RenamePdfsTask(Path inputDir, Path outputDir, int qrCodePage, boolean useFileAttributes,
                          boolean writeFileAttributes, boolean openLogFile) {
        super(inputDir, qrCodePage, useFileAttributes, writeFileAttributes, openLogFile);
        this.outputDir = outputDir;
    }

    // TODO extract PdfFileRenamer (putting the logic of renaming and creating subdirs outside of this task)

    /**
     * Iterates over every file, scans for QR codes, then renames.
     *
     * <p>
     * Note: first, all files are scanned for QR codes, only then renaming starts.
     * </p>
     *
     * @return list of results
     */
    @Override
    protected List<PdfScanResult> call() {
        List<PdfScanner> pdfs = findInputFiles(inputDir);
        List<PdfScanResult> scanResults = scanInputFiles(pdfs);
        try {
            List<PdfScanResult> results = renameScanResults(scanResults);
            logResults(results, outputDir); // TODO put this outside of task
            return results;
        } catch (IOException e) {
            Logger.error("!Unable to create or use output path.");
            return scanResults;
        }
    }

    /**
     * Finds an unique filename within the main output directory.
     *
     * @param scanResult scan result
     * @return chosen path
     * @throws IOException if unable to create sub directory
     */
    private Path findTargetPath(PdfScanResult scanResult) throws IOException {
        String qr = scanResult.getQrCode();

        // Create sub directory within main output directory.
        Path subOutputDir = outputDir.resolve(qr);
        if (!Files.isDirectory(subOutputDir)) {
            Files.createDirectory(subOutputDir);
        }

        // Find a file name that is not yet taken by appending _1, _2, _3, etc.
        // With a low number of files in every directory, this is probably the
        // fastest approach.
        for (int i = 1; i <= (Files.list(subOutputDir).count() + 1); i++) {
            Path checkPath = subOutputDir.resolve(qr + "_" + i + ".pdf");
            if (Files.notExists(checkPath)) {
                return checkPath;
            }
        }
        // This should never happen. If it does, rename to current name.
        return scanResult.getInputFilePath();
    }

    /**
     * Renames the PDFs based on the scan results.
     *
     * @param scanResults the scan results
     * @return updated scan results, including the old and new file path
     * @throws IOException if unable to create or use output directory
     */
    private List<PdfScanResult> renameScanResults(List<PdfScanResult> scanResults) throws IOException {
        int fileCount = scanResults.size();
        int success = 0;
        int failed = 0;
        int noQR = 0;
        int current = 0;
        Logger.info("Renaming starts now." + LSEP + "  Output directory: " + outputDir.getFileName());
        updateProgress(current, fileCount);

        // Create output directory.
        if (!Files.exists(outputDir)) {
            Files.createDirectory(outputDir);
            Logger.error("Output directory did not exist and has been created.");
        }

        // Iterate over every scanned file.
        for (PdfScanResult scanResult : scanResults) {
            current++;
            updateProgress(current, fileCount);

            if (!scanResult.isQRCodeFound()) {
                // Skip this file.
                noQR++;
                continue;
            }

            // Find suitable renamed path and file name.
            Path outputPath = findTargetPath(scanResult);
            try {
                Path resultPath = Files.move(scanResult.getInputFilePath(), outputPath);
                scanResult.setOutputFilePath(resultPath);
                success++;
            } catch (IOException e) {
                // Exception raised during move.
                Logger.error(e, "!Unable to rename " + scanResult.getInputFilePath().getFileName() + ".");
                failed++;
            }
        }

        String summaryMessage = "Summary: tried renaming " + fileCount + " files, " + success + " successful, " + failed
                + " unsuccessful, " + noQR + " not attempted (unable to find QR code).";
        Logger.info(summaryMessage);
        updateMessage(summaryMessage);
        return scanResults;
    }
}
