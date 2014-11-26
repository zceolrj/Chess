package com.pieces;

import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * 棋子类,继承JButton
 * 
 * @author zceolrj
 *
 */
public class Qizi extends JButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 坐标X
	 */
	protected int coordinateX;
	
	/**
	 * 坐标Y
	 */
	protected int coordinateY;
	
	/**
	 * 棋子类型
	 */
	protected int pieceType;
	
	/**
	 * 棋子尺寸大小
	 */
	protected int pieceSize;
	
	/**
	 * 棋子名称
	 */
	protected char pieceName;
	
	/**
	 * 是否死了
	 */
	protected boolean isDead;
	
	/**
	 * 棋子的图片
	 */
	protected Image image;
	
	/**
	 * 构造函数
	 * 
	 * @param qz
	 */
	public Qizi(Qizi qz)
	{
		this(qz.pieceName,qz.pieceType,qz.coordinateX,qz.coordinateY,qz.image,qz.pieceSize);
	}
	
    private Qizi()
    {
    	isDead = false;
    	this.setBorder(BorderFactory.createRaisedBevelBorder());
    	this.setBorderPainted(false);
    	this.setOpaque(false);
    	this.setVisible(false);
    }
    
    public Qizi(char name, int type)
    {
    	this();
    	pieceName = name;
    	pieceType = type;
    }
    
    public Qizi(char name, int type, Image img, int gsize)
    {
    	this();
    	pieceName = name;
    	pieceType = type;
    	setImage(img, gsize);
    }
    
    public Qizi(char name,int type, int x, int y, Image img, int gsize)
    {
    	this();
    	coordinateX = x;
    	coordinateY = y;
    	pieceType = type;
    	pieceName = name;
    	setImage(img, gsize);
    }
    
    public void setImage(Image img)
    {
    	setImage(img, pieceSize);
    }
    
    /**
     * 设置图片
     * 
     * @param img 图片
     * @param gsize 尺寸
     */
    public void setImage(Image img, int gsize)
    {
    	pieceSize = gsize;
    	image = img.getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
    	this.setSize(pieceSize, pieceSize);
    	this.setIcon(new ImageIcon(image));
    }
    
    public void resetState()
    {
    	isDead = false;
    	this.setBorderPainted(false);
    	this.setVisible(false);
    	this.setEnabled(true);
    	isDead = false;
    }
    
    public String toString()
    {
    	return "" + getPieceName() + coordinateX + "," + coordinateY;
    }
    
    public void setGridSize(int gsize)
    {
    	pieceSize = (int)(gsize*0.9);
    	this.setSize(pieceSize, pieceSize);
    }
    
    public void setCoordinate(int x, int y)
    {
    	coordinateX = x;
    	coordinateY = y;
    }
    
    public void setCoordinate(int square)
    {
    	coordinateX = square/10;
    	coordinateY = square%10;
    }
    
    /**
     * ----------------------------------------------------关注here
     * 
     * @return
     */
    public int getCoordinate()
    {
    	return coordinateX*10 + coordinateY;
    }
    
    //下面这两个方法为自己添加的
    public void setCoordinates(int square)
    {
    	coordinateX = square%9;
    	coordinateY = square/9;
    }
    
    public int getCoordinates()
    {
    	return coordinateY*9+coordinateX;
    }
    
    
    public void setToDead()
    {
    	isDead = true;
    }
    
    public void setToAlive()
    {
    	isDead = false;
    }
    
    public char getPieceName()
    {
    	return pieceName;
    }
    
    public int getPieceType()
    {
    	return pieceType;
    }
    
    public int getCoordinateX()
    {
    	return coordinateX;
    }
    
    public int getCoordinateY()
    {
    	return coordinateY;
    }
    
    public void setCoordinateX(int x){
		coordinateX = x;
	}
	public void setCoordinateY(int y){
		coordinateY = y;
	}
}
