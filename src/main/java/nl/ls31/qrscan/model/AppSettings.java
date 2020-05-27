package nl.ls31.qrscan.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

/**
 * This model holds all settings set by the user in the application. Last used values are stored for future reference.
 *
 * @author Lars Steggink
 */
public class AppSettings {

    private final Preferences storedSettings;
    private final SimpleObjectProperty<Path> codesInputFile;
    private final SimpleObjectProperty<Path> qrcodeImageOutputDirectory;
    private final SimpleIntegerProperty qrcodeImageSize;
    private final SimpleBooleanProperty qrcodeImageWithAnnotation;
    private final SimpleObjectProperty<Path> manualPdf;
    private final SimpleStringProperty manualCode;
    private final SimpleObjectProperty<Path> pdfInputDirectory;
    private final SimpleObjectProperty<Path> pdfTargetDirectory;
    private final SimpleIntegerProperty searchAtPage;
    private final SimpleBooleanProperty withFileRenaming;
    private final SimpleBooleanProperty useFileAttribute;
    private final SimpleBooleanProperty writeFileAttribute;
    private final SimpleBooleanProperty openLogFile;

    public AppSettings() {
        storedSettings = Preferences.userNodeForPackage(this.getClass());

        codesInputFile = new SimpleObjectProperty<>(Paths.get(storedSettings.get("CODES_INPUT_FILE", "")));
        qrcodeImageOutputDirectory = new SimpleObjectProperty<>(Paths.get(storedSettings.get("QRCODE_IMAGE_OUTPUT_DIR", "")));
        qrcodeImageSize = new SimpleIntegerProperty(storedSettings.getInt("QRCODE_IMAGE_SIZE", 50));
        qrcodeImageWithAnnotation = new SimpleBooleanProperty(storedSettings.getBoolean("QRCODE_IMAGE_WITH_ANNOTATION", true));
        manualPdf = new SimpleObjectProperty<>(Paths.get(storedSettings.get("MANUAL_PDF", "")));
        manualCode = new SimpleStringProperty(storedSettings.get("MANUAL_CODE", ""));
        pdfInputDirectory = new SimpleObjectProperty<>(Paths.get(storedSettings.get("PDF_INPUT_DIRECTORY", "")));
        pdfTargetDirectory = new SimpleObjectProperty<>(Paths.get(storedSettings.get("PDF_TARGET_DIRECTORY", "")));
        searchAtPage = new SimpleIntegerProperty(storedSettings.getInt("SEARCH_AT_PAGE", 1));
        withFileRenaming = new SimpleBooleanProperty(storedSettings.getBoolean("WITH_FILE_RENAMING", false));
        useFileAttribute = new SimpleBooleanProperty(storedSettings.getBoolean("USE_FILE_ATTRIBUTE", true));
        writeFileAttribute = new SimpleBooleanProperty(storedSettings.getBoolean("WRITE_FILE_ATTRIBUTE", true));
        openLogFile = new SimpleBooleanProperty(storedSettings.getBoolean("OPEN_LOG_FILE", false));
    }

    /**
     * Gets the setting for image size (px) for the creation of new GIFs with QR codes.
     *
     * @return image size (px)
     */
    public final int getQrcodeImageSize() {
        return qrcodeImageSize.get();
    }

