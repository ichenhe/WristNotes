package com.stl.wristNotes;

import android.app.*;
import android.os.*;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import android.content.*;
import android.net.*;
import org.apache.ftpserver.usermanager.impl.*;
import java.util.*;
import org.apache.ftpserver.ftplet.*;


public class ftpAct extends Activity
{
	Context ctx = this;
	ToggleButton togglebutton;
	boolean isWifi = false;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ftp);
		
		isWifi = isWifi(ctx);

		TextView ftpText1 = (TextView) findViewById(R.id.ftpTextView1);//上
		TextView ftpText2 = (TextView) findViewById(R.id.ftpTextView2);//下
		
		if(isWifi) ftpText1.setText("Wi-Fi已连接，请确保两设备在同一Wi-Fi下");
		
		togglebutton = (ToggleButton) findViewById(R.id.ftpToggleButton1);
        togglebutton.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					if (togglebutton.isChecked())
					{              
						try
						{
							FtpServerFactory serverFactory = new FtpServerFactory();
							BaseUser user = new BaseUser();
							user.setName("anonymous");
							user.setHomeDirectory(Environment.getExternalStorageDirectory().toString());
							List<Authority> authorities = new ArrayList<Authority>();
							authorities.add(new WritePermission());
							user.setAuthorities(authorities);
							serverFactory.getUserManager().save(user);
							FtpServer server = serverFactory.createServer();
							server.start();
						}
						catch (FtpException e)
						{
							e.printStackTrace();
						}
					}   
					else
					{              
						Toast.makeText(ctx, "你不喜欢球类运动", Toast.LENGTH_SHORT).show();          
					}      
				}});
		
	}

	private static boolean isWifi(Context mContext)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
			.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI)
		{
			return true;
		}
		return false;
	}
}
