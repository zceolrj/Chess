package com.message.support;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread
{
    /**
     * 服务器socket
     */
    ServerSocket serverSocket = null;
    
    /**
     * 客户端socket
     */
    Socket clientSocket = null;
    
    /**
     * 输出
     */
    DataOutputStream currentOut = null;
    
    /**
     * 输入
     */
    DataInputStream currentIn = null;
    
    /**
     * 端口号
     */
    int port;
    
    /**
     * 是否连接成功
     */
    public boolean isConnected;
    
    /**
     * 构造函数
     * 
     * @param portNum
     */
    public Server(int portNum)
    {
    	port = portNum;
    }
    
    /**
     * 建立连接
     */
    public void createConnect()
    {
    	try 
    	{
			serverSocket = new ServerSocket(port);
		} 
    	catch (IOException e) 
    	{
			System.err.println("Could not listen on port:"+port);
    		e.printStackTrace();
		}
    	try
    	{
    		clientSocket = serverSocket.accept();
    		currentIn = new DataInputStream(new BufferedInputStream(
    				clientSocket.getInputStream()));
    		currentOut = new DataOutputStream(new BufferedOutputStream(
    				clientSocket.getOutputStream()));
    		isConnected = true;
    	}
    	catch(IOException e)
    	{
    		System.err.println("Accept failed.");
    		System.exit(1);
    	}
    }
    
    /**
     * 发送消息
     * 
     * @param message
     */
    public void sendAMessage(String message)
    {
    	try
    	{
    		currentOut.writeUTF(message);
    		currentOut.flush();
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    /**
     * 接收消息
     * 
     * @return
     */
    public String receiveAMessage()
    {
    	String message;
    	try
    	{
    		message = currentIn.readUTF();
    	}
    	catch(IOException e)
    	{
    		System.err.println("Cannot receive a message!");
    		e.printStackTrace();
    		return null;
    	}
    	return message;
    }
}
