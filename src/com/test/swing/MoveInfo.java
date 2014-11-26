package com.test.swing;


public class MoveInfo 
{
    private int src;
    private int dst;
    private int newX;
    private int newY;
    
    public MoveInfo()
    {
    	dst = -1;
    }
    
    public MoveInfo(int src, int dst, int newX, int newY)
    {
    	this.src = src;
    	this.dst = dst;
    	this.newX = newX;
    	this.newY = newY;
    }
    
    public int getDstLocation()
    {
    	return (newX-5)/55+(newY-5)/55*9;
    }

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	public int getDst() {
		return dst;
	}

	public void setDst(int dst) {
		this.dst = dst;
	}

	public int getNewX() {
		return newX;
	}

	public void setNewX(int newX) {
		this.newX = newX;
	}

	public int getNewY() {
		return newY;
	}

	public void setNewY(int newY) {
		this.newY = newY;
	}
    
    
}
