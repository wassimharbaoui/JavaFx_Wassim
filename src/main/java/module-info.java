module com.example.xperiencetunuser {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens App to javafx.fxml;
    exports App;
    exports View;
    opens View to javafx.fxml;
}