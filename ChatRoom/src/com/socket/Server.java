package com.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Created by binwone on 14/10/2016.
 *
 */

public class Server {
    public static void main(String[] args) {
        int port =12345;//TCP端口号
        ServerSocket serverSocket=null;//声明服务器ServerSocket类
        try{
            serverSocket = new ServerSocket(port);//创建服务器
            Map<Socket,String> socketMap=new ConcurrentHashMap();//创建同步的Map存放连接状态的客户端及其用户名
            while (true) {
                Socket socket = serverSocket.accept();//阻塞等待客户端连接
                try{
                    new Mult(socket,socketMap);//为刚连接的客户端创建单独线程
                }catch(IOException e){
                    socket.close();//线程启动故障
                }
            }
        }catch(IOException e){
            System.out.println("Server starting failed");//服务器创建故障
        }
    }
}

