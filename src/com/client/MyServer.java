package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyServer 
{
	/*
	 * 服务器的端口号，客户端连接时端口号必须与这里的相同
	 */
	private static int port = 8088;

	private ServerSocket serverSocket;
	
	/*
	 * 这里的userMap保存了所有连接到服务器的用户信息，虽然需求中并无此要求，但是构造出来一方面能实现现有的功能
	 * 另外也方便以后的拓展。需要注意的是，这里的key使用了Integer，这里仅仅是为了方便起见才这么用，实际中
	 * 可以替换为注册用户的不重复的用户名
	 */
	private Map<Integer, AcceptObject> userMap = new HashMap<Integer, AcceptObject>();
	
	/*
	 * 这里的userCount作用是充当上面userMap的key，同时也统计了连接到服务器的用户数
	 */
	private static int userCount = 0;

	public MyServer() 
	{
		//这里开启线程让服务端也能输入字符串如12345|5，使用线程是为了避免等待而导致服务器功能阻塞
		new Thread(new InputFromCommand()).start();
		
		startService();
	}
	
	/*
	 * 启动服务
	 */
	public void startService()
	{
		try 
		{
			serverSocket = new ServerSocket(port);
            
			Socket socket = serverSocket.accept();
			
			AcceptObject accObj = new AcceptObject(socket);
			
			//保存每个连接到服务器的用户到userMap
			userMap.put(userCount, accObj);
			userCount++;
			
			//开启线程，这个线程负责处理该用户与服务器的交互
			new Thread(accObj).start();
									
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
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
	 * 服务器发送消息到客户端，这里简单起见，直接把消息发送给了所有连接到服务器的客户，实际中可以调整
	 */
	private void sendMessage(Message message)
	{
		Iterator<Map.Entry<Integer, AcceptObject>> it = userMap.entrySet().iterator();
        while(it.hasNext())
        {
        	Map.Entry<Integer, AcceptObject> entry = it.next();
        	
        	entry.getValue().send(message);
        }
	}
	
	/*
	 * 该线程的功能是让服务端也能在命令行输入字符串，请求道客户端
	 */
	private class InputFromCommand implements Runnable
	{
		public void run()
		{
			requestMaxEdge();
		}
	}
	
	/*
	 * 向客户端请求----B向A请求，12345|5
	 */
	public void requestMaxEdge()
	{
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
				
                /*
                 * 如果还没有用户连接到服务器，则发送请求失败。因为B仍然充当了服务器的角色，所以必须等有用户连接
                 * 时才能发送消息到客户端
                 */
                if(userMap==null || userMap.size()==0)
                {
                	System.out.println("can not send messages to client, " +
                			" please wait until someone connect to the server");
                }
                else
                {
                    Message reqMessage = new Message();
                    reqMessage.setHeader("REQUEST_MAX_EDGE");
                    reqMessage.setBody(line);
                    
                    sendMessage(reqMessage);
                }
                
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
	
	/*
	 * 该线程负责处理当用户与服务器之间的交互
	 */
	private class AcceptObject implements Runnable
	{
		private Socket socket;
		
		private ObjectInputStream in;
		
		private ObjectOutputStream out;
		
		public AcceptObject(Socket socket)
		{
			this.socket = socket;
			try 
			{
				this.in = new ObjectInputStream(this.socket.getInputStream());
				this.out = new ObjectOutputStream(this.socket.getOutputStream());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}			
		}			
		
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
		
		//该方法以后可能用到----待拓展
		@SuppressWarnings("unused")
		public void closeIO()
		{
			try 
			{
				if(out!=null)
				{
				    out.close();
				}
				if(in!=null)
				{
				    in.close();
				}
				if(socket!=null)
				{
				    socket.close();
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		public void run()
		{
			while(!socket.isClosed())
			{
				try 
				{
					Message message = (Message)in.readObject();
					
					if("REQUEST_MAX_EDGE".equals(message.getHeader()))
					{
						String number = (String)message.getBody();
						int maxNum = getIndexOfMaxNum(number);
						
						Message resMessage = new Message();
						resMessage.setHeader("RESPONSE_MAX_EDGE");
						resMessage.setBody(maxNum);
						
						send(resMessage);
					}
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
			}
		}
	}
		
	public static void main(String[] args) 
	{
		new MyServer();				
	}
}
