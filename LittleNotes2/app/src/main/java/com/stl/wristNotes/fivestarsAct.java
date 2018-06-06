package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;

public class fivestarsAct extends Activity
{
    Context ctx;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fivestars);
        
        ctx = this;
	}
    
    public void button1(View view)
    {
        Uri uri = Uri.parse("market://details?id=" + "com.stl.wristNotes");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void button2(View view)
    {
        Intent intent = new Intent(ctx, settileAct.class);
        startActivity(intent);
    }
}

