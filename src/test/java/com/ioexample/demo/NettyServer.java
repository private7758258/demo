package com.ioexample.demo;

import com.ioexample.netty.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Test;

public class NettyServer {
    public static void main(String[] args) {
        //创建两个线程组bossGroup和workerGroup，含有的子线程NioEventLoop的个数默认为cpu核数的两倍
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //bossGroup只是处理连接请求，真正的和客户端业务处理，会交给workerGroup完成
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);
        try {
            //创建服务器的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式变成来配置参数
            bootstrap
                    //设置两个线程组
                    .group(bossGroup, workerGroup)
                    //使用NioServerSocketChannel作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    //初始化服务器连接队列大小，服务器处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接
                    //多个客户端同时来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_RCVBUF, 64 * 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 64 * 1024)
                    //创建通道初始化对象,设置初始化参数，在socker
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //对workerGrop的SocketChannel设置处理器
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
//                        @Override
//                        protected void initChannel(Channel channel) throws Exception {
//                            ChannelPipeline pipeline = channel.pipeline();
//                            pipeline.addLast(new NettyServerHandler());
//                        }
                    });
            //绑定一个端口并且同步，生成了一个ChannelFuture异步对象，通过isDone()等方法可以判断异步事件的执行情况
            //启动服务器(并绑定端口),bing是异步操作,sync方法是等待异步操作执行完毕
            ChannelFuture channelFuture = bootstrap.bind(9000).sync();
            //给channelFuture 注册监听器，监听我们关心的事件
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
