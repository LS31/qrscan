package nl.ls31.qrscan.ui.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.ls31.qrscan.App;

/**
 * Controller for some actions in the main window.
 * 
 * @author Lars Steggink
 *
 */
public class RootController {

	@FXML
	private MenuItem exitItem;
	private App mainApp;
	@FXML
	private MenuItem aboutItem;
	@FXML
	private MenuItem createItem;
	@FXML
	private MenuItem manualTagItem;

	/**
	 * Sets a call back reference to the main application.
	 * 
	 * @param mainApp
	 *            main application
	 */
	public void setMainApp(App mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Handles clicks to the exit menu item by closing the application.
	 */
	@FXML
	public void handleExitItem() {
		mainApp.getPrimaryStage().close();
	}

	/**
	 * Handles clicks to the about item by showing an about dialog.
	 */
	@FXML
	public void handleAboutItem() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About QRScan");
		alert.setHeaderText("QRScan");
		alert.setContentText("Version 1.0.1.\r\n" + "A big thanks to the following frameworks: "
				+ "PDFBox by The Apache Software Foundation (Apache license v2.0), "
				+ "Java ImageIO plugin for JBIG2 (GNU General Public License v3), "
				+ "Java Advanced Imaging Image I/O Tools API (BSD licence), "
				+ "ZXing project (Apache license v2.0). ");
		alert.showAndWait();
	}

	/**
	 * Handles clicks to the menu item for creating QR code image files by
	 * showing the corresponding dialog.
	 */
	@FXML
	public void handleCreateItem() {
		try {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			FXMLLoader createViewLoader = new FXMLLoader();
			createViewLoader.setLocation(getClass().getResource("CreateView.fxml"));
			AnchorPane createView = (AnchorPane) createViewLoader.load();
			stage.setScene(new Scene(createView));

			// Give the controllers access to the main app.
			CreateController createController = createViewLoader.getController();
			createController.setMainApp(mainApp);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles clicks to the menu item for manually tagging PDF files by showing
	 * the corresponding dialog.
	 */
	@FXML
	public void handleManualTagItem() {
		try {
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			FXMLLoader manualTagViewLoader = new FXMLLoader();
			manualTagViewLoader.setLocation(getClass().getResource("ManualTagView.fxml"));
			AnchorPane manualTagView = (AnchorPane) manualTagViewLoader.load();
			stage.setScene(new Scene(manualTagView));

			// Give the controllers access to the main app.
			ManualTagController manualTagController = manualTagViewLoader.getController();
			manualTagController.setMainApp(mainApp);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
