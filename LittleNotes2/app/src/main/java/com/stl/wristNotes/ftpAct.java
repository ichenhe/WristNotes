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
import java.net.*;


public class ftpAct extends Activity
{
	static {
		System.setProperty("java.net.preferIPv6Addresses", "false");
	}
    Context ctx = this;
    private FtpServer mFtpServer;

    EditText ftpedit1;
    EditText ftpedit2;
    EditText ftpedit3;
    TextView ftpText1;
    TextView ftpText2;
    ToggleButton togglebutton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,  WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.ftp);

        ftpedit1 = (EditText) findViewById(R.id.ftpEditText1);//用户名
        ftpedit2 = (EditText) findViewById(R.id.ftpEditText2);//根目录
        ftpedit3 = (EditText) findViewById(R.id.ftpEditText3);//端口
        ftpText2 = (TextView) findViewById(R.id.ftpTextView2);//下

		ftpedit2.setText(Environment.getExternalStorageDirectory().toString());

        togglebutton = (ToggleButton) findViewById(R.id.ftpToggleButton1);
        togglebutton.setOnClickListener(new OnClickListener() {
				public void onClick(View v)
				{
					if (togglebutton.isChecked())
					{
						if (!(ftpedit1.getText().toString().equals("") || ftpedit2.getText().toString().equals("") || ftpedit3.getText().toString().equals("")))
						{
							if (!new File(ftpedit2.getText().toString()).exists())
							{
								Toast.makeText(ctx, "文件夹路径不存在！！请检查后重试", Toast.LENGTH_SHORT).show();
								togglebutton.setChecked(false);
							}
							else if (!new File(ftpedit2.getText().toString()).isDirectory())
							{
								Toast.makeText(ctx, "请输入一个文件夹路径(默认就可以)", Toast.LENGTH_SHORT).show();
								togglebutton.setChecked(false);
							}
							else
							{
								startFtpServer();
							}
						}
						else
						{
							Toast.makeText(ctx, "请将信息填写完整！", Toast.LENGTH_LONG).show();
							togglebutton.setChecked(false);
						}
					}
					else
					{
						closeFtp();
						ftpText2.setText("如无特殊需要无需修改上方选项，点击开关打开FTP");
					}
				}});

    }

    private void startFtpServer()
    {
		try
		{
			FtpServerFactory serverFactory = new FtpServerFactory();

			BaseUser user = new BaseUser();
			user.setName(ftpedit1.getText().toString());
			user.setHomeDirectory(Environment.getExternalStorageDirectory().toString());
			List<Authority> authorities = new ArrayList<Authority>();
			authorities.add(new WritePermission());
			user.setAuthorities(authorities);

			ListenerFactory factory = new ListenerFactory();

			// set the port of the listener
			factory.setPort(Integer.parseInt(ftpedit3.getText().toString()));


			// replace the default listener
			serverFactory.addListener("default", factory.createListener());
            serverFactory.getUserManager().save(user);
            mFtpServer = serverFactory.createServer();
            mFtpServer.start();
			ftpText2.setText("FTP已开启！\n用户名为" + ftpedit1.getText().toString() + "，密码为空，模式为被动\n\n电脑端请在文件浏览器中输入\"ftp://" + getHostIP() + ":" + ftpedit3.getText().toString() + "\"\n\n手机请用es文件浏览器的ftp功能或同款软件创建ip为" + getHostIP() + "端口为" + ftpedit3.getText().toString() + "的ftp\n注：手机连接经常连接不上，请尽量用电脑");
        }
        catch (FtpException e)
        {
			Toast.makeText(ctx, "你可能未连接Wi-Fi，请检查Wi-Fi！", Toast.LENGTH_LONG).show();
			togglebutton.setChecked(false);
			closeFtp();
		}

    }

	private void closeFtp()
	{
		if (null != mFtpServer)
        {
            mFtpServer.stop();
            mFtpServer = null;
        }
	}
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
		closeFtp();
    }

    public static String getHostIP()
    {
        String hostIp = null;
        try
        {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements())
            {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements())
                {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address)
                    {
                        continue;
                        // skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip))
                    {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }
        return hostIp;
    }
}
