package nl.ls31.qrscan.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nl.ls31.qrscan.model.SingleResult;

import java.util.List;

/**
 * A dialog window with the results of a ScanTask or RenameTask.
 */
public class ResultsDialog {
    private final Stage dialogStage;

    /**
     * A dialog window with the results of a ScanTask or RenameTask.
     */
    public ResultsDialog(List<SingleResult> results, boolean showRenamedColumn, String summary) {
        ObservableList<SingleResult> resultList = FXCollections.observableArrayList(results);

        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.DECORATED);
        dialogStage.setMinWidth(600);
        dialogStage.setMinHeight(400);
        dialogStage.setResizable(true);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Result overview");

        final Label label = new Label(summary);
        TableView<SingleResult> table = new TableView<>();

        TableColumn<SingleResult, String> inputPathCol = new TableColumn<>("File path");
        inputPathCol.setMinWidth(400);
        inputPathCol.setCellValueFactory(new PropertyValueFactory<>("inputFilePath"));
        inputPathCol.setSortType(TableColumn.SortType.DESCENDING);

        TableColumn<SingleResult, String> renamedPathCol = new TableColumn<>("Renamed file path");
        renamedPathCol.setVisible(showRenamedColumn);
        renamedPathCol.setMinWidth(400);
        renamedPathCol.setCellValueFactory(new PropertyValueFactory<>("renamedFilePath"));
        renamedPathCol.setSortType(TableColumn.SortType.DESCENDING);

        TableColumn<SingleResult, String> qrCodeStatusCol = new TableColumn<>("QR code status");
        qrCodeStatusCol.setMinWidth(40);
        qrCodeStatusCol.setCellValueFactory(new PropertyValueFactory<>("qrCodeStatus"));
        renamedPathCol.setSortType(TableColumn.SortType.DESCENDING);

        TableColumn<SingleResult, String> qrCodeCol = new TableColumn<>("QR code");
        qrCodeCol.setMinWidth(60);
        qrCodeCol.setCellValueFactory(new PropertyValueFactory<>("qrCode"));
        renamedPathCol.setSortType(TableColumn.SortType.DESCENDING);

        table.setItems(resultList);
        table.getColumns().addAll(inputPathCol, renamedPathCol, qrCodeStatusCol, qrCodeCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, table);

        Scene scene = new Scene(vbox);
        dialogStage.setScene(scene);
    }

    /**
     * Show the window.
     */
    public void show() {
        dialogStage.show();
    }
}
