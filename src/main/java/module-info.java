module com.elios.air_hockey_application {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires netty.all;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires lombok;

    opens com.elios.airhockeyapplication to javafx.fxml;
    exports com.elios.airhockeyapplication;
}