package com.elios.air_hockey_application.common;

public class GameStateMessage {
    private double firstPlayerXPos;
    private double secondPlayerXPos;
    private int puckXPosition;
    private int puckYPosition;
    private int firstPlayerScore;
    private int secondPlayerScore;

    public GameStateMessage(double firstPlayerXPos, double secondPlayerXPos, int puckXPosition, int puckYPosition, int firstPlayerScore, int secondPlayerScore) {
        this.firstPlayerXPos = firstPlayerXPos;
        this.secondPlayerXPos = secondPlayerXPos;
        this.puckXPosition = puckXPosition;
        this.puckYPosition = puckYPosition;
        this.firstPlayerScore = firstPlayerScore;
        this.secondPlayerScore = secondPlayerScore;
    }

    public double getFirstPlayerXPos() {
        return firstPlayerXPos;
    }

    public double getSecondPlayerXPos() {
        return secondPlayerXPos;
    }

    public int getPuckXPosition() {
        return puckXPosition;
    }

    public int getPuckYPosition() {
        return puckYPosition;
    }

    public int getFirstPlayerScore() {
        return firstPlayerScore;
    }

    public int getSecondPlayerScore() {
        return secondPlayerScore;
    }

}
