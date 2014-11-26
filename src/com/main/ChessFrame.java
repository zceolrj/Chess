package com.main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.engine.ActiveBoard;
import com.engine.MoveNode;
import com.engine.SearchEngine;

public class ChessFrame extends JFrame implements MouseListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private static Log log = LogFactory.getLog(ChessFrame.class);
	
	
	
	public BoardPanel boardPanel;
	
	public JPanel contentPanel;
	
	public JPanel timePanel;
	
	public JPanel operatePanel;
	
	public JLabel[] pieceLabels;
	
	public int index;
	
	public boolean firstClick;
	
	public int lastIndex;
	
	public ActiveBoard activeBoard;
	
	public PieceFlicker pieceFlicker;
	
	public boolean wonOrLost;
	
	public boolean computerThinking;
	
	public TimeCounter comCounter;
	
	public TimeCounter selfCounter;
	
	final JLabel comLabel = new JLabel("10:00", SwingConstants.CENTER);
	final JLabel selfLabel = new JLabel("10:00", SwingConstants.CENTER);
	
	JPanel comContentPanel;
	
	public ChessFrame()
	{
		setTitle("Chess");
		setSize(900,538);
		
		setLayout(new GridLayout(1, 2));
		
		boardPanel = new BoardPanel(new ImageIcon("image/CChess.GIF").getImage());
		//boardPanel.setPreferredSize(new Dimension(400, 500));
		
		contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(1, 2));
		
		timePanel = new JPanel();
		timePanel.setLayout(new GridLayout(2, 1));

		timePanel.add(comLabel);
		timePanel.add(selfLabel);
		//timePanel.add(comContentPanel);
				
		operatePanel = new JPanel();
		operatePanel.setLayout(new GridLayout(4, 1));
		JButton start = new JButton("start");
		JButton restart = new JButton("restart");
		JButton undoMove = new JButton("take back");
		JButton exit = new JButton("exit");
		/*Dimension preferredSize = new Dimension(100, 100);
		start.setPreferredSize(preferredSize);
		restart.setPreferredSize(preferredSize);
		undoMove.setPreferredSize(preferredSize);
		exit.setPreferredSize(preferredSize);*/
		operatePanel.add(start);
		operatePanel.add(restart);
		operatePanel.add(undoMove);
		operatePanel.add(exit);
		
		start.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				if(!computerThinking)
				{
				    selfCounter = new TimeCounter(selfLabel);
				    Thread selfThread = new Thread(selfCounter);
				    selfThread.start();
				}
			}			
		});
		
		exit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(!computerThinking)
				{
					System.exit(0);
				}
			}
		});
		
		contentPanel.add(operatePanel);
		contentPanel.add(timePanel);
		
		initAllPieces();
		
		add(boardPanel);
		add(contentPanel);
		
		index = -1;
		firstClick = true;
		
		activeBoard = new ActiveBoard();
		activeBoard.loadFen("rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w 0 1");
		
		wonOrLost = false;
		computerThinking = false;
	}
	
	

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(wonOrLost || computerThinking)
		{
			return;
		}
		for(int i=0;i<90;i++)
		{
			if(pieceLabels[i]==e.getSource())
			{
				index = i;
				break;
			}
		}
		if(activeBoard.getSquarePiece(index)<32 && activeBoard.getSquarePiece(index)!=0 && firstClick)
		{
			return;
		}
		if(firstClick && !pieceLabels[index].getName().equals("0"))
		{
			pieceFlicker = new PieceFlicker(pieceLabels[index], true);
			Thread thread = new Thread(pieceFlicker);
			thread.start();
		
			firstClick = false;
			lastIndex = index;
		}
		else if(!firstClick)
		{
			int src = lastIndex;
			int dst = index;
			MoveNode move = new MoveNode(src, dst);
			
			if(activeBoard.isLeagelMove(move))
			{
				
				movePiece(src, dst);
				activeBoard.makeMove(move);
				
				//如果胜利了，显示胜利信息
				if(activeBoard.isLost())
				{
					JOptionPane.showMessageDialog(null, "win");
					wonOrLost = true;
				}
				else
				{
				    selfCounter.setCounting(false);
				    selfCounter = null;
					
					computerThinking = true;
					
					comCounter = new TimeCounter(comLabel);
					Thread thread = new Thread(comCounter);
					thread.start();
					
					//成功移动着法后，通知搜索引擎让电脑走棋
				    SearchEngine searchEngine = new SearchEngine(activeBoard);
				   
				    searchEngine.control();
				    
				    MoveNode comMove = searchEngine.bestMove;
				    System.out.println(comMove.toString());
				    movePiece(comMove.src, comMove.dst);
				    
				    comCounter.setCounting(false);
				    comCounter = null;
				    
				    firstClick = true;	
				    if(activeBoard.isLost())
				    {
				    	JOptionPane.showMessageDialog(null, "lose");
				    	wonOrLost = true;
				    }
				    computerThinking = false;
				    
				    selfCounter = new TimeCounter(selfLabel);
				    new Thread(selfCounter).start();
				}
				System.out.println("here  here  here  here!!!!!!!!!");
			}
			else
			{
				firstClick = true;
			}
			pieceFlicker.setFlicker(false);
			pieceFlicker = null;					
		}
	}
	
	private void initAllPieces()
	{
		boardPanel.setLayout(new GridLayout(10, 9));
		pieceLabels = new JLabel[90];
		for(int i=0;i<90;i++)
		{
			pieceLabels[i] = new JLabel();
		}
		String fen = "rnbakabnr0000000000c00000c0p0p0p0p0p000000000" +
				"000000000P0P0P0P0P0C00000C0000000000RNBAKABNR";
		for(int i=0;i<fen.length();i++)
		{
			char c = fen.charAt(i);
			switch(c)
			{
			    case '0':				    
				    setPieceLabel(pieceLabels[i], "0", null);
				    break;
			    case 'r':
			    	setPieceLabel(pieceLabels[i], "black rook", "brook.gif");
			    	break;
			    case 'n':
			    	setPieceLabel(pieceLabels[i], "black knight", "bknight.gif");
			    	break;
			    case 'b':
			    	setPieceLabel(pieceLabels[i], "black bishop", "bbishop.gif");
			    	break;
			    case 'a':
			    	setPieceLabel(pieceLabels[i], "black advisor", "badvisor.gif");
			    	break;
			    case 'k':
			    	setPieceLabel(pieceLabels[i], "black king", "bking.gif");
			    	break;
			    case 'c':
			    	setPieceLabel(pieceLabels[i], "black cannon", "bcannon.gif");
			    	break;
			    case 'p':
			    	setPieceLabel(pieceLabels[i], "black pawn", "bpawn.gif");
			    	break;
			    case 'R':
			    	setPieceLabel(pieceLabels[i], "white rook", "wrook.gif");
			    	break;
			    case 'N':
			    	setPieceLabel(pieceLabels[i], "while knight", "wknight.gif");
			    	break;
			    case 'B':
			    	setPieceLabel(pieceLabels[i], "white bishop", "wbishop.gif");
			    	break;
			    case 'A':
			    	setPieceLabel(pieceLabels[i], "white advisor", "wadvisor.gif");
			    	break;
			    case 'K':
			    	setPieceLabel(pieceLabels[i], "white king", "wking.gif");
			    	break;
			    case 'C':
			    	setPieceLabel(pieceLabels[i], "white cannon", "wcannon.gif");
			    	break;
			    case 'P':
			    	setPieceLabel(pieceLabels[i], "white pawn", "wpawn.gif");
			    	break;
			}
		}
	}
	
	private void setPieceLabel(JLabel label, String labelName, String image)
	{
		label.setName(labelName);
		
		ImageIcon ico = new ImageIcon("image/"+image);
		Image temp = ico.getImage().getScaledInstance(46, 32, 
				Image.SCALE_DEFAULT);
		ico = new ImageIcon(temp);
		label.setIcon(ico);
		label.setBorder(BorderFactory.createLineBorder(Color.gray));
		
		label.addMouseListener(this);

		boardPanel.add(label);
	}
	
	public boolean movePiece(int src, int dst)
	{
		String name = pieceLabels[src].getName();
		Icon icon = pieceLabels[src].getIcon();
		pieceLabels[dst].setName(name);
		pieceLabels[dst].setIcon(icon);
		pieceLabels[src].setName("0");
		pieceLabels[src].setIcon(null);
		System.out.println("move piece");
		return true;
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
	}  
}
