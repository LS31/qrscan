package nl.ls31.qrscan.ui.view;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A simple dialog window with a progress bar.
 */
public class ProgressDialog {
    private final Stage dialogStage;

    public ProgressDialog(String title, ReadOnlyDoubleProperty taskProgressProperty) {
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(title);

        // Progress bar
        final Label label = new Label(title);
        ProgressBar pb = new ProgressBar();
        pb.setProgress(-1F);
        ProgressIndicator pin = new ProgressIndicator();
        pin.setProgress(-1F);

        final HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(label, pb, pin);

        Scene scene = new Scene(hb);
        dialogStage.setScene(scene);

        pb.progressProperty().bind(taskProgressProperty);
        pin.progressProperty().bind(taskProgressProperty);
    }

    /**
     * Show the dialog window.
     */
    public void show() {
        dialogStage.show();
    }

    /**
     * Close the dialog window.
     */
    public void close() {
        dialogStage.close();
    }
}
