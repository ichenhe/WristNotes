package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

import java.io.*;
import java.util.*;

import org.json.*;
import org.apache.ftpserver.util.*;


public class fileselectAct extends Activity
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static Activity fileselectCtx;
    Intent intent;
    File fileselectwillfile;
    TextView fileselecttitle;
    String[] filelist;
    String[] filelist2;
    String s;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileselect);

        fileselectCtx = this;
        sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (MainActivity.filewillpath.equals("")) MainActivity.filewillpath = sharedPreferences.getString("filepath", Environment.getExternalStorageDirectory().toString() + "/");
        fileselecttitle = (TextView) findViewById(R.id.fileselectText);
        ListView fileselectView = (ListView) findViewById(R.id.fileselectList);
        fileselecttitle.setText(MainActivity.filewillpath);
        fileselecttitle.setClickable(true);
        fileselecttitle.setOnClickListener(new View.OnClickListener()
        {
            //@Override
            public void onClick(View p1)
            {
                if (!fileselecttitle.getText().equals("/"))
                {
                    String[] filelist3 = MainActivity.filewillpath.split("/");
                    String filelist4 = "";
                    for (int i = 0; i < filelist3.length - 1; i++)
                    {
                        filelist4 += filelist3[i] + "/";
                    }
                    MainActivity.filewillpath = filelist4;
                    intent = new Intent(fileselectCtx, fileselectAct.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    MainActivity.cho = 0;
                    intent = new Intent(fileselectCtx, menuAct.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //fileselectwillfile.list();
        try
        {
            fileselectwillfile = new File(MainActivity.filewillpath);
            if (fileselectwillfile.exists())
            {
                filelist = fileselectwillfile.list();
            }
            else
            {
                MainActivity.filewillpath = Environment.getExternalStorageDirectory().toString() + "/";
                filelist = new File(Environment.getExternalStorageDirectory().toString() + "/").list();
            }
        } catch (Exception e)
        {
            filelist = new File(Environment.getExternalStorageDirectory().toString() + "/").list();
            //Toast.makeText(fileselectCtx, e.toString(), Toast.LENGTH_SHORT).show();
        }
        Arrays.sort(filelist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, filelist);
        fileselectView.setAdapter(adapter);

        fileselectView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id)
            {
                s = (String) l.getItemAtPosition(position);
                if (new File(MainActivity.filewillpath + s + "/").isDirectory())
                {
                    MainActivity.filewillpath = MainActivity.filewillpath + s + "/";
                    Intent helpint = new Intent(fileselectCtx, fileselectAct.class);
                    startActivity(helpint);
                    finish();
                }
                else
                {
//						Intent mainint = new Intent(ctx, MainActivity.class);
//						startActivity(mainint);
                    if (new File(fileselecttitle.getText().toString() + s).length() < 716800)
                    {
                        openFile(editor, fileselecttitle.getText().toString(), s);
                        finish();
                    }
                    else
                    {
                        bigFile(fileselectCtx, sharedPreferences, editor, fileselecttitle.getText().toString(), s);
                    }
                }
            }
        });

        fileselectView.setOnItemLongClickListener(new OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id)
            {
                String s = (String) l.getItemAtPosition(position);
                if (!new File(MainActivity.filewillpath + s + "/").isDirectory())
                {
                    try
                    {
                        MainActivity.filedofile = s;
                        MainActivity.filedopath = MainActivity.filewillpath;
                        Intent helpint = new Intent(fileselectCtx, filetodoAct.class);
                        startActivity(helpint);

                    } catch (Exception e)
                    {
                        Toast.makeText(fileselectCtx, "错误！", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    public static void bigFile(Context ctx, final SharedPreferences sp, final SharedPreferences.Editor ed, final String path, final String name)
    {
        new AlertDialog.Builder(ctx)
                .setMessage("您打开的文件过大，是否使用小说模式打开？\n（您也可以长按文件用小说模式打开）")
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        openNovel(sp, ed, path, name);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(fileselectCtx, "正在打开..请稍后...", Toast.LENGTH_SHORT).show();
                        openFile(ed, path, name);
                    }
                }).show();
    }

    public static void openNovel(SharedPreferences sp, SharedPreferences.Editor ed, String path, String name)
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
				Toast.makeText(fileselectCtx, "第" + i + "个，内容是" + novelpath.get(i) + "原地址是" + path + "%%" + name, Toast.LENGTH_LONG).show();
                if (novelpath.get(i).equals(path + name))
                {
                    MainActivity.p = i + 1;
                }
            }

			Toast.makeText(fileselectCtx, MainActivity.p + "rrr" + novelpath.size(), Toast.LENGTH_LONG).show();
            if (MainActivity.p == 0)//第一次打开
            {
				String nname = name.substring(0, name.length()-name.split("[.]")[name.split("[.]").length-1].length()-1);
				novelname.add(nname);
                novelpath.add(path + name);
				novelpage.add("0");
				
                novellist.put("name", join(novelname.toArray(new String[novelname.size()]), "▒"));
				novellist.put("path", join(novelpath.toArray(new String[novelpath.size()]), "▒"));
				novellist.put("page", join(novelpage.toArray(new String[novelpage.size()]), "▒"));
				
				MainActivity.textView.setText(MainActivity.novelReader(path + name, 0));
                ed.putString("novelList", novellist.toString());
                ed.putString("filepath", path);
                ed.putString("filename", name);
                ed.commit();
                MainActivity.p = 1;

                Toast.makeText(fileselectCtx, "已打开小说 " + nname, Toast.LENGTH_SHORT).show();
            }
            else
            {
                MainActivity.textView.setText(MainActivity.novelReader(path + name, Integer.valueOf(novellist.getString("page").split("▒")[MainActivity.p - 1]).intValue()));
                Toast.makeText(fileselectCtx, "已跳转至上次观看位置", Toast.LENGTH_SHORT).show();
            }
            MainActivity.mode = 1;
            ed.putInt("mode", 1);
            ed.putInt("p", MainActivity.p);
            ed.commit();
            MainActivity.filename = name;
            MainActivity.filepath = path;
            MainActivity.mainLeft.setVisibility(View.VISIBLE);
            MainActivity.mainRight.setVisibility(View.VISIBLE);
            MainActivity.mainHint.setVisibility(View.VISIBLE);
        } catch (Exception e)
        {
            Toast.makeText(fileselectCtx, "打开文件错误！" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public static void openFile(SharedPreferences.Editor editor, String path, String name)
    {
        try
        {
            MainActivity.textView.setText(MainActivity.fileReader(path + name));
            editor.putString("filename", name);
            editor.putString("filepath", path);
            editor.putInt("mode", 0);
            editor.commit();
            MainActivity.mode = 0;
            MainActivity.mainLeft.setVisibility(View.INVISIBLE);
            MainActivity.mainRight.setVisibility(View.INVISIBLE);
            MainActivity.mainHint.setVisibility(View.INVISIBLE);
            Toast.makeText(fileselectCtx, "成功打开文件:" + name, Toast.LENGTH_SHORT).show();
        } catch (IOException e)
        {
            Toast.makeText(fileselectCtx, "打开文件错误！", Toast.LENGTH_SHORT).show();
        }
    }

    public static String join(String[] strs, String splitter)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strs[0]);
        for (int i = 1; i < strs.length; i++)
        {
            Toast.makeText(fileselectCtx, strs[i], Toast.LENGTH_SHORT);
            sb.append(splitter + strs[i]);
        }
        return sb.toString();
    }

}
