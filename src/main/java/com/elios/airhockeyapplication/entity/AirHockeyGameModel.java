package com.elios.airhockeyapplication.entity;

import io.netty.channel.Channel;
import lombok.Data;

import static com.elios.airhockeyapplication.common.AppConfiguration.BACKGROUND_HEIGHT;
import static com.elios.airhockeyapplication.common.AppConfiguration.BACKGROUND_WIDTH;

@Data
public class AirHockeyGameModel {
    private Channel clientChannel;
    private double firstPlayerXPos;
    private double secondPlayerXPos;
    private int puckYPosition;
    private int puckXPosition;
    private int firstPlayerScore;
    private int secondPlayerScore;

    public AirHockeyGameModel() {
        firstPlayerXPos = BACKGROUND_WIDTH / 2.0;
        secondPlayerXPos = BACKGROUND_WIDTH / 2.0;
        puckYPosition = BACKGROUND_HEIGHT / 2;
        puckXPosition = BACKGROUND_WIDTH / 2;
        firstPlayerScore = 0;
        secondPlayerScore = 0;
    }
}