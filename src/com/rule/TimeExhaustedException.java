package com.rule;

/**
 * 时间耗尽异常
 * 
 * @author zceolrj
 *
 */
public class TimeExhaustedException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimeExhaustedException(String msg)
    {
    	super(msg);
    }
}
