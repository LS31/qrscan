package nl.ls31.qrscan.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This class provides a way to write a CSV file with a log of the scanned or
 * renamed PDF files with QR codes.
 * 
 * @author Lars Steggink
 */
public class CsvLogWriter {

	final static String SEP = ",";
	final static String QUOTE = "\"";
	final static String LSEP = System.lineSeparator();

	/**
	 * Writes a CSV file logging the results of PDF scanning and renaming.
	 * 
	 * @param results
	 *            results of scanning and renaming
	 * @param logFilePath
	 *            path where the CSV file should be created
	 * @throws IOException
	 *             if unable to save the log file
	 */
	public static void writeLogFile(List<SingleResult> results, Path logFilePath) throws IOException {
		String logContent = createLogContent(results);
		try (BufferedWriter logOut = Files.newBufferedWriter(logFilePath)){
			logOut.write(logContent);
		}
	}

	/**
	 * Creates log content.
	 * 
	 * @param results
	 *            results of scanning and renaming
	 * @return log contents
	 */
	private static String createLogContent(List<SingleResult> results) {
		StringBuilder csvBuffer = new StringBuilder();
				
		// CSV header
		csvBuffer.append("InputPath" + SEP 
				+ "RenamedPath" + SEP
				+ "FileCreated" + SEP 
				+ "PageCount" + SEP
				+ "QRCodeFound" + SEP 
				+ "QRCodePage" + SEP
				+ "QRcode" + LSEP);

		// CSV content: one line for every result
		for (SingleResult result : results) {
			csvBuffer.append(QUOTE + result.getInputFilePath().toAbsolutePath().toString() + QUOTE + SEP);
			if (result.isFileRenamed()) {
				csvBuffer.append(QUOTE + result.getOutputFilePath().toAbsolutePath().toString() + QUOTE + SEP);
			} else {
				csvBuffer.append(SEP);
			}
			csvBuffer.append(QUOTE + result.getFileCreationTime() + QUOTE + SEP);
			csvBuffer.append(result.getPageCount() + SEP);
			csvBuffer.append(QUOTE + result.getQrCodeScanStatus().toString() + QUOTE + SEP);
			csvBuffer.append(result.getQrCodePage() + SEP);
			csvBuffer.append(QUOTE + result.getQrCode() + QUOTE + LSEP);
		}
		
		return csvBuffer.toString();
	}
}
