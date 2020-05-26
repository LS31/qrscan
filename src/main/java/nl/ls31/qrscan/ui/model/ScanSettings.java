package nl.ls31.qrscan.ui.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

/**
 * This model hold all settings regarding the scan task.
 *
 * @author Lars Steggink
 */
public class ScanSettings {

    private final Preferences storedPreferences;
    private final SimpleObjectProperty<Path> inputDir;
    private final SimpleObjectProperty<Path> targetDir;
    private final SimpleIntegerProperty qrPage;
    private final SimpleBooleanProperty withRenaming;
    private final SimpleBooleanProperty useFileAttributes;
    private final SimpleBooleanProperty writeFileAttributes;
    private final SimpleBooleanProperty openLogFile;

    public ScanSettings() {
        storedPreferences = Preferences.userNodeForPackage(this.getClass());

        inputDir = new SimpleObjectProperty<>(Paths.get(storedPreferences.get("LAST_INPUT_DIR", "")));
        targetDir = new SimpleObjectProperty<>(Paths.get(storedPreferences.get("LAST_TARGET_DIR", "")));
        qrPage = new SimpleIntegerProperty(storedPreferences.getInt("LAST_QR_PAGE", 1));
        withRenaming = new SimpleBooleanProperty(storedPreferences.getBoolean("LAST_WITH_RENAMING", false));
        useFileAttributes = new SimpleBooleanProperty(storedPreferences.getBoolean("LAST_USE_FILE_ATTRIBUTES", true));
        writeFileAttributes = new SimpleBooleanProperty(storedPreferences.getBoolean("LAST_WRITE_FILE_ATTRIBUTES", true));
        openLogFile = new SimpleBooleanProperty(storedPreferences.getBoolean("LAST_OPEN_LOG_FILE", true));
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
        storedPreferences.put("LAST_INPUT_DIR", path.toAbsolutePath().toString());
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
        storedPreferences.putInt("LAST_QR_PAGE", page);
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
        storedPreferences.put("LAST_TARGET_DIR", path.toAbsolutePath().toString());
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
        storedPreferences.putBoolean("LAST_USE_FILE_ATTRIBUTES", useFileAttributes);
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
        storedPreferences.putBoolean("LAST_WITH_RENAMING", withRenaming);
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
        storedPreferences.putBoolean("LAST_WRITE_FILE_ATTRIBUTES", writeFileAttributes);
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
        storedPreferences.putBoolean("LAST_OPEN_LOG_FILE", openLogFile);
    }
}
