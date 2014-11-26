package com.engine;


public class SortedMove 
{
    public int moveNum;
    public MoveNode[] moveList;
    public int[] valueList;
    
    public int[] horseChecks;
    public int[] pawnChecks;
    public int[] rookChecks;
    public int[] cannonChecks;
    
    public static final int[] ShellStep = {0, 1, 4, 13, 40, 121, 364, 1093};
    
    public final int[] MvvValues = {//int 48
    		//被攻击者的损失：帅士相马车炮兵
    		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    		0,4,4,4,4,8,8,12,12,8,8,4,4,4,4,4,
    		0,4,4,4,4,8,8,12,12,8,8,4,4,4,4,4
    };
    
    public SortedMove()
    {
    	moveNum = 0;
    	moveList = new MoveNode[256];
    	for(int i=0;i<256;i++)
    	{
    		moveList[i] = new MoveNode();
    	}
    	
    	valueList = new int[256];
    	
    	horseChecks = new int[8];
    	for(int i=0;i<8;i++)
    	{
    		horseChecks[i] = -1;
    	}
    	pawnChecks = new int[3];
    	for(int i=0;i<3;i++)
    	{
    		pawnChecks[i] = -1;
    	}
    	rookChecks = new int[4];
    	for(int i=0;i<4;i++)
    	{
    		rookChecks[i] = -1;
    	}
    	cannonChecks = new int[4];
    	for(int i=0;i<4;i++)
    	{
    		cannonChecks[i] = -1;
    	}
    }
    
    public void shellSort()
    {
    	int step;
    	int stepIndex=1;
    	int i,j;
    	int bestValue;
    	MoveNode bestMove;
    	do
    	{
    		stepIndex++;
    	}
    	while(ShellStep[stepIndex]<moveNum);
    	stepIndex--;
    	while(stepIndex!=0)
    	{
    		step = ShellStep[stepIndex];
    		for(i=step;i<moveNum;i++)
    		{
    			bestMove = moveList[i];
    			bestValue = valueList[i];
    			j = i-step;
    			while(j>=0 && bestValue>valueList[j])
    			{
    				moveList[j+step] = moveList[j];
    				valueList[j+step] = valueList[j];
    				j -= step;
    			}
    			moveList[j+step] = bestMove;
    			valueList[j+step] = bestValue;
    		}
    		stepIndex--;
    	}
    }
    
    public void bubbleSort(int k)
    {
    	int i;
    	int tempValue;
    	MoveNode tempMove;
    	for(i=moveNum-1;i>k;i--)
    	{
    		if(valueList[i]>valueList[i-1])
    		{
    			tempValue = valueList[i-1];
    			valueList[i-1] = valueList[i];
    			valueList[i] = tempValue;
    			
    			tempMove = moveList[i-1];
    			moveList[i-1] = moveList[i];
    			moveList[i] = tempMove;
    		}
    	}
    }
	
	public void generateAllMoves(ActiveBoard activeBoard, int histTab[][])
	{
		moveNum = 0;
		generateKingMoves(activeBoard, histTab);
		generateAdvisorMoves(activeBoard, histTab);
		generateBishopMoves(activeBoard, histTab);
		generateKnightMoves(activeBoard, histTab);
		generateRookMoves(activeBoard, histTab);
		generateCannonMoves(activeBoard, histTab);
		generatePawnMoves(activeBoard, histTab);
	}
	
