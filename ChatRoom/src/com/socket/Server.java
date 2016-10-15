package com.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by binwone on 15/10/2016.
 */

public class Server {
    public static void main(String[] args) throws IOException{
        int port = 12345;
        try{
            ServerSocket ss = new ServerSocket(port);
            // 创建同步的map存放连接状态的客户端及其用户名
            Map<Socket,String> socketMap = new ConcurrentHashMap<>();
            while (true){
                Socket socket = ss.accept();
                try {
                    new MultiServer(socket,socketMap);
                }catch (IOException e){
                    socket.close(); // 线程启动故障
                }
            }
        }catch (IOException e){
            System.out.println("Server start failed!");
        }
    }
}
