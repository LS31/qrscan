package nl.lcs.qrscan.ui.view;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import nl.lcs.qrscan.core.RenameTask;
import nl.lcs.qrscan.core.ScanTask;
import nl.lcs.qrscan.core.SingleResult;
import nl.lcs.qrscan.ui.MainApp;
import nl.lcs.qrscan.ui.model.ScanSettings;

/**
 * Controller for the main function of the program: scanning PDF files for QR
 * codes and rename them accordingly.
 * 
 * @author Lars Steggink
 *
 */
public class ScanController {

	private MainApp mainApp;
	@FXML
	private TextField inputDirTextField;
	@FXML
	private Button inputDirButton;
	@FXML
	private Button scanButton;
	@FXML
	private Spinner<Integer> qrPageSpinner;
	@FXML
	private CheckBox useFileAttributeCheckBox;
	@FXML
	private CheckBox writeFileAttributeCheckBox;
	@FXML
	private CheckBox openLogFileCheckBox;
	@FXML
	private CheckBox renameCheckBox;
	@FXML
	private TextField targetDirTextField;
	@FXML
	private Button targetDirButton;
	@FXML
	private Label targetDirLabel;

	/**
	 * Sets a call back reference to the main application.
	 * 
	 * @param mainApp main application
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Handles clicks to the input directory button by showing a file open
	 * dialog.
	 */
	@FXML
	private void handleInputDirButton() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select input directory");
		File dir = dirChooser.showDialog(mainApp.getPrimaryStage());
		if (dir != null) {
			// Change the text field and update the model.
			inputDirTextField.setText(dir.toPath().toAbsolutePath().toString());
			mainApp.getScanSettings().setInputDirectory(dir.toPath());
		}
	}

	/**
	 * Handles clicks to the target directory button by showing a file open
	 * dialog.
	 */
	@FXML
	private void handleTargetDirButton() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select target directory");
		File dir = dirChooser.showDialog(mainApp.getPrimaryStage());
		if (dir != null) {
			// Change the text field and update the model.
			targetDirTextField.setText(dir.toPath().toAbsolutePath().toString());
			mainApp.getScanSettings().setTargetDirectory(dir.toPath());
		}
	}

	/**
	 * Handles clicks to the check box regarding usage of existing file
	 * attributes with the QR code.
	 */
	@FXML
	private void handleUseFileAttributeCheckBox() {
		mainApp.getScanSettings().setUseFileAttributes(useFileAttributeCheckBox.isSelected());
	}

	/**
	 * Handles clicks to the check box regarding writing new file attributes
	 * when a QR code is recognised upon scanning.
	 */
	@FXML
	private void handleWriteFileAttributeCheckBox() {
		mainApp.getScanSettings().setWriteFileAttributes(writeFileAttributeCheckBox.isSelected());
	}
	
	/**
	 * Handles clicks to the check box regarding opening the log file.
	 */
	@FXML
	private void handleOpenLogFileCheckBox() {
		mainApp.getScanSettings().setOpenLogFile(openLogFileCheckBox.isSelected());
	}

	/**
	 * Handles clicks to the check box regarding renaming of the PDF files after
	 * QR codes have been extracted.
	 */
	@FXML
	private void handleRenameCheckBox() {
		boolean doRename = renameCheckBox.isSelected();
		mainApp.getScanSettings().setWithRenaming(doRename);
		targetDirLabel.setDisable(!doRename);
		targetDirTextField.setDisable(!doRename);
		targetDirButton.setDisable(!doRename);
		if (doRename) {
			scanButton.setText("Scan & rename");
		} else {
			scanButton.setText("Scan");
		}
	}

	/**
	 * Handles clicks to the scan button (or scan+rename button) by starting up
	 * an extra thread for processing.
	 */
	@FXML
	private void handleScanButton() {
		ScanSettings settings = mainApp.getScanSettings();

		// Spinner has no nice listener, update value first.
		settings.setQRPage(qrPageSpinner.getValue().intValue());

		Path inputDir = settings.getInputDirectory();
		int qrPage = settings.getQRPage();
		boolean useFileAttributes = settings.getUseFileAttributes();
		boolean writeFileAttributes = settings.getWriteFileAttributes();
		boolean openLogFile = settings.getOpenLogFile();

		Task<List<SingleResult>> task;
		if (settings.getWithRenaming()) {
			Path targetDir = settings.getTargetDirectory();
			task = new RenameTask(inputDir, targetDir, qrPage, useFileAttributes, writeFileAttributes, openLogFile);
		} else {
			task = new ScanTask(inputDir, qrPage, useFileAttributes, writeFileAttributes, openLogFile);
		}
		// Messages are passed into the log.
		task.messageProperty().addListener((observable, oldValue, newValue) -> mainApp.log(newValue));
		new Thread(task).start();
	}
}
