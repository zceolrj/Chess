package com.main;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class TimeCounter implements Runnable
{
    private JLabel label;
    
    private String timeString;
    
    private boolean counting;
    
    public TimeCounter(JLabel label)
    {
    	this.label = label;
    	label.setHorizontalAlignment(SwingConstants.CENTER);
    	label.setVerticalAlignment(SwingConstants.CENTER);
    	timeString = "10:00";
    	counting = true;
    }
    
    public void run()
    {
    	while(counting)
    	{
    		String[] time = timeString.split(":");
    		String minuteString = time[0];
    		String secondString = time[1];
    		int minute = Integer.parseInt(minuteString);
    		int second = Integer.parseInt(secondString);
    		
    		label.setText(timeString);
    		second--;
    		
    		if(second<0)
    		{
    			second = 59;
    			minute--;
    		}
    		
    		if(minute<0)
    		{
    			JOptionPane.showMessageDialog(null, "lose");
    			break;
    		}   		
    		secondString = second<10?"0"+second:""+second;    		
    		minuteString = minute<10?"0"+minute:""+minute;
    		
    		timeString = minuteString+":"+secondString;
    		try 
    		{
				Thread.sleep(1000);
			} 
    		catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
    	}
    }

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public boolean isCounting() {
		return counting;
	}

	public void setCounting(boolean counting) {
		this.counting = counting;
	}
    
    
    
    
    
    
    
    
}