	/**
	 * 生成所有的将军着法
	 * 
	 * @param activeBoard 动态局面
	 * @param player 将哪一方
	 */
	public void generateAllChecks(ActiveBoard activeBoard, int player)
	{
		int kingSquare = player==0?activeBoard.getPieceSquare(16):activeBoard.getPieceSquare(32);
				
		if(kingSquare!=-1)
		{
			int row = kingSquare/9;
			int column = kingSquare%9;
			String rowString = activeBoard.getRowString(row);
			String columnString = activeBoard.getColumnString(column);
			int rowNum = getDecimalFromBinary(rowString);
			int columnNum = getDecimalFromBinary(columnString);
			
			/*System.out.println("row: "+row);
			System.out.println("column: "+column);
			System.out.println("rowString: "+rowString);
			System.out.println("columnString: "+columnString);
			System.out.println("rowNum: "+rowNum);
			System.out.println("columnNum: "+columnNum);*/
			
			//马将军
			int horseIndex = 0;
			int[]dstHorseArr = new int[12];
			int[]dstHorseLegArr = new int[8];
			copyArr(dstHorseArr, IllegalMove.HorseChecks[kingSquare], 12);
			copyArr(dstHorseLegArr, IllegalMove.HorseCheckLegs[kingSquare], 8);
			for(int i=0;i<dstHorseArr.length;i++)
			{
				if(dstHorseArr[i]!=-1)
				{
					if(activeBoard.getSquarePiece(dstHorseLegArr[i])==0)
					{
						horseChecks[horseIndex] = dstHorseArr[i];
						horseIndex++;
					}
				}
				else
				{
					break;
				}
			}
			
			//兵将军
			int pawnIndex = 0;
			int[] dstPawnArr = new int[4];
			copyArr(dstPawnArr, IllegalMove.PawnChecks[kingSquare], 4);
			for(int i=0;i<dstPawnArr.length;i++)
			{
				if(dstPawnArr[i]!=-1)
				{
					pawnChecks[pawnIndex] = dstPawnArr[i];
					pawnIndex++;
				}
				else
				{
					break;
				}
			}
			
			//车将军
			int rookIndex = 0;
			int[] dstRookRankArr = new int[4];
			copyArr(dstRookRankArr, IllegalMove.RookRankChecks[column][rowNum], 4);
			for(int i=0;i<dstRookRankArr.length;i++)
			{
				if(dstRookRankArr[i]!=-1)
				{
				    rookChecks[rookIndex] = 9*row+dstRookRankArr[i];
				    //System.out.println("rookChecks["+rookIndex+"]: "+(9*row+dstRookRankArr[i]));
				    rookIndex++;
				}
				else
				{
					break;
				}
			}
			int[] dstRookFileArr = new int[4];
			copyArr(dstRookFileArr, IllegalMove.RookFileChecks[row][columnNum], 4);
			for(int i=0;i<dstRookFileArr.length;i++)
			{
				if(dstRookFileArr[i]!=-1)
				{
				    rookChecks[rookIndex] = 9*dstRookFileArr[i]+column;
				    //System.out.println("rookChecks["+rookIndex+"]: "+(9*dstRookFileArr[i]+column));
				    rookIndex++;
				}
				else
				{
					break;
				}
			}
			
			//炮将军
			int cannonIndex = 0;
			int[] dstCannonRankArr = new int[4];
			copyArr(dstCannonRankArr, IllegalMove.CannonRankChecks[column][rowNum], 4);
			for(int i=0;i<dstCannonRankArr.length;i++)
			{
				if(dstCannonRankArr[i]!=-1)
				{
					cannonChecks[cannonIndex] = 9*row+dstCannonRankArr[i];
					cannonIndex++;
				}
				else
				{
					break;
				}
			}
			int[] dstCannonFileArr = new int[4];
			copyArr(dstCannonFileArr, IllegalMove.CannonFileChecks[row][columnNum], 4);
			for(int i=0;i<dstCannonFileArr.length;i++)
			{
				if(dstCannonFileArr[i]!=-1)
				{
					cannonChecks[cannonIndex] = 9*dstCannonFileArr[i]+column;
					cannonIndex++;
				}
				else
				{
					break;
				}
			}
		}
				
	}
	
