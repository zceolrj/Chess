package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyClient 
{
	/*
	 * 这里的端口号必须为服务器的端口号
	 */
	private static int port = 8088;

	/*
	 * 这里的ip请修改为服务器的ip，这里为了测试用才写的localhost
	 */
	private static String ip = "localhost";

	private Socket socket;

	private ObjectInputStream in;

	private ObjectOutputStream out;

	public MyClient() 
	{
		try 
		{
			socket = new Socket(ip, port);
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
            
			//启动一个线程，接收服务端发送过来的消息
			new Thread(new AcceptObject()).start();
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
    
	/*
	 * 简单地发送消息给服务器
	 */
	public void send(Message message) 
	{
		try 
		{
			out.writeObject(message);
			out.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
    
	/*
	 * 根据边长打印出三角形
	 */
	private void printTriangle(int length) 
	{
		if (length <= 0) 
		{
			return;
		}
		for (int i = 1; i <= length; i++) 
		{
			for (int j = 0; j < i; j++) 
			{
				System.out.print("*");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/*
	 * 获取指定数字在排序后的字符串中的索引
	 * example：字符串为253718|7     则返回4
	 */
	private int getIndexOfMaxNum(String number)
	{
		String[] parts = number.split("\\|");
		String numberSequence = parts[0];
		
		int specifiedNum = Integer.parseInt(parts[1]);
		int[] array = getArrayFromString(numberSequence);
				
		for(int i=0;i<array.length;i++)
		{
			if(array[i]==specifiedNum)
			{
				return i + 1;
			}
		}
		return -1;
	}
	
	/*
	 * 从纯数字的字符串中获取排序数组
	 */
	private int[] getArrayFromString(String numberSequence)
	{
		int[] array = new int[numberSequence.length()];
		for(int i=0;i<array.length;i++)
		{
			array[i] = Integer.parseInt(String.valueOf(numberSequence.charAt(i)));			
		}
		
		//冒泡排序
		for(int i=1;i<array.length;i++)
		{
			for(int j=0;j<array.length-i;j++)
			{
				if(array[j]>array[j+1])
				{
					int temp = array[j];
					array[j] = array[j+1];
					array[j+1] = temp;
				}
			}
		}
		return array;
	}
	
	/*
	 * 该线程不断接收服务器返回的信息，解析并作出相应的处理
	 */
	private class AcceptObject implements Runnable
	{
		
		public void run()
		{
			while(!socket.isClosed())
			{
				try 
				{
					Object object = in.readObject();
					Message message = (Message)object;
					
					/*
					 * 如果是服务器向客户端请求最大三角形的边-----意思就是B向A请求
					 */
					if("REQUEST_MAX_EDGE".equals(message.getHeader()))
					{
						String number = (String)message.getBody();
						int maxNum = getIndexOfMaxNum(number);
						
						Message resMessage = new Message();
						resMessage.setHeader("RESPONSE_MAX_EDGE");
						resMessage.setBody(maxNum);
						
						send(resMessage);
					} 
					/*
					 * 这是A向B请求最大边后，B发送给A的响应
					 */
					else if("RESPONSE_MAX_EDGE".equals(message.getHeader()))
					{
						int length = (Integer)message.getBody();
						printTriangle(length);
					}
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				} 
				catch (ClassNotFoundException e) 
				{
					e.printStackTrace();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) 
	{
		MyClient client = new MyClient();

		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

		String line = null;
		while (true) 
		{
			try 
			{
				System.out.print("please input a string: ");
				line = br.readLine();
				if ("q".equals(line)) 
				{
					System.exit(0);
				}
                System.out.println();
				
                Message reqMessage = new Message();
                reqMessage.setHeader("REQUEST_MAX_EDGE");
                reqMessage.setBody(line);
                
				client.send(reqMessage);
				
				//这里睡眠仅仅是为了在命令行显示时避免冲突，其它并无特殊意义
				Thread.sleep(1000);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}

		}
	}

}
