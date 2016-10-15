package com.socket;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by binwone on 15/10/2016.
 */

public class Client {
    public static void main(String[] args) throws IOException{
        int port = 12345;
        String hostName = "localhost";
        Socket socket = null;
        try{
            try {
                socket = new Socket(hostName,port);
            }catch (IOException e){
                System.out.println("Connect failed!");
            }
            // 获取用户键盘输入
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            //获取输出流，用于客户端向服务器端发送数据
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String historyFileName;
            do{
                historyFileName=""+(int)(Math.random()*100000);
            }while(new File("./history/"+historyFileName+".txt").exists());
            File f = new File("./history/"+historyFileName+".txt");
            PrintWriter history = new PrintWriter(new FileWriter(f,true));

            try {
                new MultiClient(socket, history);
            }catch (IOException e){
                socket.close();
                System.out.println("Client Thread start failed! This socket is closed.");
            }
            while (true){
                //获取用户键盘输入
                String str = userInput.readLine();
                //历史记录功能
                if(str.startsWith("/history")){
                    String[] strSplit=str.split(" ");
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                    String lineTxt = null;
                    int i=0,sign=0;
                    System.out.println("-------------history-------------");
                    if(strSplit.length==1){//无参数时默认显示50条历史记录
                        while((lineTxt = br.readLine()) != null){
                            i++;
                            if(i<=50){
                                System.out.println(i+"."+lineTxt);
                                sign=1;
                            }
                        }
                    }
                    if(strSplit.length==2){//可选参数，从第几条开始显示
                        while((lineTxt = br.readLine()) != null){
                            i++;
                            if(i>=Integer.valueOf(strSplit[1])){
                                System.out.println(i+"."+lineTxt);
                                sign=1;
                            }
                        }
                    }
                    if(strSplit.length==3){//可选参数，一共显示多少条
                        while((lineTxt = br.readLine()) != null){
                            i++;
                            if(i>=(Integer.valueOf(strSplit[1]))+(Integer.valueOf(strSplit[2])))
                                break;
                            if(i>=Integer.valueOf(strSplit[1])){
                                System.out.println(i+"."+lineTxt);
                                sign=1;
                            }
                        }
                    }
                    if(sign==0)
                        System.out.println("No history meet the requirements");//无满足要求历史记录
                    System.out.println("------------------------------------");
                }else{
                    dos.writeUTF(str);
                    if("/quit".equals(str)){//退出
                        System.out.println("quit!!!!!!!!!!!!!!!");

                        f.delete();
                        break;
                    }
                }
            }
            socket.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}

