package com.elios.airhockeyapplication.server;

import com.elios.airhockeyapplication.common.CustomJsonDecoder;
import com.elios.airhockeyapplication.common.CustomJsonEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class AirHockeyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new CustomJsonDecoder());
        pipeline.addLast(new CustomJsonEncoder());
        pipeline.addLast(new AirHockeyServerHandler());
    }
}
