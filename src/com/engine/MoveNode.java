package com.engine;

/**
 * 着法
 * 
 * @author soft
 *
 */
public class MoveNode 
{
    public int src;
    public int dst;
    public int cap;
    public String piece;
    public boolean chk;
    
    public MoveNode()
    {
    	src = dst = cap = -1;
    	chk = false;
    }
    
    public MoveNode(int src, int dst)
    {
    	this.src = src;
    	this.dst = dst;
    }
    
    public MoveNode(int src, int dst, String piece)
    {
    	this.src = src;
    	this.dst = dst;
    	this.piece = piece;
    }
    
    public MoveNode(String moveStr)
    {
    	move(moveStr);
    	chk = false;
    }
    
    public char[] location()//坐标
    {
    	char[] loc = new char[4];
    	loc[0] = (char)(ActiveBoard.FILE[src] + 'a');
    	loc[1] = (char)(ActiveBoard.RANK[src] + '0');
    	loc[2] = (char)(ActiveBoard.FILE[dst] + 'a');
    	loc[3] = (char)(ActiveBoard.RANK[dst] + '0');
    	
    	loc[0] = (char)(ActiveBoard.RANKS[src] + '0');
    	loc[1] = (char)(ActiveBoard.FILES[src] + 'a');
    	loc[2] = (char)(ActiveBoard.RANKS[dst] + '0');
    	loc[3] = (char)(ActiveBoard.FILES[dst] + 'a');
    	return loc;
    }
    
    public void move(String moveStr)
    {
    	//src = (char)(ActiveBoard.BOTTOM[moveStr.charAt(0) - 'a'] + moveStr.charAt(1) - '0');
    	//dst = (char)(ActiveBoard.BOTTOM[moveStr.charAt(2) - 'a'] + moveStr.charAt(3) - '0');
    	
    	src = (char)(ActiveBoard.BOTTOMS[moveStr.charAt(0) - '0'] + moveStr.charAt(1) - 'a');
    	dst = (char)(ActiveBoard.BOTTOMS[moveStr.charAt(2) - '0'] + moveStr.charAt(3) - 'a');
    	if(src<0 || src>=90 || dst<0 || dst>=90)
    	{
    		src = dst = -1;
    	}
    }
    
    public boolean equals(MoveNode move)
    {
    	if(this.src==move.src && this.dst==move.dst)
    	{
    		return true;
    	}
    	return false;
    }
    
    public String toString()
    {
    	return piece + ": "+ src+"--"+dst + "  cap:"+cap;
    }
}
