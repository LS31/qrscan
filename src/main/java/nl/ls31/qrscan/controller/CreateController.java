package nl.ls31.qrscan.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.ls31.qrscan.MainApp;
import nl.ls31.qrscan.core.CreateTask;
import nl.ls31.qrscan.view.ProgressDialog;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * Controller for the dialog where GIF files containing QR codes can be created.
 *
 * @author Lars Steggink
 */
public class CreateController {

    private MainApp mainApp;
    @FXML
    private TextField inputFileTextField;
    @FXML
    private Button inputFileButton;
    @FXML
    private TextField outputDirTextField;
    @FXML
    private Button outputDirButton;
    @FXML
    private CheckBox annotationCheckBox;
    @FXML
    private Spinner<Integer> sizeSpinner;
    @FXML
    private Button createButton;

    /**
     * Sets a reference back to the main application.
     *
     * @param mainApp the main application
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Update all control states using the model as reference.
     */
    public void updateControlsByModel() {
        inputFileTextField.setText(mainApp.getAppSettings().getCodesInputFile().toAbsolutePath().toString());
        outputDirTextField.setText(mainApp.getAppSettings().getQrcodeImageOutputDirectory().toAbsolutePath().toString());
        annotationCheckBox.setSelected(mainApp.getAppSettings().getQrcodeImageWithAnnotation());
        sizeSpinner.getValueFactory().setValue(mainApp.getAppSettings().getQrcodeImageSize());
    }

    /**
     * Handles clicks on the input file button by showing an open file dialog.
     */
    @FXML
    private void handleInputFileButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select input file");
        File file = fileChooser.showOpenDialog(inputFileButton.getScene().getWindow());
        if (file != null) {
            // Change the text field and update the model.
            inputFileTextField.setText(file.toPath().toAbsolutePath().toString());
            mainApp.getAppSettings().setCodesInputFile(file.toPath());
        }
    }

    /**
     * Handles clicks on the output directory button by showing a open directory dialog.
     */
    @FXML
    private void handleOutputDirButton() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select output directory");
        File dir = dirChooser.showDialog(outputDirButton.getScene().getWindow());
        if (dir != null) {
            // Change the text field and update the model.
            outputDirTextField.setText(dir.toPath().toAbsolutePath().toString());
            mainApp.getAppSettings().setQrcodeImageOutputDirectory(dir.toPath());
        }
    }

    /**
     * Handles changes of the annotation check box.
     */
    @FXML
    private void handleAnnotationCheckBox() {
        mainApp.getAppSettings().setQrcodeImageWithAnnotation(annotationCheckBox.isSelected());
    }

    /**
     * Handles clicks on the create button.
     */
    @FXML
    private void handleCreateButton() {
        Path inputFile = mainApp.getAppSettings().getCodesInputFile();
        Path outputDir = mainApp.getAppSettings().getQrcodeImageOutputDirectory();
        boolean withAnnotation = mainApp.getAppSettings().getQrcodeImageWithAnnotation();
        mainApp.getAppSettings().setQrcodeImageSize(sizeSpinner.getValue());
        int size = mainApp.getAppSettings().getQrcodeImageSize();

        Task<List<Path>> createTask = new CreateTask(inputFile, outputDir, size, withAnnotation);

        ProgressDialog pForm = new ProgressDialog("Creating files...", createTask.progressProperty());
        pForm.show();

        createTask.setOnSucceeded(event -> pForm.close());

        createTask.messageProperty().addListener((observable, oldValue, newValue) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Task finished.");
            alert.setHeaderText("Created QR code images.");
            alert.setContentText(newValue);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        });

        new Thread(createTask).start();

        // Close the settings window and return to main app. Meanwhile, the
        // other thread will continue.
        ((Stage) createButton.getScene().getWindow()).close();
    }
}
