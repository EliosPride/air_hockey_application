package com.elios.air_hockey_application;

import com.elios.air_hockey_application.server.AirHockeyClient;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.elios.air_hockey_application.common.AppConfiguration.*;

public class AirHockeyAppLauncher extends Application {

    public int puckYPosition = BACKGROUND_HEIGHT / 2;
    public int puckXPosition = BACKGROUND_WIDTH / 2;
    public double firstPlayerXPos = BACKGROUND_WIDTH / 2.0;
    public double secondPlayerXPos = BACKGROUND_WIDTH / 2.0;
    public static final int PLAYER_HEIGHT = 50;
    public static final int PLAYER_WIDTH = 5;
    public static final int FIRST_PLAYER_Y_POS = 0;
    public static final int SECOND_PLAYER_Y_POS = BACKGROUND_HEIGHT - PLAYER_WIDTH;
    private int firstPlayerScore = 0;
    private int secondPlayerScore = 0;
    private int puckXSpeed = 1;
    private int puckYSpeed = 1;
    private boolean gameStarted;
    private List<Integer> score = new ArrayList<>();
    private AirHockeyClient airHockeyClient;

    public void start(Stage stage) {
        stage.setTitle(APP_TITLE);
        Canvas canvas = new Canvas(BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> run(graphicsContext)));
        timeline.setCycleCount(Timeline.INDEFINITE);

        canvas.setOnMouseMoved(event -> firstPlayerXPos = event.getX());
        if (checkServerRunning()) {
            canvas.setOnMouseMoved(event -> secondPlayerXPos = event.getX());
        }
        canvas.setOnMouseClicked(event -> {
            gameStarted = true;
            if (airHockeyClient != null && checkServerRunning()) {
                airHockeyClient.sendMessage("START");
            } else if (checkServerRunning()){
                showErrorDialog();
            }
        });
        canvas.addEventFilter(MouseEvent.ANY, (event) -> canvas.requestFocus());
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        timeline.play();

        if (checkServerRunning()) {
            airHockeyClient = new AirHockeyClient();
            airHockeyClient.connect(HOST, PORT);
        }
    }

    private void run(GraphicsContext graphicsContext) {
        getGameLogic(graphicsContext);
        score.set(0, firstPlayerScore);
        score.set(1, secondPlayerScore);

        if (airHockeyClient != null && checkServerRunning()) {
            airHockeyClient.sendMessage(firstPlayerXPos, secondPlayerXPos, puckXPosition, puckYPosition, firstPlayerScore, secondPlayerScore);
        }

        renderGraphics(graphicsContext);
    }

    public void getGameLogic(GraphicsContext graphicsContext) {
        graphicsContext.setFill(Color.SLATEGRAY);
        graphicsContext.fillRect(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        graphicsContext.setFill(Color.BLACK);
        graphicsContext.setFont(Font.font(25));

        graphicsContext.fillText(String.valueOf(firstPlayerScore), 100, 100);
        graphicsContext.fillText(String.valueOf(secondPlayerScore), 500, 900);
        graphicsContext.fillRect(secondPlayerXPos, SECOND_PLAYER_Y_POS, PLAYER_HEIGHT, PLAYER_WIDTH);
        graphicsContext.fillRect(firstPlayerXPos, FIRST_PLAYER_Y_POS, PLAYER_HEIGHT, PLAYER_WIDTH);

        if (gameStarted) {
            puckXPosition += puckXSpeed;
            puckYPosition += puckYSpeed;

            if (puckYPosition < BACKGROUND_HEIGHT - BACKGROUND_HEIGHT / 4) {
                secondPlayerXPos = BACKGROUND_WIDTH / 2 - PLAYER_WIDTH / 2;
            } else {
                moveAIPlayer();
            }

            graphicsContext.fillOval(puckXPosition, puckYPosition, PUCK_RADIUS, PUCK_RADIUS);

        } else {
            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.setTextAlign(TextAlignment.CENTER);
            graphicsContext.strokeText(GAME_STARTER, BACKGROUND_WIDTH / 2, BACKGROUND_HEIGHT / 2);

            puckYPosition = BACKGROUND_HEIGHT / 2;
            puckXPosition = BACKGROUND_WIDTH / 2;

            puckYSpeed = getPuckDirection();
            puckXSpeed = getPuckDirection();
        }

        if (puckXPosition > BACKGROUND_WIDTH || puckXPosition < 0) {
            puckXSpeed *= -1;
        }

        if (puckYPosition < FIRST_PLAYER_Y_POS - PLAYER_HEIGHT) {
            secondPlayerScore++;
            score.set(1, secondPlayerScore);
            gameStarted = false;
            if (checkServerRunning()) {
                airHockeyClient.sendMessage("SCORE-" + score.get(0) + " : " + score.get(1));
            }
        }

        if (puckYPosition > SECOND_PLAYER_Y_POS + PLAYER_HEIGHT) {
            firstPlayerScore++;
            score.set(0, firstPlayerScore);
            gameStarted = false;
            if (checkServerRunning()) {
                airHockeyClient.sendMessage("SCORE-" + score.get(0) + " : " + score.get(1));
            }
        }

        if (((puckYPosition + PUCK_RADIUS > SECOND_PLAYER_Y_POS) && puckXPosition >= secondPlayerXPos && puckXPosition <= secondPlayerXPos + PLAYER_HEIGHT) ||
                ((puckYPosition < FIRST_PLAYER_Y_POS + PLAYER_WIDTH) && puckXPosition >= firstPlayerXPos && puckXPosition <= firstPlayerXPos + PLAYER_HEIGHT)) {
            puckXSpeed += 1 * Math.signum(puckXSpeed);
            puckYSpeed += 1 * Math.signum(puckYSpeed);
            puckYSpeed *= -1;
            puckXSpeed *= -1;
        }
    }

    private int getPuckDirection() {
        return new Random().nextInt(5) == 0 ? 1 : -1;
    }

    private void moveAIPlayer() {
        double targetXPos = puckXPosition - PLAYER_HEIGHT / 2;

        double movementSpeed = 2.0;

        if (secondPlayerXPos < targetXPos) {
            secondPlayerXPos += movementSpeed;
        } else if (secondPlayerXPos > targetXPos) {
            secondPlayerXPos -= movementSpeed;
        }
    }

    private void renderGraphics(GraphicsContext graphicsContext) {
        getGameLogic(graphicsContext);
    }

    private void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Connection Error");
        alert.setHeaderText(null);
        alert.setContentText("Cannot send message to server. Connection not established.");
        alert.showAndWait();
    }

    private static boolean checkServerRunning() {
        try (Socket socket = new Socket("localhost", PORT)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}