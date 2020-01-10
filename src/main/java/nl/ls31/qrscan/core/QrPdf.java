package nl.ls31.qrscan.core;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

/**
 * PDF file with a QR code.
 *
 * @author Lars Steggink
 */
public class QrPdf {
    /**
     * This custom file attribute is used to add the QR code to the PDF file meta data.
     */
    final static public String FILE_ATTRIBUTE = "custom.qrcode";
    private Path docPath;
    private Map<Integer, String> qrCodeMap;

    /**
     * PDF file containing a QR code.
     *
     * @param docPath Path of the document.
     */
    public QrPdf(Path docPath) {
        this.docPath = docPath;
        this.qrCodeMap = new HashMap<>();
    }

    /**
     * Checks if a string is a valid QR code.
     *
     * <p>
     * A valid QR code is A-Z, a-z, 0-9, space, dash (-) or underscore (_). This strict pattern is used to avoid trouble
     * with file names etc. Although spaces are allowed, be cautious, as a starting or trailing space may cause
     * trouble.
     * </p>
     *
     * @param code code to check
     * @return whether the code is a valid QR code
     */
    public static boolean isValidQRCode(String code) {
        return code.matches("^[\\w\\- ]+");
    }

    /**
     * Gets creation time of the QR PDF (if this attribute is supported by the file system).
     *
     * @return FileTime attribute of creation.
     * @throws IOException Unable to determine date/time.
     */
    public FileTime getCreationTime() throws IOException {
        return Files.readAttributes(docPath, BasicFileAttributes.class).creationTime();
    }

    /**
     * Gets the number of pages.
     *
     * @return number of pages
     * @throws IOException if unable to determine number of pages
     */
    public int getNumberOfPages() throws IOException {
        PDDocument pdfDoc = PDDocument.load(docPath.toFile());
        int numberOfPages = pdfDoc.getNumberOfPages();
        pdfDoc.close();
        return numberOfPages;
    }

    /**
     * Renders a page.
     *
     * @param pageIndex page to render
     * @param dpi       renderer DPI
     * @return render of the page
     * @throws IOException if failed to read the file
     */
    private BufferedImage getPageImage(int pageIndex, int dpi) throws IOException {
        PDDocument pdfDoc = PDDocument.load(docPath.toFile());
        PDFRenderer renderer = new PDFRenderer(pdfDoc);
        BufferedImage image = renderer.renderImageWithDPI(pageIndex - 1, dpi, ImageType.BINARY);
        pdfDoc.close();
        return image;
    }

    /**
     * Gets the path
     * <p>
     * Note: the document may be renamed and the path invalid.
     *
     * @return document path
     */
    public Path getPath() {
        return docPath;
    }

    /**
     * Extracts and decodes the QR code from the specified page of the PDF file.
     *
     * <p>
     * You may chose to use the file attribute <i>custom.qrcode</i> as if it was the actual QR code in the file. Only
     * use this if you trust the file attribute.
     * </p>
     *
     * <p>
     * Default return order, to optimise speed:
     * </p>
     *
     * <ol>
     * <li>check if QR code was already scanned/found in the current run
     * (unlikely, but fast)</li>
     * <li>check the file attribute (if enabled, fast),</li>
     * <li>scan the PDF file itself (slow).</li>
     * </ol>
     *
     *
     * <p>
     * Note that if <i>writeFileAttributes</i> is true, the file attribute is
     * only updated if the QR code was found using 'true' scanning (i.e. not
     * through prior file attributes). You can force renewal of file attributes
     * by disabling <i>useQRCodeFileAttribute</i> and enabling
     * <i>writeQRCodeFileAttribute</i>.
     * </p>
     *
     * @param page                   page where QR code is placed (starting at 1)
     * @param useQRCodeFileAttribute whether to use the file attribute to find an associated QR code
     * @param writeFileAttributes    whether to write the custom file attribute to the PDF file if a QR code was found
     *                               while scanning. If the file already has a custom file attribute, it is updated. If
     *                               the file attribute could not be written, it fails silently.
     * @return QR code that was extracted
     * @throws IOException       if reading file failed or no such page
     * @throws NotFoundException if QR code recognition failed
     */
    public String getQRCode(int page, boolean useQRCodeFileAttribute, boolean writeFileAttributes)
            throws IOException, NotFoundException {
        // Use a stored value in available for speed.
        if (qrCodeMap.containsKey(page)) {
            return qrCodeMap.get(page);
        }

        // Use an available file attribute for speed.
        if (useQRCodeFileAttribute && hasQRCodeFileAttribute()) {
            return getQRCodeFileAttribute();
        }

        if (page > getNumberOfPages()) {
            throw new IOException("Page does not exist!");
        }
        // No quick solutions, so lets scan!
        String qrCode = scanQRCode(page);
        qrCodeMap.put(page, qrCode);

        if (writeFileAttributes) {
            // QR code was found through scanning directly, or after the current
            // File Attribute proved invalid, so lets try to update
            try {
                setQRCodeFileAttribute(qrCode);
            } catch (Exception e) {
                // Don't care that much.
            }
        }
        return qrCode;
    }

