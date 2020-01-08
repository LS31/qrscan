package nl.ls31.qrscan.ui.view;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nl.ls31.qrscan.core.CreateTask;
import nl.ls31.qrscan.App;

/**
 * Controller for the dialog where GIF files containing QR codes can be created.
 * 
 * @author Lars Steggink
 *
 */
public class CreateController {

	private App mainApp;
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
	 * @param mainApp
	 *            the main application
	 */
	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
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
			mainApp.getCreateSettings().setInputFile(file.toPath());
		}
	}

	/**
	 * Handles clicks on the output directory button by showing a open directory
	 * dialog.
	 */
	@FXML
	private void handleOutputDirButton() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select output directory");
		File dir = dirChooser.showDialog(outputDirButton.getScene().getWindow());
		if (dir != null) {
			// Change the text field and update the model.
			outputDirTextField.setText(dir.toPath().toAbsolutePath().toString());
			mainApp.getCreateSettings().setOutputDirectory(dir.toPath());
		}
	}

	/**
	 * Handles changes of the annotation check box.
	 */
	@FXML
	private void handleAnnotationCheckBox() {
		mainApp.getCreateSettings().setWithAnnotation(annotationCheckBox.isSelected());
	}

	/**
	 * Handles clicks on the create button.
	 */
	@FXML
	private void handleCreateButton() {
		Path inputFile = mainApp.getCreateSettings().getInputFile();
		Path outputDir = mainApp.getCreateSettings().getOutputDirectory();
		boolean withAnnotation = mainApp.getCreateSettings().getWithAnnotation();
		mainApp.getCreateSettings().setImageSize(sizeSpinner.getValue());
		int size = mainApp.getCreateSettings().getImageSize();
		Task<List<Path>> createTask = new CreateTask(inputFile, outputDir, size, withAnnotation);
		// Messages are passed into the log.
		createTask.messageProperty().addListener((observable, oldValue, newValue) -> mainApp.log(newValue));
		new Thread(createTask).start();
		// Close the settings window and return to main app. Meanwhile, the
		// other thread will continue.
		((Stage) createButton.getScene().getWindow()).close();
	}
}
