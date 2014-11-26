package com.test.swing;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import com.engine.ActiveBoard;
import com.engine.MoveNode;
import com.engine.SearchEngine;
import com.main.BoardPanel;
import com.test.piece.Piece;
import com.test.piece.PieceFactory;

public class MainFrame extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
    private static int edgeX = 5;
	
	private static int edgeY = 5;
	
	private int boardGridSize;
	
	private BoardPanel boardPanel;
	
	private JPanel buttonPanel;
	
	private JPanel contentPanel;
	
	private JPanel messagePanel;
	
	private JLabel messageLabel;
	
	private Piece[] pieces;
			
	private FlickerPiece flickerPiece;
	
	private Piece lastSelected;
	
	private boolean firstClick;
			
	private int player = 0;
	
	private ActiveBoard activeBoard;
	
	private boolean computerIsThinking;
	
	private boolean competitionIsProceeding;

	public MainFrame()
    {
    	setSize(1000, 700);
    	setLayout(new GridBagLayout());
    	
    	boardPanel = new BoardPanel();
    	boardPanel.setBackground(Color.gray);
    	boardPanel.setLayout(null);
    	BoardMouseListener bml = new BoardMouseListener();
    	boardPanel.addMouseListener(bml);
    	
    	setBoardGridSize(55);
    	
    	initAllPieces();
    	   	   	
    	buttonPanel = new JPanel();
    	buttonPanel.setBackground(Color.yellow);
    	JButton startBtn = new JButton("start");
    	JButton restartBtn = new JButton("restart");
    	JButton takebackBtn = new JButton("take back");
    	JButton exitBtn = new JButton("exit");
    	
    	startBtn.addActionListener(new ActionListener()
    	{
    		public void actionPerformed(ActionEvent e)
    		{
    			
    		}
    	});
    	
    	exitBtn.addActionListener(new ActionListener()
    	{
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}		
    	});
    	
    	buttonPanel.setLayout(new GridBagLayout());
    	buttonPanel.add(startBtn, new GBC(0, 0, 1, 1).setFill(GBC.HORIZONTAL).setIpad(1, 1));
    	buttonPanel.add(restartBtn, new GBC(0, 1, 1, 1).setFill(GBC.HORIZONTAL).setIpad(1, 1));
    	buttonPanel.add(takebackBtn, new GBC(0, 2, 1, 1).setFill(GBC.HORIZONTAL).setIpad(1, 1));
    	buttonPanel.add(exitBtn, new GBC(0, 3, 1, 1).setFill(GBC.HORIZONTAL).setIpad(1, 1));
    	
    	contentPanel = new JPanel();
    	contentPanel.setBackground(Color.green);
    	contentPanel.setLayout(new GridLayout(2, 1));
    	
    	JPanel computerPanel = new JPanel();
    	JPanel selfPanel = new JPanel();
    	
    	computerPanel.setBorder(new LineBorder(Color.gray));
    	computerPanel.setBackground(Color.green);
    	computerPanel.setLayout(new GridBagLayout());
    	
    	JLabel computerImage = new JLabel();
    	computerImage.setBorder(new LineBorder(Color.black));
    	computerImage.setIcon(new ImageIcon("image/computer.jpg"));
    	JLabel computerContent = new JLabel();
    	computerContent.setBorder(new LineBorder(Color.black));
    	computerContent.setText("<html>nickname: <br>" +
    			"level: <br>" +
    			"win: <br>" +
    			"lose: <br>" +
    			"total: <br>" +
    			"rank: <br> </html>");
    	JLabel computerTime = new JLabel("10:00");
    	computerTime.setBorder(new LineBorder(Color.black));
    	computerTime.setBackground(Color.green);
    	
    	computerPanel.add(computerImage, new GBC(0, 0).setFill(GBC.BOTH).setIpad(150, 100).setWeight(0, 100));
    	computerPanel.add(computerContent, new GBC(1, 0).setFill(GBC.BOTH).setWeight(0, 100));
    	computerPanel.add(computerTime, new GBC(0, 1, 2, 1).setFill(GBC.BOTH).setIpad(100, 100).setWeight(100, 0));
    	
    	selfPanel.setBorder(new LineBorder(Color.black));
    	selfPanel.setBackground(Color.green);
    	selfPanel.setLayout(new GridBagLayout());
    	
    	JLabel selfImage = new JLabel();
    	selfImage.setBorder(new LineBorder(Color.black));
    	selfImage.setIcon(new ImageIcon("image/self.jpg"));
    	JLabel selfContent = new JLabel();
    	selfContent.setBorder(new LineBorder(Color.black));
    	selfContent.setText("<html>nickname: <br>" +
    			"level: <br>" +
    			"win: <br>" +
    			"lose: <br>" +
    			"total: <br>" +
    			"rank: <br> </html>");
    	JLabel selfTime = new JLabel("10:00");
    	selfTime.setBorder(new LineBorder(Color.black));
    	selfTime.setBackground(Color.green);
    	
    	selfPanel.add(selfImage, new GBC(0, 0).setFill(GBC.BOTH).setIpad(150, 100).setWeight(0, 100));
    	selfPanel.add(selfContent, new GBC(1, 0).setFill(GBC.BOTH).setWeight(0, 100));
    	selfPanel.add(selfTime, new GBC(0, 1, 2, 1).setFill(GBC.BOTH).setIpad(100, 100).setWeight(100, 0));
    	
    	contentPanel.add(computerPanel);
    	contentPanel.add(selfPanel);
    	
    	messagePanel = new JPanel();
    	messagePanel.setBackground(Color.white);
    	
    	messageLabel = new JLabel();
    	messageLabel.setText("<html> aaa  bbb </html>");
    	//messageLabel.setSize(100, 200);
    	//messageLabel.setBorder(new LineBorder(Color.green));
    	messagePanel.add(messageLabel);
    	
    	add(boardPanel, new GBC(0, 0).setFill(GBC.BOTH).setIpad(500, 550).setWeight(0, 100));
    	add(buttonPanel, new GBC(1, 0).setFill(GBC.BOTH).setIpad(100, 550).setWeight(0, 100));
    	add(contentPanel, new GBC(2, 0).setFill(GBC.BOTH));
    	add(messagePanel, new GBC(0, 1, 3, 1).setFill(GBC.BOTH).setIpad(200, 83).setWeight(100, 0));
    	
    	initActiveBoard();
    	
    	firstClick = true;
    	
    	computerIsThinking = false;
    	
    	competitionIsProceeding = true;
    }
	
	private void initAllPieces()
	{
		pieces = new Piece[90];
		for(int i=0;i<90;i++)
		{
			pieces[i] = null;
		}
		
		String fen = "rnbakabnr0000000000c00000c0p0p0p0p0p000000000" +
		            "000000000P0P0P0P0P0C00000C0000000000RNBAKABNR";
		PieceMouseListener pml = new PieceMouseListener();
		for(int i=0;i<fen.length();i++)
		{
			if(fen.charAt(i)!='0')
			{
				pieces[i] = PieceFactory.getPiece(i, fen.charAt(i));
				pieces[i].addMouseListener(pml);
			}
		}
		
		for(int i=0;i<90;i++)
		{
			if(pieces[i]!=null)
			{
				boardPanel.add(pieces[i]);
			}
		}
		
		displayAllPieces();
	}
	
	private void displayAllPieces()
	{
		for(int i=0;i<90;i++)
		{
			if(pieces[i]!=null)
			{
				displayAPiece(pieces[i]);
			}
		}
	}
	
	private void displayAPiece(Piece piece)
	{
		setPieceLocation(piece);
		piece.setVisible(true);
	}
	
	private void setPieceLocation(Piece piece)
	{
		int x0,y0;
		x0 = piece.getCoordinateX();
		y0 = piece.getCoordinateY();
		piece.setLocation(edgeX+x0*boardGridSize, edgeY+y0*boardGridSize);
	}
		
	public int getBoardGridSize() 
	{
		return boardGridSize;
	}

	public void setBoardGridSize(int boardGridSize) 
	{
		this.boardGridSize = boardGridSize;
	}
	
	private void initActiveBoard()
	{
		activeBoard = new ActiveBoard();
		activeBoard.loadFen("rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w 0 1");
	}
	
	private void movePiece(MoveInfo move)
	{
		
		flickerPiece.setFlicker(false);
		firstClick = true;
		
		if(move.getDst()!=-1)
		{
			removePiece(pieces[move.getDst()]);
		}
		pieces[move.getDstLocation()] = new Piece();
		pieces[move.getDstLocation()].addMouseListener(new PieceMouseListener());
		pieces[move.getDstLocation()].setPiece(pieces[move.getSrc()]);
		boardPanel.add(pieces[move.getDstLocation()]);				
		pieces[move.getDstLocation()].setLocation(move.getNewX(), move.getNewY());
		pieces[move.getDstLocation()].setVisible(true);
		
		removePiece(pieces[move.getSrc()]);
		
		player = 1- player;
	}
	
	private void removePiece(Piece piece)
	{
		piece.setVisible(false);
		boardPanel.remove(piece);
	}
	
	private MoveInfo convertMoveNodeToMoveInfo(MoveNode moveNode)
	{		
		MoveInfo moveInfo = new MoveInfo();
		int src = moveNode.src;
		int dst = moveNode.dst;
		int newX=0,newY=0;
		//System.out.println("moveNode.cap= "+moveNode.cap);
		if(moveNode.cap!=0)
		{
			moveInfo.setDst(dst);			
		}
		moveInfo.setSrc(src);
		newX = edgeX + dst%9*boardGridSize;
		newY = edgeY + dst/9*boardGridSize;
		
		moveInfo.setNewX(newX);
		moveInfo.setNewY(newY);
		return moveInfo;
	}
	
	private MoveNode convertMoveInfoToMoveNode(MoveInfo moveInfo)
	{
		MoveNode moveNode;
		int src = moveInfo.getSrc();
		int newX = moveInfo.getNewX();
		int newY = moveInfo.getNewY();
		int dst = (newX-edgeX)/boardGridSize + (newY-edgeY)/boardGridSize*9;
		moveNode = new MoveNode(src, dst);
		return moveNode;
	}
	
	private int getIndexOfPiece(Piece piece)
	{
		for(int i=0;i<90;i++)
		{
			if(pieces[i]!=null && pieces[i].equals(piece))
			{
				return i;
			}
		}
		return -1;
	}
	
	private boolean ignoreMouseAction(Object eventSource)
	{
		if(computerIsThinking || !competitionIsProceeding)
		{
			System.out.println("computer is thinking!!!!");
			return true;
		}
		return false;
	}
	
	private void computerMove()
	{
		messageLabel.setText("Computer is thinking!");
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				computerIsThinking = true;
				
				SearchEngine searchEngine = new SearchEngine(activeBoard);
		    	searchEngine.control();
		    	MoveNode move = searchEngine.bestMove;
		    	MoveInfo comMove = convertMoveNodeToMoveInfo(move);

		    	movePiece(comMove);
		    	//activeBoard.makeMove(move);
		    	
		    	computerIsThinking = false;
		    	messageLabel.setText("It is your turn to move.");
			}
		});
		
		/*new Thread()
		{
			public void run()
			{
				computerIsThinking = true;
				//messageLabel.setText("Computer is thinking!");
				SearchEngine searchEngine = new SearchEngine(activeBoard);
		    	searchEngine.control();
		    	MoveNode move = searchEngine.bestMove;
		    	MoveInfo comMove = convertMoveNodeToMoveInfo(move);

		    	movePiece(comMove);
		    	//activeBoard.makeMove(move);
		    	
		    	computerIsThinking = false;
		    	//messageLabel.setText("It is your turn to move.");
			}
		}.start();*/
	}
	
	private class BoardMouseListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if(ignoreMouseAction(e.getSource()))
			{
				return;
			}
			if(!firstClick)
			{
			    int x = e.getX();
			    int y = e.getY();
			    int nx = (x-edgeX)/boardGridSize;
			    int ny = (y-edgeY)/boardGridSize;
			    int dstX = edgeX + nx*boardGridSize;
			    int dstY = edgeY + ny*boardGridSize;
			    MoveInfo thisMove = new MoveInfo(getIndexOfPiece(lastSelected), -1, dstX, dstY);
			    MoveNode moveNode = convertMoveInfoToMoveNode(thisMove);

			    if(activeBoard.isLeagelMove(moveNode))
			    {
			    	movePiece(thisMove);			    
			    	activeBoard.makeMove(moveNode);
			    }
			    else
			    {
			    	return;
			    }
			    
			    //如果轮到电脑走棋，则通知搜索引擎
			    if(player==1)
			    {
			    	computerMove();			    	
			    }			    
			    firstClick = true;
			}
		}
	}
	
	private class PieceMouseListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			if(ignoreMouseAction(e.getSource()))
			{
				return;
			}
			if(firstClick)
			{
				lastSelected = (Piece)e.getSource();
				flickerPiece = new FlickerPiece(lastSelected, true);
				Thread thread = new Thread(flickerPiece);
				thread.start();
				firstClick = false;
			}
			else
			{
				int src = getIndexOfPiece(lastSelected);
				int dst = getIndexOfPiece((Piece)e.getSource());
				MoveNode moveNode = new MoveNode(src, dst);
				if(activeBoard.isLeagelMove(moveNode))
				{
					flickerPiece.setFlicker(false);
					
					int newX = edgeX + dst%9*boardGridSize;
					int newY = edgeY + dst/9*boardGridSize;
					MoveInfo moveInfo = new MoveInfo(src, dst, newX, newY);
					movePiece(moveInfo);
					
					activeBoard.makeMove(moveNode);
				}
				else
				{
					flickerPiece.setFlicker(false);
					firstClick = true;
					return;
				}
				//如果轮到电脑走棋，则通知搜索引擎
			    if(player==1)
			    {
			    	computerMove();			    	
			    }								
				firstClick = true;
			}
		}
	}

	public static void main(String[] args)
	{
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