	public void generateKingMoves(ActiveBoard activeBoard, int histTab[][])
	{
		int king = activeBoard.getPlayer()==0?16:32;
		int srcSquare = activeBoard.getPieceSquare(king);
		int dstSquare;
		int attack;
		int[] dstArr = new int[8];
		int indexDst = 0;
		int listIndex = moveNum;
				
		if(srcSquare!=-1)
		{
			copyArr(dstArr, IllegalMove.KingMoves[srcSquare], 8);
			dstSquare = dstArr[indexDst];
			
			while(dstSquare!=-1)
			{
				attack = activeBoard.getSquarePiece(dstSquare);
				if((activeBoard.getPlayer()==0 && attack>31)||(activeBoard.getPlayer()==1 && attack<32 && attack>15))
				{//吃子
					moveList[listIndex].src = srcSquare;
					moveList[listIndex].dst = dstSquare;
					if(histTab==null)
					{
						valueList[listIndex] = MvvValues[attack];
					}
					else
					{
						valueList[listIndex] = histTab[srcSquare][dstSquare];
					}
					listIndex++;
				}
				else if(histTab!=null && attack==0)
				{
					moveList[listIndex].src = srcSquare;
					moveList[listIndex].dst = dstSquare;
					valueList[listIndex] = histTab[srcSquare][dstSquare];
					listIndex++;
				}
				indexDst++;
				dstSquare = dstArr[indexDst];
			}
		}
		moveNum = listIndex;
	}
	
	public void generateAdvisorMoves(ActiveBoard activeBoard, int histTab[][])
	{
		int king = activeBoard.getPlayer()==0?16:32;
		int srcSquare;
		int dstSquare;
		int attack;
		int indexDst = 0;
		int listIndex = moveNum;
		int[] dstArr = new int[8];
		int player = activeBoard.getPlayer();
		
		for(int i=1;i<=2;i++)
		{
			srcSquare = activeBoard.getPieceSquare(king+i);
			if(srcSquare!=-1)
			{
				copyArr(dstArr, IllegalMove.AdvisorMoves[srcSquare], 8);
				indexDst = 0;
				dstSquare = dstArr[indexDst];
				while(dstSquare!=-1)
				{
					attack = activeBoard.getSquarePiece(dstSquare);
					if((player==0&&attack>31)||(player==1&&attack>15&&attack<32))
					{//吃子
						moveList[listIndex].src = srcSquare;
						moveList[listIndex].dst = dstSquare;
						if(histTab==null)
						{
							valueList[listIndex] = MvvValues[attack] + 3;
						}
						else
						{
							valueList[listIndex] = histTab[srcSquare][dstSquare];
						}
						listIndex++;
					}
					else if(histTab!=null && attack==0)
					{
						moveList[listIndex].src = srcSquare;
						moveList[listIndex].dst = dstSquare;
						valueList[listIndex] = histTab[srcSquare][dstSquare];
						listIndex++;
					}
					indexDst++;
					dstSquare = dstArr[indexDst];
				}
			}
		}
		moveNum = listIndex;
	}
	
	public void generateBishopMoves(ActiveBoard activeBoard, int histTab[][])
	{
		int king = activeBoard.getPlayer()==0?16:32;
		int srcSquare;
		int dstSquare;
		int attack;
		int dstArr[] = new int[8];
		int eyeArr[] = new int[4];
		int indexDst = 0;
		int indexEye = 0;
		int listIndex = moveNum;
		int player = activeBoard.getPlayer();
		
		for(int i=3;i<=4;i++)
		{
			srcSquare = activeBoard.getPieceSquare(king+i);
			if(srcSquare!=-1)
			{
				copyArr(dstArr, IllegalMove.BishopMoves[srcSquare], 8);
				copyArr(eyeArr, IllegalMove.ElephantEyes[srcSquare], 4);
				indexDst = 0;
				indexEye = 0;
				dstSquare = dstArr[indexDst];
				
				while(dstSquare!=-1)
				{
					attack = activeBoard.getSquarePiece(dstSquare);
					if((player==0&&attack>31)||(player==1&&attack>15&&attack<32))
					{//吃子
						if(activeBoard.getSquarePiece(eyeArr[indexEye])==0)
						{
							moveList[listIndex].src = srcSquare;
							moveList[listIndex].dst = dstSquare;
							if(histTab==null)
							{
								valueList[listIndex] = MvvValues[attack] + 3;
							}
							else
							{
								valueList[listIndex] = histTab[srcSquare][dstSquare];
							}
							listIndex++;
						}
						
					}
					else if(histTab!=null && attack==0)
					{
						if(activeBoard.getSquarePiece(eyeArr[indexEye])==0)
						{
							moveList[listIndex].src = srcSquare;
							moveList[listIndex].dst = dstSquare;
							valueList[listIndex] = histTab[srcSquare][dstSquare];
							listIndex++;
						}
					}
					indexDst++;
					dstSquare = dstArr[indexDst];
					indexEye++;
				}
			}
		}
		moveNum = listIndex;
	}
	
