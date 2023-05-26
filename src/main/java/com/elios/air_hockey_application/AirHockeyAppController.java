package com.elios.air_hockey_application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AirHockeyAppController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to AirHockey Game!");
    }
}