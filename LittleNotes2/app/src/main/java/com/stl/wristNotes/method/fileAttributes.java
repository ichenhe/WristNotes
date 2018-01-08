package com.stl.wristNotes.method;

import java.text.*;

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
		StringBuffer attributes= new StringBuffer();
		attributes.append("");
		return attributes.toString();
	}
	
	public String getNetFileSizeDescription(long size)
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
