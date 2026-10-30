module com.fu.ch08 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;

    opens com.fu.ch08 to javafx.fxml;
    opens com.fu.ch08.b4 to javafx.fxml;

    exports com.fu.ch08;
    exports com.fu.ch08.b1;
    exports com.fu.ch08.b2;
    exports com.fu.ch08.b3;
    exports com.fu.ch08.b4;
    exports com.fu.ch08.common;
}
