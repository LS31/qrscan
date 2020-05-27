package nl.ls31.qrscan.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import nl.ls31.qrscan.MainApp;
import nl.ls31.qrscan.model.AppSettings;
import nl.ls31.qrscan.model.QrPdf;
import org.tinylog.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Controller for the dialog where an individual PDF file can be tagged manually. Tagging means that a provided code is
 * set as a custom file attribute for that particular PDF file.
 *
 * @author Lars Steggink
 */
public class ManualTagController {

    private MainApp mainApp;
    @FXML
    private TextField pdfPathField;
    @FXML
    private Button pdfPathButton;
    @FXML
    private TextField codeField;
    @FXML
    private Button tagButton;

    /**
     * Handles edits to the code field, checking for validity.
     */
    @FXML
    private void handleCodeFieldEdit() {
        String userInput = codeField.getText();
        if (QrPdf.isValidQRCode(userInput)) {
            codeField.setStyle("-fx-border-color: green; -fx-border-width: 2px ;");
        } else {
            codeField.setStyle("-fx-border-color: red; -fx-border-width: 2px ;");
        }
    }

    /**
     * Handles clicks on the file button by showing a file open dialog.
     */
    @FXML
    private void handlePDFPathButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF file");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("PDF file", "*.pdf"));
        File file = fileChooser.showOpenDialog(pdfPathButton.getScene().getWindow());
        if (file != null) {
            // Change the text field and update the model.
            pdfPathField.setText(file.toPath().toAbsolutePath().toString());
            mainApp.getAppSettings().setManualPdf(file.toPath());
        }
    }

    /**
     * Handles clicks on the tag button by updating the model and tagging.
     */
    @FXML
    private void handleTagButton() {
        try {
            mainApp.getAppSettings().setManualCode(codeField.getText());
            tagFile();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid code");
            alert.setHeaderText("Invalid code");
            alert.setContentText("Only characters A-Z, a-z, 0-9, space, -, and _ are allowed to prevent trouble.");
            alert.showAndWait();
        }
    }

    /**
     * Sets the main application to reference back to itself.
     *
     * @param mainApp main application
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Tags a PDF file according to the current settings in the settings model.
     */
    private void tagFile() {
        AppSettings settings = mainApp.getAppSettings();
        QrPdf pdf = new QrPdf(settings.getManualPdf());
        try {
            pdf.setQRCodeFileAttribute(settings.getManualCode());
            String message = "Successfully tagged " + settings.getManualPdf().getFileName().toString() + " with code "
                    + settings.getManualCode() + ". ";
            Logger.info(message);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Task finished.");
            alert.setHeaderText("Added file attribute.");
            alert.setContentText(message);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
            ((Stage) tagButton.getScene().getWindow()).close();
        } catch (IllegalArgumentException | IOException e) {
            Logger.error("Adding custom file attributes may not be supported by the file system, or writing to the file was denied.");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Unable to tag");
            alert.setHeaderText("Unable to edit custom file attribute.");
            alert.setContentText(
                    "Adding custom file attributes may not be supported by the file system, or writing to the file was denied.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    public void updateControlsByModel() {
        pdfPathField.setText(mainApp.getAppSettings().getManualPdf().toAbsolutePath().toString());
        codeField.setText(mainApp.getAppSettings().getManualCode());
    }
}
