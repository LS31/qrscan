package nl.ls31.qrscan.core;

import nl.ls31.qrscan.model.PdfScanResult;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This class provides a way to write a CSV file with a log of the scanned or renamed PDF files with QR codes.
 * <p>
 * TODO Migrate to Apache Open CSV implementation
 *
 * @author Lars Steggink
 */
public class CsvLogWriter {

    final static String SEP = ",";
    final static String QUOTE = "\"";
    final static String LSEP = System.lineSeparator();

    /**
     * Writes a CSV file logging the results of PDF scanning and renaming.
     *
     * @param results     results of scanning and renaming
     * @param logFilePath path where the CSV file should be created
     * @throws IOException if unable to save the log file
     */
    public static void writeLogFile(List<PdfScanResult> results, Path logFilePath) throws IOException {
        String logContent = createLogContent(results);
        try (BufferedWriter logOut = Files.newBufferedWriter(logFilePath)) {
            logOut.write(logContent);
        }
    }

    /**
     * Creates log content.
     *
     * @param results results of scanning and renaming
     * @return log contents
     */
    private static String createLogContent(List<PdfScanResult> results) {
        StringBuilder csvBuffer = new StringBuilder();

        // CSV header
        csvBuffer.append("InputPath" + SEP + "RenamedPath" + SEP + "FileCreated" + SEP + "PageCount" + SEP + "QRCodeFound" + SEP + "QRCodePage" + SEP + "QRcode").append(LSEP);

        // CSV content: one line for every result
        for (PdfScanResult result : results) {
            csvBuffer.append(QUOTE).append(result.getInputFilePath().toAbsolutePath().toString()).append(QUOTE).append(SEP);
            if (result.isFileRenamed()) {
                csvBuffer.append(QUOTE).append(result.getOutputFilePath().toAbsolutePath().toString()).append(QUOTE).append(SEP);
            } else {
                csvBuffer.append(SEP);
            }
            csvBuffer.append(QUOTE).append(result.getFileCreationTime()).append(QUOTE).append(SEP);
            csvBuffer.append(result.getPageCount()).append(SEP);
            csvBuffer.append(QUOTE).append(result.getQrCodeScanStatus().toString()).append(QUOTE).append(SEP);
            csvBuffer.append(result.getQrCodePage()).append(SEP);
            csvBuffer.append(QUOTE).append(result.getQrCode()).append(QUOTE).append(LSEP);
        }

        return csvBuffer.toString();
    }
}
