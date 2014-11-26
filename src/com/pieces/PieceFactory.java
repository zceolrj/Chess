package com.pieces;

import java.awt.Image;

/**
 * 棋子工厂
 * 
 * @author zceolrj
 *
 */
public class PieceFactory 
{
    /**
     * 棋子对象
     */
    private static PieceObject po;
    
    static
    {
    	po = new PieceObject().getInstance();
    }
    
    /**
     * 棋子尺寸大小
     */
    public static int PIECE_SIZE = 45;
    
    /**
     * 设置棋子尺寸大小
     * 
     * @param size
     */
    public static void setPieceSize(int size)
    {
    	PIECE_SIZE = size;
    }
    
    /**
     * 获取一个棋子
     * 
     * @param name 棋子名称
     * @return
     */
    public static Qizi getAPiece(char name)
    {
    	Image pieceImg;
    	int pieceType;
    	
    	switch(name)
    	{
    	case 'K'://帅
    		pieceImg = po.getImage(0);
    		pieceType = 0;
    		break;
    	case 'k'://
    		pieceImg = po.getImage(7);
    		pieceType = 7;
    		break;
    	case 'A':// 仕
			pieceImg = po.getImage(1);
			pieceType = 1;
			break;
		case 'a':
			pieceImg = po.getImage(8);
			pieceType = 8;
			break;
		case 'B':// 相
			pieceImg = po.getImage(2);
			pieceType = 2;
			break;
		case 'b':
			pieceImg = po.getImage(9);
			pieceType = 9;
			break;
		case 'N':// 马
			pieceImg = po.getImage(3);
			pieceType = 3;
			break;
		case 'n':
			pieceImg = po.getImage(10);
			pieceType = 10;
			break;
		case 'R':// 车
			pieceImg = po.getImage(4);
			pieceType = 4;
			break;
		case 'r':
			pieceImg = po.getImage(11);
			pieceType = 11;
			break;
		case 'C':// 炮
			pieceImg = po.getImage(5);
			pieceType = 5;
			break;
		case 'c':
			pieceImg = po.getImage(12);
			pieceType = 12;
			break;
		case 'P':
			pieceImg = po.getImage(6);
			pieceType = 6;
			break;
		default:
			pieceImg = po.getImage(13);
			pieceType = 13;
    	}
    	return new Qizi(name, pieceType, pieceImg, PIECE_SIZE);
    }
    
    /**
     * 获取一个棋子对象并设置它的X和Y坐标
     * 
     * @param pieceType 棋子类型
     * @param x 坐标X
     * @param y 坐标Y
     * @return
     */
    public static Qizi getAPiece(int pieceType, int x, int y)
    {
    	Qizi tmpPiece = getAPiece(pieceType);
    	tmpPiece.setCoordinate(x, y);
    	return tmpPiece;
    }
    
    /**
     * 获取一个棋子对象并设置它的X和Y坐标
     * 
     * @param name 棋子名称
     * @param x 坐标X
     * @param y 坐标Y
     * @return
     */
    public static Qizi getAPiece(char name, int x, int y)
    {
    	Qizi tmpPiece = getAPiece(name);
    	tmpPiece.setCoordinate(x, y);
    	return tmpPiece;
    }
    
    /**
     * 根据棋子类型获取一个棋子
     * 
     * @param pieceType 棋子类型
     * @return
     */
    public static Qizi getAPiece(int pieceType)
    {
    	Image pieceImg;
    	char pieceName;
    	
    	switch(pieceType)
    	{
    	case 0:
    		pieceImg = po.getImage(0);
    		pieceName = 'K';
    		break;
    	case 7:
			pieceImg = po.getImage(7);
			pieceName = 'k';
			break;
		case 1:// 仕
			pieceImg = po.getImage(1);
			pieceName = 'A';
			break;
		case 8:
			pieceImg = po.getImage(8);
			pieceName = 'a';
			break;
		case 2:// 相
			pieceImg = po.getImage(2);
			pieceName = 'B';
			break;
		case 9:
			pieceImg = po.getImage(9);
			pieceName = 'b';
			break;
		case 3:// 马
			pieceImg = po.getImage(3);
			pieceName = 'N';
			break;
		case 10:
			pieceImg = po.getImage(10);
			pieceName = 'n';
			break;
		case 4:// 车
			pieceImg = po.getImage(4);
			pieceName = 'R';
			break;
		case 11:
			pieceImg = po.getImage(11);
			pieceName = 'r';
			break;
		case 5:// 炮
			pieceImg = po.getImage(5);
			pieceName = 'C';
			break;
		case 12:
			pieceImg = po.getImage(12);
			pieceName = 'c';
			break;
		case 6:
			pieceImg = po.getImage(6);
			pieceName = 'P';
			break;
		default:
			pieceImg = po.getImage(13);
			pieceName = 'p';
    	}
    	
    	return new Qizi(pieceName, pieceType, pieceImg, PIECE_SIZE);
    }
}







