package nl.ls31.qrscan.core;

import com.google.zxing.WriterException;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This task imports creates GIF image files displaying QR codes (with or without an human-readable annotation).
 *
 * <p>
 * The 'codes' to be transformed into QR codes are imported from a text file, with every 'code' on a different line. To
 * prevent difficulties in handling the QR codes later on, only A-z, space, - or _ are allowed as 'code'.
 * </p>
 *
 * @author Lars Steggink
 */
public class CreateTask extends Task<List<Path>> {

    final static private String LSEP = System.lineSeparator();
    private Path inputFile;
    private Path outputDir;
    private int size;
    private boolean withText;

    /**
     * This task imports creates GIF image files displaying QR codes (with or without an human-readable annotation).
     *
     * @param inputFile text file with codes
     * @param outputDir directory for image output
     * @param size      size (height and width) of the QR code. Note that the height of the actual GIF will be larger if
     *                  annotation is requested.
     * @param withText  whether the code should be placed as regular text below the QR code
     */
    public CreateTask(Path inputFile, Path outputDir, int size, boolean withText) {
        this.inputFile = inputFile;
        this.outputDir = outputDir;
        this.size = size;
        this.withText = withText;
    }

    /**
     * Starts the task.
     *
     * @return list of created files
     */
    @Override
    protected List<Path> call() {
        updateMessage("Creating new images files for QR codes." + LSEP + "  Input file:       "
                + inputFile.getFileName().toString() + LSEP + "  Output directory: "
                + outputDir.getFileName().toString() + LSEP + "  Size (px):        " + size + LSEP
                + "  Annotation:       " + withText);

        Set<String> codeList = readQRCodesFromFile(inputFile);
        createOutputDirectory(outputDir);
        return createImages(codeList);
    }

    /**
     * Create images from a list of codes.
     *
     * @param codeList list of codes
     * @return list of image paths
     */
    private List<Path> createImages(Set<String> codeList) {
        int current = 0;
        int success = 0;
        int failed = 0;
        int illegal = 0;
        final int allCodes = codeList.size();

        List<Path> imageList = new ArrayList<>();
        for (String code : codeList) {
            updateProgress(++current, allCodes);
            if (!QrPdf.isValidQRCode(code)) {
                updateMessage("Skipped code " + code + " with illegal characters. ");
                illegal++;
                continue;
            }

            try {
                imageList.add(createImage(code, outputDir));
                success++;
            } catch (WriterException e) {
                updateMessage("!Unable to encode \"" + code + "\". ");
                failed++;
            } catch (IOException e) {
                updateMessage("!Unable to save file for code \"" + code + "\". ");
                failed++;
            }
        }

        updateMessage("Summary for " + allCodes + " codes: " + LSEP
                + "  Successful:                     " + success + " codes" + LSEP
                + "  Skipped (illegal characters):  " + illegal + " codes" + LSEP
                + "  Unable to create image:        " + failed + " codes");

        return imageList;
    }

    /**
     * Creates output directory if needed.
     *
     * @param outputDir output directory
     */
    private void createOutputDirectory(Path outputDir) {
        if (!Files.exists(outputDir)) {
            try {
                Files.createDirectory(outputDir);
                updateMessage("Output directory did not exist and has been created.");
            } catch (IOException e) {
                updateMessage("!Unable to create or use output path.");
            }
        }
    }

    /**
     * Reads the list with QR codes from the code text file.
     *
     * @param codeFile the code text file
     * @return code list
     */
    private Set<String> readQRCodesFromFile(Path codeFile) {
        Set<String> codeList = new HashSet<>();
        try {
            codeList.addAll(Files.readAllLines(codeFile));
        } catch (IOException e) {
            updateMessage("!Unable to read code file.");
        }
        if (codeList.isEmpty()) {
            updateMessage("The code file seems empty.");
        }
        return codeList;
    }

    /**
     * Creates a single image file.
     *
     * @param code      code for QR code
     * @param outputDir output directory
     * @return path to created image file
     * @throws WriterException if unable to encode the QR code to an image
     * @throws IOException     if unable to save the image file
     */
    private Path createImage(String code, Path outputDir)
            throws WriterException, IOException {
        Path imagePath = outputDir.resolve(code + ".gif");
        QrImageWriter.writeGIF(imagePath, code, size, withText);
        return imagePath;
    }
}
