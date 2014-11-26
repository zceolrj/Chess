package com.message.support;

import com.message.Message;

/**
 * 外部消息发送器
 * 
 * @author zceolrj
 *
 */
public class OuterMsgSender 
{
    /**
     * 连接
     */
    private Connection connection;
    
    /**
     * 构造函数
     */
    public OuterMsgSender()
    {
    	connection = null;
    }
    
    /**
     * @param con
     */
    public OuterMsgSender(Connection con)
    {
    	connection = con;
    }
    
    /**
     * 是否可用
     * 
     * @return true/false
     */
    public boolean available()
    {
    	//如果连接不为空且连接可用,则该消息发送器可用
    	return (connection!=null && connection.available());
    }
       
    /**
     * 发送消息
     * 
     * @param msg
     */
    public void send(Message msg)
    {
    	//如果发送器可用,则发送消息
    	if(available())
    	{
    		//通过连接发送消息
    		connection.sendData(msg);
    	}
    	else
    	{
    		System.err.println("OuterMsgSender is not available, "+
    	             "you maybe need invoke setConnection(con)!");
    	}
    }
    
    
    public Connection getConnection()
    {
    	return connection;
    }
    
    public void setConnection(Connection connection) {
		this.connection = connection;
	}
}
