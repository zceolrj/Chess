package com.pieces;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 * 
 * 
 * @author zceolrj
 *
 */
public class PieceObject implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	/**
	 * 
	 */
	private int[] piecesType = null;
	
	private byte[][] pictureData = null;
	
	private byte[] header = null;
	
	private boolean decoded = false;
	
	private PieceObject instance;
	
	public PieceObject()
	{
		
	}
	
	public PieceObject(byte[] h, int[] piecesType, byte[][] pictureData)
	{
		this.header = h;
		this.piecesType = piecesType;
		this.pictureData = pictureData;
	}
	
	public int[] getPiecesType()
	{
		return piecesType;
	}
	
	/**
	 * 根据棋子类型获取图片
	 * 
	 * @param pieceType
	 * @return
	 */
	public Image getImage(int pieceType)
	{
		//return Toolkit.getDefaultToolkit().createImage(pictureData[pieceType]);
		return getImageSelf(pieceType);
	}
	
	public PieceObject getInstance()
	{
		if(instance!=null)
		{
			return instance;
		}
		
		InputStream is = null;
		ObjectInputStream ois = null;
		
		try
		{
			is = PieceObject.class.getClassLoader().getResourceAsStream("data/pieceObj");
			ois = new ObjectInputStream(is);
			//PieceObject piece = (PieceObject)ois.readObject();
			ois.close();
			instance = new PieceObject();
			return instance;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			instance = null;
			return null;
		} 
		/*catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			instance = null;
			return null;
		}*/
	}

		
	/**
	 * 目前是自己写的一个获取图片的方法----------------------here
	 * 
	 * @param pieceType
	 * @return
	 */
	public Image getImageSelf(int pieceType)
	{
		String path = "";
		switch(pieceType)
		{
		case 0:
			path = "images/wking.gif";
			break;
		case 7:
			path = "images/bking.gif";
			break;
		case 1:
			path = "images/wadvisor.gif";
			break;
		case 8:
			path = "images/badvisor.gif";
			break;
		case 2:
			path = "images/wbishop.gif";
			break;
		case 9:
			path = "images/bbishop.gif";
			break;
		case 3:
			path = "images/wknight.gif";
			break;
		case 10:
			path = "images/bknight.gif";
			break;
		case 4:
			path = "images/wrook.gif";
			break;
		case 11:
			path = "images/brook.gif";
			break;
		case 5:
			path = "images/wcannon.gif";
			break;
		case 12:
			path = "images/bcannon.gif";
			break;
		case 6:
			path = "images/wpawn.gif";
			break;
		case 13:
			path = "images/bpawn.gif";
			break;
		default:
			path = "";	
		}
		Image image = new ImageIcon(path).getImage();
		return image;
	}
	
	//getter and setter
	public byte[] getHeader() {
		return header;
	}

	public void setHeader(byte[] header) {
		this.header = header;
	}

	public boolean isDecoded() {
		return decoded;
	}

	public void setDecoded(boolean decoded) {
		this.decoded = decoded;
	}
}
