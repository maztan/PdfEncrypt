module org.pdfencrypt.app {
    requires org.slf4j;
    requires java.logging;
    requires jul.to.slf4j;
    requires javafx.graphics;
    requires javafx.fxml;
    requires kernel;
    requires javafx.controls;
    requires java.net.http;
    requires com.google.common;
    requires org.apache.commons.io;
    exports org.pdfencrypt.gui to javafx.graphics, com.google.common;
    opens org.pdfencrypt.app to com.google.common;
    opens org.pdfencrypt.gui.controller to javafx.fxml;
}