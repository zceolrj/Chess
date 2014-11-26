package com.message;

/**
 * 消息传递接口
 * 
 * @author zceolrj
 *
 */
public interface MessageDeliver 
{
    /**
     * 注册一个消费者
     * 
     * @param consumer
     */
    public void registerAConsumer(Consumer consumer);
    
    /**
     * 移除一个消费者
     * 
     * @param consumer
     */
    public void removeAConsumer(Consumer consumer);
    
    /**
     * 传递一条消息
     * 
     * @param m
     */
    public void deliveryAMessage(Message m);
}
