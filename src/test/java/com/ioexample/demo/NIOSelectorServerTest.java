package com.ioexample.demo;


import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * @author hyf
 * @version 1.0
 * @description: TODO
 * @date 2024/4/14 3:02
 */
class NIOSelectorServerTest {

    //创建客户端连接
    static List<SocketChannel> channelList = new ArrayList();

    @Test
    public void NiOSelectorServer() throws Exception{
        //创建NIO
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(9000));
        //设置ServerSocketChannel为非阻塞
        serverSocket.configureBlocking(false);
        //开发Selector(多路复用器)处理Channel，即创建epoll(事件轮询)
        Selector selector = Selector.open();
        //注册到serverSocket,OP_ACCEPT 是IO的 建立连接事件
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务启动成功");
        while (true){
            //阻塞等待需要处理的事件发生
            //虽然会阻塞，但是并不会占用CPU
            //等待注册到Selector中的事件
            selector.select();

            //获取selector中注册的全部事件的selectionKey实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            //遍历SelectionKey对事件进行处理
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //如果是OP_ACCEPT事件，则进行连接获取和事件
                if (key.isAcceptable()){
                    ServerSocketChannel server =(ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    //这里值注册了读事件，如果需要给客户端发送数据可以注册写事件
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");
                }else if ( key.isReadable() ){
                    //弱国是OP_READ事件，则进行读取和打印
                    SocketChannel socketChannel =(SocketChannel) key.channel();
                    //创建缓冲区
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    //非阻塞模式read方法不会阻塞，
                    int len = socketChannel.read(byteBuffer);
                    //如果有数据，把数据打印出来
                    if (len > 0){
                        System.out.println("接收到的消息: "+ byteBuffer.array());
                    }else if (len == -1){
                        iterator.remove();
                        System.out.println("客户端断开连接");
                        socketChannel.close();
                    }
                }
                //从事件集合中删除本次处理的key，防止下次select重复处理
                iterator.remove();
            }
        }
    }
}