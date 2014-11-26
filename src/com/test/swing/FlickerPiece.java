package com.test.swing;

import javax.swing.JButton;

public class FlickerPiece implements Runnable
{
    private JButton button;
    
    private boolean flicker;
    
    public FlickerPiece(JButton button, boolean flicker)
    {
    	this.button = button;
    	this.flicker = flicker;
    }
		
	public void run() 
	{
	    while(flicker)
	    {
	    	try 
	    	{
				button.setVisible(false);
	    		Thread.sleep(500);
	    		button.setVisible(true);
	    		Thread.sleep(500);
			} 
	    	catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
	    }
		
	}

	public JButton getButton() {
		return button;
	}

	public void setButton(JButton button) {
		this.button = button;
	}

	public boolean isFlicker() {
		return flicker;
	}

	public void setFlicker(boolean flicker) {
		this.flicker = flicker;
	}
    
}
