package com.message.impl;

import java.util.ArrayList;

import com.message.Message;
import com.message.MessageQueue;

/**
 * 象棋消息队列
 * 
 * @author zceolrj
 *
 */
public class ChessMessageQueue implements MessageQueue
{
    /**
     * 这是一个线性表,保存了消息.名为消息队列,其实是个线性表
     */
    private ArrayList<Message> msgQueue = new ArrayList<Message>();
    
    /* (non-Javadoc)
     * @see com.message.MessageQueue#getAMessage()
     * 
     * 获取一条消息
     */
    public synchronized Message getAMessage()
    {
    	if(msgQueue.size()>0)
    	{
    		return (Message)msgQueue.remove(0);
    	}
    	return null;
    }
    
    /* (non-Javadoc)
     * @see com.message.MessageQueue#putAMessage(com.message.Message)
     * 
     * 将一条消息放入队列
     */
    public synchronized void putAMessage(Message msg)
    {
    	msgQueue.add(msg);
    }
    
    /* (non-Javadoc)
     * @see com.message.MessageQueue#isEmpty()
     * 
     * 判断队列是否为空
     */
    public synchronized boolean isEmpty()
    {
    	return msgQueue.size()==0;
    }
    
    /* (non-Javadoc)
     * @see com.message.MessageQueue#removeAll()
     * 
     * 移除队列中所有的消息
     */
    public synchronized void removeAll()
    {
    	msgQueue.clear();
    }
}
