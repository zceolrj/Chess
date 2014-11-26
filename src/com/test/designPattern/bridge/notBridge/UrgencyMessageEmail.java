package com.test.designPattern.bridge.notBridge;

public class UrgencyMessageEmail implements UrgencyMessage
{
    public void send(String message, String toUser)
    {
    	message = "加急:"+message;
    	System.out.println("使用Email的方式发送消息"
    			+message+"给"+toUser);
    }
    public Object watch(String messageId)
    {
    	return messageId;
    }
}
