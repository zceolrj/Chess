package com.pieces;

/**
 * 不合法的移动异常
 * 
 * @author zceolrj
 *
 */
public class InvalidMoveException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidMoveException(String msg)
    {
    	super(msg);
    }
}
