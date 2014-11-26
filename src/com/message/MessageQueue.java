package com.message;

/**
 * 消息队列接口
 * 
 * @author zceolrj
 *
 */
public interface MessageQueue 
{
    /**
     * 获取一个消息
     * 
     * @return
     */
    public Message getAMessage();
    
    /**
     * 将一条消息放入队列
     * 
     * @param msg
     */
    public void putAMessage(Message msg);
    
    /**
     * 队列是否为空
     * 
     * @return
     */
    public boolean isEmpty();
    
    /**
     * 移除所有的消息
     */
    public void removeAll();
}
