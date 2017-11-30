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
        if (MainActivity.filewillpath.equals(""))
        {
            MainActivity.filewillpath = sharedPreferences.getString("filepath", Environment.getExternalStorageDirectory().toString() + "/");
        }
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
                        Toast.makeText(fileselectCtx, "成功打开文件:" + s, Toast.LENGTH_SHORT).show();
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
                        MainActivity.filedopath = MainActivity.filewillpath + s + "/";
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
        new AlertDialog.Builder(ctx).setTitle("提示")
                .setMessage("您打开的文件过大，是否使用小说模式打开？\n（您也可以长按文件用小说模式打开）")
                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        openNovel(sp, path, name);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(fileselectCtx, "正在打开..请稍后...", Toast.LENGTH_SHORT).show();
                        openFile(ed, path, name);
                        Toast.makeText(fileselectCtx, "成功打开文件:" + name, Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    public static void openNovel(SharedPreferences sp, String path, String name)
    {
        try
        {
            int j = 0;
            JSONObject novellist = new JSONObject(sp.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
            List<String> novelpath = new ArrayList(Arrays.asList(novellist.getString("name").split("$♂$")));
            Toast.makeText(fileselectCtx, novelpath.size()+"", Toast.LENGTH_LONG).show();
            if (novelpath.size() != 1)
            {
                for (int i = 0; i < novelpath.size()-1; i++)
                {
                    if (novelpath.get(i).equals(path + name))
                    {
                        j = i + 1;
                    }
                }
            }
            if (j == 0)//第一次打开
            {
                novelpath.add(path + name);
                novellist.put("name", join(novelpath.toArray(new String[novelpath.size()]), "$♂$"));
                MainActivity.novelReader(path + name, 0);
                sp.edit().putString("novelList", novellist.toString());
                sp.edit().commit();
                MainActivity.mode = 1;
                Toast.makeText(fileselectCtx, "已打开小说" + name, Toast.LENGTH_SHORT);
            }
            else
            {
                MainActivity.novelReader(path + name, novellist.getJSONArray("page").getInt(j - 1));
                MainActivity.mode = 1;
                Toast.makeText(fileselectCtx, "已跳转至上次观看位置", Toast.LENGTH_SHORT);
            }
            MainActivity.mainLeft.setVisibility(View.INVISIBLE);
            MainActivity.mainRight.setVisibility(View.INVISIBLE);
            MainActivity.mainHint.setVisibility(View.INVISIBLE);
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
            editor.commit();
            MainActivity.mode = 0;
            MainActivity.mainLeft.setVisibility(View.VISIBLE);
            MainActivity.mainRight.setVisibility(View.VISIBLE);
            MainActivity.mainHint.setVisibility(View.VISIBLE);
        } catch (IOException e)
        {
            Toast.makeText(fileselectCtx, "打开文件错误！", Toast.LENGTH_SHORT).show();
        }
    }

    private static String join(String[] strs, String splitter)
    {
        StringBuffer sb = new StringBuffer();
        for (String s : strs)
        {
            sb.append(s + splitter);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

}
