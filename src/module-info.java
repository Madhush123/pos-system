module pos.system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.devstack.pos to javafx.fxml;
    opens com.devstack.pos.controller to javafx.fxml;
    exports com.devstack.pos;
}