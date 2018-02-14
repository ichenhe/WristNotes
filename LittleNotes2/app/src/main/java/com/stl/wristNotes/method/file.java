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

	/**
	 * 创建文件或文件夹
	 * @param mode 0为文件，1为文件夹
	 * @param file 要创建的文件目录的file
	 * @return int 返回1为成功，0为失败，2为已存在
	 */
	public static int create(int mode, File file)
	{
		try
		{
			if(!file.exists())
			{
				Boolean success;

				if(mode == 1) success = file.createNewFile();
				else success = file.mkdir();

				if(success) return 1;
				else return 0;
			}
			return 2;
		}
		catch(IOException e)
		{
			return 0;
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

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir 将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful.
	 *                 If a deletion fails, the method stops attempting to
	 *                 delete and returns "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			//递归删除目录中的子目录下
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}


}
