package com.message;

import java.io.Serializable;

/**
 * 消息头
 * 
 * @author zceolrj
 *
 */
public class Header implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 头字符串
	 */
	private String headerString;
	
	private Header(String headerString)
	{
		this.headerString = headerString;
	}
	
	public boolean equals(Object ob)
	{
		return (ob instanceof Header)&&((Header)ob).headerString.equals(headerString);
	}
    
	public int hashCode()
	{
		return headerString.hashCode();
	}
	
	public static final Header PIECE_MOVED = new Header("piece moved");
	public static final Header CHART = new Header("chart message");
	public static final Header RED_TIME_USED=new Header("red time used!");
	public static final Header BLACK_TIME_USED=new Header("black time used!");
	public static final Header SET_TIME_RULE=new Header("set time rule");
	public static final Header DISAGREE=new Header("disagree");
	public static final Header AGREE=new Header("agree");
	public static final Header SYSINFO=new Header("Sysinfo from opponent.");
	public static final Header BLACK_FAILED=new Header("black failed");
	public static final Header RED_FAILED=new Header("red failed");
}










