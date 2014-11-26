package com.message.support;

/**
 * 连接接口
 * 
 * @author zceolrj
 *
 */
public interface Connection 
{
    /**
     * 是否可用
     * 
     * @return
     */
    public boolean available();
    
    /**
     * 发送数据
     * 
     * @param data
     */
    public void sendData(Object data);
    
    /**
     * 接收数据
     * 
     * @return
     */
    public Object receiveData();
}
