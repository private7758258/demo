package com.ioexample.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
        * @description: netty初始化方法，初始化业务逻辑写在此处
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接通道连接完成");
    }

    /**
        * @description:
        * @param: ctx  上下文对象，含有通道channel，管道pipeline
        * @param: msg 客户端发送的数据
        * @return:
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf= (ByteBuf)msg;
        byte[] body=new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(body);
        System.out.println(new String(body));
        byte[] responseBytes = "hi,客户端,我是服务端".getBytes();
        ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(responseBytes));
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
