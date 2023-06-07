package com.elios.airhockeyapplication.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static com.elios.airhockeyapplication.common.AppConfiguration.*;


public class AirHockeyNettyServer {

    public static void main(String[] args) {
        Thread serverThread = new Thread(new ServerRunnable());
        serverThread.start();
    }

    private static class ServerRunnable implements Runnable {
        @Override
        public void run() {
            EventLoopGroup bossGroup = new NioEventLoopGroup(BOSS_THREADS);
            EventLoopGroup workerGroup = new NioEventLoopGroup(WORKER_THREADS);

            try {
                ServerBootstrap bootstrap = new ServerBootstrap()
                        .group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new AirHockeyServerInitializer());

                ChannelFuture future = bootstrap.bind(HOST, PORT).sync();
                System.out.println("Server started on: " + HOST + ":" + PORT);

                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                System.out.println("Error occurred during" + e.getMessage());
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
    }
}
