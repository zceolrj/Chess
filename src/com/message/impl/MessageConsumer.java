package com.message.impl;

import java.util.ArrayList;

import com.message.Consumer;
import com.message.Message;
import com.message.MessageListener;
import com.message.support.ObjectCopyer;

/**
 * 消息消费者
 * 
 * @author zceolrj
 *
 */
public class MessageConsumer implements Consumer
{
    /**
     * 消息监听器线性表
     */
    private ArrayList<MessageListener> listeners = new ArrayList<MessageListener>();
    
    /* (non-Javadoc)
     * @see com.message.Consumer#consumeMessage(com.message.Message)
     * 
     * 消费一条消息
     */
    public void consumeMessage(Message msg)
    {
    	for(int i=0;i<listeners.size();i++)
    	{
    		Message newMsg = (Message)ObjectCopyer.getACopy(msg);
    		((MessageListener)listeners.get(i)).onMessage(newMsg);
    	}
    }
    
    /* (non-Javadoc)
     * @see com.message.Consumer#registerAListener(com.message.MessageListener)
     * 
     * 注册一个监听器
     */
    public void registerAListener(MessageListener listener)
    {
    	listeners.add(listener);
    }
    
    /* (non-Javadoc)
     * @see com.message.Consumer#removeAListener(com.message.MessageListener)
     * 
     * 移除一个监听器
     */
    public void removeAListener(MessageListener listener)
    {
    	listeners.remove(listener);
    }
}
