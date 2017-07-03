package nl.lcs.qrscan.ui.model;

import java.nio.file.Path;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import nl.lcs.qrscan.core.QrPdf;

/**
 * This model holds all settings regarding the manual tagging operation.
 * 
 * @author Lars Steggink
 */
public class ManualTagSettings {

	private SimpleObjectProperty<Path> pdfPath = new SimpleObjectProperty<Path>();
	private SimpleStringProperty code = new SimpleStringProperty();

	/**
	 * Sets the PDF path. This does not check the path for validity.
	 * 
	 * @param file
	 *            PDF path
	 */
	public final void setPDFPath(Path file) {
		pdfPath.set(file);
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
	 * Sets the code to use as the custom file attribute.
	 * 
	 * @param code
	 *            the code
	 * @throws IllegalArgumentException
	 *             if the provided code had invalid characters
	 */
	public final void setCode(String code) {
		if (!QrPdf.isValidQRCode(code)) {
			throw new IllegalArgumentException("Invalid characters in code.");
		}

		this.code.set(code);
	}

	/**
	 * Gets the code to use as the custom file attribute.
	 * 
	 * @return code
	 */
	public final String getCode() {
		return code.get();
	}
}
