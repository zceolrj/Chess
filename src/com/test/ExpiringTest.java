package com.test;

public class ExpiringTest implements TimeRunnable 
{
    private boolean expired;
    
    public void run()
    {
    	while(!expired)
    	{
    		try
    		{
    			System.out.println("I am still running...");
    			Thread.sleep(10000);
    		}
    		catch(InterruptedException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	System.out.println("expired!");
    }
    
    public synchronized void expire()
    {
    	expired = true;
    }
    
    public static void main(String[] args)
    {
    	TimedTaskExecuter.execute(new ExpiringTest(), 5000);
    }
}
