package com.elios.airhockeyapplication.server.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class AirHockeyClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String message = (String) msg;

        if (message.equals("GAME_START")) {
            System.out.println("Game started");
        } else if (message.startsWith("SCORE-")) {
            String[] parts = message.split("-");
            if (parts.length == 2) {
                String score = parts[1];
                System.out.println("Updated score: " + score);
            }
        } else {
            System.out.println("Message from the server: " + message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("System error occurred");
        ctx.close();
    }
}

