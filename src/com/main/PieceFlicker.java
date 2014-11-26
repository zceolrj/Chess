package com.main;

import javax.swing.JLabel;

public class PieceFlicker implements Runnable 
{
    private JLabel label;
    
    private boolean flicker;
    
    public PieceFlicker(JLabel label, boolean flicker)
    {
    	this.label = label;
    	this.flicker = flicker;
    }
	
	public void run() 
	{
		while(flicker)
		{			
			try 
			{
				label.setVisible(false);
				Thread.sleep(500);
				
				label.setVisible(true);
				Thread.sleep(500);
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

	public boolean isFlicker() {
		return flicker;
	}

	public void setFlicker(boolean flicker) {
		this.flicker = flicker;
	}
    
}
