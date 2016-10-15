package com.socket;

import java.io.*;
import java.net.*;

/**
 *
 * @author lqshanshuo
 */
class ReceiveThread extends Thread{
    private Socket socket;
    private PrintWriter history;
    private DataInputStream dis;
    public ReceiveThread(Socket s,PrintWriter h) throws IOException{
        socket =  s;
        history = h;
        dis = new DataInputStream(socket.getInputStream());

        start();
    }
    public void run(){
        try{
            while(true){
                String strTemp=dis.readUTF();//读取服务器发送来的内容
                System.out.println(strTemp);//显示在屏幕上
                history.println(strTemp);//存在历史记录里
                history.flush();
            }
        }catch(IOException e){
            //System.out.println("ReceiveThread is over.");
        }
    }
}


