package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import java.io.*;

import android.content.pm.*;
import android.content.pm.PackageManager.*;

import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.*;
import java.text.*;
import java.util.*;
import info.monitorenter.cpdetector.io.*;
import com.stl.wristNotes.method.*;
//import com.mobvoi.android.gesture.*;

public class MainActivity extends Activity
{
    //filename:最后打开文件名:...0学习文档
    //filepath:最后打开文件路径:1.txt
    //light:亮度:3
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //外部-input
    public static String inputtitle = "";
    public static String inputset = "";
    public static String inputhite = "";
    public static int inputact;
	public static String inputkeys = "";
    //外部-fileSelect
    public static String filepath;
    public static String filename;
    public static String filewillpath = "";
    //
    public static String passwdsp = "";
    //外部-filetodo
    public static String filedofile = "";
    public static String filedopath = "";
    //外部-help
    public static int helpor = 1;

    Context ctx = this;
    public static int pass = 0;
    int isalpha = 0;
    public static int light;
    public static TextView textView;
    public static Button mainLeft;
    public static Button mainRight;
    public static TextView mainHint;
    public static ScrollView mainScrollView;
	public static LinearLayout mainLinearLayout;
    public static int cho = 0;
    String startHideText;
    public static int mode = 0;
    //0 普通 1 小说
    public static String smartScroll = "开启";
    public static int p = 0;
    //当前小说在列表中的位置
    JSONObject novellist;
	BroadcastReceiver batteryLevelReceiver;
	public static int batteryLevel;//电量
	IntentFilter batteryLevelFilter;
	
