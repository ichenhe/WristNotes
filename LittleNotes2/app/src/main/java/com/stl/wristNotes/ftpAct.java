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
import org.apache.ftpserver.listener.*;
import java.io.*;
import org.apache.ftpserver.usermanager.*;


public class ftpAct extends Activity
{
	static {
		System.setProperty("java.net.preferIPv6Addresses", "false");
	}
	Context ctx = this;
	private FtpServer mFtpServer;
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

		if (isWifi) ftpText1.setText("Wi-Fi已连接，请确保两设备在同一Wi-Fi下");

		togglebutton = (ToggleButton) findViewById(R.id.ftpToggleButton1);
        togglebutton.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					if (togglebutton.isChecked())
					{
						/*FtpServerFactory serverFactory = new FtpServerFactory();
						 BaseUser user = new BaseUser();
						 user.setName("anonymous");
						 user.setHomeDirectory(Environment.getExternalStorageDirectory().toString());
						 List<Authority> authorities = new ArrayList<Authority>();
						 authorities.add(new WritePermission());
						 user.setAuthorities(authorities);
						 serverFactory.getUserManager().save(user);
						 FtpServer server = serverFactory.createServer();
						 server.start();
						 FtpServerFactory serverFactory = new FtpServerFactory();
						 FtpServer server = serverFactory.createServer();
						 server.start();*/
						startFtpServer();
					}   
					else
					{              
						onDestroy();
					}      
				}});

	}

	private void startFtpServer()
	{
		FtpServerFactory serverFactory = new FtpServerFactory();

		ListenerFactory factory = new ListenerFactory();

		// set the port of the listener
		int port = 2221;
		factory.setPort(port);

		FIleUtils fu = new FIleUtils(ctxt);
        try
		{
			File file = new File(path);
			if (!file.isDirectory())
			{
				file.mkdir();
			}
            fu.createFile(path + "ftpserver.properties");
            String str = "" +
				"ftpserver.user.admin.username=admin\n" +
				"ftpserver.user.admin.userpassword=bff4d7685c1475b68c016c478a728b6e\n" +
				"ftpserver.user.admin.homedirectory=/mnt/sdcard\n" +
				"ftpserver.user.admin.enableflag=true\n" +  
				"ftpserver.user.admin.writepermission=true\n" +
				"ftpserver.user.admin.maxloginnumber=250\n" +
				"ftpserver.user.admin.maxloginperip=250\n" +
				"ftpserver.user.admin.idletime=300\n" +
				"ftpserver.user.admin.uploadrate=10000\n" +  
				"ftpserver.user.admin.downloadrate=10000\n";

            fu.writeFile(str, path + "ftpserver.properties");

            File files=new File(path + "ftpserver.properties");

            PropertiesUserManagerFactory usermanagerfactory = new PropertiesUserManagerFactory();
            usermanagerfactory.setFile(files);
            serverFactory.setUserManager(usermanagerfactory.createUserManager());}
		// replace the default listener
		serverFactory.addListener("default", factory.createListener());


		// start the server
		FtpServer server = serverFactory.createServer();
		this.mFtpServer = server;
		try
		{
			server.start();
		}
		catch (FtpException e)
		{
			e.printStackTrace();
		}
		/*FtpServerFactory serverFactory = new FtpServerFactory();

		 BaseUser user = new BaseUser();
		 user.setName("anonymous");
		 user.setHomeDirectory(Environment.getExternalStorageDirectory().toString());

		 List<Authority> authorities = new ArrayList<Authority>();
		 authorities.add(new WritePermission());

		 user.setAuthorities(authorities);
		 try
		 {
		 serverFactory.getUserManager().save(user);
		 FtpServer server = serverFactory.createServer();
		 server.start();
		 }
		 catch (FtpException e)
		 {}*/

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		if (null != mFtpServer)
		{
			mFtpServer.stop();
			mFtpServer = null;
		}
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
