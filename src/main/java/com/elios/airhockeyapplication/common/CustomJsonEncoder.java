package com.elios.airhockeyapplication.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.OutputStream;

public class CustomJsonEncoder extends MessageToByteEncoder<Object> {

    private final ObjectMapper objectMapper;

    public CustomJsonEncoder() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ByteBufOutputStream outputStream = new ByteBufOutputStream(out);
        try {
            objectMapper.writeValue((OutputStream) outputStream, msg);
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }
}
