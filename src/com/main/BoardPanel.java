package com.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BoardPanel extends JPanel 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Image image;
	
	public BoardPanel()
	{
		this.image = new ImageIcon("image/CChess.GIF").getImage();
		Dimension size = new Dimension(image.getWidth(null),
				image.getHeight(null));
		setSize(size);//设置JPanel的大小为Image的大小
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}
	
	public BoardPanel(Image image)
	{
		this.image = image;
		Dimension size = new Dimension(image.getWidth(null),
				image.getHeight(null));
		setSize(size);//设置JPanel的大小为Image的大小
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
	}
	
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.drawImage(image, 0, 0, 500, 550, null);//用G把Image画出来
		
		Dimension size = this.getParent().getSize();
		g.drawImage(image, 0, 0, size.width, size.height, null);
	}
	
	
	
	

}
