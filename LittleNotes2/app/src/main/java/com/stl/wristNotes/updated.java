package com.stl.wristNotes;

import android.app.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.*;
import android.content.*;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class updated extends Activity
{
	Context ctx = this;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	public static TextView updateText;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.updated);

			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
			sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
			updateText = (TextView) findViewById(R.id.updateText);
			if(sharedPreferences.getInt("isUpdated", 0) == 0) updateText.setText("欢迎使用\n腕上小纸条！");

			editor.putInt("isUpdated", pi.versionCode);
			editor.commit();
		}
		catch(Exception e)
		{
			Toast.makeText(ctx, "错误！", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void update1(View view)//更新日志
	{
		MainActivity.helpor = 3;
		Intent startint = new Intent(ctx, helpAct.class);
		startActivity(startint);
	}
	
	public void update2(View view)
	{
		finish();
	}
}
