package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;
import java.io.*;
import android.view.*;
import android.view.inputmethod.*;
import com.stl.wristNotes.method.*;


public class editfileAct extends Activity
{
	Context ctx = this;
	EditText editfileEdit = null;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.editfile);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();

		editfileEdit = (EditText) findViewById(R.id.editfileEdit);
		editfileEdit.clearFocus();
		try
		{
			editfileEdit.setText(fileOpen.fileReader(sharedPreferences.getString("filepath", Environment.getExternalStorageDirectory() + "/0学习文档/") + sharedPreferences.getString("filename", "1.txt")));
		}
		catch (IOException e)
		{
			Toast.makeText(ctx, "打开文件错！误！了！╮(╯▽╰)╭", Toast.LENGTH_LONG);
		}

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			File f = new File(sharedPreferences.getString("filepath", Environment.getExternalStorageDirectory() + "/0学习文档/"), sharedPreferences.getString("filename", "1.txt"));
			if (!f.exists())
			{
				Toast.makeText(ctx, "未打开文件或文件不存在！请先打开一个文件或检查文件是否被删除（这么长感觉好有B格啊罒▽罒）", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(ctx, "外置储存不可用！。。顺便说一句是我搞坏的呦╮(ˉ▽ˉ)╭嘻嘻嘻", Toast.LENGTH_LONG).show();
		}
	}
	
	public void editfileButton1(View view)
	{
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			File f = new File(sharedPreferences.getString("filepath", Environment.getExternalStorageDirectory() + "/0学习文档/"), sharedPreferences.getString("filename", "1.txt"));
			FileOutputStream out = null;
			try
			{
				if (f.exists())
				{
					out = new FileOutputStream(f);
					out.write(editfileEdit.getText().toString().getBytes("UTF-8"));
					out.close();
					Toast.makeText(ctx, "保存成攻！绝对不是我故意打错字的(*/ω＼*)", Toast.LENGTH_SHORT).show();
					MainActivity.textView.setText(editfileEdit.getText().toString());
				}
				else
				{
					Toast.makeText(ctx, "未打开文件或文件不存在！请先打开一个文件或检查文件是否被删除（。。知道不行还点我，再点戳你哦）", Toast.LENGTH_LONG).show();
				}
			}
			catch (FileNotFoundException e)
			{
				Toast.makeText(ctx, "未打开文件或文件不存在！请先打开一个文件或检查文件是否被删除（。。知道不行还点我，再点戳你哦）", Toast.LENGTH_LONG).show();
			}
			catch (IOException e)
			{
				Toast.makeText(ctx, "你输入了什么不该输入的东西嘛。。歪，110嘛？", Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			Toast.makeText(ctx, "外置储存不可用！。。顺便说一句是我搞坏的呦╮(ˉ▽ˉ)╭嘻嘻嘻", Toast.LENGTH_LONG).show();
		}
	}
	public void editfileButton2(View view)
	{
		int po = editfileEdit.getSelectionStart();
		editfileEdit.setText(editfileEdit.getText().insert(po, "\n"));
		editfileEdit.setSelection(po + 1);
		((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editfileAct.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
