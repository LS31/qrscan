package nl.ls31.qrscan.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Hashtable;

/**
 * Image file containing a specified QR code and (optionally) a small annotation
 * underneath.
 * 
 * @author Lars Steggink
 */
public class QrImageWriter {

	/**
	 * Write an image file containing a specified QR code and (optionally) a
	 * small annotation underneath.
	 * 
	 * @param filePath
	 *            desired save path of image file
	 * @param qrCode
	 *            QR code to be encoded into an image
	 * @param size
	 *            size (height and width) of the QR code. Note: height of the
	 *            actual GIF will be larger if annotation was requested.
	 * @param withAnnotation
	 *            whether the code should be placed as regular text below the QR
	 *            code
	 * @throws IOException
	 *             if writing operation or access to file failed
	 * @throws WriterException
	 *             if encoding QR code into image failed
	 */
	public static void writeGIF(Path filePath, String qrCode, int size, boolean withAnnotation)
			throws WriterException, IOException {
		BitMatrix bitMatrix = encode(qrCode, size);
		BufferedImage image = createImage(bitMatrix, qrCode, size, withAnnotation);
		try (OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)) {
			ImageIO.write(image, "gif", outputStream);
		}
	}

	/**
	 * Creates the QR image.
	 * 
	 * @param bitMatrix
	 *            matrix representing the QR code internally
	 * @param qrCode
	 *            QR code to be encoded into an image
	 * @param withAnnotation
	 *            whether the code should be placed as regular text below the QR
	 *            code
	 * @param width
	 *            width of the QR code. Note: height of the actual GIF will be
	 *            larger if annotation was requested.
	 * @return BufferedImage of created QR image
	 */
	private static BufferedImage createImage(BitMatrix bitMatrix, String qrCode, int width, boolean withAnnotation) {
		// Add extra height when we want an annotation
		int height = withAnnotation ? width + 10 : width;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		Graphics2D graphics = (Graphics2D) image.getGraphics();

		// First, all white.
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);

		// Then, add black to a pixel in case the byteMatrix says so
		graphics.setColor(Color.BLACK);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (bitMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}

		// Add annotation.
		if (withAnnotation) {
			graphics.drawString(qrCode, 1, height - 3);
		}

		return image;
	}

	/**
	 * Encodes a code string into a bit matrix for a QR code.
	 * 
	 * @param qrCode
	 *            code
	 * @param size
	 *            size of image
	 * @return bit matrix
	 * @throws WriterException
	 *             if encoding QR code into image failed
	 */
	private static BitMatrix encode(String qrCode, int size) throws WriterException {
		// Encoding options.
		Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hintMap.put(EncodeHintType.MARGIN, 1);

		// Encoding the QR code.
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		return qrCodeWriter.encode(qrCode, BarcodeFormat.QR_CODE, size, size, hintMap);
	}
}
