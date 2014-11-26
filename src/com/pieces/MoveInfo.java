package com.pieces;

/**
 * 移动着法
 * 
 * @author zceolrj
 *
 */
public class MoveInfo 
{
    /**
     * 移动的棋子
     */
    private Qizi moveQizi;
    
    /**
     * 吃的棋子
     */
    private Qizi eatenQizi;
    
    /**
     * 新的X坐标
     */
    private int newLocX;
    
    /**
     * 新的Y坐标
     */
    private int newLocY;
    
    public MoveInfo()
    {
    	moveQizi = null;
    	eatenQizi = null;
    	newLocX = -1;
    	newLocY = -1;
    }
    
    //getter and setter
	public Qizi getMoveQizi() {
		return moveQizi;
	}

	public void setMoveQizi(Qizi moveQizi) {
		this.moveQizi = moveQizi;
	}

	public Qizi getEatenQizi() {
		return eatenQizi;
	}

	public void setEatenQizi(Qizi eatenQizi) {
		this.eatenQizi = eatenQizi;
	}

	public int getNewLocX() {
		return newLocX;
	}

	public void setNewLocX(int newLocX) {
		this.newLocX = newLocX;
	}

	public int getNewLocY() {
		return newLocY;
	}

	public void setNewLocY(int newLocY) {
		this.newLocY = newLocY;
	}
    
    
}
