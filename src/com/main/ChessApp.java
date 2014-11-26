package com.main;

import javax.swing.JFrame;

public class ChessApp 
{
    public static void main(String[] args)
    {
    	JFrame.setDefaultLookAndFeelDecorated(true);
    	ChessMainFrame frame = new ChessMainFrame();
    	frame.setVisible(true);
    }
}
