package com.engine;

public class HashRecord
{
	long zobristLock;
	int flag;
	int depth;
	int value;
	MoveNode bestMove;
		
	public HashRecord()
	{
		zobristLock = 0;
		flag = 0;
		depth = 0;
		value = 0;
		bestMove = new MoveNode();
		
	}
	
	public HashRecord(int value, MoveNode move, int depth)
	{
		this.value = value;
		this.bestMove = move;
		this.depth = depth;
	}
	
	public String toString()
	{
		return bestMove.toString()+" ----value: "+value+" ----depth: "+depth;
	}
}