	public void generateKnightMoves(ActiveBoard activeBoard, int histTab[][])
	{
		int king = activeBoard.getPlayer()==0?16:32;
		int srcSquare;
		int dstSquare;
		int attack;
		int[] dstArr = new int[12];
		int[] legArr = new int[8];
		int indexDst = 0;
		int indexLeg = 0;
		int listIndex = moveNum;
		int player = activeBoard.getPlayer();
						
		for(int i=5;i<=6;i++)
		{
			srcSquare = activeBoard.getPieceSquare(king+i);
			if(srcSquare!=-1)
			{				
				copyArr(dstArr, IllegalMove.KnightMoves[srcSquare], 12);
				copyArr(legArr, IllegalMove.HorseLegs[srcSquare], 8);
				indexLeg = 0;
				indexDst = 0;
				dstSquare = dstArr[indexDst];
				
				while(dstSquare!=-1)
				{
					attack = activeBoard.getSquarePiece(dstSquare);
					if((player==0&&attack>31)||(player==1&&attack>15&&attack<32))
					{//吃子
						if(activeBoard.getSquarePiece(legArr[indexLeg])==0)
						{
							moveList[listIndex].src = srcSquare;
							moveList[listIndex].dst = dstSquare;
							if(histTab==null)
							{
								valueList[listIndex] = MvvValues[attack]+2;
							}
							else
							{
								valueList[listIndex] = histTab[srcSquare][dstSquare];
							}
							listIndex++;
						}
					}
					else if(histTab!=null && attack==0)
					{
						if(activeBoard.getSquarePiece(legArr[indexLeg])==0)
						{
							moveList[listIndex].src = srcSquare;
							moveList[listIndex].dst = dstSquare;
							valueList[listIndex] = histTab[srcSquare][dstSquare];
							listIndex++;
						}
					}
					indexDst++;
					dstSquare = dstArr[indexDst];
					indexLeg++;
				}
			}
		}
		moveNum = listIndex;
	}
			
