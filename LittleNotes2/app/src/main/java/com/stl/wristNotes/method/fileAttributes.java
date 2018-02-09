package com.stl.wristNotes.method;

import java.text.*;
import java.io.*;

public class fileAttributes
{
	public String fileName;
	public String filePath;
	public String exten;
	
	public fileAttributes(String name, String path)
	{
		fileName = name;
		filePath = path;
		exten = getExten(path);
	}
	
	public String getFileAttributes()
	{
		File file = new File(filePath);
		StringBuffer attributes= new StringBuffer();
		attributes.append("<b><big>" + fileName + "属性</big></b>");
		attributes.append("<br/> <br/>");
		if(exten.equals("txt"))
		{
			attributes.append("<b>文件类型：</b><br/>文本（小说）");
			attributes.append("<br/> <br/>");
			attributes.append("<b>文本编码格式：</b><br/>" + fileOpen.getFileEncode(filePath));
			attributes.append("<br/> <br/>");
		}
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
	
	public String getExten(String path)
	{
		if(path.contains("/"))
		{
			String[] pathPart = path.split("/");
			if(pathPart[pathPart.length-1].contains("."))
			{
				return pathPart[pathPart.length-1].split("[.]")[pathPart[pathPart.length-1].split("[.]").length-1];
			}
			return "";
		}
		else if(path.contains("."))
		{
			return path.split("[.]")[path.split("[.]").length-1];
		}
		return "";
	}
}
