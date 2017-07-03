package nl.lcs.qrscan.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nl.lcs.qrscan.ui.model.CreateSettings;
import nl.lcs.qrscan.ui.model.ManualTagSettings;
import nl.lcs.qrscan.ui.model.ScanSettings;
import nl.lcs.qrscan.ui.view.RootController;
import nl.lcs.qrscan.ui.view.ScanController;

/**
 * Main GUI of QRScan.
 * 
 * @author Lars Steggink
 */
public class MainApp extends Application {
	final static private String LSEP = System.lineSeparator();
	/**
	 * Starts the application.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {
		launch(args);
	}
	private Stage primaryStage;
	private BorderPane rootLayout;
	private ScanSettings scanSettings;
	@FXML
	private TextArea logArea;
	private CreateSettings createSettings;
	@FXML
	private MenuButton exitButton;

	private ManualTagSettings manualTagSettings;

	/**
	 * Main application.
	 */
	public MainApp() {
		this.scanSettings = new ScanSettings();
		this.createSettings = new CreateSettings();
		this.manualTagSettings = new ManualTagSettings();
	}

	/**
	 * Gets the current create settings.
	 * 
	 * @return create settings
	 */
	public CreateSettings getCreateSettings() {
		return createSettings;
	}

	/**
	 * Gets the current manual tagging settings.
	 * 
	 * @return manual tagging settings
	 */
	public ManualTagSettings getManualTagSettings() {
		return manualTagSettings;
	}

	/**
	 * Gets the primary stage of this application.
	 * 
	 * @return primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Gets the current scan settings.
	 * 
	 * @return scan settings
	 */
	public ScanSettings getScanSettings() {
		return scanSettings;
	}

	/**
	 * Adds a message to the application log. For now, these messages are shown
	 * on a new line in the lower 'logging panel' of the application.
	 * 
	 * @param message Log message
	 */
	public void log(String message) {
		logArea.appendText(message + LSEP);
	}

	/**
	 * Starts the main application.
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("QRScan");

		// Load layouts from FXML file.
		FXMLLoader loader = new FXMLLoader();
		FXMLLoader scanViewLoader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
		scanViewLoader.setLocation(getClass().getResource("view/ScanView.fxml"));
		try {
			rootLayout = (BorderPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(rootLayout);
		primaryStage.setScene(scene);
		primaryStage.show();

		logArea = (TextArea) rootLayout.getBottom();
		RootController rootController = loader.getController();
		rootController.setMainApp(this);
		try {
			AnchorPane scanView = (AnchorPane) scanViewLoader.load();
			rootLayout.setCenter(scanView);

		} catch (IOException e) {
			e.printStackTrace();
		}
		ScanController scanController = scanViewLoader.getController();
		scanController.setMainApp(this);
	}
}
