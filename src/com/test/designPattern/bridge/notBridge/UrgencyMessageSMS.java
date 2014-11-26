package com.test.designPattern.bridge.notBridge;

public class UrgencyMessageSMS implements UrgencyMessage
{
    public void send(String message, String toUser)
    {
    	message = "加急:"+message;
    	System.out.println("使用站内短消息的方式，发送消息"
    			+message+"给"+toUser);
    }
    
    public Object watch(String messageId)
    {
    	return messageId;
    }
}
