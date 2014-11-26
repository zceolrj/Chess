package com.test.piece;

import java.awt.Image;

import javax.swing.ImageIcon;

public class PieceFactory 
{
    public static int PIECE_SIZE = 45;
    
    public static Piece getPiece(int index, char c)
    {
    	Piece piece = getPiece(c);
    	piece.setCoordinateX(index%9);
    	piece.setCoordinateY(index/9);
    	return piece;
    }
	
	public static Piece getPiece(char c)
    {
    	Image pieceImage;
    	int pieceType;
    	
    	switch(c)
    	{
    	    case 'k':
    		    pieceImage = new ImageIcon("image/bking.gif").getImage();
    		    pieceType = 0;
    		    break;
    	    case 'a':
    	    	pieceImage = new ImageIcon("image/badvisor.gif").getImage();
    	    	pieceType = 1;
    	    	break;
    	    case 'b':
    	    	pieceImage = new ImageIcon("image/bbishop.gif").getImage();
    	    	pieceType = 2;
    	    	break;
    	    case 'n':
    	    	pieceImage = new ImageIcon("image/bknight.gif").getImage();
    	    	pieceType = 3;
    	    	break;
    	    case 'r':
    	    	pieceImage = new ImageIcon("image/brook.gif").getImage();
    	    	pieceType = 4;
    	    	break;
    	    case 'c':
    	    	pieceImage = new ImageIcon("image/bcannon.gif").getImage();
    	    	pieceType = 5;
    	    	break;
    	    case 'p':
    	    	pieceImage = new ImageIcon("image/bpawn.gif").getImage();
    	    	pieceType = 6;
    	    	break;
    	    case 'K':
    	    	pieceImage = new ImageIcon("image/wking.gif").getImage();
    	    	pieceType = 7;
    	    	break;
    	    case 'A':
    	    	pieceImage = new ImageIcon("image/wadvisor.gif").getImage();
    	    	pieceType = 8;
    	    	break;
    	    case 'B':
    	    	pieceImage = new ImageIcon("image/wbishop.gif").getImage();
    	    	pieceType = 9;
    	    	break;
    	    case 'N':
    	    	pieceImage = new ImageIcon("image/wknight.gif").getImage();
    	    	pieceType = 10;
    	    	break;
    	    case 'R':
    	    	pieceImage = new ImageIcon("image/wrook.gif").getImage();
    	    	pieceType = 11;
    	    	break;
    	    case 'C':
    	    	pieceImage = new ImageIcon("image/wcannon.gif").getImage();
    	    	pieceType = 12;
    	    	break;
    	    default:
    	    	pieceImage = new ImageIcon("image/wpawn.gif").getImage();
    	    	pieceType = 13;
    	    	break;
    	}
    	
    	return new Piece(c, pieceType, pieceImage, PIECE_SIZE);
    }
	
	
	
	
	
	
	
	
}