    /**
     * Gets the QR code from the file attribute.
     *
     * <p>
     * Note: only use this for operations if you trust that no incorrect QR codes are stored as file attributes.
     * </p>
     *
     * @return QR code
     * @throws IOException if file not found, QR code not in an attribute, or illegal QR code.
     */
    public String getQRCodeFileAttribute() throws IOException {
        UserDefinedFileAttributeView view = Files.getFileAttributeView(docPath, UserDefinedFileAttributeView.class);
        if (!view.list().contains(FILE_ATTRIBUTE)) {
            throw new IOException("No QR code in file attributes.");
        }
        ByteBuffer buf = ByteBuffer.allocate(view.size(FILE_ATTRIBUTE));
        view.read(FILE_ATTRIBUTE, buf);
        buf.flip();
        String value = Charset.defaultCharset().decode(buf).toString();
        if (QrPdf.isValidQRCode(value)) {
            return value;
        } else {
            throw new IOException("Invalid QR code in file attribute.");
        }
    }

    /**
     * Sets the QR code for the file attribute.
     *
     * @param code code to store as file attribute
     * @throws IOException              if file attribute could not be saved
     * @throws IllegalArgumentException if code contain illegal characters
     */
    public void setQRCodeFileAttribute(String code) throws IOException, IllegalArgumentException {
        if (!QrPdf.isValidQRCode(code)) {
            throw new IllegalArgumentException("Illegal characters in QR code.");
        } else {
            UserDefinedFileAttributeView view = Files.getFileAttributeView(docPath, UserDefinedFileAttributeView.class);
            view.write(FILE_ATTRIBUTE, Charset.defaultCharset().encode(code));
        }
    }

    /**
     * Checks whether this PDF has a (valid) QR code in the custom file attribute.
     *
     * @return whether this PDF has a QR code in the custom file attribute
     */
    public boolean hasQRCodeFileAttribute() {
        try {
            getQRCodeFileAttribute();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Scans for and decodes QR code from page render image.
     *
     * @param pageIndex page to render
     * @return QR code that was decoded.
     * @throws NotFoundException if QR code recognition failed
     * @throws IOException       if reading file failed
     */
    private String scanQRCode(int pageIndex) throws NotFoundException, IOException {
        // Hints for scanning
        Vector<BarcodeFormat> decodeFormat = new Vector<>();
        decodeFormat.add(BarcodeFormat.QR_CODE);
        Hashtable<DecodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(DecodeHintType.TRY_HARDER, true);
        hintMap.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormat);
        MultiFormatReader qrcodeReader = new MultiFormatReader();
        qrcodeReader.setHints(hintMap);

        // We try for several images of the PDF page at several DPI settings,
        // starting at the lowest setting, this might help for speed...
        int[] dpiSettings = {150, 200, 250, 300};
        for (int i = 0; i < dpiSettings.length; i++) {
            try {
                // Try lowest DPI first.
                BufferedImage pageImage = getPageImage(pageIndex, dpiSettings[i]);
                LuminanceSource source = new BufferedImageLuminanceSource(pageImage);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                // By using decodeWithState, we keep the Hints that we set earlier.
                Result result = qrcodeReader.decodeWithState(bitmap);
                return result.getText();
            } catch (NotFoundException e) {
                // Attempt failed. Try next resolution.
                // What if this fails again and again?
                // A NotFoundException is thrown.
                if (i == dpiSettings.length - 1) {
                    throw e;
                }
            }
        }
        // This should never happen, ever...
        return null;
    }
}
