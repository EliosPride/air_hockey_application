package com.elios.airhockeyapplication.server.entity;

public class GameStateMessage {
    private final double firstPlayerXPos;
    private final double secondPlayerXPos;
    private final int puckXPosition;
    private final int puckYPosition;
    private final int firstPlayerScore;
    private final int secondPlayerScore;

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
