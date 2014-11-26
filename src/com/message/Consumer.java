package com.message;

/**
 * 消费接口
 * 
 * @author zceolrj
 *
 */
public interface Consumer 
{
    /**
     * 消费一条消息
     * 
     * @param message
     */
    public void consumeMessage(Message message);
    
    /**
     * 注册一个监听器
     * 
     * @param listener
     */
    public void registerAListener(MessageListener listener);
    
    /**
     * 移除一个监听器
     * 
     * @param listener
     */
    public void removeAListener(MessageListener listener);
}
