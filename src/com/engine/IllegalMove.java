package com.engine;

import java.util.Random;
import java.util.Stack;

public class IllegalMove 
{
    private static Random rand = new Random();
    public static long ZobristKeyPlayer;
    public static long ZobristLockPlayer;
    public static long[][] ZobristKeyTable = new long[14][90];
    public static long[][] ZobristLockTable = new long[14][90];
	
    //king has 90 locations,it has 8 move ways in every location
	/**
	 * 
	 */
	public static int[][] KingMoves = new int[90][8];
    	
    /**
     * 士的移动着法
     */
    public static int[][] AdvisorMoves = new int[90][8];
    
    /**
     * 
     */
    public static int[][] BishopMoves = new int[90][8];
    
    /**
     * 
     */
    public static int[][] ElephantEyes = new int[90][4];
    
    /**
     * 
     */
    public static int[][] KnightMoves = new int[90][12];
    
    /**
     * 
     */
    public static int[][] HorseLegs = new int[90][8];
    
    /**
     * 兵的移动着法，9第一维代表在棋盘上的位置，第二维代表是红还是黑--0代表红，1代表黑，第三维代表有几个着法
     */
    public static int[][][] PawnMoves = new int[90][2][4];
    
    //rook and cannon   moves in rank
    public static int[][][] RookCannonRankMoves = new int[9][512][12];    
    //rook and cannon moves in file
    public static int[][][] RookCannonFileMoves = new int[10][1024][12];
    
    //rook capture in rank
    public static int[][][] RookRankCaps = new int[9][512][4];
    //rook capture in file
    public static int[][][] RookFileCaps = new int[10][1024][4];
    
    //cannon capture in rank
    public static int[][][] CannonRankCaps = new int[9][512][4];
    //cannon capture in file
    public static int[][][] CannonFileCaps = new int[10][1024][4];
    
    
    //horse checks
    public static int[][] HorseChecks = new int[90][12];
    public static int[][] HorseCheckLegs = new int[90][8];
    
    //pawn checks
    public static int[][] PawnChecks = new int[90][4];
    
    //rook checks
    public static int[][][] RookRankChecks = new int[9][512][4];
    public static int[][][] RookFileChecks = new int[10][1024][4];
    
    //cannon checks
    public static int[][][] CannonRankChecks = new int[9][512][4];
    public static int[][][] CannonFileChecks = new int[10][1024][4];
    
    static
    {
    	initZobrist();
    	initAllMoves();
    }
    
    private IllegalMove()
    {
    	
    }
    
    public static void initZobrist()
    {
    	int i,j;
    	long randSeed = 1;
    	rand.setSeed(randSeed);
    	ZobristKeyPlayer = rand.nextLong();
    	for(i=0;i<14;i++)
    	{
    		for(j=0;j<90;j++)
    		{
    			ZobristKeyTable[i][j] = rand.nextLong();
    		}
    	}
    	ZobristLockPlayer = rand.nextLong();
    	for(i=0;i<14;i++)
    	{
    		for(j=0;j<90;j++)
    		{
    			ZobristLockTable[i][j] = rand.nextLong();
    		}
    	}
    }
    
