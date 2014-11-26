package com.message.support;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 客户端
 * 
 * @author zceolrj
 *
 */
public class Client 
{
    /**
     * 客户端socket
     */
    Socket clientSocket = null;
    
    /**
     * 输出
     */
    DataOutputStream out;
    
    /**
     * 输入
     */
    DataInputStream in;
    
    /**
     * 服务器的IP
     */
    private String serverIP;
    
    /**
     * 服务器的端口号
     */
    int port;
    
    /**
     * 是否连接成功
     */
    public boolean isConnected;
    
    /**
     * 构造函数
     * 
     * @param ipAdd 服务器的IP
     * @param portNum 服务器的端口号
     */
    public Client(String ipAdd, int portNum)
    {
    	this.serverIP = ipAdd;
    	port = portNum;
    	isConnected = false;
    }
    
    /**
     * 创建连接
     */
    public void createConnect()
    {
    	try
    	{
    		clientSocket = new Socket(InetAddress.getByName(serverIP), port);
    		out = new DataOutputStream(clientSocket.getOutputStream());
    		in = new DataInputStream(clientSocket.getInputStream());
    		isConnected = true;
    	}
    	catch(UnknownHostException e)
    	{
    		System.err.println("Don't know about host:localhost");
    		//e.printStackTrace();
    	} 
    	catch (IOException e) 
    	{
			System.err.println("Couldn't get I/O for the connection to:localhost");
    		//e.printStackTrace();
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
    		out.writeUTF(message);
    		out.flush();
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
    		message = in.readUTF();
    	}
    	catch(IOException e)
    	{
    		System.err.println("");
    		e.printStackTrace();
    		return null;
    	}
    	return message;
    }
}