	public void generateRookMoves(ActiveBoard activeBoard, int[][] histTab)
	{
		int king = activeBoard.getPlayer()==0?16:32;
		int rookSrcSq;
		int rookDstSq;
		int attack;
		int[] dstRookArr = new int[12];
		int indexDst = 0;
		int listIndex = moveNum;
		int player = activeBoard.getPlayer();
		
		for(int i=7;i<=8;i++)
		{
			rookSrcSq = activeBoard.getPieceSquare(king+i);
			if(rookSrcSq!=-1)
			{
				int row = rookSrcSq/9;
				int column = rookSrcSq%9;
				String rowString = activeBoard.getRowString(row);
				String columnString = activeBoard.getColumnString(column);
				int rowNum = getDecimalFromBinary(rowString);
				int columnNum = getDecimalFromBinary(columnString);

				copyArr(dstRookArr, IllegalMove.RookRankCaps[column][rowNum], 4);
				
				indexDst = 0;
				rookDstSq = dstRookArr[indexDst];
				while(rookDstSq!=-1)
				{
					//计算得到rookDstSq的真正位置
					rookDstSq = 9*row + rookDstSq;
					attack = activeBoard.getSquarePiece(rookDstSq);
					if((player==0&&attack>31)||(player==1&&attack>15&&attack<32))
					{
						moveList[listIndex].src = rookSrcSq;
						moveList[listIndex].dst = rookDstSq;
						if(histTab==null)
						{
							valueList[listIndex] = MvvValues[attack] + 1;
						}
						else
						{
							valueList[listIndex] = histTab[rookSrcSq][rookDstSq];
						}
						listIndex++;
					}
					indexDst++;
					rookDstSq = dstRookArr[indexDst];
				}
				
				if(histTab!=null)
				{
					copyArr(dstRookArr, IllegalMove.RookCannonRankMoves[column][rowNum], 12);
					indexDst = 0;
					rookDstSq = dstRookArr[indexDst];
					while(rookDstSq!=-1)
					{
						rookDstSq = 9*row + rookDstSq;
						moveList[listIndex].src = rookSrcSq;
						moveList[listIndex].dst = rookDstSq;
						valueList[listIndex] = histTab[rookSrcSq][rookDstSq];
						
						listIndex++;
						indexDst++;
						rookDstSq = dstRookArr[indexDst];
					}
				}
				
				copyArr(dstRookArr, IllegalMove.RookFileCaps[row][columnNum], 4);
				indexDst = 0;
				rookDstSq = dstRookArr[indexDst];
				while(rookDstSq!=-1)
				{
					rookDstSq = 9*rookDstSq + column;
					attack = activeBoard.getSquarePiece(rookDstSq);
					if((player==0&&attack>31)||(player==1&&attack>15&&attack<32))
					{
						moveList[listIndex].src = rookSrcSq;
						moveList[listIndex].dst = rookDstSq;
						if(histTab==null)
						{
							valueList[listIndex] = MvvValues[attack] + 1;
						}
						else
						{
							valueList[listIndex] = histTab[rookSrcSq][rookDstSq];
						}
						listIndex++;
					}
					indexDst++;
					rookDstSq = dstRookArr[indexDst];
				}
				
				if(histTab!=null)
				{
					copyArr(dstRookArr, IllegalMove.RookCannonFileMoves[row][columnNum], 12);
					indexDst = 0;
					rookDstSq = dstRookArr[indexDst];
					while(rookDstSq!=-1)
					{
						rookDstSq = 9*rookDstSq + column;
						moveList[listIndex].src = rookSrcSq;
						moveList[listIndex].dst = rookDstSq;
						valueList[listIndex] = histTab[rookSrcSq][rookDstSq];
						listIndex++;
						indexDst++;
						rookDstSq = dstRookArr[indexDst];
					}
				}
			}
		}
		moveNum = listIndex;
	}
	
