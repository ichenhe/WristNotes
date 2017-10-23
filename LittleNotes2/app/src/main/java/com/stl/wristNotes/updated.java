package com.stl.wristNotes;

import android.app.*;
import android.os.*;
import android.content.*;
import android.view.*;


public class updated extends Activity
{
	Context ctx = this;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updated);
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
