package com.message.support;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 消息网络连接
 * 
 * @author zceolrj
 *
 */
public class MsgNetConnection extends Thread implements Connection
{
    /**
     * 1:master   2:client
     */
    private int pointType;
    
    /**
     * 服务器端
     */
    private Server masterPoint;
    
    /**
     * 客户端
     */
    private Client clientPoint;
    
    /**
     * 该连接是否可用
     */
    private boolean available;
    
    /**
     * 日志
     */
    private static Log log = LogFactory.getLog(MsgNetConnection.class);
    
    /**
     * 构造函数
     */
    public MsgNetConnection()
    {
    	pointType = 0;
    	masterPoint = null;
    	clientPoint = null;
    }
    
    /**
     * 返回输出流
     * 
     * @return
     */
    public DataOutputStream getDataOutputStream()
    {
    	//根据节点类型返回服务器或者客户端的输出流
    	if(pointType==1)//服务器端
    	{
    		return masterPoint.currentOut;
    	}
    	else//客户端
    	{
    		return clientPoint.out;
    	}
    }
    
    /**
     * 返回输入流
     * 
     * @return
     */
    public DataInputStream getDataInputStream()
    {
    	//根据节点类型返回服务器或客户端的输入流
    	if(pointType==1)
    	{
    		return masterPoint.currentIn;
    	}
    	else
    	{
    		return clientPoint.in;
    	}
    }
    
    /**
     * 返回类型  是服务器还是客户端
     * 
     * @return
     */
    public int getPointType()
    {
    	return pointType;
    }
    
    /**
     * 设置节点为服务器
     * 
     * @param port
     */
    public void setPointAsServer(int port)
    {
    	pointType = 1;
    	masterPoint = new Server(port);
    	clientPoint = null;
    }
    
    /**
     * 设置节点为客户端
     * 
     * @param ipAddress
     * @param port
     */
    public void setPointAsClient(String ipAddress, int port)
    {
    	pointType = 2;
    	clientPoint = new Client(ipAddress, port);
    	masterPoint = null;
    }
	
	/* (non-Javadoc)
	 * @see com.message.support.Connection#available()
	 * 
	 * 返回该连接是否可用
	 */
	public boolean available() 
	{
		return available;
	}

	/* (non-Javadoc)
	 * @see com.message.support.Connection#sendData(java.lang.Object)
	 * 
	 * 通过该连接发送数据
	 */
	public void sendData(Object data) 
	{
		ObjectOutputStream out;
		DataOutputStream dos = this.getDataOutputStream();
		try 
		{
			out = new ObjectOutputStream(new BufferedOutputStream(dos));
			out.writeObject(data);
			out.flush();
		} 
		catch(SocketException exc)
		{
			available = false;
			log.info(exc.getMessage());
		}
		catch (IOException e) 
		{
			log.info(e.getMessage());
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.message.support.Connection#receiveData()
	 * 
	 * 通过该连接接收数据
	 */
	public Object receiveData() 
	{
		ObjectInputStream in;
		DataInputStream dis = this.getDataInputStream();
		Object result = null;
		try 
		{
			in = new ObjectInputStream(new BufferedInputStream(dis));
			result = in.readObject();
		} 
		catch (IOException e) 
		{
			log.info(e.getMessage());
			available = false;
			return null;
		} 
		catch (ClassNotFoundException e) 
		{
			log.info(e.getMessage());
			return null;
		}
		return result;
	}
    
	/**
	 * 创建连接
	 */
	public void createConnection()
	{
		if(pointType==1)//如果是服务器
		{
			System.out.println("Accept connecting from Client....");
			masterPoint.createConnect();
			if(masterPoint.isConnected)
			{
				available = true;
				System.out.println("A client has connected!!");
			}
		}
		else
		{
			while(!clientPoint.isConnected)
			{
				log.info("Trying connected to Server...");
				clientPoint.createConnect();
				if(clientPoint.isConnected)
				{
					available = true;
					log.info("Have Already Connected to Server!!");
					return;
				}
				else
				{
					try 
					{
						Thread.sleep(2000);
					} 
					catch (InterruptedException e) 
					{
						log.info(e.getMessage());
						e.printStackTrace();
					}
				}
			}
			if(!available)
			{
				log.info("Cannot get connection!!!");
			}
		}
	}
}
