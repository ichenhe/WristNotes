package com.stl.wristNotes;

import android.app.*;
import android.os.*;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;


public class ftpAct extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ftp);

		try {
			FtpServerFactory serverFactory = new FtpServerFactory();
			FtpServer server = serverFactory.createServer();
			server.start();
		} catch (FtpException e) {
			e.printStackTrace();
		}
	}

}
