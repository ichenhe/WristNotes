package com.stl.wristNotes.method;

import java.text.*;
import java.io.*;

public class fileAttributes
{
	public String fileName;
	public String filePath;
	
	public fileAttributes(String name, String path)
	{
		fileName = name;
		filePath = path;
	}
	
	public String getFileAttributes()
	{
		File file = new File(filePath);
		StringBuffer attributes= new StringBuffer();
		attributes.append("<b><big>" + fileName + "属性</big></b>");
		attributes.append("<br/> <br/>");
		attributes.append("<b>大小：</b><br/>" + getNetFileSizeDescription(file.length()) + "<br/>(" + file.length() + "字节)");
		attributes.append("<br/> <br/>");
		attributes.append("<b>最后修改时间：</b><br/>" + file.lastModified());
		attributes.append("<br/> <br/>");
		attributes.append("<b>路径：</b><br/>" + filePath);
		return attributes.toString();
	}
	
	public static String getNetFileSizeDescription(long size)
	{
		StringBuffer bytes = new StringBuffer();
		DecimalFormat format = new DecimalFormat("###.0");
		if (size >= 1024 * 1024 * 1024)
		{
			double i = (size / (1024.0 * 1024.0 * 1024.0));
			bytes.append(format.format(i)).append("GB");
		}
		else if (size >= 1024 * 1024)
		{
			double i = (size / (1024.0 * 1024.0));
			bytes.append(format.format(i)).append("MB");
		}
		else if (size >= 1024)
		{
			double i = (size / (1024.0));
			bytes.append(format.format(i)).append("KB");
		}
		else if (size < 1024)
		{
			if (size <= 0)
			{
				bytes.append("0B");
			}
			else
			{
				bytes.append((int) size).append("B");
			}
		}
		return bytes.toString();
	}
}
