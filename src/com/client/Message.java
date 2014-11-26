package com.client;

import java.io.Serializable;

/*
 * 该类封装客户端和服务器之间发送的消息
 * header指明是请求还是响应--request or response
 * body包含具体的消息内容
 * 
 * 该类随着需求增加可以进一步拓展
 */
public class Message implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String header;
    
    private Object body;
    
    public String toString()
    {
    	return "header: " + header + "  body: " + body.toString();
    }

	public String getHeader() 
	{
		return header;
	}

	public void setHeader(String header) 
	{
		this.header = header;
	}

	public Object getBody() 
	{
		return body;
	}

	public void setBody(Object body) 
	{
		this.body = body;
	}
    
    
}