    /**
     * 生成所有的着法，包括合法和不合法着法
     */
    public static void initAllMoves()
    {
    	//车和炮在横向将军的位置
    	for(int i=0;i<9;i++)//这里的i代表的是将在该行的位置，共9列，共有9个位置
    	{
			for (int j = 0; j < 512; j++)//每一行的数字--是二进制化为的十进制---如000011001-25 
			{
				//由于将必须位于九宫格中，因此将的位置必须位于列3<=i<=5
				if (i > 2 && i < 6) 
				{
					//获得该行数字的二进制表示，表示成9位
					String rowString = getBinaryFromDecimal(j, 9);
					
					int rookIndex = 0;
					int cannonIndex = 0;
					int k, m;
					for (k = i - 1; k >= 0; k--)//将左边，车能将军的位置 
					{
						if (rowString.charAt(k) == '1') //找到第一个'1'，即为左边车将军位置
						{
							RookRankChecks[i][j][rookIndex] = k;
							rookIndex++;
							break;
						}
					}
					for (m = k - 1; m >= 0; m--) //将左边，炮能将军的位置
					{
						if (rowString.charAt(m) == '1') //找到第二个'1'，即为左边炮将军位置
						{
							CannonRankChecks[i][j][cannonIndex] = m;
							cannonIndex++;
							break;
						}
					}

					for (k = i + 1; k < rowString.length(); k++) //将右边，车能将军的位置
					{
						if (rowString.charAt(k) == '1') //找到右边第一个'1'，即为右边车将军位置
						{
							RookRankChecks[i][j][rookIndex] = k;
							rookIndex++;
							break;
						}
					}
					for (m = k + 1; m < rowString.length(); m++) //将右边，炮能将军的位置
					{
						if (rowString.charAt(m) == '1') //找到右边第二个'1'，即为右边炮将军位置
						{
							CannonRankChecks[i][j][cannonIndex] = m;
							cannonIndex++;
							break;
						}
					}
					//将标志位设为-1，当哨兵用
					RookRankChecks[i][j][rookIndex] = -1;
					CannonRankChecks[i][j][cannonIndex] = -1;
				}
    		}
    	}
    	
    	//车和炮在纵向的将军位置		
		for (int i = 0; i < 10; i++) //i实际代表将在该列所处的位置，有10行，共10个位置
		{
			for (int j = 0; j < 1024; j++) //j是该列数字的二进制表示转化为的十进制
			{
				//将必须位于九宫格中，故行号必须0<=i<=2或者7<=i<=9
				if (i < 3 || (i < 10 && i > 6)) 
				{
					//获取该列的二进制表示
					String columnString = getBinaryFromDecimal(j, 10);
					int k, m;
					int rookIndex = 0;
					int cannonIndex = 0;
					for (k = i - 1; k >= 0; k--)//将的上面，车能将军的位置 
					{
						if (columnString.charAt(k) == '1') //将上面第一个'1'的位置，即为车将军的位置
						{
							RookFileChecks[i][j][rookIndex] = k;
							rookIndex++;
							break;
						}
					}
					for (m = k - 1; m >= 0; m--) //将上面，炮能将军的位置
					{
						if (columnString.charAt(m) == '1')//将上面第二个'1'的位置，即为炮将军的位置 
						{
							CannonFileChecks[i][j][cannonIndex] = m;
							cannonIndex++;
							break;
						}
					}

					for (k = i + 1; k < columnString.length(); k++) //将下面，车能将军的位置
					{
						if (columnString.charAt(k) == '1') //将下面第一个'1',即为车将军的位置
						{
							RookFileChecks[i][j][rookIndex] = k;
							rookIndex++;
							break;
						}
					}
					for (m = k + 1; m < columnString.length(); m++) //将下面，炮能将军的位置
					{
						if (columnString.charAt(m) == '1') //将下面第二个'1'，即为炮将军的位置
						{
							CannonFileChecks[i][j][cannonIndex] = m;
							cannonIndex++;
							break;
						}
					}
					RookFileChecks[i][j][rookIndex] = -1;
					CannonFileChecks[i][j][cannonIndex] = -1;
				}
			}
		}
    	
    	    	   	   	
    	for(int i=0;i<10;i++)//i代表行
    	{
    		for(int j=0;j<9;j++)//j代表列
    		{
    			int index=0;
    			
    			//在九宫格内,这里是将和士的着法,吃子和不吃子的着法相同
    			if(inCity(i, j))//将必须在九宫格内
    			{
    				//兵的将军位置-----------注意PawnChecks数组的第一个坐标代表的是将的位置
    				//兵的纵向将军位置
    				if(i<5)
    				{
    					PawnChecks[9*i+j][index] = 9*(i+1)+j;
    					index++;
    				}
    				else
    				{
    					PawnChecks[9*i+j][index] = 9*(i-1)+j;
    					index++;
    				}
    				//兵的横向将军位置
    				PawnChecks[9*i+j][index++] = 9*i+j-1;
    				PawnChecks[9*i+j][index++] = 9*i+j+1;
    				
    				//哨兵标识
    				PawnChecks[9*i+j][index] = -1;
    				
    				//马将军的位置和蹩马腿的位置
    				index = 0;
    				for(int m=-2;m<=2;m+=4)
    				{
    					for(int n=-1;n<=1;n+=2)
    					{
    						if(inBoard(i+m,j+n))//必须保证马将军的位置在棋盘上
    						{
    							HorseChecks[9*i+j][index] = 9*(i+m)+(j+n);
    							HorseCheckLegs[9*i+j][index] = 9*(i+m/2)+j;
    							index++;
    						}
    						if(inBoard(i+n,j+m))
    						{
    							HorseChecks[9*i+j][index] = 9*(i+n)+(j+m);
    							HorseCheckLegs[9*i+j][index] = 9*i+(j+m/2);
    							index++;
    						}
    					}
    				}
    				//设置哨兵，蹩马腿的数组不需要设置哨兵
    				HorseChecks[9*i+j][index] = -1;
    				
    				
    				//将的移动着法
    				index = 0;
    				for(int k=-1;k<=1;k+=2)
    				{
    					if(inCity(i, j+k))//必须保证将移动后的位置在九宫格内
    					{
    						KingMoves[9*i+j][index] = 9*i + (j+k);
    						index++;
    					}
    					if(inCity(i+k, j))
    					{
    						KingMoves[9*i+j][index] = 9*(i+k) + j;
    						index++;
    					}
    				}
    				KingMoves[9*i+j][index] = -1;//哨兵
    				
    				//士的移动着法
    				index = 0;
    				for(int k=-1;k<=1;k+=2)
    				{
    					for(int m=-1;m<=1;m+=2)
    					{
    						if(inCity(i+k, j+m))//保证移动后士在九宫格内
    						{
    							AdvisorMoves[9*i+j][index] = 9*(i+k) + (j+m);
    							index++;
    						}
    					}
    				}
    				AdvisorMoves[9*i+j][index] = -1;//哨兵
    				
    			}
    			
    			//象的移动着法以及相应的象眼位置
    			index = 0;
    			for(int k=-2;k<=2;k+=4)
    			{
    				for(int m=-2;m<=2;m+=4)
    				{
    					if(inBoard(i+k, j+m))//像移动后的位置必须在棋盘上
    					{
    						//象不能过河
    						if((i<=4 &&(i+k)<=4) || (i>=5&&(i+k)>=5))
    						{
    							BishopMoves[9*i+j][index] = 9*(i+k) + (j+m);
    							ElephantEyes[9*i+j][index] = 9*(i+k/2) + (j+m/2);
    							index++;
    						}
    					}
    				}
    			}
    			BishopMoves[9*i+j][index] = -1;//哨兵
    			
    			//马移动着法以及相应的蹩马腿位置
    			index = 0;
    			for(int k=-1;k<=1;k+=2)
    			{
    				for(int m=-2;m<=2;m+=4)
    				{
    					if(inBoard(i+k, j+m))
    					{
    						KnightMoves[9*i+j][index] = 9*(i+k) + (j+m);
    						HorseLegs[9*i+j][index] = 9*i + (j+m/2);
    						index++;
    					}
    					if(inBoard(i+m, j+k))
    					{
    						KnightMoves[9*i+j][index] = 9*(i+m) + (j+k);
    						HorseLegs[9*i+j][index] = 9*(i+m/2) + j;
    						index++;
    					}
    					
    				}
    			}
    			KnightMoves[9*i+j][index] = -1;
    			
    			//兵移动着法
    			
    			for(int k=0;k<=1;k++)
    			{
    				index = 0;
    				if(inBoard(k==0?i-1:i+1, j))
    				{
    					PawnMoves[9*i+j][k][index] = 9*(k==0?i-1:i+1)+j;
    					index++;
    				}
    				
    				if(k==0?i<=4:i>=5)
    				{
    					for(int m=-1;m<=1;m+=2)
        				{
        					if(inBoard(i,j+m))
        					{
        						PawnMoves[9*i+j][k][index] = 9*i+(j+m);
        						index++;
        					}
        				}
    				}
    				//设置哨兵
    				PawnMoves[9*i+j][k][index] = -1;
    			}
    		}
    	}
    	
    	//rook cannon moves in rank-------车和炮的横向移动着法
    	for(int i=0;i<9;i++)//i代表车或炮在该行的位置，共9列，9个位置
    	{
    		for(int j=0;j<512;j++)//j代表该行的二进制表示
    		{
    			String rowNum = getBinaryFromDecimal(j, 9);
    			int index=0;
    			int k;
    			for(k=i+1;k<rowNum.length();k++)//横向右移的着法
    			{
    				if(rowNum.charAt(k)=='1')//碰到'1'即退出循环
    				{
    					break;
    				}
    				else//碰到'0'代表是可以移动到的位置
    				{
    					RookCannonRankMoves[i][j][index]=k;
    					index++;
    				}
    			}
    			for(k=i-1;k>=0;k--)//横向左移的着法
    			{
    				if(rowNum.charAt(k)=='1')
    				{
    					break;
    				}
    				else
    				{
    					RookCannonRankMoves[i][j][index] = k;
    					index++;
    				}
    			}
    			RookCannonRankMoves[i][j][index] = -1;
    		}    		
    	}
    	
    	//rook cannon move in file----------车和炮的纵向移动着法
    	for(int i=0;i<10;i++)
    	{
    		for(int j=0;j<1024;j++)
    		{
    			String columnNum = getBinaryFromDecimal(j, 10);
    			int index=0;
    			int k;
    			for(k=i+1;k<columnNum.length();k++)//纵向向下的移动着法
    			{
    				if(columnNum.charAt(k)=='1')
    				{
    					break;
    				}
    				else
    				{
    					RookCannonFileMoves[i][j][index] = k;
    					index++;
    				}
    			}
    			for(k=i-1;k>=0;k--)//纵向向上的移动着法
    			{
    				if(columnNum.charAt(k)=='1')
    				{
    					break;
    				}
    				else
    				{
    					RookCannonFileMoves[i][j][index] = k;
    					index++;
    				}
    			}
    			RookCannonFileMoves[i][j][index] = -1;
    		}
    	}
    	
    	
    	//rook  and  cannon capture in rank----------车和炮横向的吃子着法
    	for(int i=0;i<9;i++)
    	{
    		for(int j=0;j<512;j++)
    		{
    			String rowNum = getBinaryFromDecimal(j, 9);
    			int rookIndex=0;
    			int cannonIndex = 0;
    			int k,m;
    			for(k=i+1;k<rowNum.length();k++)//往右吃子
    			{
    				if(rowNum.charAt(k)=='1')//往右找到的第一个'1'即为车吃子的位置
    				{
    					RookRankCaps[i][j][rookIndex] = k;
    					rookIndex++;
    					break;
    				}
    			}
    			for(m=k+1;m<rowNum.length();m++)
    			{
    				if(rowNum.charAt(m)=='1')//往右找到的第二个'1'为炮吃子的位置
    				{
    					CannonRankCaps[i][j][cannonIndex] = m;
    					cannonIndex++;
    					break;
    				}
    			}
    			
    			for(k=i-1;k>=0;k--)//往左吃子
    			{
    				if(rowNum.charAt(k)=='1')
    				{
    					RookRankCaps[i][j][rookIndex] = k;
    					rookIndex++;
    					break;
    				}
    			}
    			for(m=k-1;m>=0;m--)
    			{
    				if(rowNum.charAt(m)=='1')
    				{
    					CannonRankCaps[i][j][cannonIndex] = m;
    					cannonIndex++;
    					break;
    				}
    			}
    			RookRankCaps[i][j][rookIndex] = -1;
    			CannonRankCaps[i][j][cannonIndex] = -1;
    		}
    	}
    	
    	//rook and cannon capture in file---------车和炮纵向吃子的着法
    	for(int i=0;i<10;i++)
    	{
    		for(int j=0;j<1024;j++)
    		{
    			String columnNum = getBinaryFromDecimal(j, 10);
    			int rookIndex=0;
    			int cannonIndex=0;
    			int k,m;
    			for(k=i+1;k<columnNum.length();k++)//往下吃子
    			{
    				if(columnNum.charAt(k)=='1')
    				{
    					RookFileCaps[i][j][rookIndex] = k;
    					rookIndex++;
    					break;
    				}
    			}
    			for(m=k+1;m<columnNum.length();m++)
    			{
    				if(columnNum.charAt(m)=='1')
    				{
    					CannonFileCaps[i][j][cannonIndex] = m;
    					cannonIndex++;
    					break;
    				}
    			}
    			
    			for(k=i-1;k>=0;k--)//往上吃子
    			{
    				if(columnNum.charAt(k)=='1')
    				{
    					RookFileCaps[i][j][rookIndex] = k;
    					rookIndex++;
    					break;
    				}
    			}
    			for(m=k-1;m>=0;m--)
    			{
    				if(columnNum.charAt(m)=='1')
    				{
    					CannonFileCaps[i][j][cannonIndex] = m;
    					cannonIndex++;
    					break;
    				}
    			}
    			RookFileCaps[i][j][rookIndex] = -1;
    			CannonFileCaps[i][j][cannonIndex] = -1;
    		}
    	}
    }
    
