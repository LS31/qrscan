package nl.ls31.qrscan.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

/**
 * This model holds all settings set by the user.
 *
 * @author Lars Steggink
 */
public class AppSettings {

    private final Preferences storedSettings;
    private final SimpleObjectProperty<Path> inputFile;
    private final SimpleObjectProperty<Path> outputDir;
    private final SimpleIntegerProperty imageSize;
    private final SimpleBooleanProperty withAnnotation;
    private final SimpleObjectProperty<Path> pdfPath;
    private final SimpleStringProperty code;
    private final SimpleObjectProperty<Path> inputDir;
    private final SimpleObjectProperty<Path> targetDir;
    private final SimpleIntegerProperty qrPage;
    private final SimpleBooleanProperty withRenaming;
    private final SimpleBooleanProperty useFileAttributes;
    private final SimpleBooleanProperty writeFileAttributes;
    private final SimpleBooleanProperty openLogFile;

    public AppSettings() {
        storedSettings = Preferences.userNodeForPackage(this.getClass());

        inputFile = new SimpleObjectProperty<>(Paths.get(storedSettings.get("LAST_INPUT_FILE", "")));
        outputDir = new SimpleObjectProperty<>(Paths.get(storedSettings.get("LAST_OUTPUT_DIR", "")));
        imageSize = new SimpleIntegerProperty(storedSettings.getInt("LAST_IMAGE_SIZE", 50));
        withAnnotation = new SimpleBooleanProperty(storedSettings.getBoolean("LAST_WITH_ANNOTATION", true));
        pdfPath = new SimpleObjectProperty<>(Paths.get(storedSettings.get("LAST_PDF_PATH", "")));
        code = new SimpleStringProperty(storedSettings.get("LAST_CODE", ""));
        inputDir = new SimpleObjectProperty<>(Paths.get(storedSettings.get("LAST_INPUT_DIR", "")));
        targetDir = new SimpleObjectProperty<>(Paths.get(storedSettings.get("LAST_TARGET_DIR", "")));
        qrPage = new SimpleIntegerProperty(storedSettings.getInt("LAST_QR_PAGE", 1));
        withRenaming = new SimpleBooleanProperty(storedSettings.getBoolean("LAST_WITH_RENAMING", false));
        useFileAttributes = new SimpleBooleanProperty(storedSettings.getBoolean("LAST_USE_FILE_ATTRIBUTES", true));
        writeFileAttributes = new SimpleBooleanProperty(storedSettings.getBoolean("LAST_WRITE_FILE_ATTRIBUTES", true));
        openLogFile = new SimpleBooleanProperty(storedSettings.getBoolean("LAST_OPEN_LOG_FILE", true));
    }

    /**
     * Gets image size setting (in px).
     *
     * @return image size
     */
    public final int getImageSize() {
        return imageSize.get();
    }

