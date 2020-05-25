package nl.ls31.qrscan.ui.view;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import nl.ls31.qrscan.App;
import nl.ls31.qrscan.core.RenameTask;
import nl.ls31.qrscan.core.ScanTask;
import nl.ls31.qrscan.core.SingleResult;
import nl.ls31.qrscan.ui.model.ScanSettings;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * Controller for the main function of the program: scanning PDF files for QR codes and rename them accordingly.
 *
 * @author Lars Steggink
 */
public class ScanController {

    private App mainApp;
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
    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Update all control states using the model as reference.
     */
    public void updateControlsByModel() {
        inputDirTextField.setText(mainApp.getScanSettings().getInputDirectory().toAbsolutePath().toString());
        targetDirTextField.setText(mainApp.getScanSettings().getTargetDirectory().toAbsolutePath().toString());
        useFileAttributeCheckBox.setSelected(mainApp.getScanSettings().getUseFileAttributes());
        writeFileAttributeCheckBox.setSelected(mainApp.getScanSettings().getWriteFileAttributes());
        renameCheckBox.setSelected(mainApp.getScanSettings().getWithRenaming());
        toggleRenaming();
        openLogFileCheckBox.setSelected(mainApp.getScanSettings().getOpenLogFile());
        qrPageSpinner.getValueFactory().setValue(mainApp.getScanSettings().getQRPage());
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
            mainApp.getScanSettings().setInputDirectory(dir.toPath());
            inputDirTextField.setText(mainApp.getScanSettings().getInputDirectory().toAbsolutePath().toString());
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
            mainApp.getScanSettings().setTargetDirectory(dir.toPath());
            targetDirTextField.setText(mainApp.getScanSettings().getTargetDirectory().toAbsolutePath().toString());
        }
    }

    /**
     * Handles clicks to the check box regarding usage of existing file attributes with the QR code.
     */
    @FXML
    private void handleUseFileAttributeCheckBox() {
        mainApp.getScanSettings().setUseFileAttributes(useFileAttributeCheckBox.isSelected());
    }

    /**
     * Handles clicks to the check box regarding writing new file attributes when a QR code is recognised upon
     * scanning.
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
     * Handles clicks to the check box regarding renaming of the PDF files after QR codes have been extracted.
     */
    @FXML
    private void handleRenameCheckBox() {
        mainApp.getScanSettings().setWithRenaming(renameCheckBox.isSelected());
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
        ScanSettings settings = mainApp.getScanSettings();

        // Spinner has no nice listener, update value first.
        settings.setQRPage(qrPageSpinner.getValue());

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
        task.messageProperty().addListener((observable, oldValue, newValue) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Task finished.");
            alert.setHeaderText("Scanned and/or renamed PDF files.");
            alert.setContentText(newValue);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        });
        new Thread(task).start();
    }
}
