package com.stl.wristNotes.method;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.stl.wristNotes.*;
import java.io.*;
import java.util.*;
import org.json.*;
import android.app.*;

public class fileOpen
{
	public static String fileReader(String path) throws IOException
    {
		//普通模式读取文件
        FileReader reader = new FileReader(path);
        BufferedReader bReader = new BufferedReader(reader);
        StringBuffer temp = new StringBuffer();
        String temp1 = "";
        while ((temp1 = bReader.readLine()) != null)
        {
            //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();
            temp.append(temp1 + "\n");
        }
        bReader.close();
        MainActivity.textView.setTextColor(Color.argb(255, MainActivity.light * 40, MainActivity.light * 40, MainActivity.light * 40));
        return temp.toString();
    }

    public static String novelReader(String path, int page) throws IOException
    {
        FileReader reader = new FileReader(path);
        BufferedReader bReader = new BufferedReader(reader);
        StringBuffer temp = new StringBuffer();
        bReader.skip(page * 500);
        char[] ch = new char[500];
        bReader.read(ch, 0, 500);
        for (char b : ch) temp.append(b);
        bReader.close();
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
				String nname = name.substring(0, name.length() - name.split("[.]")[name.split("[.]").length - 1].length() - 1);
				if(nname.equals("")) nname = " ";
				novelname.add(nname);
                novelpath.add(path + name);
				novelpage.add("0");

                novellist.put("name", MainActivity.join(novelname.toArray(new String[novelname.size()]), "▒"));
				novellist.put("path", MainActivity.join(novelpath.toArray(new String[novelpath.size()]), "▒"));
				novellist.put("page", MainActivity.join(novelpage.toArray(new String[novelpage.size()]), "▒"));

				MainActivity.textView.setText(novelReader(path + name, 0));
                ed.putString("novelList", novellist.toString());
                MainActivity.p = novelname.size();

                Toast.makeText(ctx, "已打开小说 " + nname, Toast.LENGTH_SHORT).show();
            }
            else
            {
                MainActivity.textView.setText(novelReader(path + name, Integer.valueOf(novellist.getString("page").split("▒")[MainActivity.p - 1]).intValue()));
                Toast.makeText(ctx, "已跳转至上次观看位置", Toast.LENGTH_SHORT).show();
            }
            MainActivity.mode = 1;
            ed.putInt("mode", 1);
            ed.putInt("p", MainActivity.p);
			ed.putString("filepath", path);
			ed.putString("filename", name);
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
            Toast.makeText(ctx, "成功打开文件:" + name + " 喵", Toast.LENGTH_SHORT).show();
        }
		catch (IOException e)
        {
            Toast.makeText(ctx, "打开文件错误！", Toast.LENGTH_SHORT).show();
        }
    }
	
	public byte[] getBytes(String filePath){  
        byte[] buffer = null;  
        try {  
            File file = new File(filePath);  
            FileInputStream fis = new FileInputStream(file);  
            ByteArrayOutputStream bos = new ByteArrayOutputStream(102400);  
            byte[] b = new byte[102400];  
            bos.write(b, 0, 102400);
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return buffer;  
    }
	
	public static void bigFile(final Context ctx, final SharedPreferences sp, final SharedPreferences.Editor ed, final String path, final String name)
    {
        new AlertDialog.Builder(ctx)
			.setMessage("您打开的文件过大，使用普通模式打开可能会导致应用卡死，是否使用小说模式打开？\n（您也可以长按文件用小说模式打开）")
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
