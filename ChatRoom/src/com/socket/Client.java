package com.socket;

import java.io.*;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author lqshanshuo
 */
public class Client {
    public static void main(String[] args){
        String hostName = "localhost";
        int portNumber = 12345;
        Socket socket=null;//声明Socket类
        try {
            try{
                socket = new Socket(hostName,portNumber);//新建socket连接
            }catch(Exception e){
                System.out.println("Connection failed!");
            }
            //获取用户键盘输入字符流
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            //获取输出流，用于客户端向服务器端发送数据
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            //本地创建文本文件存储历史记录
            String historyFileName;
            do{
                historyFileName=""+(int)(Math.random()*100000);
            }while(new File("./history/"+historyFileName+".txt").exists());
            File f = new File("./history/"+historyFileName+".txt");
            PrintWriter history = new PrintWriter(new FileWriter(f,true));

            try{
                new ReceiveThread(socket,history);//创建单独线程接收服务器信息
            }catch(IOException e){
                socket.close();
                f.delete();
                System.out.println("Receive Thread start failed!This socket is closed.");
            }
            while(true){
                //获取用户键盘输入
                String str = userInput.readLine();
                try{sleep(1);}catch(Exception e){System.out.println(e.getMessage());}
                //历史记录功能
                if(str.startsWith("/history")){
                    String[] strSplit=str.split(" +");
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
