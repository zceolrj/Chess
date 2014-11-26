package com.test.designPattern.bridge.isBridge;

/*
 * 以手机短消息的方式发送消息
 */
public class MessageMobile implements MessageImplementor
{
    public void send(String message, String toUser)
    {
    	System.out.println("使用手机短消息的方式发送消息"
    			+message+"给"+toUser);
    }
}
