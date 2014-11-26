package com.test.main;

public class MainTest02 
{
    public static void main(String[] args)
    {
    	char a = (char)(5 + 'a');
    	System.out.println(a);
    	
    	int b = (char)(5 + 'a');
    	System.out.println(b);
    	
    	
    	int c = (int)(Math.pow(2, 3)-1);
    	System.out.println(c);
    	
    	int d = 2;
    	int e = d%c;
    	int f = d&c;
    	System.out.println("e:"+e);
    	System.out.println("f:"+f);
    	
    	int index = 1;
    	System.out.println("index++:"+index++);
    	System.out.println("index:"+index);
    }
}
