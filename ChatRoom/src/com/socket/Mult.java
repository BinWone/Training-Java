package com.socket;

import java.io.*;
import java.net.*;
import java.util.Map;


class Mult extends Thread {
    private Socket socket;
    private Map<Socket,String> socketMap;
    private DataOutputStream dos;
    private DataInputStream dis;
    String strName[]=null;
    public Mult(Socket s,Map<Socket,String> sM) throws IOException{
        socket = s;
        socketMap=sM;
        //用于向客户端发送数据的输出流
        dos = new DataOutputStream(socket.getOutputStream());
        //用于接收客户端发来的数据的输入流
        dis = new DataInputStream(socket.getInputStream());
        start();
    }
    public void run(){
        try{
            //登录检测
            dos.writeUTF("Please login:");
            while(true){
                String str = dis.readUTF();
                strName = str.split(" +");
                if(str.startsWith("/login")&&(strName.length>1))
                {
                    //检测用户名是否重复
                    int nameUsed = 0;
                    for(Socket s:socketMap.keySet()){
                        if(strName[1].equals(socketMap.get(s))){
                            nameUsed = 1;
                        }
                    }
                    if(nameUsed==1){
                        dos.writeUTF("This name has been used.Please choose another.");
                    }else{
                        //链接成功，将用户信息存入Map，并通知所有用户有人上线了
                        System.out.println(strName[1]+" has connected successfully");
                        socketMap.put(socket, strName[1]);
                        dos.writeUTF("You have logined.");
                        for(Socket s:socketMap.keySet()){
                            if(!s.equals(socket)){
                                dos = new DataOutputStream(s.getOutputStream());
                                dos.writeUTF(strName[1]+" has logined.");
                            }
                        }
                        break;
                    }
                }
                dos.writeUTF("Invalid command.");
            }
            while(true){
                //开始聊天
                int noticeNum=0;
                String str = dis.readUTF();
                if("/quit".equals(str))//检测是否退出
                {
                    System.out.println(strName[1]+" has finished");
                    break;
                }else if("/who".equals(str)){//为用户提供查看当前所有在线用户服务
                    dos = new DataOutputStream(socket.getOutputStream());
                    for(Socket s:socketMap.keySet()){
                        dos.writeUTF(socketMap.get(s));
                    }
                    dos.writeUTF("Total online user:"+String.valueOf(socketMap.size()));
                }else{
                    //消息处理（判断1群聊还是私聊2是否发送表情）
                    for(Socket s:socketMap.keySet()){
                        if((str.contains(socketMap.get(s)))&&(str.indexOf(socketMap.get(s))>0)&&('@'==str.charAt(str.indexOf(socketMap.get(s))-1))){
                            if(!str.startsWith("//")){
                                dos = new DataOutputStream(s.getOutputStream());
                                dos.writeUTF(strName[1]+" says to you: "+str.replace("@"+socketMap.get(s), ""));
                            }else{
                                dos = new DataOutputStream(s.getOutputStream());
                                dos.writeUTF(strName[1]+" says to you: "+emotion(str,socketMap.get(s)));
                            }
                            if(noticeNum<1){
                                if(!str.startsWith("//")){
                                    dos = new DataOutputStream(socket.getOutputStream());
                                    dos.writeUTF("you says to "+socketMap.get(s)+": "+str.replace("@"+socketMap.get(s), ""));
                                }else{
                                    dos = new DataOutputStream(socket.getOutputStream());
                                    dos.writeUTF("you says to "+socketMap.get(s)+": "+emotion(str,socketMap.get(s)));
                                }
                            }
                            noticeNum++;
                        }
                    }
                    if(noticeNum<1){
                        for(Socket s:socketMap.keySet()){
                            if(!s.equals(socket)){
                                if((str.startsWith("//"))&&(str.contains("@all"))){
                                    dos = new DataOutputStream(s.getOutputStream());
                                    dos.writeUTF(strName[1]+" says to all: "+emotion(str,"all"));
                                }else{
                                    dos = new DataOutputStream(s.getOutputStream());
                                    dos.writeUTF(strName[1]+" says to all: "+str);
                                }
                            }else{
                                if((str.startsWith("//"))&&(str.contains("@all"))){
                                    dos = new DataOutputStream(s.getOutputStream());
                                    dos.writeUTF(strName[1]+" says to all: "+emotion(str,"all"));
                                }else{
                                    dos = new DataOutputStream(s.getOutputStream());
                                    dos.writeUTF("You says to all: "+str);
                                }

                            }
                        }
                    }
                    noticeNum=0;
                }

            }
            socketMap.remove(this.socket);
            socket.close();
        }catch(IOException e){
            //System.out.println("lala"+e.getMessage());
        }
    }
    public String emotion(String cc,String str){
        //预定义表情
        char c=cc.charAt(2);
        if("all".equals(str)){
            if('G'==c)
                return("Hello everybody~");
            else if('A'==c)
                return("I hate all of you!");
            else if('B'==c)
                return("It's time for me to leave,bye-bye.");
            else
                return("There is no perset emotion for your words.");
        }else{
            if('G'==c)
                return("Nice to meet you,"+str+".");
            else if('A'==c)
                return(str+",you make me angry!");
            else if('B'==c)
                return(str+"I look forward to talk with you next time,goodbye.");
            else
                return("There is no perset emotion for your words.");
        }
    }
}

