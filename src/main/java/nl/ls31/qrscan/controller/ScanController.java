package nl.ls31.qrscan.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import nl.ls31.qrscan.MainApp;
import nl.ls31.qrscan.core.RenameTask;
import nl.ls31.qrscan.core.ScanTask;
import nl.ls31.qrscan.model.AppSettings;
import nl.ls31.qrscan.model.SingleResult;
import nl.ls31.qrscan.view.ProgressDialog;
import nl.ls31.qrscan.view.ResultsDialog;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * Controller for the main function of the program: scanning PDF files for QR codes and rename them accordingly.
 *
 * @author Lars Steggink
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
     * Update all control states using the model as reference.
     */
    public void updateControlsByModel() {
        inputDirTextField.setText(mainApp.getAppSettings().getInputDirectory().toAbsolutePath().toString());
        targetDirTextField.setText(mainApp.getAppSettings().getTargetDirectory().toAbsolutePath().toString());
        useFileAttributeCheckBox.setSelected(mainApp.getAppSettings().getUseFileAttribute());
        writeFileAttributeCheckBox.setSelected(mainApp.getAppSettings().getWriteFileAttribute());
        renameCheckBox.setSelected(mainApp.getAppSettings().getWithFileRenaming());
        toggleRenaming();
        openLogFileCheckBox.setSelected(mainApp.getAppSettings().getOpenLogFile());
        qrPageSpinner.getValueFactory().setValue(mainApp.getAppSettings().getQRPage());
    }

    /**
     * Handles clicks to the input directory button by showing a file open dialog.
     */
    @FXML
    private void handleInputDirButton() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select input directory");
        File dir = dirChooser.showDialog(mainApp.getPrimaryStage());
        if (dir != null) {
            // Change the text field and update the model.
            mainApp.getAppSettings().setInputDirectory(dir.toPath());
            inputDirTextField.setText(mainApp.getAppSettings().getInputDirectory().toAbsolutePath().toString());
        }
    }

    /**
     * Handles clicks to the target directory button by showing a file open dialog.
     */
    @FXML
    private void handleTargetDirButton() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select target directory");
        File dir = dirChooser.showDialog(mainApp.getPrimaryStage());
        if (dir != null) {
            // Change the text field and update the model.
            mainApp.getAppSettings().setTargetDirectory(dir.toPath());
            targetDirTextField.setText(mainApp.getAppSettings().getTargetDirectory().toAbsolutePath().toString());
        }
    }

    /**
     * Handles clicks to the check box regarding usage of existing file attributes with the QR code.
     */
    @FXML
    private void handleUseFileAttributeCheckBox() {
        mainApp.getAppSettings().setUseFileAttribute(useFileAttributeCheckBox.isSelected());
    }

    /**
     * Handles clicks to the check box regarding writing new file attributes when a QR code is recognised upon
     * scanning.
     */
    @FXML
    private void handleWriteFileAttributeCheckBox() {
        mainApp.getAppSettings().setWriteFileAttribute(writeFileAttributeCheckBox.isSelected());
    }

    /**
     * Handles clicks to the check box regarding opening the log file.
     */
    @FXML
    private void handleOpenLogFileCheckBox() {
        mainApp.getAppSettings().setOpenLogFile(openLogFileCheckBox.isSelected());
    }

    /**
     * Handles clicks to the check box regarding renaming of the PDF files after QR codes have been extracted.
     */
    @FXML
    private void handleRenameCheckBox() {
        mainApp.getAppSettings().setWithFileRenaming(renameCheckBox.isSelected());
        toggleRenaming();
    }

    /**
     * Toggle a set of controls depending on whether or not we want to rename.
     */
    private void toggleRenaming() {
        boolean doRename = renameCheckBox.isSelected();
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
     * Handles clicks to the scan button (or scan+rename button) by starting up an extra thread for processing.
     */
    @FXML
    private void handleScanButton() {
        AppSettings appSettings = mainApp.getAppSettings();

        // Spinner has no nice listener, update value first.
        appSettings.setQRPage(qrPageSpinner.getValue());

        Path inputDir = appSettings.getInputDirectory();
        int qrPage = appSettings.getQRPage();
        boolean useFileAttributes = appSettings.getUseFileAttribute();
        boolean writeFileAttributes = appSettings.getWriteFileAttribute();
        boolean openLogFile = appSettings.getOpenLogFile();

        Task<List<SingleResult>> task;
        if (appSettings.getWithFileRenaming()) {
            Path targetDir = appSettings.getTargetDirectory();
            task = new RenameTask(inputDir, targetDir, qrPage, useFileAttributes, writeFileAttributes, openLogFile);
        } else {
            task = new ScanTask(inputDir, qrPage, useFileAttributes, writeFileAttributes, openLogFile);
        }

        ProgressDialog pDialog = new ProgressDialog("Processing...", task.progressProperty());
        pDialog.show();
        scanButton.setDisable(true);

        task.setOnSucceeded(event -> {
            pDialog.close();
            scanButton.setDisable(false);
            ResultsDialog rDialog = new ResultsDialog(
                    task.getValue(),
                    task instanceof RenameTask,
                    task.getMessage());
            rDialog.show();
            // TODO Move code to create CSV log file here.
        });

        new Thread(task).start();
    }
}
