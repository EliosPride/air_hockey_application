package com.elios.air_hockey_application.server;

import com.elios.air_hockey_application.common.GameStateMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

import static com.elios.air_hockey_application.common.AppConfiguration.BACKGROUND_HEIGHT;
import static com.elios.air_hockey_application.common.AppConfiguration.BACKGROUND_WIDTH;

public class AirHockeyServerHandler extends ChannelHandlerAdapter {

    private int puckYPosition = BACKGROUND_HEIGHT / 2;
    private int puckXPosition = BACKGROUND_WIDTH / 2;
    private double firstPlayerXPos = BACKGROUND_WIDTH / 2.0;
    private double secondPlayerXPos = BACKGROUND_WIDTH / 2.0;
    private int firstPlayerScore = 0;
    private int secondPlayerScore = 0;
    private List<ChannelHandlerContext> clientChannels = new ArrayList<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof GameStateMessage gameStateMessage) {

            double firstPlayerXPos = gameStateMessage.getFirstPlayerXPos();
            double secondPlayerXPos = gameStateMessage.getSecondPlayerXPos();
            int puckXPosition = gameStateMessage.getPuckXPosition();
            int puckYPosition = gameStateMessage.getPuckYPosition();
            int firstPlayerScore = gameStateMessage.getFirstPlayerScore();
            int secondPlayerScore = gameStateMessage.getSecondPlayerScore();

            updateGameState(firstPlayerXPos, secondPlayerXPos, puckXPosition, puckYPosition, firstPlayerScore, secondPlayerScore);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected: " + ctx.channel().remoteAddress());
        String welcomeMessage = "Welcome to the Air Hockey Game!";
        ctx.writeAndFlush(welcomeMessage);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    private void updateGameState(double firstPlayerXPos, double secondPlayerXPos, int puckXPosition, int puckYPosition, int firstPlayerScore, int secondPlayerScore) {
        this.firstPlayerXPos = firstPlayerXPos;
        this.secondPlayerXPos = secondPlayerXPos;
        this.puckXPosition = puckXPosition;
        this.puckYPosition = puckYPosition;
        this.firstPlayerScore = firstPlayerScore;
        this.secondPlayerScore = secondPlayerScore;

        String msg = constructGameStateMessage();

        for (ChannelHandlerContext clientChannel : clientChannels) {
            clientChannel.writeAndFlush(msg);
        }
    }

    private String constructGameStateMessage() {
        String gameState = "GAME_STATE:" +
                firstPlayerXPos + "," +
                secondPlayerXPos + "," +
                puckXPosition + "," +
                puckYPosition + "," +
                firstPlayerScore + "," +
                secondPlayerScore;

        return gameState;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
