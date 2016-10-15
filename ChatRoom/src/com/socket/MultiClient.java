package com.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by binwone on 15/10/2016.
 */
public class MultiClient extends Thread {
    private Socket socket;
    private DataInputStream dis;
    private PrintWriter history;
    public MultiClient(Socket s,PrintWriter h) throws IOException{
        socket = s;
        history = h;
        dis = new DataInputStream(socket.getInputStream());

        start();
    }
    public void run(){
        try {
            while (true){
                String msgFromServer = dis.readUTF();
                System.out.println(msgFromServer);
                history.println(msgFromServer);
                history.flush();
            }
        }catch (IOException e){
            //e.printStackTrace();
        }
    }
}