	public void generateCannonMoves(ActiveBoard activeBoard, int[][] histTab)
	{
		int king = activeBoard.getPlayer()==0?16:32;
		int cannonSrcSq;
		int cannonDstSq;
		int attack;
		int[] dstCannonArr = new int[12];
		int indexDst = 0;
		int listIndex = moveNum;
		int player = activeBoard.getPlayer();
		
		for(int i=9;i<=10;i++)
		{
			cannonSrcSq = activeBoard.getPieceSquare(king+i);
			if(cannonSrcSq !=-1)
			{
				int row = cannonSrcSq/9;
				int column = cannonSrcSq%9;
				String rowString = activeBoard.getRowString(row);
				String columnString = activeBoard.getColumnString(column);
				int rowNum = getDecimalFromBinary(rowString);
				int columnNum = getDecimalFromBinary(columnString);
				
				int[] dstCannonRankArr = new int[12];
				int[] dstCannonFileArr = new int[12];
				copyArr(dstCannonRankArr, IllegalMove.RookCannonRankMoves[column][rowNum], 12);
				copyArr(dstCannonFileArr, IllegalMove.RookCannonFileMoves[row][columnNum], 12);
				
				copyArr(dstCannonArr, IllegalMove.CannonRankCaps[column][rowNum], 4);
				indexDst = 0;
				cannonDstSq = dstCannonArr[indexDst];
				while(cannonDstSq!=-1)
				{
					cannonDstSq = 9*row + cannonDstSq;
					attack = activeBoard.getSquarePiece(cannonDstSq);
					
					if((player==0&&attack>31)||(player==1&&attack>15&&attack<32))
					{
						moveList[listIndex].src = cannonSrcSq;
						moveList[listIndex].dst = cannonDstSq;
						if(histTab==null)
						{
							valueList[listIndex] = MvvValues[attack] + 2;
						}
						else
						{
							valueList[listIndex] = histTab[cannonSrcSq][cannonDstSq];
						}
						listIndex++;
					}
					indexDst++;
					cannonDstSq = dstCannonArr[indexDst];
				}
				
				if(histTab!=null)
				{
					copyArr(dstCannonArr, IllegalMove.RookCannonRankMoves[column][rowNum], 12);
					indexDst = 0;
					cannonDstSq = dstCannonArr[indexDst];
					
					while(cannonDstSq!=-1)
					{
						cannonDstSq = 9*row + cannonDstSq;
						moveList[listIndex].src = cannonSrcSq;
						moveList[listIndex].dst = cannonDstSq;
						valueList[listIndex] = histTab[cannonSrcSq][cannonDstSq];
						
						listIndex++;
						indexDst++;
						cannonDstSq = dstCannonArr[indexDst];
					}
				}
				
				copyArr(dstCannonArr, IllegalMove.CannonFileCaps[row][columnNum], 4);
				indexDst = 0;
				cannonDstSq = dstCannonArr[indexDst];
				while(cannonDstSq!=-1)
				{
					cannonDstSq = 9*cannonDstSq+column;
					attack = activeBoard.getSquarePiece(cannonDstSq);
					if((player==0&&attack>31)||(player==1&&attack>15&&attack<32))
					{
						moveList[listIndex].src = cannonSrcSq;
						moveList[listIndex].dst = cannonDstSq;
						if(histTab==null)
						{
							valueList[listIndex] = MvvValues[attack] + 2;
						}
						else
						{
							valueList[listIndex] = histTab[cannonSrcSq][cannonDstSq];
						}
						listIndex++;
					}
					indexDst++;
					cannonDstSq = dstCannonArr[indexDst];
				}
				
				if(histTab!=null)
				{
					copyArr(dstCannonArr, IllegalMove.RookCannonFileMoves[row][columnNum], 12);
					indexDst = 0;
					cannonDstSq = dstCannonArr[indexDst];
					while(cannonDstSq!=-1)
					{
						cannonDstSq = 9*cannonDstSq+column;
						moveList[listIndex].src = cannonSrcSq;
						moveList[listIndex].dst = cannonDstSq;
						valueList[listIndex] = histTab[cannonSrcSq][cannonDstSq];
						
						listIndex++;
						indexDst++;
						cannonDstSq = dstCannonArr[indexDst];
					}
				}
			}
		}
		moveNum = listIndex;
	}
	
	public void generatePawnMoves(ActiveBoard activeBoard, int[][] histTab)
	{
		int king = activeBoard.getPlayer()==0?16:32;
		int srcSquare;
		int dstSquare;
		int attack;
		int[] dstArr = new int[4];
		int indexDst = 0;
		int listIndex = moveNum;
		int player = activeBoard.getPlayer();		
		
		for(int i=11;i<=15;i++)
		{
			srcSquare = activeBoard.getPieceSquare(king+i);
			if(srcSquare!=-1)
			{
				
				copyArr(dstArr, IllegalMove.PawnMoves[srcSquare][activeBoard.getPlayer()], 4);
				indexDst = 0;
				dstSquare = dstArr[indexDst];
				
				while(dstSquare!=-1)
				{
					attack = activeBoard.getSquarePiece(dstSquare);
					if((player==0&&attack>31)||(player==1&&attack>15&&attack<32))
					{
						moveList[listIndex].src = srcSquare;
						moveList[listIndex].dst = dstSquare;
						if(histTab==null)
						{
							valueList[listIndex] = MvvValues[attack]+3;
						}
						else
						{
							valueList[listIndex] = histTab[srcSquare][dstSquare];
						}
						listIndex++;
					}
					else if(histTab!=null && attack==0)
					{
						moveList[listIndex].src = srcSquare;
						moveList[listIndex].dst = dstSquare;
						valueList[listIndex] = histTab[srcSquare][dstSquare];
						listIndex++;
					}
					indexDst++;
					dstSquare = dstArr[indexDst];
				}
			}
		}
		moveNum = listIndex;
	}
	
	
	private void copyArr(int[]dstArr, int[]srcArr, int len)
	{
		for(int i=0;i<len;i++)
		{
			dstArr[i] = srcArr[i];
		}
		for(int j=len;j<dstArr.length;j++)
		{
			dstArr[j] = -1;
		}
	}
	
