module com.elios.air_hockey_application {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires netty.all;

    opens com.elios.air_hockey_application to javafx.fxml;
    exports com.elios.air_hockey_application;
}