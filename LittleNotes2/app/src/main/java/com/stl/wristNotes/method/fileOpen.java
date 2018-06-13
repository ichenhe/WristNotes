package com.stl.wristNotes.method;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.stl.wristNotes.*;
import info.monitorenter.cpdetector.io.*;
import java.io.*;
import java.util.*;
import org.json.*;

public class fileOpen
{
	public static String fileReader(String path) throws IOException
    {
		//普通模式读取文件
        BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), getFileEncode(path)));
        StringBuffer temp = new StringBuffer();
        String temp1 = "";
        while ((temp1 = bReader.readLine()) != null)
        {
            //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();
            temp.append(temp1 + "\n");
        }
        bReader.close();
        MainActivity.textView.setTextColor(Color.argb(MainActivity.light * 40 - 10, 255, 255, 255));
        return temp.toString();
    }

    public static String novelReader(int skip) throws IOException
    {
        StringBuffer temp = new StringBuffer();
        if(skip != 0) MainActivity.novelReader.skip(skip);
        char[] ch = new char[500];
        MainActivity.novelReader.read(ch, 0, 500);
        for (char b : ch)
        {
            if((int)b == 0) return "";
            temp.append(b);
        }
        //textView.setTextColor(Color.argb(255, light * 8, light * 8, light * 8));
        return temp.toString();
    }

	
	public static void openNovel(final Context ctx, SharedPreferences sp, SharedPreferences.Editor ed, String path, String name)
    {
        try
        {
            MainActivity.p = 0;
            JSONObject novellist = new JSONObject(sp.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
			List<String> novelname = new ArrayList<>();
			if (!novellist.getString("name").equals("")) novelname = new ArrayList(Arrays.asList(novellist.getString("name").split("▒")));
            List<String> novelpath = new ArrayList<>();
            if (!novellist.getString("path").equals("")) novelpath = new ArrayList(Arrays.asList(novellist.getString("path").split("▒")));
			List<String> novelpage = new ArrayList<>();
			if (!novellist.getString("page").equals("")) novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));

            String code = getFileEncode(path + name);
            for (int i = 0; i < novelpath.size(); i++)
            {
				//Toast.makeText(fileselectCtx, "第" + i + "个，内容是" + novelpath.get(i) + "原地址是" + path + "%%" + name, Toast.LENGTH_LONG).show();
                if (novelpath.get(i).equals(path + name))
                {
                    MainActivity.p = i + 1;
                }
            }

			//Toast.makeText(fileselectCtx, MainActivity.p + "rrr" + novelpath.size(), Toast.LENGTH_LONG).show();
            if (MainActivity.p == 0)//第一次打开
            {
				String nname;
				if(name.contains("."))
				{
					nname = name.substring(0, name.length() - name.split("[.]")[name.split("[.]").length - 1].length() - 1);
				}
				else
				{
					nname = name;
				}
				if(nname.equals("")) nname = " ";
				novelname.add(nname);
                novelpath.add(path + name);
				novelpage.add("0");

                novellist.put("name", MainActivity.join(novelname.toArray(new String[novelname.size()]), "▒"));
				novellist.put("path", MainActivity.join(novelpath.toArray(new String[novelpath.size()]), "▒"));
				novellist.put("page", MainActivity.join(novelpage.toArray(new String[novelpage.size()]), "▒"));

                MainActivity.novelReader = new BufferedReader(new InputStreamReader(new FileInputStream(path + name), code));
				MainActivity.textView.setText(novelReader(0));
                ed.putString("novelList", novellist.toString());
                MainActivity.p = novelname.size();

                Toast.makeText(ctx, "已打开小说 " + nname, Toast.LENGTH_SHORT).show();
            }
            else
            {
                MainActivity.novelReader = new BufferedReader(new InputStreamReader(new FileInputStream(path + name), code));
                if(Integer.valueOf(novellist.getString("page").split("▒")[MainActivity.p - 1]).intValue() != 0)
                {
                    MainActivity.novelReader.skip(Integer.valueOf(novellist.getString("page").split("▒")[MainActivity.p - 1]).intValue() * 500);
                }
                MainActivity.textView.setText(novelReader(0));
                Toast.makeText(ctx, "已跳转至上次观看位置", Toast.LENGTH_SHORT).show();
            }
            MainActivity.mode = 1;
            MainActivity.code = code;
            ed.putInt("mode", 1);
            ed.putInt("p", MainActivity.p);
			ed.putString("filepath", path);
			ed.putString("filename", name);
            ed.putString("code", code);
            ed.commit();
            MainActivity.filename = name;
            MainActivity.filepath = path;
            MainActivity.mainLeft.setVisibility(View.VISIBLE);
            MainActivity.mainRight.setVisibility(View.VISIBLE);
            MainActivity.mainHint.setVisibility(View.VISIBLE);
			MainActivity.mainHint.setText(MainActivity.getHintText(sp));
        }
		catch (Exception e)
        {
            Toast.makeText(ctx, "打开文件错误！" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

	public static void openFile(Context ctx, SharedPreferences.Editor editor, String path, String name)
    {
        try
        {
            MainActivity.textView.setText(fileReader(path + name));
            editor.putString("filename", name);
            editor.putString("filepath", path);
            editor.putInt("mode", 0);
            editor.commit();
            MainActivity.mode = 0;
            MainActivity.mainLeft.setVisibility(View.INVISIBLE);
            MainActivity.mainRight.setVisibility(View.INVISIBLE);
            MainActivity.mainHint.setVisibility(View.INVISIBLE);
            MainActivity.autoReadChange(2);
            Toast.makeText(ctx, "成功打开文件:" + name + " 喵", Toast.LENGTH_SHORT).show();
        }
		catch (IOException e)
        {
            Toast.makeText(ctx, "打开文件错误！", Toast.LENGTH_SHORT).show();
        }
    }
	
	/**
     * 获得编码
     * @param filePath
     * @return
     */
    public static String getFileEncode(String filePath) {
        String charsetName = null;
		byte[] filebyte;
        try {
            filebyte = file.getBytes(filePath, 2048);
            CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
            detector.add(new ParsingDetector(false));
            detector.add(JChardetFacade.getInstance());
            detector.add(ASCIIDetector.getInstance());
            detector.add(UnicodeDetector.getInstance());
            java.nio.charset.Charset charset = null;
            //charset = detector.detectCodepage(file, 51200);
			charset = detector.detectCodepage(new ByteArrayInputStream(filebyte), filebyte.length);
            if (charset != null) {
                charsetName = charset.name();
            } else {
                charsetName = "UTF-8";
            }
        } catch (Exception e) {
            return e.toString();
        }
        return charsetName;
    }
	
	public static void bigFile(final Context ctx, final SharedPreferences sp, final SharedPreferences.Editor ed, final String path, final String name)
    {
        new AlertDialog.Builder(ctx)
			.setMessage("您打开的文件过大，可能是小说，请使用小说模式打开！使用普通模式可能会卡顿或卡死\n（您也可以长按文件用小说模式打开）")
			.setPositiveButton("小说模式", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					fileOpen.openNovel(ctx, sp, ed, path, name);
				}
			})
			.setNegativeButton("普通模式", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Toast.makeText(ctx, "正在打开。。前方应用即将卡死，实在不行就卸了重装吧→_→", Toast.LENGTH_SHORT).show();
					fileOpen.openFile(ctx, ed, path, name);
				}
			}).show();
    }
}
