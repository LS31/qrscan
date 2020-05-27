package nl.ls31.qrscan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nl.ls31.qrscan.controller.RootController;
import nl.ls31.qrscan.controller.ScanPdfsController;
import nl.ls31.qrscan.model.AppSettings;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * Main GUI of QRScan.
 *
 * @author Lars Steggink
 */
public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    private final AppSettings appSettings;

    /**
     * Main application.
     */
    public MainApp() {
        this.appSettings = new AppSettings();
    }

    /**
     * Starts the application.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Gets the current settings.
     *
     * @return current settings
     */
    public AppSettings getAppSettings() {
        return appSettings;
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
     * Starts the main application.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("QRScan");

        // Load layouts from FXML file.
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/RootLayout.fxml"));
        FXMLLoader scanViewLoader = new FXMLLoader(MainApp.class.getResource("/fxml/ScanPdfsView.fxml"));
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            Logger.error(e, "Root layout not found.");
        }
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();

        RootController rootController = loader.getController();
        rootController.setMainApp(this);
        try {
            AnchorPane scanView = scanViewLoader.load();
            rootLayout.setCenter(scanView);
        } catch (IOException e) {
            Logger.error(e, "Scan layout not found.");
        }
        ScanPdfsController scanPdfsController = scanViewLoader.getController();
        scanPdfsController.setMainApp(this);
        scanPdfsController.updateControlsByModel();
    }
}