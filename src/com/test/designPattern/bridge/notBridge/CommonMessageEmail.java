package com.test.designPattern.bridge.notBridge;

/*
 * 以Email的方式发送普通消息
 */
public class CommonMessageEmail implements Message
{
    public void send(String message, String toUser)
    {
    	System.out.println("使用Email的方式发送消息"
    			+message+"给"+toUser);
    }
}
