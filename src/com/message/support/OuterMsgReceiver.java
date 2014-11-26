package com.message.support;

import com.message.Message;
import com.message.Producer;

/**
 * 外部消息接收器---------------------------------？？？？？？？？？？？？？
 * 
 * @author zceolrj
 *
 */
public class OuterMsgReceiver extends Thread
{
    /**
     * 连接
     */
    private Connection connection;
    
    /**
     * 生产者
     */
    private Producer producer;
    
    /**
     * 是否接收数据
     */
    private boolean acceptData;
    
    /**
     * 构造函数
     */
    public OuterMsgReceiver()
    {
    	super("OuterReceiver");
    	connection = null;
    	producer = null;
    }
    
    /**
     * 构造函数
     * 
     * @param producer
     */
    public OuterMsgReceiver(Producer producer)
    {
    	super("OuterReceiver");
    	this.producer = producer;
    	connection = null;
    }
    
    /**
     * 构造函数
     * 
     * @param con
     */
    public OuterMsgReceiver(Connection con)
    {
    	super("OuterReceiver");
    	producer = null;
    	connection = con;
    }
    
    /**
     * 构造函数
     * 
     * @param p
     * @param con
     */
    public OuterMsgReceiver(Producer p, Connection con)
    {
    	super("OuterReceiver");
    	this.producer = p;
    	this.connection = con;
    }
    
    /**
     * 是否可用
     * 
     * @return
     */
    private boolean available()
    {
    	//如果生产者为空或者连接为空,返回false
    	if(producer==null || connection==null)
    	{
    		return false;
    	}
    	return connection.available();
    }
      
    /**
     * 开始接收数据
     */
    public void startReceiveData()
    {
    	//设为true  启动线程
    	acceptData = true;
    	this.start();
    }
    
    /**
     * 停止接收数据
     */
    public void stopReceiveData()
    {
    	acceptData = false;
    }
    
    /**
     * 接收----该方法很重要,它将从客户端或服务器接收到的数据放到消息队列中,
     * 然后有另外的线程会处理消息队列中的消息
     */
    public void receive()
    {
    	Object data = null;
    	//如果接收器可用
    	if(available())
    	{
    		//如果连接接收的数据不为空    则生产者发送该数据
    		if((data=connection.receiveData())!=null)
    		{
    			producer.send((Message)data);
    		}
    	}
    	else//否则就停止接收数据
    	{
    		System.err.println("OuterMsgReceiver is not available, "+
    	            "you maybe need invoke setMsgQueue(msg) or/and setConnection(con)!!");
    		stopReceiveData();
    	}
    }
    
    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     * 
     * run方法
     */
    public void run()
    {
    	//如果可以接收数据
    	while(acceptData)
    	{
    		//接收数据
    		receive();
    	}
    }
    
    //getter and setter
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Producer getProducer() {
		return producer;
	}

	public void setProducer(Producer producer) {
		this.producer = producer;
	}
    
}
