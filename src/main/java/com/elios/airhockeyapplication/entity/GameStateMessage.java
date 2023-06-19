package com.elios.airhockeyapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStateMessage {
    private double firstPlayerXPos;
    private double secondPlayerXPos;
    private int puckXPosition;
    private int puckYPosition;
    private int firstPlayerScore;
    private int secondPlayerScore;
    private boolean gameStarted;
}
