module qrscan {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.zxing;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires com.google.zxing.javase;
    requires org.tinylog.api;
    requires org.tinylog.impl;

    opens nl.ls31.qrscan.ui.view to javafx.fxml;
    exports nl.ls31.qrscan;
}