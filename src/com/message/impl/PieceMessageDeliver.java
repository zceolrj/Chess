package com.message.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.message.Consumer;
import com.message.Message;
import com.message.MessageDeliver;
import com.message.MessageQueue;
import com.message.support.ObjectCopyer;

/**
 * 棋子消息传递
 * 
 * @author zceolrj
 *
 */
public class PieceMessageDeliver extends Thread implements MessageDeliver
{
    /**
     * 日志
     */
    private static Log log = LogFactory.getLog(PieceMessageDeliver.class);
    
    /**
     * 消息队列
     */
    private MessageQueue msgQueue = null;
    
    /**
     * 消费者线性表
     */
    private List<Consumer> consumers = new ArrayList<Consumer>();
    
    /**
     * 构造函数
     * 
     * @param msgQueue
     */
    public PieceMessageDeliver(MessageQueue msgQueue)
    {
    	this.msgQueue = msgQueue;
    }
    
    /* (non-Javadoc)
     * @see com.message.MessageDeliver#registerAConsumer(com.message.Consumer)
     * 
     * 注册一个消费者----在消费者列表中增加一个消费者
     */
    public void registerAConsumer(Consumer consumer)
    {
    	consumers.add(consumer);
    }
    
    /* (non-Javadoc)
     * @see com.message.MessageDeliver#removeAConsumer(com.message.Consumer)
     * 
     * 在消费者列表中移除一个消费者
     */
    public void removeAConsumer(Consumer consumer)
    {
    	consumers.remove(consumer);
    }
    
    /* (non-Javadoc)
     * @see com.message.MessageDeliver#deliveryAMessage(com.message.Message)
     * 
     * 传递一条消息
     */
    public void deliveryAMessage(Message msg)
    {
    	//对于消费者列表中的每一个消费者,都依次消费这条消息
    	for(int i=0;i<consumers.size();i++)
    	{
    		Message newMsg = (Message)ObjectCopyer.getACopy(msg);
    		((Consumer)consumers.get(i)).consumeMessage(newMsg);
    	}
    }
    
    /* (non-Javadoc)
     * @see java.lang.Thread#run()
     * 
     * 该线程的run方法---不断地从消息队列中获取一条消息,然后传递该消息
     */
    public void run()
    {
    	while(true)
    	{
    		Message m = null;
    		synchronized(msgQueue)
    		{
    			if(msgQueue.isEmpty())
    			{
    				try
    				{
    					msgQueue.wait();
    					log.info("all message delivered!");
    				}
    				catch(InterruptedException e)
    				{
    					e.printStackTrace();
    				}
    			}
    			//从消息队列中获取一条消息
    			m = msgQueue.getAMessage();
    			msgQueue.notifyAll();
    		}
    		//传递该消息,实际是消费队列中的每个消费者都消费该消息
    		deliveryAMessage(m);
    	}
    }
}
