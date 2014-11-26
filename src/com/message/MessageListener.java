package com.message;

/**
 * 消息监听
 * 
 * @author zceolrj
 *
 */
public interface MessageListener 
{
    /**
     * @param msg
     */
    public void onMessage(Message msg);
}
