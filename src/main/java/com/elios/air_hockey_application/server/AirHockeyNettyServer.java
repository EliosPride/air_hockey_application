package com.elios.air_hockey_application.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static com.elios.air_hockey_application.common.AppConfiguration.PORT;

public class AirHockeyNettyServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new AirHockeyServerInitializer());

            ChannelFuture future = bootstrap.bind(PORT).sync();
            System.out.println("Server started on port " + PORT);

            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
