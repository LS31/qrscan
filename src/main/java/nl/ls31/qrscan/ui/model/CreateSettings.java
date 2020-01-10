package nl.ls31.qrscan.ui.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.nio.file.Path;

/**
 * This model holds all settings regarding the creation of new QR images.
 *
 * @author Lars Steggink
 */
public class CreateSettings {

    private SimpleObjectProperty<Path> inputFile = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Path> outputDir = new SimpleObjectProperty<>();
    private SimpleIntegerProperty imageSize = new SimpleIntegerProperty(50);
    private SimpleBooleanProperty withAnnotation = new SimpleBooleanProperty(true);

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
    }
}
