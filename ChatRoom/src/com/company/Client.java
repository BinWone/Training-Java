package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by binwone on 13/10/2016.
 */
public class Client {
    Socket client;
    PrintWriter pw;
    public Client() throws UnknownHostException, IOException{
        client = new Socket("localhost",12345);
        pw = new PrintWriter(client.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        pw.write(br.readLine());
        pw.close();
        br.close();
    }
    public static void main(String[] args){
        try{
            new Client();

        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