	/*private int capValue(ActiveBoard board, int dstSquare)
	{
		int dstPiece = board.getSquarePiece(dstSquare);
		int pieceIndex = ActiveBoard.PIECE_TYPE[dstPiece];
		
		//System.out.println("caps dstSquare: "+dstSquare+"   caps pieceIndex: "+pieceIndex);
		
		if(pieceIndex>=7)
		{
			pieceIndex-=7;
		}
		if(pieceIndex!=-1)
		{
			return CCEvalue.BasicValues[pieceIndex] + 
			    CCEvalue.PosValues[pieceIndex][board.getPlayer()==0?dstSquare:89-dstSquare];
		}
		else
		{
		    return 0;
		}
	}*/
	
	private int getDecimalFromBinary(String binary)
	{
		int bin = Integer.parseInt(binary);
		int dec = 0;
		int digit = 0;
		while(bin!=0)
		{
			if(bin%10==1)
			{
			    dec+=Math.pow(2, digit);
			}
			bin = bin/10;
			digit++;
		}
		return dec;
	}
	
	public void print(ActiveBoard board)
	{
		for(int i=0;i<moveNum;i++)
		{
			if(board.isLeagelMove(moveList[i]))
			{
			    System.out.println(moveList[i].toString() + "    value: "+valueList[i]);
			}
		}
	}
	
	public static void main(String[] args)
	{
		SortedMove move = new SortedMove();
		//System.out.println(move.getDecimalFromBinary("1000"));
		
		ActiveBoard board = new ActiveBoard();
		board.loadFen("rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w 0 1");
		/*MoveNode movenode = new MoveNode(64, 1);
		board.makeMove(movenode);
		//board.undoMove();
		board.makeMove(new MoveNode(0,1));
		//System.out.println("player"+board.getPlayer());
		move.generateAllMoves(board);
		move.print(board);*/
		
		
		/*MoveNode move1 = new MoveNode(64,46);
		MoveNode move2 = new MoveNode(19,82);
		MoveNode move3 = new MoveNode(81,82);
		MoveNode move4 = new MoveNode(7,24);
		MoveNode move5 = new MoveNode(46,49);
		board.makeMove(move1);
		board.makeMove(move2);
		board.makeMove(move3);
		board.makeMove(move4);
		board.makeMove(move5);
		move.generateAllMoves(board);
		//move.generateAllChecks(board, 0);
		move.print(board);*/
		
		//MoveNode move01 = new MoveNode(64, 1);
		//MoveNode move02 = new MoveNode(25, 88);
		//MoveNode move03 = new MoveNode(89, 88);
		//board.makeMove(move01);
		//board.makeMove(move02);
		//board.makeMove(move03);
		
		
		board.makeMove(new MoveNode(64, 67));
    	/*board.makeMove(new MoveNode(19, 20));
    	board.makeMove(new MoveNode(82, 63));
    	board.makeMove(new MoveNode(1, 18));
    	board.makeMove(new MoveNode(81, 82));
    	board.makeMove(new MoveNode(0, 9));
    	board.makeMove(new MoveNode(82, 1));
    	board.makeMove(new MoveNode(9, 12));
    	board.makeMove(new MoveNode(1, 2));*/
    	
		//move.generateAllMoves(board);
		//move.print(board);
		
		int[][] hisTable = new int[90][90];
		
		
		move.generateAllMoves(board,hisTable);
		
		for(int i=0;i<move.moveNum;i++)
		{
			MoveNode moveNode = move.moveList[i];
			int srcSq = moveNode.src;
			int piece = board.getSquarePiece(srcSq);
			char c = ActiveBoard.PIECE_CHAR[piece];
			moveNode.piece = String.valueOf(c);
			if(moveNode!=null)
			{
				System.out.println(moveNode.toString());
			}
		}
	}
	
}