    /**
     * Sets the image size setting (in px).
     *
     * @param size image size
     * @throws IllegalArgumentException if size was negative or zero
     */
    public final void setImageSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size is negative or zero.");
        }
        this.imageSize.set(size);
        storedSettings.putInt("LAST_IMAGE_SIZE", size);
    }

    /**
     * Gets the input file path setting. The path may be invalid and the file may not exist.
     *
     * @return input file path
     */
    public final Path getInputFile() {
        return inputFile.get();
    }

    /**
     * Sets the input file path setting where a text file with requested QR codes can be found. This does not check the
     * validity of the path.
     *
     * @param file input file path
     */
    public final void setInputFile(Path file) {
        inputFile.set(file);
        storedSettings.put("LAST_INPUT_FILE", file.toAbsolutePath().toString());
    }

    /**
     * Gets the output directory setting. The path may be invalid and the directory may not exist.
     *
     * @return output directory path
     */
    public final Path getOutputDirectory() {
        return outputDir.get();
    }

    /**
     * Sets the output directory path setting, where QR images will be stored. This does not check the validity of the
     * path.
     *
     * @param directory directory path setting
     */
    public final void setOutputDirectory(Path directory) {
        outputDir.set(directory);
        storedSettings.put("LAST_OUTPUT_DIR", directory.toAbsolutePath().toString());
    }

    /**
     * Gets the setting whether the images should be annotated with human readable text.
     *
     * @return whether to add annotation
     */
    public final boolean getWithAnnotation() {
        return withAnnotation.get();
    }

    /**
     * Sets the setting whether the images should be annotated with human readable text.
     *
     * @param withAnnotation whether to add annotation
     */
    public final void setWithAnnotation(boolean withAnnotation) {
        this.withAnnotation.set(withAnnotation);
        storedSettings.putBoolean("LAST_WITH_ANNOTATION", withAnnotation);
    }

    /**
     * Gets the PDF path. The path may not be valid.
     *
     * @return PDF path
     */
    public final Path getPDFPath() {
        return pdfPath.get();
    }

    /**
     * Sets the PDF path. This does not check the path for validity.
     *
     * @param file PDF path
     */
    public final void setPDFPath(Path file) {
        pdfPath.set(file);
        storedSettings.put("LAST_PDF_PATH", file.toAbsolutePath().toString());
    }

    /**
     * Gets the code to use as the custom file attribute.
     *
     * @return code
     */
    public final String getCode() {
        return code.get();
    }

    /**
     * Sets the code to use as the custom file attribute.
     *
     * @param code the code
     * @throws IllegalArgumentException if the provided code had invalid characters
     */
    public final void setCode(String code) {
        if (!QrPdf.isValidQRCode(code)) {
            throw new IllegalArgumentException("Invalid characters in code.");
        }

        this.code.set(code);
        storedSettings.put("LAST_CODE", code);
    }


    /**
     * Gets the input directory path setting. The path may be invalid and the directory may not exist.
     *
     * @return input directory path
     */
    public final Path getInputDirectory() {
        return inputDir.get();
    }

    /**
     * Sets the input directory path. This does not check the path for validity.
     *
     * @param path input directory path
     */
    public final void setInputDirectory(Path path) {
        this.inputDir.set(path);
        storedSettings.put("LAST_INPUT_DIR", path.toAbsolutePath().toString());
    }

    /**
     * Gets the page number where the QR code should be, according to the user.
     *
     * @return page number
     */
    public final int getQRPage() {
        return qrPage.get();
    }

    /**
     * Sets the page number where the QR code should be, according to the user.
     *
     * @param page the page number
     * @throws IllegalArgumentException if the page number is negative or zero
     */
    public final void setQRPage(int page) {
        if (page < 1) {
            throw new IllegalArgumentException("Page is negative or zero.");
        }
        this.qrPage.set(page);
        storedSettings.putInt("LAST_QR_PAGE", page);
    }

    /**
     * Gets the target directory path setting. The path may be invalid and the directory may not exist.
     *
     * @return target directory path
     */
    public final Path getTargetDirectory() {
        return targetDir.get();
    }

    /**
     * Sets the target directory path. This does not check the path for validity.
     *
     * @param path target directory path
     */
    public final void setTargetDirectory(Path path) {
        this.targetDir.set(path);
        storedSettings.put("LAST_TARGET_DIR", path.toAbsolutePath().toString());
    }

    /**
     * Gets whether the custom file attributes should be used to detect the QR code.
     *
     * @return whether to use the custom file attributes
     */
    public final boolean getUseFileAttributes() {
        return useFileAttributes.getValue();
    }

    /**
     * Sets whether the custom file attributes should be used to detect the QR code.
     *
     * @param useFileAttributes whether to use the custom file attributes
     */
    public final void setUseFileAttributes(boolean useFileAttributes) {
        this.useFileAttributes.set(useFileAttributes);
        storedSettings.putBoolean("LAST_USE_FILE_ATTRIBUTES", useFileAttributes);
    }

    /**
     * Gets whether the PDF files should be renamed according to the found QR code.
     *
     * @return whether to rename
     */
    public final boolean getWithRenaming() {
        return withRenaming.getValue();
    }

    /**
     * Sets whether the PDF files should be renamed according to the found QR code.
     *
     * @param withRenaming whether to rename
     */
    public final void setWithRenaming(boolean withRenaming) {
        this.withRenaming.set(withRenaming);
        storedSettings.putBoolean("LAST_WITH_RENAMING", withRenaming);
    }

    /**
     * Gets whether the custom file attributes should be written when a QR code was detected.
     *
     * @return whether to write the custom file attributes
     */
    public final boolean getWriteFileAttributes() {
        return writeFileAttributes.getValue();
    }

    /**
     * Sets whether the custom file attributes should be written when a QR code was detected.
     *
     * @param writeFileAttributes whether to write the custom file attributes
     */
    public final void setWriteFileAttributes(boolean writeFileAttributes) {
        this.writeFileAttributes.set(writeFileAttributes);
        storedSettings.putBoolean("LAST_WRITE_FILE_ATTRIBUTES", writeFileAttributes);
    }

    /**
     * Gets whether the CSV log file should be opened externally at the end.
     *
     * @return whether to open the file
     */
    public final boolean getOpenLogFile() {
        return openLogFile.getValue();
    }

    /**
     * Sets whether to open the CSV log file after all operations.
     *
     * @param openLogFile whether to open the file
     */
    public final void setOpenLogFile(boolean openLogFile) {
        this.openLogFile.set(openLogFile);
        storedSettings.putBoolean("LAST_OPEN_LOG_FILE", openLogFile);
    }
}
