package com.main;

import java.awt.Container;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rule.Rule;

/**
 * 设置系统配置信息的对话框
 * 
 * SysConfigInfo.class
 * 
 * private String userName
 * private int selectedRb;//red or black    red=0 black=1
 * private int selectedSc;//server=1  client=2
 * private String ipAddress;//ipAddress or hostName
 * private int portNum;//port number
 * private int battleModel;//对战模式 1:单机双人   2.网络对战   3.人机对战
 * 
 * @author soft
 *
 */
public class SetSysCfgDialog extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 昵称
	 */
	private JTextField nameTf;
	
	/**
	 * 红方的单选按钮------------只能选择红方或者黑方
	 */
	private JRadioButton redRb;
	
	/**
	 * 黑方的单选按钮
	 */
	private JRadioButton blackRb;
	
	/**
	 * 服务器端的单选按钮-----------只能选择作为服务器或者是客户端
	 */
	private JRadioButton serverRb;
	
	/**
	 * 客户端的单选按钮
	 */
	private JRadioButton clientRb;
	
	/**
	 * ip地址文本域
	 */
	private JTextField ipAddressTf;
	
	/**
	 * 端口号文本域
	 */
	private JTextField portNumTf;
	
	/**
	 * 单人模式
	 */
	private JRadioButton modelSingleRb;
	
	/**
	 * 网络对战模式
	 */
	private JRadioButton modelNetRb;
	
	/**
	 * 人机对战模式
	 */
	private JRadioButton modelPvsCRb;
	
	/**
	 * panel1包含：服务器和客户端的单选按钮、
	 * 主机名label、ip地址输入文本域、端口号label、端口号输入文本域
	 */
	private JPanel panel1;
	
	/**
	 * panel2包含：选择对战模式label、单机双人、网络对战、人机对战的单选按钮
	 */
	private JPanel panel2;
	
	/**
	 * panel3包含选择红方或黑方的label、红方和黑方的单选按钮、输入姓名label、姓名输入文本域
	 */
	private JPanel panel3;
	
	/**
	 * 确认按钮
	 */
	private JButton confirmBt;
	
	/**
	 * 取消按钮
	 */
	private JButton cancelBt;
	
	/**
	 * 系统配置信息----------包含了这个重要的属性
	 */
	private SysConfigInfo sysCfg;
	
	/**
	 * 容器
	 */
	private Container content;
	
	/**
	 * 该类的实例
	 */
	private static SetSysCfgDialog sscd;
	
	/**
	 * 系统配置是否可编辑
	 */
	private boolean sysIsEditable;
	
	/**
	 * 构造函数
	 * 
	 * @param frame
	 * @param cfg
	 */
	private SetSysCfgDialog(Frame frame,SysConfigInfo cfg)
	{
		super(frame, true);
		sysCfg = cfg;
		
		//panel1包含：服务器和客户端的单选按钮、
		//主机名label、ip地址输入文本域、端口号label、端口号输入文本域
		panel1 = new JPanel();
		panel1.setSize(200, 125);
		panel1.setBorder(BorderFactory.createEtchedBorder());
		panel1.setLayout(null);
		
		ServerClientRbListener scrl = new ServerClientRbListener();
		serverRb = new JRadioButton("作为服务器(Server)");
		serverRb.setSize(140, 25);
		serverRb.setLocation(5, 5);
		serverRb.addChangeListener(scrl);
		
		clientRb = new JRadioButton("作为客户端(Client)");
		clientRb.setSize(140, 25);
		clientRb.setLocation(5, 35);
		clientRb.addChangeListener(scrl);
		
		ButtonGroup g1 = new ButtonGroup();
		g1.add(serverRb);
		g1.add(clientRb);
		
		JLabel label11 = new JLabel("主机名:");
		label11.setSize(45, 25);
		label11.setLocation(20, 65);
		
		JLabel label12 = new JLabel("端口号:");
		label12.setSize(45, 25);
		label12.setLocation(20, 95);
		
		ipAddressTf = new JTextField();
		ipAddressTf.setSize(100, 25);
		ipAddressTf.setLocation(65, 65);
		ipAddressTf.setText("127.0.0.1");
		
		portNumTf = new JTextField();
		portNumTf.setSize(100, 25);
		portNumTf.setLocation(65, 95);
		portNumTf.setText("4444");
		
		panel1.add(serverRb);
		panel1.add(clientRb);
		panel1.add(label11);
		panel1.add(ipAddressTf);
		panel1.add(label12);
		panel1.add(portNumTf);
		
		//panel2包含：选择对战模式label、单机双人、网络对战、人机对战的单选按钮
		panel2 = new JPanel();
		panel2.setSize(120, 125);
		panel2.setBorder(BorderFactory.createEtchedBorder());
		panel2.setLayout(null);
		JLabel label2 = new JLabel("选择对战模式");
		label2.setSize(100,25);
		label2.setLocation(5,5);
		ModelRbListener mrl = new ModelRbListener();
		modelSingleRb = new JRadioButton("单机双人");
		modelSingleRb.setSize(90, 25);
		modelSingleRb.setLocation(20, 35);
		modelNetRb = new JRadioButton("网络对战");
		modelNetRb.setSize(90,25);
		modelNetRb.setLocation(20,65);
		modelPvsCRb = new JRadioButton("人机对战");
		modelPvsCRb.setSize(90,25);
		modelPvsCRb.setLocation(20, 95);
		modelSingleRb.addChangeListener(mrl);
		modelNetRb.addChangeListener(mrl);
		modelPvsCRb.addChangeListener(mrl);
		
        ButtonGroup g2 = new ButtonGroup();
		g2.add(modelSingleRb);
		g2.add(modelNetRb);
		g2.add(modelPvsCRb);
				
		panel2.add(label2);
		panel2.add(modelSingleRb);
		panel2.add(modelNetRb);
		panel2.add(modelPvsCRb);
		
		//panel3包含选择红方或黑方的label、红方和黑方的单选按钮、输入姓名label、姓名输入文本域
		panel3 = new JPanel();
		panel3.setSize(330, 65);
		panel3.setBorder(BorderFactory.createEtchedBorder());
		panel3.setLayout(null);
		
		JLabel label31 = new JLabel("选择红方或黑方");
		label31.setSize(100,25);
		label31.setLocation(5,5);
		
		redRb = new JRadioButton("红方");
		redRb.setSize(60,25);
		redRb.setLocation(105,5);
		
		blackRb = new JRadioButton("黑方");
		blackRb.setSize(60,25);
		blackRb.setLocation(180,5);
		
		JLabel label32 = new JLabel("输入姓名");
		label32.setSize(100,25);
		label32.setLocation(5,35);
		
		nameTf = new JTextField();
		nameTf.setSize(90,25);
		nameTf.setLocation(110,35);

		ButtonGroup g3 = new ButtonGroup();
		g3.add(redRb);
		g3.add(blackRb);
		
		panel3.add(label31);
		panel3.add(redRb);
		panel3.add(blackRb);
		panel3.add(label32);
		panel3.add(nameTf);
		
		//将panel1、panel2、panel3添加到主面板中并设置相应的位置
		//*************************************************************
		content = this.getContentPane();
		content.setLayout(null);
		panel1.setLocation(140,10);//size:200,125
		content.add(panel1);
		panel2.setLocation(10,10);//size:120,125
		content.add(panel2);
		panel3.setLocation(10,140);//size:100,65
		content.add(panel3);
		//*************************************************************
		
		//确认和取消按钮
		ButtonActionListener bl = new ButtonActionListener();
		confirmBt = new JButton("Confirm");
		confirmBt.setSize(90,25);
		confirmBt.setLocation(30,220);
		confirmBt.setActionCommand("confirm");
		confirmBt.addActionListener(bl);
		
		cancelBt = new JButton("Cancel");
		cancelBt.setSize(90,25);
		cancelBt.setLocation(230,220);
		cancelBt.setActionCommand("cancel");
		cancelBt.addActionListener(bl);
		
		content.add(confirmBt);
		content.add(cancelBt);
		
		this.setSize(360,300);
		Point loc=SCREEN.getLocationForCenter(getSize());
		this.setLocation(loc);
		this.setResizable(false);
	}
	
	/**
	 * 显示系统配置信息
	 */
	private void display()
	{
		if(sysCfg.getSelectedRb()==Rule.PLAYER_RED)
		{
			redRb.setSelected(true);
		}
		else
		{
			blackRb.setSelected(true);
		}
		if(sysCfg.getBattleModel()==1)
		{
			modelSingleRb.setSelected(true);
		}
		else if(sysCfg.getBattleModel()==2)
		{
			modelNetRb.setSelected(true);
			portNumTf.setText(Integer.toString(sysCfg.getPortNum()));
			if(sysCfg.getSelectedSc()==1)//选择的是服务器
			{
				serverRb.setSelected(true);
			}
			else
			{
				clientRb.setSelected(true);
				ipAddressTf.setText(sysCfg.getIpAddress());
			}
		}
		else
		{
			modelPvsCRb.setSelected(true);
		}
	}
	
	/**
	 * @param frame
	 * @param cfg
	 * @param editable
	 * @param stitle
	 */
	public static void createAndDisplay(Frame frame,SysConfigInfo cfg,
			boolean editable,String stitle)
	{
		if(sscd==null)
		{
			sscd = new SetSysCfgDialog(frame, cfg);
		}
		else
		{
			sscd.setSysCfg(cfg);
		}
		sscd.sysIsEditable = editable;
		sscd.display();
		sscd.setTitle(stitle);
		sscd.setVisible(true);
	}
	
	public void closeDialog()
	{
		this.setVisible(false);
		this.dispose();
	}
	
	public SysConfigInfo getSysCfg()
	{
		return sysCfg;
	}
	
	public void setSysCfg(SysConfigInfo info)
	{
		sysCfg = info;
	}
	
	/**
	 * 保存修改
	 * 
	 * @throws NumberFormatException
	 */
	private void saveChange() throws NumberFormatException
	{
		if(modelNetRb.isSelected())//如果是网络对战模式
		{
			sysCfg.setBattleModel(2);
			sysCfg.setPortNum(Integer.parseInt(portNumTf.getText()));
			sysCfg.setUserName(nameTf.getText());
			
			if(clientRb.isSelected())//如果选择的是客户端
			{
				sysCfg.setSelectedSc(2);
				sysCfg.setIpAddress(ipAddressTf.getText());
			}
			else//选择的是服务器
			{
				sysCfg.setSelectedSc(1);
			}
		}
		else if(modelSingleRb.isSelected())//如果是单人双机模式
		{
			sysCfg.setBattleModel(1);
		}
		else if(modelPvsCRb.isSelected())//如果是人机对战模式
		{
			sysCfg.setBattleModel(3);
		}
		
		if(redRb.isSelected())//如果选择的是红方
		{
			sysCfg.setSelectedRb(Rule.PLAYER_RED);
		}
		else//选择的是黑方
		{
			sysCfg.setSelectedRb(Rule.PLAYER_BLACK);
		}
	}
	
	
    /**
     * 确认和取消按钮监听器
     * 
     * private class
     */
	//for confirm and cancel ActionListener
	private class ButtonActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getActionCommand().equals("confirm"))
			{
				try
				{
					if(sysIsEditable)//如果系统配置可编辑,则保存修改
					{
						saveChange();
					}
					closeDialog();
				}
				catch(NumberFormatException exc)
				{
					System.err.println("Invalid data you inputed!!!"+exc.getMessage());
				}
			}
			else if(e.getActionCommand().equals("cancel"))
			{
				closeDialog();
			}
		}
	}
	
	/**
	 * 服务器和客户端单选按钮监听器
	 * 
	 * @author soft
	 *
	 */
	private class ServerClientRbListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
			if(serverRb.isSelected())//如果选择了服务器
			{
				ipAddressTf.setEditable(false);
				redRb.setEnabled(true);
				blackRb.setEnabled(true);
			}
			else if(clientRb.isSelected())//如果选择了客户端
			{
				ipAddressTf.setEditable(true);
				redRb.setEnabled(false);
				blackRb.setEnabled(false);
			}
		}
	}
	
	/**
	 * 游戏模式监听器----单人双机or网络对战or人机对战
	 * 
	 * @author soft
	 *
	 */
	private class ModelRbListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
			if(modelNetRb.isSelected())
			{
				serverRb.setEnabled(true);
				clientRb.setEnabled(true);
				ipAddressTf.setEnabled(true);
				portNumTf.setEnabled(true);
				nameTf.setEnabled(true);
			}
			else//if(modelSingleRb.isSelected())
			{
				serverRb.setEnabled(false);
				clientRb.setEnabled(false);
				ipAddressTf.setEnabled(false);
				portNumTf.setEnabled(false);
				nameTf.setEnabled(false);
			}
		}
	}
}
