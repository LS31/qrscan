package nl.ls31.qrscan.ui.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

/**
 * This model holds all settings regarding the creation of new QR images.
 *
 * @author Lars Steggink
 */
public class CreateSettings {

    private final Preferences storedPreferences;
    private final SimpleObjectProperty<Path> inputFile;
    private final SimpleObjectProperty<Path> outputDir;
    private final SimpleIntegerProperty imageSize;
    private final SimpleBooleanProperty withAnnotation;

    public CreateSettings() {
        storedPreferences = Preferences.userNodeForPackage(this.getClass());

        inputFile = new SimpleObjectProperty<>(Paths.get(storedPreferences.get("LAST_INPUT_FILE", "")));
        outputDir = new SimpleObjectProperty<>(Paths.get(storedPreferences.get("LAST_OUTPUT_DIR", "")));
        imageSize = new SimpleIntegerProperty(storedPreferences.getInt("LAST_IMAGE_SIZE", 50));
        withAnnotation = new SimpleBooleanProperty(storedPreferences.getBoolean("LAST_WITH_ANNOTATION", true));
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
        storedPreferences.putInt("LAST_IMAGE_SIZE", size);
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
        storedPreferences.put("LAST_INPUT_FILE", file.toAbsolutePath().toString());
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
        storedPreferences.put("LAST_OUTPUT_DIR", directory.toAbsolutePath().toString());
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
        storedPreferences.putBoolean("LAST_WITH_ANNOTATION", withAnnotation);
    }
}
