package nl.ls31.qrscan.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import nl.ls31.qrscan.MainApp;

import java.io.IOException;

/**
 * Controller for some actions in the main window.
 *
 * @author Lars Steggink
 */
public class RootController {

    private MainApp mainApp;
    @FXML
    private MenuItem exitItem, manualTagItem, createItem, aboutItem;

    /**
     * Sets a call back reference to the main application.
     *
     * @param mainApp main application
     */
    public void setMainApp(MainApp mainApp) {
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
        alert.setHeaderText("QRScan version 2.2.0");
        alert.setContentText("A big thanks to the following frameworks: "
                + "PDFBox by The Apache Software Foundation (Apache license v2.0), "
                + "Java Advanced Imaging Image I/O Tools API (BSD licence), "
                + "ImageIO by TwelveMonkeys (BSD 3 license), "
                + "ZXing project (Apache license v2.0). ");
        alert.showAndWait();
    }

    /**
     * Handles clicks to the menu item for creating QR code image files by showing the corresponding dialog.
     */
    @FXML
    public void handleCreateItem() {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader createViewLoader = new FXMLLoader(MainApp.class.getResource("/fxml/CreateImagesView.fxml"));
            AnchorPane createView = createViewLoader.load();
            stage.setScene(new Scene(createView));

            // Give the controllers access to the main app.
            CreateImagesController createImagesController = createViewLoader.getController();
            createImagesController.setMainApp(mainApp);
            createImagesController.updateControlsByModel();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles clicks to the menu item for manually tagging PDF files by showing the corresponding dialog.
     */
    @FXML
    public void handleManualTagItem() {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader manualTagViewLoader = new FXMLLoader(MainApp.class.getResource("/fxml/ManualTagPdfView.fxml"));
            AnchorPane manualTagView = manualTagViewLoader.load();
            stage.setScene(new Scene(manualTagView));

            // Give the controllers access to the main app.
            ManualTagPdfController manualTagPdfController = manualTagViewLoader.getController();
            manualTagPdfController.setMainApp(mainApp);
            manualTagPdfController.updateControlsByModel();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
