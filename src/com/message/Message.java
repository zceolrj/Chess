package com.message;

import java.io.Serializable;

/**
 * 消息接口
 * 
 * @author zceolrj
 *
 */
public interface Message extends Serializable 
{
    /**
     * 是否是本地消息
     * 
     * @return
     */
    public boolean isLocalMessage();
    
    /**
     * 获取消息头
     * 
     * @return
     */
    public Header getMessageHeader();
    
    /**
     * 设置消息头
     * 
     * @param header
     */
    public void setMessageHeader(Header header);
    
    /**
     * 获取消息体
     * 
     * @return
     */
    public Object getMessageBody();
    
    /**
     * 设置消息体
     * 
     * @param ob
     */
    public void setMessageBody(Object ob);
}
