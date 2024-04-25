package com.ioexample.demo;


import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


class DemoApplicationTests {


    /**
        * @description: BIO测试 在支撑并发的时候有问题  所以不用
     */
    @Test
    void BIOTest() throws Exception{
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true){
            //命令控制telnet localhost 9000 建立连接
            System.out.println("等待连接...");
            final Socket client = serverSocket.accept();
            System.out.println("客户已连接");
            //创建个线程  使用子线程去处理逻辑
            //此种方法会产生OOM(内存溢出)
           new Thread(
                   new Runnable() {
                       public void run() {
                           try {
                               handler(client);
                           }catch (Exception e){
                               e.printStackTrace();
                           }
                       }
                   }
           ).start();

        }
    }

    public static void  handler(Socket client) throws Exception{
        byte[] bytes = new byte[1024];
        System.out.println("准备");
        //当客服端未向服务端发送消息  会阻塞
        //所以不使用BIO   这里未接收到数据  会一直阻塞
        int read = client.getInputStream().read(bytes);
        if (read != -1){
            System.out.println(new String(bytes, 0 ,read));
        }
    }

    //创建客户端连接
    static List<SocketChannel> channelList = new ArrayList();

    /**
      NIO测试
     */
    @Test
    public void NIOTest() throws Exception{
        //创建NIO
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(9000));
        //设置ServerSocketChannel为非阻塞
        serverSocket.configureBlocking(false);
        System.out.println("服务启动成功");
        while (true){
            //命令控制telnet localhost 9000 建立连接
            //非阻塞模式的accept方法不会阻塞
            //NIO的非阻塞是由操作系统内部实现的，底层调用了linux内核的accept函数
            SocketChannel socketChannel = serverSocket.accept();
           if (socketChannel != null){
               System.out.println("连接成功");
               //设置sicketChannel为非阻塞
               socketChannel.configureBlocking(false);
               //保存客户端连接在list中
               channelList.add(socketChannel);
           }
            //遍历连接进行数据读取
           Iterator<SocketChannel> iterator = channelList.iterator();
           while (iterator.hasNext()){
               SocketChannel sc = iterator.next();
               //创建缓冲区
               ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
               //非阻塞模式read方法不会阻塞，
               int len = sc.read(byteBuffer);
               //如果有数据，把数据答应出来
               if (len > 0){
                   System.out.println("接收到的消息: "+ byteBuffer.array());
               }else if (len == -1){
                   iterator.remove();
                   System.out.println("客户端断开连接");
                   sc.close();
               }
           }
        }
    }



}
