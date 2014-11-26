package com.test.designPattern.bridge.isBridge;

/*
 * 以Email的方式发送消息
 */
public class MessageEmail implements MessageImplementor
{
    public void send(String message, String toUser)
    {
    	System.out.println("使用Email的方式发送消息"
    			+message+"给"+toUser);
    }
}
