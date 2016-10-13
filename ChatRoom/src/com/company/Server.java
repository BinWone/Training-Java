package com.company;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.ExportException;

/**
 * Created by binwone on 13/10/2016.
 * Java Training - Project1 Chat Room
 */
public class Server {
    private Socket socket;
    private ServerSocket ss;

    public Server() throws IOException{
        ss = new ServerSocket(12345);
        while (true){
            socket = ss.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("客户端发来的信息是：" + br.readLine());

            br.close();
        }
    }

    public static void main(String[] args){
        try{
            new Server();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
