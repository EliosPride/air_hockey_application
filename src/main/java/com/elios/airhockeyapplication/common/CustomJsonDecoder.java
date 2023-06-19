package com.elios.airhockeyapplication.common;

import com.elios.airhockeyapplication.entity.GameStateMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomJsonDecoder extends ChannelHandlerAdapter {

    private ObjectMapper objectMapper;

    public CustomJsonDecoder() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf byteBuf) {
            try {
                byte[] bytes = new byte[byteBuf.readableBytes()];
                byteBuf.readBytes(bytes);

                String jsonString = new String(bytes, StandardCharsets.UTF_8);

                GameStateMessage jsonObject = objectMapper.readValue(jsonString, GameStateMessage.class);

                ctx.fireChannelRead(jsonObject);
            } catch (IOException e) {
                System.out.println("Error occurred during parsing" + e.getMessage());;
            } finally {
                byteBuf.release();
            }
        }
    }
}
