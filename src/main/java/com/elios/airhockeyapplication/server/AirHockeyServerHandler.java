package com.elios.airhockeyapplication.server;

import com.elios.airhockeyapplication.entity.AirHockeyGameModel;
import com.elios.airhockeyapplication.entity.GameStateMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AirHockeyServerHandler extends ChannelHandlerAdapter {

    private ConcurrentHashMap<ChannelId, AirHockeyGameModel> gameSessions;
    private ExecutorService service;

    public AirHockeyServerHandler() {
        gameSessions = new ConcurrentHashMap<>();
        service = Executors.newCachedThreadPool();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof String json) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                GameStateMessage gameMessage = objectMapper.readValue(json, GameStateMessage.class);
                updateGameState(ctx, gameMessage);
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
        gameSessions.put(channelId, new AirHockeyGameModel());
        ctx.writeAndFlush(welcomeMessage);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ChannelId channelId = ctx.channel().id();
        gameSessions.remove(channelId);
        super.channelInactive(ctx);
    }

    private void updateGameState(ChannelHandlerContext ctx, GameStateMessage gameStateMessage) {
        ChannelId channelId = ctx.channel().id();
        AirHockeyGameModel gameSession = gameSessions.get(channelId);
        if (gameSession != null) {
            gameSession.setFirstPlayerXPos(gameStateMessage.getFirstPlayerXPos());
            gameSession.setSecondPlayerXPos(gameStateMessage.getSecondPlayerXPos());
            gameSession.setPuckXPosition(gameStateMessage.getPuckXPosition());
            gameSession.setPuckYPosition(gameStateMessage.getPuckYPosition());
            gameSession.setFirstPlayerScore(gameStateMessage.getFirstPlayerScore());
            gameSession.setSecondPlayerScore(gameStateMessage.getSecondPlayerScore());

            service.execute(() -> {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(gameStateMessage);

                    for (ChannelId id : gameSessions.keySet()) {
                        Channel clientChannel = gameSessions.get(id).getClientChannel();
                        clientChannel.writeAndFlush(json);
                    }
                } catch (JsonProcessingException e) {
                    System.out.println("Error occurred during JSON serialization: " + e.getMessage());
                }
            });
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("System error occurred");
        ctx.close();
    }
}
