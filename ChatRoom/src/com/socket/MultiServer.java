package com.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

/**
 * Created by binwone on 15/10/2016.
 */

public class MultiServer extends Thread {
    private Socket socket;
    private Map<Socket,String > socketMap;
    private DataInputStream dis;
    private DataOutputStream dos;
    private DataOutputStream dos_new;

    String strName[];
    public MultiServer(Socket s,Map<Socket,String> sM) throws IOException{
        socket = s;
        socketMap = sM;
        // 接收来自客户端的数据
        dis = new DataInputStream(socket.getInputStream());
        // 向客户端发送数据
        dos = new DataOutputStream(socket.getOutputStream());

        start();
    }
    public void run(){
        try {
            dos.writeUTF("Please login: ");
            while (true){
                String userInput = dis.readUTF();
                strName = userInput.split(" ");
                // 登录检测
                if (userInput.startsWith("/login") && (strName.length > 1)){
                    // 检测用户名是否被使用
                    int nameUsed = 0;
                    for (Socket s : socketMap.keySet()){
                        if (strName[1].equals(socketMap.get(s))){
                            nameUsed = 1;
                        }
                    }
                    if (nameUsed == 1){
                        dos.writeUTF("Name exist, please choose anthoer name.");
                    }else {
                        // 连接成功，开始通信，将信息存入map，并通知所有用户此人已上线
                        System.out.println(strName[1] + " has connected successfully!");
                        socketMap.put(socket,strName[1]);
                        dos.writeUTF("You have logged in.");
                        for (Socket s : socketMap.keySet()){
                            if (!s.equals(socket)){
                                dos = new DataOutputStream(s.getOutputStream());
                                dos.writeUTF(strName[1] + " has logged in");
                            }
                        }
                        break;
                    }
                }
                dos.writeUTF("Invalid command!");
            }
        while (true){
            // begin talk
            String userInput = dis.readUTF();
            if (userInput.equals("/quit")){
                System.out.println(strName[1] + " has quit!");
                for (Socket s: socketMap.keySet()) {
                    dos = new DataOutputStream(s.getOutputStream());
                    dos.writeUTF(strName[1]+ " has quit!");
                }
                break;
            }else if (userInput.equals("/who")){
                dos = new DataOutputStream(socket.getOutputStream());
                for (Socket s: socketMap.keySet()){
                    dos.writeUTF(socketMap.get(s));
                }
                dos.writeUTF("Total online user: "+String.valueOf(socketMap.size()));
            }else {
                if (userInput.startsWith("/to")){
                    String userName = userInput.split(" ")[1]; // 这样就要求名字中不能包含空格
                    if (socketMap.values().contains(userName)){
                        for (Socket s:socketMap.keySet()){
                            if (userName.equals(socketMap.get(s)) && (!userName.equals(strName[1]))){
                                dos_new = new DataOutputStream(s.getOutputStream());
                                dos_new.writeUTF(strName[1] +"对你说："+userInput.substring(4+userName.length()));
                                dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeUTF("你对"+userName+"说："+userInput.substring(4+userName.length()));
                            }else if (userName.equals(socketMap.get(s)) && (userName.equals(strName[1]))){
                                dos = new DataOutputStream(socket.getOutputStream());
                                dos.writeUTF("Stop talking to yourself!");
                            }
                        }
                    }else {
                        dos.writeUTF(userName + " is not online.");
                    }

                }else if (userInput.startsWith("//hi")){
                    String userInputSplit[];
                    String broadcastNews;
                    userInputSplit = userInput.split(" ");
                    if (userInputSplit.length > 1){
                        broadcastNews = strName[1]+ "向" + userInputSplit[1] + "打招呼：\"Hi，你好啊！\"";
                    }else {
                        broadcastNews = strName[1] + "向大家打招呼：\"Hi，大家好，我来咯～\"";
                    }
                    for (Socket s: socketMap.keySet()){
                        dos_new = new DataOutputStream(s.getOutputStream());
                        dos_new.writeUTF(broadcastNews);
                    }
                }else if (!userInput.startsWith("/")){
                    dos.writeUTF("你说："+userInput);
                    for (Socket s: socketMap.keySet()){
                        dos_new = new DataOutputStream(s.getOutputStream());
                        dos_new.writeUTF(strName[1]+"说："+userInput);
                    }
                }else{
                    dos.writeUTF("Invaild command! Please input again.");
                }


            }
        }
        socketMap.remove(this.socket);
        socket.close();
        }catch (IOException e){
            //e.printStackTrace();
        }

    }

}
