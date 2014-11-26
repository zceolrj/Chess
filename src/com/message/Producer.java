package com.message;

/**
 * 生产接口-----两个方法：设置目的地和发送消息
 * 
 * @author zceolrj
 *
 */
public interface Producer 
{
    /**
     * 设置目的地
     * 
     * @param queue 消息队列
     */
    public void setDestination(MessageQueue queue);
    
    /**
     * 发送一条消息
     * 
     * @param msg
     */
    public void send(Message msg);
}
