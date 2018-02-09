package com.stl.wristNotes.method;

import java.io.*;
import android.util.*;

public class file
{
	public static byte[] getBytes(String filePath, int size){  
        byte[] b = null;
        try {  
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            //ByteArrayOutputStream bos = new ByteArrayOutputStream(fis);
            b = new byte[size];
            //bos.write(b, 0, size);
			fis.read(b, 0, size);
            fis.close();
            //bos.close();
            //buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {  
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }
	
	public static boolean createFile(File file)
	{
		try
		{
			if(!file.exists())
			{
				file.createNewFile();
			}
			return true;
		}
		catch(IOException e)
		{
			return false;
		}
	}
	
	public static boolean writeFile(String filepath, byte[] data)
	{
		File file = new File(filepath);
		try
		{
			FileOutputStream fos = new FileOutputStream(filepath);
			fos.write(data);
			fos.close();
		}
		catch (IOException e)
		{
			return false;
		}
		return true;
	}
	
}
