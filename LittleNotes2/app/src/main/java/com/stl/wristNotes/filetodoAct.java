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
	Context ctx = this;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filetodo);
		
		TextView title = (TextView) findViewById(R.id.filedoText);
		ListView listView = (ListView) findViewById(R.id.filedoList);
		
		String[] todo = new String[] { "用隐私模式打开", "打开为...", "重命名", "分享", "删除", "属性" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, todo);
		title.setText(MainActivity.filedofile);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> l, View v, int position, long id)
				{
					String s =(String) l.getItemAtPosition(position);
					if(s.equals("用隐私模式打开"))
					{
						try
						{
							MainActivity.textView.setText(MainActivity.fileReader(MainActivity.filedopath));
							Toast.makeText(ctx, "隐私模式成功打开文件:" + s, Toast.LENGTH_SHORT);
							fileselectAct.fileselectCtx.finish();
							finish();
						}
						catch (IOException e)
						{
							Toast.makeText(ctx, "打开文件错误！" + e.toString(), Toast.LENGTH_SHORT).show();
						}
					}
					else if(s.equals("mm"))
					{
						
					}
					else
					{
						Toast.makeText(ctx, "很抱歉，该功能正在开发中，请等待版本更新！", Toast.LENGTH_SHORT).show();
					}
				}
			});
	}
}
