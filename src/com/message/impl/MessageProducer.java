package com.message.impl;

import com.message.Message;
import com.message.MessageQueue;
import com.message.Producer;

/**
 * 消息生产者---该类的主要作用就是将消息放入消息队列中
 * 
 * @author zceolrj
 *
 */
public class MessageProducer implements Producer
{
    /**
     * 消息队列
     */
    private MessageQueue queue;
    
    /**
     * @param queue
     */
    public MessageProducer(MessageQueue queue)
    {
    	this.queue = queue;
    }
    
    /* (non-Javadoc)
     * @see com.message.Producer#setDestination(com.message.MessageQueue)
     * 
     * 设置目的队列
     */
    public void setDestination(MessageQueue queue)
    {
    	this.queue = queue;
    }
    
    /* (non-Javadoc)
     * @see com.message.Producer#send(com.message.Message)
     * 
     * 发送消息
     */
    public void send(Message msg)
    {
    	if(queue==null)
    	{
    		System.err.println("From MessageProducer:message queue is null!");
    		return;
    	}
    	synchronized(queue)
    	{
    		//将消息放到队列中
    		queue.putAMessage(msg);
    		queue.notifyAll();
    	}
    }
}
