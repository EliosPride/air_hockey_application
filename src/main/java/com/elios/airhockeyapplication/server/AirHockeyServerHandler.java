package com.elios.airhockeyapplication.server;

import com.elios.airhockeyapplication.server.entity.GameStateMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.elios.airhockeyapplication.common.AppConfiguration.BACKGROUND_HEIGHT;
import static com.elios.airhockeyapplication.common.AppConfiguration.BACKGROUND_WIDTH;

public class AirHockeyServerHandler extends ChannelHandlerAdapter {

    private int puckYPosition = BACKGROUND_HEIGHT / 2;
    private int puckXPosition = BACKGROUND_WIDTH / 2;
    private double firstPlayerXPos = BACKGROUND_WIDTH / 2.0;
    private double secondPlayerXPos = BACKGROUND_WIDTH / 2.0;
    private int firstPlayerScore = 0;
    private int secondPlayerScore = 0;
    private ConcurrentHashMap<ChannelId, Channel> clientChannels;
    private ExecutorService service;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof String json) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                GameStateMessage gameMessage = objectMapper.readValue(json, GameStateMessage.class);
                updateGameState(gameMessage);
            } catch (JsonProcessingException e) {
                System.out.println("Error occurred during JSON deserialization: " + e.getMessage());
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        ChannelId channelId = channel.id();
        System.out.println("Client connected: " + channel.remoteAddress());
        String welcomeMessage = "Welcome to the Air Hockey Game!";
        clientChannels.put(channelId, channel);
        ctx.writeAndFlush(welcomeMessage);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelId channelId = ctx.channel().id();
        clientChannels.remove(channelId);
        super.channelInactive(ctx);
    }


    public AirHockeyServerHandler() {
        clientChannels = new ConcurrentHashMap<>();
        service = Executors.newCachedThreadPool();
    }

    private void updateGameState(GameStateMessage gameStateMessage) {
        this.firstPlayerXPos = gameStateMessage.getFirstPlayerXPos();
        this.secondPlayerXPos = gameStateMessage.getSecondPlayerXPos();
        this.puckXPosition = gameStateMessage.getPuckXPosition();
        this.puckYPosition = gameStateMessage.getPuckYPosition();
        this.firstPlayerScore = gameStateMessage.getFirstPlayerScore();
        this.secondPlayerScore = gameStateMessage.getSecondPlayerScore();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(gameStateMessage);

            for (Channel clientChannel : clientChannels.values()) {
                clientChannel.writeAndFlush(json);
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error occurred during JSON serialization: " + e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("System error is occurred");
        ctx.close();
    }

}
