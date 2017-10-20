package com.stl.wristNotes;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;


public class aboutAct extends Activity
{
	Context ctx = this;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
	}
	public void about1(View view)
	{
		MainActivity.helpor = 2;
		Intent startint = new Intent(ctx, helpAct.class);
		startActivity(startint);
	}
	public void about2(View view)
	{
		Intent startint = new Intent(ctx, settileAct.class);
		startActivity(startint);
	}
	public void about3(View view)
	{
		Intent startint = new Intent(ctx, donationAct.class);
		startActivity(startint);
	}
}