    /**
     * Sets the setting for image size (px) for the creation of new GIFs with QR codes.
     *
     * @param size image size (px)
     * @throws IllegalArgumentException if size was negative or zero
     */
    public final void setQrcodeImageSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size is negative or zero.");
        }
        this.qrcodeImageSize.set(size);
        storedSettings.putInt("QRCODE_IMAGE_SIZE", size);
    }

    /**
     * Gets the input file path setting where a text file with requested QR codes can be found. This does not check the
     * * validity of the path.
     *
     * @return input file path
     */
    public final Path getCodesInputFile() {
        return codesInputFile.get();
    }

    /**
     * Sets the input file path setting where a text file with requested QR codes can be found. This does not check the
     * validity of the path.
     *
     * @param file input file path
     */
    public final void setCodesInputFile(Path file) {
        codesInputFile.set(file);
        storedSettings.put("CODES_INPUT_FILE", file.toAbsolutePath().toString());
    }

    /**
     * Gets the setting for the output directory where the GIFs with QR codes will be saved. This does not check the validity of the
     * path.
     *
     * @return output directory path
     */
    public final Path getQrcodeImageOutputDirectory() {
        return qrcodeImageOutputDirectory.get();
    }

    /**
     * Sets the setting for the output directory where the GIFs with QR codes will be saved. This does not check the validity of the
     * path.
     *
     * @param directory output directory path
     */
    public final void setQrcodeImageOutputDirectory(Path directory) {
        qrcodeImageOutputDirectory.set(directory);
        storedSettings.put("QRCODE_IMAGE_OUTPUT_DIR", directory.toAbsolutePath().toString());
    }

    /**
     * Gets the setting whether the images should be annotated with human readable text.
     *
     * @return whether to add annotation
     */
    public final boolean getQrcodeImageWithAnnotation() {
        return qrcodeImageWithAnnotation.get();
    }

    /**
     * Sets the setting whether the images should be annotated with human readable text.
     *
     * @param withAnnotation whether to add annotation
     */
    public final void setQrcodeImageWithAnnotation(boolean withAnnotation) {
        this.qrcodeImageWithAnnotation.set(withAnnotation);
        storedSettings.putBoolean("QRCODE_IMAGE_WITH_ANNOTATION", withAnnotation);
    }

    /**
     * Gets the path to the PDF that will be manually tagged with a custom file attribute. This does not check the path for validity.
     *
     * @return PDF path
     */
    public final Path getManualPdf() {
        return manualPdf.get();
    }

    /**
     * Sets the path to the PDF that will be manually tagged with a custom file attribute. This does not check the path for validity.
     *
     * @param file PDF path
     */
    public final void setManualPdf(Path file) {
        manualPdf.set(file);
        storedSettings.put("MANUAL_PDF", file.toAbsolutePath().toString());
    }

    /**
     * Gets the code to use as the custom file attribute.
     *
     * @return code
     */
    public final String getManualCode() {
        return manualCode.get();
    }

    /**
     * Sets the code to use as the custom file attribute.
     *
     * @param code the code
     * @throws IllegalArgumentException if the provided code had invalid characters
     */
    public final void setManualCode(String code) {
        if (!QrPdf.isValidQRCode(code)) {
            throw new IllegalArgumentException("Invalid characters in code.");
        }

        this.manualCode.set(code);
        storedSettings.put("MANUAL_CODE", code);
    }


    /**
     * Gets the input directory path for scanning and/or renaming of PDFs. This does not check the path for validity.
     *
     * @return input directory path
     */
    public final Path getInputDirectory() {
        return pdfInputDirectory.get();
    }

    /**
     * Sets the input directory path for scanning and/or renaming of PDFs. This does not check the path for validity.
     *
     * @param directory input directory path
     */
    public final void setInputDirectory(Path directory) {
        this.pdfInputDirectory.set(directory);
        storedSettings.put("PDF_INPUT_DIRECTORY", directory.toAbsolutePath().toString());
    }

    /**
     * Gets the page number where the QR code should be searched for, according to the user.
     *
     * @return page number
     */
    public final int getQRPage() {
        return searchAtPage.get();
    }

    /**
     * Sets the page number where the QR code should be searched for, according to the user.
     *
     * @param page the page number
     * @throws IllegalArgumentException if the page number is negative or zero
     */
    public final void setQRPage(int page) {
        if (page < 1) {
            throw new IllegalArgumentException("Page is negative or zero.");
        }
        this.searchAtPage.set(page);
        storedSettings.putInt("SEARCH_AT_PAGE", page);
    }

    /**
     * Gets the target directory path for renamed PDFs. This does not check the path for validity.
     *
     * @return target directory path
     */
    public final Path getTargetDirectory() {
        return pdfTargetDirectory.get();
    }

    /**
     * Sets the target directory path for renamed PDFs. This does not check the path for validity.
     *
     * @param directory target directory path
     */
    public final void setTargetDirectory(Path directory) {
        this.pdfTargetDirectory.set(directory);
        storedSettings.put("PDF_TARGET_DIRECTORY", directory.toAbsolutePath().toString());
    }

    /**
     * Gets whether the custom file attributes should be used to detect a (QR) code.
     *
     * @return whether to use the custom file attributes
     */
    public final boolean getUseFileAttribute() {
        return useFileAttribute.getValue();
    }

    /**
     * Sets whether the custom file attributes should be used to detect a (QR) code.
     *
     * @param useFileAttribute whether to use the custom file attributes
     */
    public final void setUseFileAttribute(boolean useFileAttribute) {
        this.useFileAttribute.set(useFileAttribute);
        storedSettings.putBoolean("USE_FILE_ATTRIBUTE", useFileAttribute);
    }

    /**
     * Gets whether the PDF files should be renamed according to the found QR code.
     *
     * @return whether to rename
     */
    public final boolean getWithFileRenaming() {
        return withFileRenaming.getValue();
    }

    /**
     * Sets whether the PDF files should be renamed according to the found QR code.
     *
     * @param withFileRenaming whether to rename
     */
    public final void setWithFileRenaming(boolean withFileRenaming) {
        this.withFileRenaming.set(withFileRenaming);
        storedSettings.putBoolean("WITH_FILE_RENAMING", withFileRenaming);
    }

    /**
     * Gets whether the custom file attributes should be written when a QR code was detected.
     *
     * @return whether to write the custom file attributes
     */
    public final boolean getWriteFileAttribute() {
        return writeFileAttribute.getValue();
    }

    /**
     * Sets whether the custom file attributes should be written when a QR code was detected.
     *
     * @param writeFileAttribute whether to write the custom file attributes
     */
    public final void setWriteFileAttribute(boolean writeFileAttribute) {
        this.writeFileAttribute.set(writeFileAttribute);
        storedSettings.putBoolean("WRITE_FILE_ATTRIBUTE", writeFileAttribute);
    }

    /**
     * Gets whether the CSV log file should be opened externally at the end of scanning and/or renaming.
     *
     * @return whether to open the file
     */
    public final boolean getOpenLogFile() {
        return openLogFile.getValue();
    }

    /**
     * Sets whether to open the CSV log file at the end of scanning and/or renaming.
     *
     * @param openLogFile whether to open the file
     */
    public final void setOpenLogFile(boolean openLogFile) {
        this.openLogFile.set(openLogFile);
        storedSettings.putBoolean("OPEN_LOG_FILE", openLogFile);
    }
}
