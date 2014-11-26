package com.message;

public class MessageQueueNotSetException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageQueueNotSetException()
    {
    	super("Message queue not set");
    }
}
