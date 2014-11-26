package com.test.designPattern.bridge.isBridge;

public class SpecialUrgencyMessage extends AbstractMessage
{
    public SpecialUrgencyMessage(MessageImplementor impl)
    {
    	super(impl);
    }
    
    public void hurry(String messageId)
    {
    	System.out.println(messageId+" must hurry up");
    }
    
    public void sendMessage(String message, String toUser)
    {
    	message = "特急:"+message;
    	super.sendMessage(message, toUser);
    }
}
