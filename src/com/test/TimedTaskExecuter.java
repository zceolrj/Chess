package com.test;

public class TimedTaskExecuter 
{
    private static class ExpiringThread extends Thread
    {
    	TimeRunnable timed;
    	long expired;
    	public ExpiringThread(TimeRunnable task, long expiredTime)
    	{
    		timed = task;
    		expired = expiredTime;
    	}
    	public void run()
    	{
    		try 
    		{
				Thread.sleep(expired);
			} 
    		catch (InterruptedException e) 
    		{
				e.printStackTrace();
			}
    		timed.expire();
    		
    	}
    }
    
    public static void execute(TimeRunnable task, long expiredTime)
    {
    	new Thread(task).start();
    	new ExpiringThread(task, expiredTime).start();
    }
}
