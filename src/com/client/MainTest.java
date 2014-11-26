package com.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainTest 
{

	public static void main(String[] args) 
	{
		double fen = 1;
		double yuan = fen/100;
		
		double yuan1 = 0.01;
		double fen1 = yuan1*100;
		
		System.out.println(yuan);
		System.out.println(fen1);
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);

		String line = null;
		while (true) 
		{
			try 
			{
				System.out.print("please input a string: ");
				line = br.readLine();
				System.out.println("line:"+line);
				if ("q".equals(line)) 
				{
					System.exit(0);
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
        
		//String ss = "12345";
		
	}

}
