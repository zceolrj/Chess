package com.test.piece;

import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Piece extends JButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7016534063570231556L;
	
	private int coordinateX;
	
	private int coordinateY;
	
	private int pieceType;
	
	private int pieceSize;
	
	private char pieceName;
	
	private boolean isDead;
	
	private Image image;
	
	public Piece()
	{
		isDead = false;
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setOpaque(false);
		this.setVisible(false);
	}
	
	public Piece(char pieceName, int pieceType)
	{
		this();
		this.pieceName = pieceName;
		this.pieceType = pieceType;
	}
	
	public Piece(char pieceName, int pieceType, Image image, int pieceSize)
	{
		this();
		this.pieceName = pieceName;
		this.pieceType = pieceType;
		setImage(image, pieceSize);
	}
	
	public Piece(char pieceName, int pieceType, int coordinateX, int coordinateY, Image image, int pieceSize)
	{
		this();
		this.pieceName = pieceName;
		this.pieceType = pieceType;
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		setImage(image, pieceSize);
	}
	
	public void setImage(Image image, int pieceSize)
	{
		this.pieceSize = pieceSize;
		this.image = image.getScaledInstance(pieceSize, pieceSize, Image.SCALE_SMOOTH);
		this.setSize(pieceSize, pieceSize);
		this.setIcon(new ImageIcon(this.image));
	}
	
	public void setPiece(Piece piece)
	{
		this.setCoordinateX(piece.getCoordinateX());
		this.setCoordinateY(piece.getCoordinateY());
		this.setPieceName(piece.getPieceName());
		this.setPieceType(piece.getPieceType());
		this.setImage(piece.getImage(), piece.getPieceSize());
	}

	public int getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(int coordinateX) {
		this.coordinateX = coordinateX;
	}

	public int getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(int coordinateY) {
		this.coordinateY = coordinateY;
	}

	public int getPieceType() {
		return pieceType;
	}

	public void setPieceType(int pieceType) {
		this.pieceType = pieceType;
	}

	public int getPieceSize() {
		return pieceSize;
	}

	public void setPieceSize(int pieceSize) {
		this.pieceSize = pieceSize;
	}

	public char getPieceName() {
		return pieceName;
	}

	public void setPieceName(char pieceName) {
		this.pieceName = pieceName;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	public Image getImage()
	{
		return image;
	}
	
    
}
