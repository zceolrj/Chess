package com.main;

import java.io.Serializable;

import com.rule.Rule;

/**
 * 系统配置信息
 * 
 * @author soft
 *
 */
public class SysConfigInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * red or black   red=0  black=1
	 */
	private int selectedRb;
	
	/**
	 * server(1) or client(2)
	 */
	private int selectedSc;
    
	/**
	 * ipAddress or hostName
	 */
	private String ipAddress;
	
	/**
	 * port number
	 */
	private int portNum;
	
	/**
	 * 对战模式        1:单机双人        2:网络对战       3:人机对战
	 */
	private int battleModel;
	
	/**
	 * 构造函数
	 */
	public SysConfigInfo()
	{
		userName = "superMan";
		selectedRb = Rule.PLAYER_RED;
		selectedSc = 2;
		portNum = 4444;
		battleModel = 3;
	}
    
	//getter and setter
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getSelectedRb() {
		return selectedRb;
	}

	public void setSelectedRb(int selectedRb) {
		this.selectedRb = selectedRb;
	}

	public int getSelectedSc() {
		return selectedSc;
	}

	public void setSelectedSc(int selectedSc) {
		this.selectedSc = selectedSc;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}

	public int getBattleModel() {
		return battleModel;
	}

	public void setBattleModel(int battleModel) {
		this.battleModel = battleModel;
	}
	
}
