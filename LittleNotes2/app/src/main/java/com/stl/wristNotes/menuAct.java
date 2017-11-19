package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.graphics.*;
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
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
        menutitle = (TextView) findViewById(R.id.menuText);
        ListView listView = (ListView) findViewById(R.id.menuList);
		//LinearLayout footViewLayout = (LinearLayout) LayoutInflater.from(ctx).inflate(R.layout.menu, null);
		//listView.addHeaderView(footViewLayout);

		menutitle.setClickable(true);
		menutitle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					if (MainActivity.cho == 2 || MainActivity.cho == 3 || MainActivity.cho == 4)
					{
						MainActivity.cho = 0;
						menuintent = new Intent(ctx, menuAct.class);
						startActivity(menuintent);
						finish();
					}
					else if (MainActivity.cho == 1||MainActivity.cho == 5)
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
			String[] menu_main = new String[] { "打开文档", "编辑文档", "我的小说", "显示设置", "偏好设置", "FTP文件传输", "帮助", "关于" };
			adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, menu_main);
			menutitle.setText("设置");
		}
		else if (MainActivity.cho == 1)
		{
			String[] menu_light = new String[] { "1", "2", "3", "4", "5", "6" };
			adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, menu_light);
			menutitle.setText("亮度调整");
		}
		else if (MainActivity.cho == 2)
		{
			String[] menu_display = new String[] { "调整亮度：" + (sharedPreferences.getInt("light", 6) - 1), "字号选择：" + sharedPreferences.getInt("bs", 14), "主题选择" };
			adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, menu_display);
			menutitle.setText("显示设置");
		}
		else if (MainActivity.cho == 3)
		{
			String[] menu_per = new String[] { "触摸隐藏文字：" + sharedPreferences.getString("touchHideText", "关闭"), "启动应用隐藏文字：" + sharedPreferences.getString("startHideText", "关闭"), "密码保护："+mima(), "更改密码", "密码入口伪装" };
			adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, menu_per);
			menutitle.setText("偏好设置");
		}
		else if (MainActivity.cho == 4)
		{
			String[] menu_watch = new String[] { "左右空出像素数", "上下空出行数" };
			adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, menu_watch);
			menutitle.setText("手表优化");
		}
		else if (MainActivity.cho == 5)
		{
			String[] menu_bs = new String[] { "8", "10", "12", "14", "16" };
			adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, menu_bs);
			menutitle.setText("字号调整");
		}

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
						menuintent = new Intent(ctx, editfileAct.class);
						startActivity(menuintent);
						finish();
					}
					else if (s.equals("显示设置") || s.equals("偏好设置") || s.equals("手表优化"))
					{
						MainActivity.cho = position - 1;
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
						MainActivity.textView.setTextColor(Color.argb(255, (1 + Integer.parseInt(s)) * 12, (1 + Integer.parseInt(s)) * 12, (1 + Integer.parseInt(s)) * 12));
						editor.putInt("light", 1 + Integer.parseInt(s));
						editor.commit();
						Toast.makeText(ctx, "已调整亮度", Toast.LENGTH_SHORT).show();
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
						if(mima().equals("关闭"))
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
					else if(s.equals("更改密码"))
					{
						if(mima().equals("开启"))
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
					else if(s.equals("密码入口伪装"))
					{
						if(mima().equals("开启"))
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
					else if(s.indexOf("触摸隐藏文字") != -1)
					{
						if(sharedPreferences.getString("touchHideText", "关闭").equals("开启"))
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
					else if(s.indexOf("启动应用隐藏文字") != -1)
					{
						if(sharedPreferences.getString("startHideText", "关闭").equals("开启"))
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
					else if(s.equals("FTP文件传输"))
					{
						passint = new Intent(ctx, ftpAct.class);
						startActivity(passint);
					}
					else if(s.equals("我的小说"))
					{
						Toast.makeText(ctx, "还未制作完成，请期待下一版！", Toast.LENGTH_LONG).show();
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