	int scrollLength;

	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);

		//以下是各种储存信息的读取
        sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        filepath = sharedPreferences.getString("filepath", Environment.getExternalStorageDirectory() + "/0学习文档/");
        filename = sharedPreferences.getString("filename", "1.txt");
        light = sharedPreferences.getInt("light", 5);
        startHideText = sharedPreferences.getString("startHideText", "关闭");
        mode = sharedPreferences.getInt("mode", 0);
        p = sharedPreferences.getInt("p", 0);
        smartScroll = sharedPreferences.getString("smartScroll", "开启");
		filewillpath = Environment.getExternalStorageDirectory().toString() + "/";
		scrollLength = new Double(((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() * Math.sqrt(2) / 3).intValue();
        try
        {
            novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
        }
        catch (JSONException e)
        {
            Toast.makeText(ctx, "小说观看记录解析错误，请尝试重启应用程序或重新安装喵", Toast.LENGTH_LONG).show();
        }
		
		//主界面的控件
        mainLeft = (Button) findViewById(R.id.mainButtonLeft);
        mainRight = (Button) findViewById(R.id.mainButtonRight);
        mainHint = (TextView) findViewById(R.id.mainHint);
        mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
		mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);    //scroll套着的layout

		//从外部打开，调用这里，获取文件路径及文件大小判断等
		//暂时在这里测试获取文件编码
        Intent intent = getIntent();
        String action = intent.getAction();
        if (intent.ACTION_VIEW.equals(action))
        {
            filepath = intent.getDataString()/*.replaceAll("%(?![0-9a-fA-F]{2})", "%25")*/;
            try
            {
                filepath = URLDecoder.decode(filepath, "UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                Toast.makeText(ctx, "路径解码错误。。我也不知道怎么办", Toast.LENGTH_LONG).show();
            }
            String[] filet1 = filepath.split("/");
            filepath = "/";
            for (int i = 3; i < filet1.length - 1; i++)
            {
                filepath += filet1[i] + "/";
            }
            filename = filet1[filet1.length - 1];

            if (new File(filepath + filename).length() < 512000)
            {
                editor.putString("filename", filename);
                editor.putString("filepath", filepath);
                mode = 0;
                Toast.makeText(ctx, "成功打开文件:" + filename, Toast.LENGTH_SHORT).show();
            }
            else
            {
                fileOpen.bigFile(ctx, sharedPreferences, editor, filepath, filename);
                mode = 1;
            }
			Toast.makeText(ctx, getFileEncode((filepath + filename)), Toast.LENGTH_LONG).show();
			editor.putInt("mode", mode);
			editor.commit();
        }

        //createFloatView()


        textView = (TextView) findViewById(R.id.mainTextView);

		//检查应用是否为更新版本或新安装
        PackageManager pm = ctx.getPackageManager();//context为当前Activity上下文
        try
        {
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
            if (sharedPreferences.getInt("isUpdated", 0) < pi.versionCode)
            {
                Intent startint = new Intent(ctx, updated.class);
                startActivity(startint);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
			Toast.makeText(ctx, "应用版本信息获取失败", Toast.LENGTH_LONG).show();
        }

        if (mode == 0)    //不为小说模式隐藏按钮和提示信息
        {
            mainLeft.setVisibility(View.INVISIBLE);
            mainRight.setVisibility(View.INVISIBLE);
            mainHint.setVisibility(View.INVISIBLE);
        }
        else if (mode == 1)
        {
            try
            {
                //Toast.makeText(ctx, filepath + filename + "   " + mode, Toast.LENGTH_SHORT).show();
                textView.setText(fileOpen.novelReader(filepath + filename, Integer.valueOf(novellist.getString("page").split("▒")[p - 1]).intValue()));
                Toast.makeText(ctx, "已跳转至上次观看位置，请享用∼", Toast.LENGTH_SHORT).show();
				mainHint.setText(getHintText(sharedPreferences));
				batterylevel();
            }
            catch (JSONException e)
            {
                Toast.makeText(ctx, "json未知错误！", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(ctx, "未知错误！", Toast.LENGTH_SHORT).show();
            }
        }

        try
        {
            if (!new File(filepath + filename).exists())    //文件不存在时
            {
                textView.setText("\n\n你当前没有打开任何文档\n长按这里进入菜单，点击文档选择可以打开文档哦\n\n\n");
                textView.setTextColor(Color.argb(255, 203, 203, 203));
                isalpha = 0;
                if (!new File(filepath).exists())
                {
                    Intent startint = new Intent(ctx, startAct.class);
                    startActivity(startint);
                    editor.putString("filepath", Environment.getExternalStorageDirectory().toString() + "/");
                    editor.commit();
                }
            }
            else
            {
                if (mode == 0) textView.setText(fileOpen.fileReader(filepath + filename));
            }
        }
        catch (Exception e)
        {
            Toast.makeText(ctx, "未知错误喵。。", Toast.LENGTH_SHORT).show();
        }

        if (sharedPreferences.getString("startHideText", "关闭").equals("开启"))    //开启应用隐藏文字
        {
            textView.setTextColor(Color.argb(0, 0, 0, 0));
            isalpha = 1;
			if (mode == 1)
			{
				MainActivity.mainLeft.setVisibility(View.INVISIBLE);
				MainActivity.mainRight.setVisibility(View.INVISIBLE);
				MainActivity.mainHint.setVisibility(View.INVISIBLE);
			}
        }
        else
        {
            textView.setTextColor(Color.argb(255, light * 40, light * 40, light * 40));
            isalpha = 0;
        }

        if (!sharedPreferences.getString("password", "").equals(""))    //有密码时
        {
            if (sharedPreferences.getString("passtext", "").equals("  再次输入  ") || sharedPreferences.getString("passtext", "").equals("  设定新密码  ") || sharedPreferences.getString("passtext", "").equals("  输入原密码  ") || sharedPreferences.getString("passtext", "").equals(" 输入原密码 "))
            {
                pass = 1;
                editor.putString("passtext", "输入密码");
                editor.commit();
                Intent passwordint = new Intent(ctx, passwordAct.class);
                startActivity(passwordint);
            }
            else
            {
                pass = 0;
                textView.setTextColor(Color.argb(255, 0, 0, 0));
                Intent passwordint = new Intent(ctx, passwordAct.class);
                startActivity(passwordint);
            }
        }
        else
        {
            pass = 1;
        }

		try
		{
		if(sharedPreferences.getString("function", "0000").split("")[1].equals("0") && mode == 1)
		{
			LayoutInflater infla = LayoutInflater.from(this);
			final View view = infla.inflate(R.layout.widget_newfunction, null);

			((TextView)view.findViewById(R.id.functiontext)).setText("点击上方标题栏可以查看更多文件选项喵~");
			LinearLayout button = (LinearLayout) view.findViewById(R.id.functionbutton);
    	    button.setClickable(true);
			button.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View p1)
					{
						mainLinearLayout.removeView(view);
						String[] function = sharedPreferences.getString("function", "0000").split("");
						function[1] = "1";
						editor.putString("function", MainActivity.join(function, ""));
						editor.commit();
					}
				});
			mainLinearLayout.addView(view, 1);
		}
		}
		catch(Exception e)
		{
			Toast.makeText(ctx, "#我是醒目的Toast!#" + e.toString(), Toast.LENGTH_LONG).show();
		}
		
        textView.setTextSize(sharedPreferences.getInt("bs", 14));
        textView.setClickable(true);
        textView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					textClick();
				}
			});
        textView.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View p1)
				{
					textLongClick();
					return true;
				}
			});

        mainLeft.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					try
					{
						novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
						List<String> novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));
						novelpage.set(p - 1, String.valueOf(Integer.valueOf(novelpage.get(p - 1)).intValue() - 1));
						novellist.put("page", join(novelpage.toArray(new String[novelpage.size()]), "▒"));
						textView.setText(fileOpen.novelReader(filepath + filename, Integer.valueOf(novelpage.get(p - 1)).intValue()));
						mainScrollView.fullScroll(View.FOCUS_UP);
						editor.putString("novelList", novellist.toString());
						editor.commit();
						mainHint.setText(getHintText(sharedPreferences));
						//batteryLevel();
					}
					catch (Exception e)
					{
						if (e.toString().contains("charCount")) Toast.makeText(ctx, "已是第一页！", Toast.LENGTH_SHORT).show();
						else Toast.makeText(ctx, "发生未知错误！", Toast.LENGTH_SHORT).show();
					}
				}
			});

        mainRight.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					novelScroll(mainLinearLayout, mainScrollView);
				}
			});

        mainHint.setClickable(true);
        mainHint.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					MainActivity.cho = 7;
					Intent intent = new Intent(ctx, menuAct.class);
					startActivity(intent);
				}
			});


    }


	public void novelScroll(LinearLayout layout, ScrollView scroll)
	{
		if (((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() + scroll.getScrollY() + 10 >= layout.getMeasuredHeight() || smartScroll.equals("关闭"))
		{
			try
			{
				novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
				List<String> novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));
				novelpage.set(p - 1, String.valueOf(Integer.valueOf(novelpage.get(p - 1)).intValue() + 1));
				novellist.put("page", join(novelpage.toArray(new String[novelpage.size()]), "▒"));
				textView.setText("");
				textView.setText(fileOpen.novelReader(filepath + filename, Integer.valueOf(novelpage.get(p - 1)).intValue()));
				mainScrollView.fullScroll(View.FOCUS_UP);
				editor.putString("novelList", novellist.toString());
				editor.commit();
				mainHint.setText(getHintText(sharedPreferences));
				//batteryLevel();
			}
			catch (JSONException e)
			{
				Toast.makeText(ctx, "json错误" + e.toString(), Toast.LENGTH_SHORT).show();
			}
			catch (Exception e)
			{
				Toast.makeText(ctx, "错误" + e.toString(), Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			scroll.smoothScrollTo(0, scroll.getScrollY() + scrollLength);
		}
	}

    public void textClick()
    {
        if (pass == 1 && sharedPreferences.getString("touchHideText", "关闭").equals("开启"))
        {
            textView.setTextColor(Color.argb(0, 0, 0, 0));
            isalpha = 1;
			if (mode == 1)
			{
				MainActivity.mainLeft.setVisibility(View.INVISIBLE);
				MainActivity.mainRight.setVisibility(View.INVISIBLE);
				MainActivity.mainHint.setVisibility(View.INVISIBLE);
			}
        }
        else if (pass == 0)
        {
            Intent passwordint = new Intent(ctx, passwordAct.class);
            startActivity(passwordint);
        }
    }

    public void textLongClick()
    {
        if (pass == 1)//无密码或已解锁
        {
            if (isalpha == 0)//调出菜单
            {
                cho = 0;
                Intent menuint = new Intent(ctx, menuAct.class);
                startActivity(menuint);
            }
            else//显示文字
            {
                textView.setTextColor(Color.argb(255, light * 40, light * 40, light * 40));
                isalpha = 0;
				if (mode == 1)
				{
					MainActivity.mainLeft.setVisibility(View.VISIBLE);
					MainActivity.mainRight.setVisibility(View.VISIBLE);
					MainActivity.mainHint.setVisibility(View.VISIBLE);
				}
            }
        }
        else//密码未解锁
        {
            Intent passwordint = new Intent(ctx, passwordAct.class);
            startActivity(passwordint);
        }
    }

	public static String getHintText(SharedPreferences sp)
	{
		try
		{
			JSONObject novellist = new JSONObject(sp.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
			ArrayList<String> novelpath = new ArrayList(Arrays.asList(novellist.getString("path").split("▒")));
			ArrayList<String> novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));
			return new SimpleDateFormat("HH:mm").format(new Date()) + "\n" + (Integer.valueOf(novelpage.get(p - 1)).intValue() + 1) + "页  " + batteryLevel + "%";
		}
		catch (JSONException e)
		{
			return "";
		}
	}

	/**
     * 获得编码
     * @param filePath
     * @return
     */
    public static String getFileEncode(String filePath) {
        String charsetName = null;
		byte[] filebyte;
        try {
            filebyte = file.getBytes(filePath, 512);
			File tempFile = new File(Environment.getExternalStorageDirectory() + "/wsxzttemp.txt");
			file.createFile(tempFile);
			file.writeFile(tempFile.getPath(), filebyte);
            CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
            detector.add(new ParsingDetector(false));
            detector.add(JChardetFacade.getInstance());
            detector.add(ASCIIDetector.getInstance());
            detector.add(UnicodeDetector.getInstance());
            java.nio.charset.Charset charset = null;
            //charset = detector.detectCodepage(file, 51200);
			charset = detector.detectCodepage(new ByteArrayInputStream(filebyte), filebyte.length);
            if (charset != null) {
                charsetName = charset.name();
            } else {
                charsetName = "UTF-8";
            }
        } catch (Exception e) {
            //Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
            return e.toString();
        }
        return charsetName;
    }
	
    public static String join(String[] strs, String splitter)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(strs[0]);
        for (int i = 1; i < strs.length; i++)
        {
            //Toast.makeText(fileselectCtx, strs[i], Toast.LENGTH_SHORT);
            sb.append(splitter + strs[i]);
        }
        return sb.toString();
    }

	public void batterylevel()
	{
        if (batteryLevelReceiver == null)
        {
            batteryLevelReceiver = new BroadcastReceiver()
            {
                public void onReceive(Context context, Intent intent)
                {
                    int rawlevel = intent.getIntExtra("level", -1);//获得当前电量
                    int scale = intent.getIntExtra("scale", -1);//获得总电量
                    batteryLevel = -1;
                    if (rawlevel >= 0 && scale > 0)
                    {
                        batteryLevel = (rawlevel * 100) / scale;
                    }
                    if (mode == 1)
                    {
                        mainHint.setText(mainHint.getText().toString().split("  ")[0] + "  " + batteryLevel + "%");
                    }
                }
            };
            batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(batteryLevelReceiver, batteryLevelFilter);
        }
	}

    //挠挠
    public boolean onSingleTapSidePanel(MotionEvent e)
    {
        //Toast.makeText(ctx, "挠挠单击！", Toast.LENGTH_SHORT).show();
        textClick();
        return false;
    }

    public boolean onLongPressSidePanel(MotionEvent e)
    {
        //Toast.makeText(ctx, "挠挠长按！", Toast.LENGTH_SHORT).show();
        textLongClick();
        return false;
    }


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		//销毁广播 
		if (batteryLevelReceiver != null) unregisterReceiver(batteryLevelReceiver);
		//ctx.unregisterReceiver(this);
	}

	/*private boolean isAdded = false; // 是否已增加悬浮窗
	 private static WindowManager wm;
	 private static WindowManager.LayoutParams params;
	 private Button btn_floatView;


     * 创建悬浮窗

	 private void createFloatView()
	 {
	 btn_floatView = new Button(getApplicationContext());
	 //btn_floatView.setBackground();

	 wm = (WindowManager) getApplicationContext()
	 .getSystemService(Context.WINDOW_SERVICE);
	 params = new WindowManager.LayoutParams();

	 // 设置window type
	 params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

	 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE;
	 * 那么优先级会降低一些, 即拉下通知栏不可见


	 params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

	 // 设置Window flag
	 params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
	 | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

	 * 下面的flags属性的效果形同“锁定”。
	 * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
	 wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
	 | LayoutParams.FLAG_NOT_FOCUSABLE
	 | LayoutParams.FLAG_NOT_TOUCHABLE;


	 // 设置悬浮窗的长得宽
	 params.gravity = Gravity.RIGHT | Gravity.TOP;
	 params.width = 10;
	 params.height = wm.getDefaultDisplay().getHeight();

	 params.x = 0;
	 params.y = 0;
	 // 设置悬浮窗的Touch监听
	 btn_floatView.setOnTouchListener(new OnTouchListener()
	 {

	 public boolean onTouch(View v, MotionEvent event)
	 {
	 switch (event.getAction())
	 {
	 case MotionEvent.ACTION_DOWN:
	 Toast.makeText(getApplicationContext(), "悬浮窗！", Toast.LENGTH_LONG).show();
	 break;
	 }
	 return true;
	 }
	 });

	 wm.addView(btn_floatView, params);
	 wm.updateViewLayout(btn_floatView, params);
	 isAdded = true;
	 }*/

}



