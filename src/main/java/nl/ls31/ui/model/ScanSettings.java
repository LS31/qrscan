package nl.lcs.qrscan.ui.model;

import java.nio.file.Path;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * This model hold all settings regarding the scan task.
 * 
 * @author Lars Steggink
 *
 */
public class ScanSettings {

	private SimpleObjectProperty<Path> inputDir = new SimpleObjectProperty<>();
	private SimpleObjectProperty<Path> targetDir = new SimpleObjectProperty<>();
	private SimpleIntegerProperty qrPage = new SimpleIntegerProperty(1);
	private SimpleBooleanProperty withRenaming = new SimpleBooleanProperty(false);
	private SimpleBooleanProperty useFileAttributes = new SimpleBooleanProperty(true);
	private SimpleBooleanProperty writeFileAttributes = new SimpleBooleanProperty(true);
	private SimpleBooleanProperty openLogFile = new SimpleBooleanProperty(true);

	/**
	 * Gets the input directory path setting. The path may be invalid and the
	 * directory may not exist.
	 * 
	 * @return input directory path
	 */
	public final Path getInputDirectory() {
		return inputDir.get();
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
	 * Gets the target directory path setting. The path may be invalid and the
	 * directory may not exist.
	 * 
	 * @return target directory path
	 */
	public final Path getTargetDirectory() {
		return targetDir.get();
	}

	/**
	 * Gets whether the custom file attributes should be used to detect the QR
	 * code.
	 * 
	 * @return whether to use the custom file attributes
	 */
	public final boolean getUseFileAttributes() {
		return useFileAttributes.getValue().booleanValue();
	}

	/**
	 * Gets whether the PDF files should be renamed according to the found QR
	 * code.
	 * 
	 * @return whether to rename
	 */
	public final boolean getWithRenaming() {
		return withRenaming.getValue().booleanValue();
	}

	/**
	 * Gets whether the custom file attributes should be written when a QR code
	 * was detected.
	 * 
	 * @return whether to write the custom file attributes
	 */
	public final boolean getWriteFileAttributes() {
		return writeFileAttributes.getValue().booleanValue();
	}
	
	/**
	 * Gets whether the CSV log file should be opened externally at the end.
	 * 
	 *  @return whether to open the file
	 */
	public final boolean getOpenLogFile() {
		return openLogFile.getValue().booleanValue();
	}

	/**
	 * Sets the input directory path. This does not check the path for validity.
	 * 
	 * @param path
	 *            input directory path
	 */
	public final void setInputDirectory(Path path) {
		this.inputDir.set(path);
	}

	/**
	 * Sets the page number where the QR code should be, according to the user.
	 * 
	 * @param page
	 *            the page number
	 * @throws IllegalArgumentException
	 *             if the page number is negative or zero
	 */
	public final void setQRPage(int page) {
		if (page < 1) {
			throw new IllegalArgumentException("Page is negative or zero.");
		}
		this.qrPage.set(page);
	}

	/**
	 * Sets the target directory path. This does not check the path for
	 * validity.
	 * 
	 * @param path
	 *            target directory path
	 */
	public final void setTargetDirectory(Path path) {
		this.targetDir.set(path);
	}

	/**
	 * Sets whether the custom file attributes should be used to detect the QR
	 * code.
	 * 
	 * @param useFileAttributes
	 *            whether to use the custom file attributes
	 */
	public final void setUseFileAttributes(boolean useFileAttributes) {
		this.useFileAttributes.set(useFileAttributes);
	}

	/**
	 * Sets whether the PDF files should be renamed according to the found QR
	 * code.
	 * 
	 * @param withRenaming
	 *            whether to rename
	 */
	public final void setWithRenaming(boolean withRenaming) {
		this.withRenaming.set(withRenaming);
	}

	/**
	 * Sets whether the custom file attributes should be written when a QR code
	 * was detected.
	 * 
	 * @param writeFileAttributes
	 *            whether to write the custom file attributes
	 */
	public final void setWriteFileAttributes(boolean writeFileAttributes) {
		this.writeFileAttributes.set(writeFileAttributes);
	}
	
	/**
	 * Sets whether to open the CSV log file after all operations.
	 * @param openLogFile
	 */
	public final void setOpenLogFile(boolean openLogFile) {
		this.openLogFile .set(openLogFile);
	}
}
