package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.io.*;

public class menuAct extends Activity
{
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
    public static Activity ctx;
	TextView menutitle;
	ListAdapter adapter;
	Intent menuintent;
	Intent passint;
	String[] menuList;
	int[] menuimg;
	Intent i;
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		ctx = this;
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
			menuimg = new int[] { R.drawable.files, R.drawable.edit, R.drawable.novelfile, R.drawable.xs, R.drawable.preference, R.drawable.ftp, R.drawable.helps, R.drawable.about};
			menutitle.setText("设置");
		}
		else if (MainActivity.cho == 1)
		{
			menuList = new String[] { "1", "2", "3", "4", "5", "6" };
			menuimg = new int[] { 0, 0, 0, 0, 0, 0 };
			menutitle.setText("亮度调整");
		}
		else if (MainActivity.cho == 2)
		{
			menuList = new String[] { "调整亮度：" + sharedPreferences.getInt("light", 5), "字号选择：" + sharedPreferences.getInt("bs", 14), "主题选择" };
			menuimg = new int[] { 0, 0, R.drawable.theme};
			menutitle.setText("显示设置");
		}
		else if (MainActivity.cho == 3)
		{
			menuList = new String[] { "触摸隐藏文字：" + sharedPreferences.getString("touchHideText", "关闭"), "启动应用隐藏文字：" + sharedPreferences.getString("startHideText", "关闭"), "重置新功能提示", "密码保护：" + mima(), "更改密码", "密码入口伪装" };
			menuimg = new int[] { 0, 0, 0, R.drawable.password, 0, 0 };
			menutitle.setText("偏好设置");
		}
		else if (MainActivity.cho == 5)
		{
			menuList = new String[] { "8", "10", "12", "14" };
			menuimg = new int[] { 0, 0, 0, 0 };
			menutitle.setText("字号调整");
		}
		else if(MainActivity.cho == 6)
		{
			menuList = new String[] { "FTP文件传输", "蓝牙传输" };
			menuimg = new int[] { R.drawable.filecs, R.drawable.bluetooth };
			menutitle.setText("文件传输");
		}
		else if(MainActivity.cho == 7)
		{
			menuList = new String[] { "跳转页数", "智能翻页：" + sharedPreferences.getString("smartScroll", "开启") };
			menuimg = new int[] { 0, 0 };
			menutitle.setText("阅读菜单");
		}
		else if(MainActivity.cho == 8)
        {
			i = new Intent();
			i.putExtra("info", -1);
			setResult(0, i);
			menuList = new String[] { "删除该条小说记录", "文件属性" };
			menuimg = new int[] { R.drawable.rubb, R.drawable.about };
			menutitle.setText("小说记录");
        }

		adapter = new mAdapter(menuList, menuimg, getLayoutInflater());

		if(MainActivity.cho != 7 && MainActivity.cho != 8 && MainActivity.cho != 0 && sharedPreferences.getString("function", "0000").split("")[3].equals("0"))
		{
			LayoutInflater infla = LayoutInflater.from(this);
			final View headView = infla.inflate(R.layout.widget_newfunction, null);
			listView.addHeaderView(headView, null, true);

			((TextView)findViewById(R.id.functiontext)).setText("点击上方标题栏可以回到上一层喵~");
			LinearLayout button = (LinearLayout) headView.findViewById(R.id.functionbutton);
    	    button.setClickable(true);
			button.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					listView.removeHeaderView(headView);
					String[] function = sharedPreferences.getString("function", "0000").split("");
					function[3] = "1";
					editor.putString("function", MainActivity.join(function, ""));
					editor.commit();
				}
			});
		}
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> l, View v, int position, long id)
				{
					String s = menuList[position];
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
					else if (s.equals("8") || s.equals("10") || s.equals("12") || s.equals("14"))
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
						if(sharedPreferences.getString("smartScroll", "开启").equals("开启"))
                        {
                            editor.putString("smartScroll", "关闭");
                            MainActivity.smartScroll = "关闭";
                            Toast.makeText(ctx, "已关闭智能翻页功能！", Toast.LENGTH_LONG).show();
                        }
						else if(sharedPreferences.getString("smartScroll", "开启").equals("关闭"))
                        {
                            editor.putString("smartScroll", "开启");
                            MainActivity.smartScroll = "开启";
                            Toast.makeText(ctx, "已开启智能翻页功能！", Toast.LENGTH_LONG).show();
                        }
						editor.commit();
						finish();
					}
					else if(s.equals("删除该条小说记录"))
					{
						//Intent i = new Intent();
						i.putExtra("info", 1);
						//Toast.makeText(ctx, "#我是醒目的Toast!#" + i.getIntExtra("position", -1), Toast.LENGTH_LONG).show();
						setResult(0, i);
						finish();
					}
					else if(s.equals("文件属性"))
					{
						//Intent i = new Intent();
						i.putExtra("info", 2);
						setResult(0, i);
						finish();
					}
					else if(s.equals("重置新功能提示"))
					{
						editor.putString("function", "0000");
						editor.commit();
						Toast.makeText(ctx, "已重置！重启应用就能正常查看喵~", Toast.LENGTH_SHORT).show();
					}
					else if(s.equals("蓝牙传输"))
					{
						MainActivity.helpor = 5;
						menuintent = new Intent(ctx, helpAct.class);
						startActivity(menuintent);
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

class mAdapter extends BaseAdapter
{

	private String[] mData;//定义数据。
	private int[] mImg;
	private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。

	/*
	 定义构造器，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
	 */
	public mAdapter(String[] data, int[] img, LayoutInflater inflater)
	{
		mData = data;
		mImg = img;
		mInflater = inflater;
	}

	@Override
	public int getCount()
	{
		return mData.length;
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup viewGroup)
	{
		//获得ListView中的view
		View layoutview = mInflater.inflate(R.layout.menulist, null);

		//获得自定义布局中每一个控件的对象。
		ImageView image = (ImageView) layoutview.findViewById(R.id.menulistimg);
		TextView name = (TextView) layoutview.findViewById(R.id.menulistText);

		//将数据一一添加到自定义的布局中。
		name.setText(mData[position]);
		if(mImg[position] == 0)
		{
			image.setVisibility(View.GONE);
		}
		else
		{
			image.setImageResource(mImg[position]);
		}
		
		return layoutview;
	}
}
