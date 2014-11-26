package com.main;

public class CannotGetAgreeOnMoreThanThreeTimes extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CannotGetAgreeOnMoreThanThreeTimes()
    {
    	super("Can not get agree on more than three times.");
    }
}
