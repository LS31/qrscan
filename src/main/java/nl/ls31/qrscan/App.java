package nl.ls31.qrscan;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nl.ls31.qrscan.ui.model.CreateSettings;
import nl.ls31.qrscan.ui.model.ManualTagSettings;
import nl.ls31.qrscan.ui.model.ScanSettings;
import nl.ls31.qrscan.ui.view.RootController;
import nl.ls31.qrscan.ui.view.ScanController;

import java.io.IOException;

/**
 * Main GUI of QRScan.
 *
 * @author Lars Steggink
 */
public class App extends Application {
    final static private String LSEP = System.lineSeparator();

    /**
     * Starts the application.
     *
     * @param args unused
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

    private ManualTagSettings manualTagSettings;

    /**
     * Main application.
     */
    public App() {
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
     * Adds a message to the application log. For now, these messages are shown on a new line in the lower 'logging
     * panel' of the application.
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
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/RootLayout.fxml"));
        FXMLLoader scanViewLoader = new FXMLLoader(App.class.getResource("/fxml/ScanView.fxml"));
        try {
            rootLayout = loader.load();
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
            AnchorPane scanView = scanViewLoader.load();
            rootLayout.setCenter(scanView);

        } catch (IOException e) {
            e.printStackTrace();
        }
        ScanController scanController = scanViewLoader.getController();
        scanController.setMainApp(this);
    }
}
