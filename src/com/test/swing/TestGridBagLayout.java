package com.test.swing;

import java.awt.Button;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class TestGridBagLayout extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;










	public TestGridBagLayout()
    {
    	setSize(800, 500);
    	setLayout(new GridBagLayout());
    	
    	Button btn1 = new Button("button01");
    	add(btn1, new GBC(0, 0, 1, 1).setAnchor(GBC.CENTER).setFill(GBC.NONE).setWeight(0, 0).setInsets(0).setIpad(0, 0));
    	
    	Button btn2 = new Button("button02");
    	add(btn2, new GBC(1, 0, 1, 1).setAnchor(GBC.CENTER).setFill(GBC.NONE).setWeight(0, 0).setInsets(0).setIpad(0, 0));
    	
    	Button btn3 = new Button("button03");
    	add(btn3, new GBC(2, 0, 1, 1).setAnchor(GBC.CENTER).setFill(GBC.NONE).setWeight(0, 0).setInsets(0).setIpad(0, 0));
    	
    	Button btn4 = new Button("button04");
    	add(btn4, new GBC(3, 0, 1, 1).setAnchor(GBC.CENTER).setFill(GBC.NONE).setWeight(0, 0).setInsets(0).setIpad(0, 0));
    	
    	Button btn5 = new Button("button05");
    	add(btn5, new GBC(0, 1, 4, 1).setAnchor(GBC.CENTER).setFill(GBC.NONE).setWeight(0, 0).setInsets(0).setIpad(0, 0));
    	
    	Button btn6 = new Button("button06");
    	add(btn6, new GBC(0, 2, 3, 1).setAnchor(GBC.CENTER).setFill(GBC.NONE).setWeight(0, 0).setInsets(0).setIpad(0, 0));
    	
    	Button btn7 = new Button("button07");
    	add(btn7, new GBC(3, 2, 1, 1).setAnchor(GBC.CENTER).setFill(GBC.NONE).setWeight(0, 0).setInsets(0).setIpad(0, 0));
    	
    	Button btn8 = new Button("button08");
    	add(btn8, new GBC(0, 3, 1, 2).setAnchor(GBC.CENTER).setFill(GBC.NONE).setWeight(0, 0).setInsets(0).setIpad(0, 0));
    	
    	Button btn9 = new Button("button09");
    	add(btn9, new GBC(1, 3, 3, 1).setAnchor(GBC.CENTER).setFill(GBC.NONE).setWeight(0, 0).setInsets(0).setIpad(0, 0));
    	
    	Button btn10 = new Button("button10");
    	add(btn10, new GBC(1, 4, 3, 1).setAnchor(GBC.CENTER).setFill(GBC.NONE).setWeight(0, 0).setInsets(0).setIpad(0, 0));
    	
    }
	
	
	
	
	
	
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		JFrame frame = new TestGridBagLayout();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}

}
