package com.engine;

public class KillerStruct
{
	int moveNum;
	MoveNode[] moveList;
	public KillerStruct()
	{
		moveNum = 0;
		moveList = new MoveNode[SearchEngine.MaxKiller];
		for(int i=0;i<SearchEngine.MaxKiller;i++)
		{
			moveList[i] = new MoveNode();
		}
		
	}
}
