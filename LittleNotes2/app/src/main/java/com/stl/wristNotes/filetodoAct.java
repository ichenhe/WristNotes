package com.stl.wristNotes;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.view.*;

import java.io.*;

import android.content.*;


public class filetodoAct extends Activity
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context ctx = this;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filetodo);
        sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        TextView title = (TextView) findViewById(R.id.filedoText);
        ListView listView = (ListView) findViewById(R.id.filedoList);

        String[] todo = new String[]{"用隐私模式打开", "用小说模式打开", "打开为...", "重命名", "分享", "删除", "属性"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, todo);
        title.setText(MainActivity.filedofile);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> l, View v, int position, long id)
				{
					String s = (String) l.getItemAtPosition(position);
					if (s.equals("用隐私模式打开"))
					{
						try
						{
							MainActivity.textView.setText(MainActivity.fileReader(MainActivity.filedopath + MainActivity.filedofile));
							Toast.makeText(ctx, "隐私模式成功打开文件:" + s + ",要看什么的话。。小心身后哦♪(´▽｀)", Toast.LENGTH_SHORT).show();
							fileselectAct.fileselectCtx.finish();
							finish();
						}
						catch (IOException e)
						{
							Toast.makeText(ctx, "打开文件错！！误！！", Toast.LENGTH_SHORT).show();
						}
					}
					else if (s.equals("用小说模式打开"))
					{
						MainActivity.openNovel(ctx, sharedPreferences, editor, MainActivity.filedopath, MainActivity.filedofile);
//						try{
//							new MainActivity().batterylevel();
//						}
//						catch(Exception e)
//						{
//							Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
//						}
						//MainActivity.batterylevel();
						fileselectAct.fileselectCtx.finish();
						finish();
					}
					else if (s.equals("删除"))
					{
						new AlertDialog.Builder(ctx)
							.setMessage("确认删除" + MainActivity.filedofile + "吗？")
							.setPositiveButton("确定", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									if(new File(MainActivity.filedopath + MainActivity.filedofile).delete()) Toast.makeText(ctx, "删除成功！~(≥▽≤)~", Toast.LENGTH_SHORT).show();
									else Toast.makeText(ctx, "删除失败？？喵喵喵？ºΔº", Toast.LENGTH_SHORT).show();
								}
							})
							.setNegativeButton("取消", null).show();
					}
					else
					{
						Toast.makeText(ctx, "很抱歉，该功能正在开发中，请等待版本更新！(tan90˚)", Toast.LENGTH_SHORT).show();
					}
				}
			});
    }
}
