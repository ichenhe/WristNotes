package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

public class menuAct extends Activity
{
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
    Context ctx = this;
	TextView menutitle;
	ListAdapter adapter;
	Intent menuintent;
	Intent passint;
	String[] menuList;
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
        menutitle = (TextView) findViewById(R.id.menuText);
        final ListView listView = (ListView) findViewById(R.id.menuList);
		//LinearLayout footViewLayout = (LinearLayout) LayoutInflater.from(ctx).inflate(R.layout.menu, null);
		//listView.addHeaderView(footViewLayout);

		menutitle.setClickable(true);
		menutitle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					if (MainActivity.cho == 2 || MainActivity.cho == 3 || MainActivity.cho == 4 || MainActivity.cho == 6)
					{
						MainActivity.cho = 0;
						menuintent = new Intent(ctx, menuAct.class);
						startActivity(menuintent);
						finish();
					}
					else if (MainActivity.cho == 1 || MainActivity.cho == 5)
					{
						MainActivity.cho = 2;
						menuintent = new Intent(ctx, menuAct.class);
						startActivity(menuintent);
						finish();
					}
					else if (MainActivity.cho == 999)
					{
						MainActivity.cho = 2;
						menuintent = new Intent(ctx, menuAct.class);
						startActivity(menuintent);
						finish();
					}
				}
			});

		if (MainActivity.cho == 0)
		{
			menuList = new String[] { "打开文档", "编辑文档", "我的小说", "显示设置", "偏好设置", "文件传输", "帮助", "关于" };
			menutitle.setText("设置");
		}
		else if (MainActivity.cho == 1)
		{
			menuList = new String[] { "1", "2", "3", "4", "5", "6" };
			menutitle.setText("亮度调整");
		}
		else if (MainActivity.cho == 2)
		{
			menuList = new String[] { "调整亮度：" + sharedPreferences.getInt("light", 5), "字号选择：" + sharedPreferences.getInt("bs", 14), "主题选择" };
			menutitle.setText("显示设置");
		}
		else if (MainActivity.cho == 3)
		{
			menuList = new String[] { "触摸隐藏文字：" + sharedPreferences.getString("touchHideText", "关闭"), "启动应用隐藏文字：" + sharedPreferences.getString("startHideText", "关闭"), "密码保护：" + mima(), "更改密码", "密码入口伪装" };
			menutitle.setText("偏好设置");
		}
		else if (MainActivity.cho == 4)//无
		{
			menuList = new String[] { "左右空出像素数", "上下空出行数" };
			menutitle.setText("手表优化");
		}
		else if (MainActivity.cho == 5)
		{
			menuList = new String[] { "8", "10", "12", "14", "16" };
			menutitle.setText("字号调整");
		}
		else if(MainActivity.cho == 6)
		{
			menuList = new String[] { "FTP文件传输", "蓝牙传输" };
			menutitle.setText("文件传输");
		}
		else if(MainActivity.cho == 7)
		{
			menuList = new String[] { "跳转页数", "智能翻页：" + sharedPreferences.getString("smartScroll", "开启") };
			menutitle.setText("阅读菜单");
		}
		else if(MainActivity.cho == 8)
        {
			menuList = new String[] { "删除该条小说记录", "小说文件属性" };
			menutitle.setText("小说记录");
        }

		adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, menuList);

        LayoutInflater infla = LayoutInflater.from(this);
        final View headView = infla.inflate(R.layout.widget_newfunction, null);
        listView.addHeaderView(headView, null, true);

        ((TextView)findViewById(R.id.functiontext)).setText("测试测试测试测试测试测试被测试！！！！");
		LinearLayout button = (LinearLayout) headView.findViewById(R.id.functionbutton);
        button.setClickable(true);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View p1)
			{
				Toast.makeText(ctx, "啊。。被点了", Toast.LENGTH_SHORT).show();
				listView.removeHeaderView(headView);
			}
		});

		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> l, View v, int position, long id)
				{
					String s =(String) l.getItemAtPosition(position);
					if (s.equals("打开文档"))
					{
						MainActivity.filewillpath = "";
						menuintent = new Intent(ctx, fileselectAct.class);
						startActivity(menuintent);
						finish();
					}
					else if (s.equals("编辑文档"))
					{
						if (MainActivity.mode == 0)
						{
							menuintent = new Intent(ctx, editfileAct.class);
							startActivity(menuintent);
							finish();
						}
						else
						{
							Toast.makeText(ctx, "小说不支持编辑！", Toast.LENGTH_SHORT).show();
						}

					}
					else if (s.equals("显示设置") || s.equals("偏好设置"))
					{
						if(s.equals("显示设置")) MainActivity.cho = 2;
						if(s.equals("偏好设置")) MainActivity.cho = 3;
						menuintent = new Intent(ctx, menuAct.class);
						startActivity(menuintent);
						finish();
					}
					else if (s.equals("帮助"))
					{
						Intent helpint = new Intent(ctx, helpAct.class);
						startActivity(helpint);
					}
					else if (s.equals("关于"))
					{
						Intent helpint = new Intent(ctx, aboutAct.class);
						startActivity(helpint);
						//menuintent = new Intent(ctx, aboutAct.class);
						//startActivity(menuintent);
					}
					else if (s.indexOf("调整亮度") != -1)
					{
						MainActivity.cho = 1;
						menuintent = new Intent(ctx, menuAct.class);
						startActivity(menuintent);
						finish();
					}
					else if (s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5") || s.equals("6"))
					{
						MainActivity.textView.setTextColor(Color.argb(255, Integer.parseInt(s) * 40, Integer.parseInt(s) * 40, Integer.parseInt(s) * 40));
						editor.putInt("light", Integer.parseInt(s));
						editor.commit();
						Toast.makeText(ctx, "已调整亮度", Toast.LENGTH_SHORT).show();
						MainActivity.light = Integer.parseInt(s);
						finish();
					}
					else if (s.equals("8") || s.equals("10") || s.equals("12") || s.equals("14") || s.equals("16") || s.equals("18") || s.equals("20"))
					{
						MainActivity.textView.setTextSize(Integer.parseInt(s));
						editor.putInt("bs", Integer.parseInt(s));
						editor.commit();
						Toast.makeText(ctx, "已调整字号", Toast.LENGTH_SHORT).show();
						finish();
					}
					else if (s.indexOf("字号选择") != -1)
					{
						MainActivity.cho = 5;
						menuintent = new Intent(ctx, menuAct.class);
						startActivity(menuintent);
						finish();
					}
					else if (s.indexOf("密码保护") != -1)
					{
						if (mima().equals("关闭"))
						{
							editor.putString("passtext", "  设定新密码  ");
						}
						else
						{
							editor.putString("passtext", " 输入原密码 ");
						}
						editor.commit();
						passint = new Intent(ctx, passwordAct.class);
						startActivity(passint);
						finish();
					}
					else if (s.equals("更改密码"))
					{
						if (mima().equals("开启"))
						{
							editor.putString("passtext", "  输入原密码  ");
							editor.commit();
							passint = new Intent(ctx, passwordAct.class);
							startActivity(passint);
							finish();
						}
						else
						{
							Toast.makeText(ctx, "你还没开启密码服务呢..", Toast.LENGTH_SHORT).show();
						}
					}
					else if (s.equals("密码入口伪装"))
					{
						if (mima().equals("开启"))
						{
							MainActivity.inputtitle = "密码入口伪装";
							MainActivity.inputhite = "输入密码";
							MainActivity.inputset = sharedPreferences.getString("passtext", "输入密码");
							MainActivity.inputact = 1;
							passint = new Intent(ctx, inputAct.class);
							startActivity(passint);
							finish();
						}
						else
						{
							Toast.makeText(ctx, "你还没开启密码服务呢..", Toast.LENGTH_SHORT).show();
						}
					}
					else if (s.indexOf("触摸隐藏文字") != -1)
					{
						if (sharedPreferences.getString("touchHideText", "关闭").equals("开启"))
						{
							editor.putString("touchHideText", "关闭");
							Toast.makeText(ctx, "已关闭触摸隐藏文字！", Toast.LENGTH_SHORT).show();
						}
						else
						{
							editor.putString("touchHideText", "开启");
							Toast.makeText(ctx, "已开启触摸隐藏文字！\n隐藏后长按文字可重新显示！", Toast.LENGTH_SHORT).show();
						}
						editor.commit();
						finish();
					}
					else if (s.indexOf("启动应用隐藏文字") != -1)
					{
						if (sharedPreferences.getString("startHideText", "关闭").equals("开启"))
						{
							editor.putString("startHideText", "关闭");
						}
						else
						{
							editor.putString("startHideText", "开启");
						}
						editor.commit();
						Toast.makeText(ctx, "已" + sharedPreferences.getString("startHideText", "关闭") + "启动应用隐藏文字！", Toast.LENGTH_SHORT).show();
						finish();
					}
					else if (s.equals("FTP文件传输"))
					{
						passint = new Intent(ctx, ftpAct.class);
						startActivity(passint);
					}
					else if (s.equals("我的小说"))
					{
						if (sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}").equals("{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"))
						{
							Toast.makeText(ctx, "小说列表为空！请先打开一个小说", Toast.LENGTH_LONG).show();
						}
						else
						{
							passint = new Intent(ctx, novelAct.class);
							startActivity(passint);
						}
					}
					else if(s.equals("主题选择"))
					{
						Toast.makeText(ctx, "还未制作完成，请期待下一版！", Toast.LENGTH_LONG).show();
					}
					else if(s.equals("文件传输"))
					{
						MainActivity.cho = 6;
						menuintent = new Intent(ctx, menuAct.class);
						startActivity(menuintent);
						finish();
					}
					else if(s.equals("跳转页数"))
					{
						MainActivity.inputtitle = "跳转页数";
						MainActivity.inputhite = "请输入页号";
						MainActivity.inputkeys = "0123456789";
						MainActivity.inputset = "";
						MainActivity.inputact = 2;
						menuintent = new Intent(ctx, inputAct.class);
						startActivity(menuintent);
						finish();
					}
					else if(s.contains("智能翻页"))
					{
						if(sharedPreferences.getString("smartScroll", "开启").equals("开启")) editor.putString("smartScroll", "关闭"); MainActivity.smartScroll = "关闭";
						if(sharedPreferences.getString("smartScroll", "开启").equals("关闭")) editor.putString("smartScroll", "开启"); MainActivity.smartScroll = "开启";
						editor.commit();
						Toast.makeText(ctx, "已" + sharedPreferences.getString("smartScroll", "开启") + "智能翻页功能！", Toast.LENGTH_LONG).show();
						finish();
					}
				}
			});
    }

	private String mima()
	{
		if (sharedPreferences.getString("password", "").equals(""))
		{
			return "关闭";
		}
		else
		{
			return "开启";
		}
	}
	

}


