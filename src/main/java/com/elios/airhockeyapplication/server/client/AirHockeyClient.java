package com.elios.airhockeyapplication.server.client;

import com.elios.airhockeyapplication.server.entity.GameStateMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class AirHockeyClient {
    private Channel channel;

    public void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new AirHockeyClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("Error occurred during" + e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendMessage(String msg) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(msg);
        }
    }

    public void sendMessage(double firstPlayerXPos, double secondPlayerXPos, int puckXPosition, int puckYPosition, int firstPlayerScore, int secondPlayerScore) {
        GameStateMessage gameStateMessage = new GameStateMessage(firstPlayerXPos, secondPlayerXPos, puckXPosition, puckYPosition, firstPlayerScore, secondPlayerScore);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(gameStateMessage);
        }
    }
}
