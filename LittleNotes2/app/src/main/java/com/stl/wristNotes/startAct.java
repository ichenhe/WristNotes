package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;


public class startAct extends Activity
{
	public Context ctx=this;
	EditText fileselectedit;
	File startfile;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);

		sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		fileselectedit = (EditText) findViewById(R.id.startEdit);
		fileselectedit.setText(Environment.getExternalStorageDirectory() + "/0学习文档");
	}

	public void startbutton2(View view)
	{
		if ((fileselectedit.getText().toString().charAt(fileselectedit.getText().toString().length() - 1) + "").equals("/"))
		{
			fileselectedit.setText(fileselectedit.getText().toString().substring(0, fileselectedit.getText().toString().length() - 1));
			//Toast.makeText(ctx, fileselectedit.getText().toString(), Toast.LENGTH_SHORT).show();
		}
		try
		{
			startfile = new File(fileselectedit.getText().toString());
			startfile.mkdir();
			editor.putString("filepath", fileselectedit.getText().toString() + "/");
			editor.commit();
			view.setEnabled(false);
			Toast.makeText(ctx, "创建文件夹成功！", Toast.LENGTH_SHORT).show();
		}
		catch (Exception e)
		{
			Toast.makeText(ctx, "创建文件夹失败，请检查路径是否存在", Toast.LENGTH_SHORT).show();
			//editor.putString("filepath", Environment.getExternalStorageDirectory().toString() + "/");
			//editor.commit();
		}
	}

	public void startButton(View view)//帮助
	{
		Intent helpint = new Intent(ctx, helpAct.class);
		startActivity(helpint);
		finish();
	}

	public void startButton1(View view)//进入
	{
		finish();
	}
}