    /**
     * 判断是否在九宫格中
     * 
     * @param row 行
     * @param column 列
     * @return true/false
     */
    public static boolean inCity(int row, int column)
    {
    	if(((row>=0&&row<=2)||(row>=7&&row<=9))&&(column>=3&&column<=5))
    	{
    		return true;
    	}
    	return false;
    }
    
    /**
     * 判断是否在棋盘上
     * 
     * @param row 行
     * @param column 列
     * @return true/false
     */
    public static boolean inBoard(int row, int column)
    {
    	if((row>=0&&row<=9)&&(column>=0&&column<=8))
    	{
    		return true;
    	}
    	return false;
    }
    
    /**
     * 或者一个十进制数字的二进制表示---该方法有限定
     * 
     * @param dec 十进制数字
     * @param digit 表示的二进制的位数，不够用0补上
     * @return
     */
    public static String getBinaryFromDecimal(int dec, int digit)
    {
    	StringBuffer sb = new StringBuffer();
    	Stack<Integer> stack = new Stack<Integer>();
    	while(dec/2!=0)
    	{
    		stack.push(dec%2);
    		dec = dec/2;
    	}
    	stack.push(dec%2);
    	
    	while(stack.size()<digit)
    	{
    		stack.push(0);
    	}
    	
    	while(!stack.isEmpty())
    	{
    		sb.append(stack.pop());
    	}
    	
    	return sb.toString();
    }
    
    
    public static void main(String[] args)
    {
    	String s9 = getBinaryFromDecimal(51, 9);
    	String s10 = getBinaryFromDecimal(1023, 10);
    	System.out.println(s9);
    	System.out.println(s10);
    }
}
