package com.pieces;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GetGifData 
{
    public void readFile()
    {
    	try 
    	{
			InputStream stream = new BufferedInputStream(new FileInputStream("./src/data/pieceObj"));
			ObjectInputStream ois = new ObjectInputStream(stream);
			PieceObject piece = (PieceObject)ois.readObject();
			Image i = piece.getImage(1);
			
			System.out.println(i);
			ois.close();
		} 
    	catch (FileNotFoundException e) 
    	{
			e.printStackTrace();
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
    
    public void writeFile()
    {
    	try
    	{
    	    InputStream streams[] = 
    	    {
    		    new BufferedInputStream(new FileInputStream("./images/wking.gif")),	
    		    new BufferedInputStream(new FileInputStream("./images/wadvisor.gif")),
    		    new BufferedInputStream(new FileInputStream("./images/wbishop.gif")),
    			new BufferedInputStream(new FileInputStream("./images/wknight.gif")),
    			new BufferedInputStream(new FileInputStream("./images/wrook.gif")),
    			new BufferedInputStream(new FileInputStream("./images/wcannon.gif")),
    			new BufferedInputStream(new FileInputStream("./images/wpawn.gif")),
    			
    			new BufferedInputStream(new FileInputStream("./images/bking.gif")),
    			new BufferedInputStream(new FileInputStream("./images/badvisor.gif")),
    			new BufferedInputStream(new FileInputStream("./images/bbishop.gif")),
    			new BufferedInputStream(new FileInputStream("./images/bknight.gif")),
    			new BufferedInputStream(new FileInputStream("./images/brook.gif")),
    			new BufferedInputStream(new FileInputStream("./images/bcannon.gif")),
    			new BufferedInputStream(new FileInputStream("./images/bpawn.gif"))
    	    };
    	    
    	    int[] types = {0,1,2,3,4,5,6,7,8,9,10,11,12,13};
    	    int[] fileSize = new int[14];
    	    int totalSize = 0;
    	    byte[][] content = new byte[14][];
    	    for(int i=0;i<14;i++)
    	    {
    	    	fileSize[i] = streams[i].available();
    	    	content[i] = new byte[fileSize[i]];
    	    	totalSize += streams[i].read(content[i]);
    	    	streams[i].close();
    	    }
    	    
    	    byte[] header = {7,9,1,1,0,1,2,5,3};
    	    PieceObject piece = new PieceObject(header, types, content);
    	    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("./data/pieceObj"));
    	    oos.writeObject(piece);
    	    oos.close();
    	    System.out.println("totalSize:"+totalSize);
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    }
    
    public static void encode(byte[] data)
    {
    	
    }
}












