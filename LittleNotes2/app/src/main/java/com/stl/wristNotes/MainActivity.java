package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.Log;
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
import com.stl.wristNotes.method.*;
//import org.apache.http.*;
import android.widget.Toolbar.*;
//import com.mobvoi.android.gesture.*;

public class MainActivity extends Activity
{
    //filename:最后打开文件名:...0学习文档
    //filepath:最后打开文件路径:1.txt
    //light:亮度:3
    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;

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
	public static int filedopo = -1;
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
    public static String code;
    //当前小说的编码
    public static int autoScoll = 0;
    //自动翻页
    public static int autoScollSec = 0;
    static int autoScollNowSec = 0;
    public static String cuffMode = "关闭";

    static Handler autoReadHandler = new Handler();
    static Runnable autoReadRunnable;

    JSONObject novellist;
	BroadcastReceiver batteryLevelReceiver;
	public static int batteryLevel;//电量
	IntentFilter batteryLevelFilter;

	int scrollLength;

    public static BufferedReader novelReader;//小说

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
        code = sharedPreferences.getString("code", "UTF-8");
        smartScroll = sharedPreferences.getString("smartScroll", "开启");
        autoScollSec = sharedPreferences.getInt("autoScollSec", 4);
        autoScoll = sharedPreferences.getInt("autoScoll", 0);
        autoScollNowSec = autoScollSec;
        cuffMode = sharedPreferences.getString("cuffMode", "关闭");
		filewillpath = Environment.getExternalStorageDirectory().toString() + "/";
		scrollLength = new Double(((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() / 2).intValue();
        try
        {
            novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
        }
        catch(JSONException e)
        {
            Toast.makeText(ctx, "小说观看记录解析错误，请尝试重启应用程序或重新安装喵", Toast.LENGTH_LONG).show();
        }

		//主界面的控件
        mainLeft = (Button) findViewById(R.id.mainButtonLeft);
        mainRight = (Button) findViewById(R.id.mainButtonRight);
        mainHint = (TextView) findViewById(R.id.mainHint);
        mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
		mainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);    //scroll套着的layout

        int l = light * 45;
        if(l > 255) l = 255;
		mainLeft.setTextColor(Color.argb(l, 255, 255, 255));
		mainRight.setTextColor(Color.argb(l, 255, 255, 255));
		mainHint.setTextColor(Color.argb(l, 255, 255, 255));

		//从外部打开，调用这里，获取文件路径及文件大小判断等
        Intent intent = getIntent();
        String action = intent.getAction();
        if(intent.ACTION_VIEW.equals(action))
        {
            filepath = intent.getDataString()/*.replaceAll("%(?![0-9a-fA-F]{2})", "%25")*/;
            try
            {
                filepath = URLDecoder.decode(filepath, "UTF-8");
            }
            catch(UnsupportedEncodingException e)
            {
                Toast.makeText(ctx, "路径解码错误。。我也不知道怎么办", Toast.LENGTH_LONG).show();
            }
            String[] filet1 = filepath.split("/");
            filepath = "/";
            for(int i = 3; i < filet1.length - 1; i++)
            {
                filepath += filet1[i] + "/";
            }
            filename = filet1[filet1.length - 1];

            if(new File(filepath + filename).length() < 92160)
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
			//Toast.makeText(ctx, fileOpen.getFileEncode((filepath + filename)), Toast.LENGTH_LONG).show();
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
			if(sharedPreferences.getInt("isUpdated", 0) < 82)
			{
				editor.putString("function", "00000");
				editor.commit();
			}
            if(sharedPreferences.getInt("isUpdated", 0) < pi.versionCode)
            {
                Intent startint = new Intent(ctx, updated.class);
                startActivity(startint);
            }
        }
        catch(PackageManager.NameNotFoundException e)
        {
			Toast.makeText(ctx, "应用版本信息获取失败", Toast.LENGTH_LONG).show();
        }

        cuffModeChange(ctx);//袖口模式

        if(mode == 0)    //不为小说模式隐藏按钮和提示信息
        {
            mainLeft.setVisibility(View.INVISIBLE);
            mainRight.setVisibility(View.INVISIBLE);
            mainHint.setVisibility(View.INVISIBLE);
            autoReadChange(2);
        }
        else if(mode == 1)
        {
            try
            {
                novelReader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath + filename), code));
                if(Integer.valueOf(novellist.getString("page").split("▒")[p - 1]).intValue() != 0)
                {
                    novelReader.skip(Integer.valueOf(novellist.getString("page").split("▒")[p - 1]).intValue() * 500);
                }
                textView.setText(fileOpen.novelReader(0));
                //Toast.makeText(ctx, "已跳转至上次观看位置，请享用∼", Toast.LENGTH_SHORT).show();
				mainHint.setText(getHintText(sharedPreferences));
            }
            catch(Exception e)
            {
                Toast.makeText(ctx, "未知错误！", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        batterylevel();

        try
        {
            if(!new File(filepath + filename).exists())    //文件不存在时
            {
                textView.setText("\n\n你当前没有打开任何文档\n长按这里进入菜单，点击文档选择可以打开文档哦\n\n\n");
                textView.setTextColor(Color.argb(255, 203, 203, 203));
                isalpha = 0;
                if(!new File(filepath).exists())
                {
                    Intent startint = new Intent(ctx, startAct.class);
                    startActivity(startint);
                    editor.putString("filepath", Environment.getExternalStorageDirectory().toString() + "/");
                    editor.commit();
                }
            }
            else
            {
                if(mode == 0)
                {
                    textView.setText("正在打开文件...\n请稍后喵...");
                    runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            { 
                                try
                                {
                                    textView.setText(fileOpen.fileReader(filepath + filename));
                                }
                                catch(Exception e)
                                {
                                    textView.setText("打开文件错误。。请重试试试");
                                }
                            }
                        });
                }
            }
        }
        catch(Exception e)
        {
            Toast.makeText(ctx, "未知错误喵。。", Toast.LENGTH_SHORT).show();
        }

        if(sharedPreferences.getString("startHideText", "关闭").equals("开启"))    //开启应用隐藏文字
        {
            textView.setTextColor(Color.argb(0, 0, 0, 0));
            isalpha = 1;
			if(mode == 1)
			{
				mainLeft.setVisibility(View.INVISIBLE);
			    mainRight.setVisibility(View.INVISIBLE);
				mainHint.setVisibility(View.INVISIBLE);
			}
        }
        else
        {
            textView.setTextColor(Color.argb(light * 40, 255, 255, 255));
            isalpha = 0;
        }

        if(!sharedPreferences.getString("password", "").equals(""))    //有密码时
        {
            if(sharedPreferences.getString("passtext", "").equals("  再次输入  ") || sharedPreferences.getString("passtext", "").equals("  设定新密码  ") || sharedPreferences.getString("passtext", "").equals("  输入原密码  ") || sharedPreferences.getString("passtext", "").equals(" 输入原密码 "))
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
                mainLeft.setVisibility(View.INVISIBLE);
                mainRight.setVisibility(View.INVISIBLE);
                mainHint.setVisibility(View.INVISIBLE);
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
            if(sharedPreferences.getString("function", "00000").split("")[1].equals("0") && mode == 1)
            {
                final View menuClickBg = findViewById(R.id.mainClickBg);
                final View menuClickBu = findViewById(R.id.mainClickBu);

                ((Button)menuClickBg.findViewById(R.id.clickBg1)).getLayoutParams().height = 0;
                ((Button)menuClickBg.findViewById(R.id.clickBg2)).getLayoutParams().height = 79;//title高度

                ((TextView)menuClickBu.findViewById(R.id.clickBu2)).setText("点击上方标题栏\n可以查看更多阅读选项喵~");

                menuClickBg.setVisibility(View.VISIBLE);
                menuClickBu.setVisibility(View.VISIBLE);

                menuClickBu.findViewById(R.id.clickBu3).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View p1)
                        {
                            String[] function = sharedPreferences.getString("function", "00000").split("");
                            function[1] = "1";
                            editor.putString("function", MainActivity.join(function, ""));
                            editor.commit();

                            menuClickBg.setVisibility(View.GONE);
                            menuClickBu.setVisibility(View.GONE);
                        }
                    });


                menuClickBu.findViewById(R.id.clickBu4).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View p1)
                        {
                            startActivity(new Intent(ctx, settileAct.class));
                        }
                    });
            }
		}
		catch(Exception e)
		{
			Toast.makeText(ctx, "提示信息显示错误..", Toast.LENGTH_LONG).show();
		}

		autoReadRunnable = new Runnable(){
            @Override
            public void run()
            {
                if(autoScollNowSec - 1 == 0)
                {
                    novelScroll(mainLinearLayout, mainScrollView, true);
                    autoScollNowSec = autoScollSec;
                    mainRight.setText(String.valueOf(autoScollNowSec));
                }
                else
                {
                    autoScollNowSec--;
                    mainRight.setText(String.valueOf(autoScollNowSec));
                }
                //要做的事情，这里再次调用此Runnable对象，以实现每一秒实现一次的定时器操作
                autoReadHandler.postDelayed(this, 1000);
            }  
        };

        if(autoScoll == 1)
        {
            autoReadChange(3);
            if(isalpha == 0) autoReadChange(1);
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
                    if(autoScoll == 0)
                    {
                        try
                        {
                            novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
                            List<String> novelpage = new ArrayList<String>(Arrays.asList(novellist.getString("page").split("▒")));
                            if(Integer.valueOf(novelpage.get(p - 1)).intValue() != 0)
                            {
                                novelpage.set(p - 1, String.valueOf(Integer.valueOf(novelpage.get(p - 1)).intValue() - 1));
                                novellist.put("page", join(novelpage.toArray(new String[novelpage.size()]), "▒"));

                                novelReader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath + filename), code));
                                novelReader.skip(Integer.valueOf(novellist.getString("page").split("▒")[p - 1]).intValue() * 500);
                                textView.setText(fileOpen.novelReader(0));
                                mainScrollView.fullScroll(View.FOCUS_UP);
                                editor.putString("novelList", novellist.toString());
                                editor.commit();
                                mainHint.setText(getHintText(sharedPreferences));
                                //batteryLevel();
                            }
                            else
                            {
                                Toast.makeText(ctx, "已是第一页！", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch(Exception e)
                        {
                            Toast.makeText(ctx, "发生未知错误！", Toast.LENGTH_SHORT).show(); e.printStackTrace();
                        }
                    }
                    else if(autoScoll == 1)
                    {
                        autoReadChange(3);
                    }
                    else if(autoScoll == 3)
                    {
                        autoReadChange(1);
                    }
				}
			});

        mainRight.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
                    if(autoScoll == 0)
                    {
				    	novelScroll(mainLinearLayout, mainScrollView, false);
                    }
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

    //Activity重新启动
    @Override
    protected void onResume()
    {
        if(autoScoll == 3 && isalpha == 0) autoReadChange(1);
        super.onResume();
    }

    //Activity暂停
    @Override
    protected void onPause()
    {
        if(autoScoll == 1) autoReadChange(3);
        super.onPause();
    }

    public static void cuffModeChange(Context ctx)
    {
        if(cuffMode.equals("开启"))
        {
            mainLinearLayout.setPadding(((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() / 4, 0, 0, 0);
            if(mode == 1) mainHint.setText(getHintText(sharedPreferences));
        }
        else
        {
            mainLinearLayout.setPadding(0, 0, 0, 0);
            if(mode == 1) mainHint.setText(getHintText(sharedPreferences));
        }
    }

    public static void autoReadChange(int status)
    {
        if(status == 1)
        {
            autoScoll = 1;
            mainLeft.setText("■");
            autoReadHandler.postDelayed(autoReadRunnable, 1000);
        }
        else if(status == 2)
        {
            autoScoll = 0;
            editor.putInt("autoScoll", 0);
            editor.commit();
            mainLeft.setText("◀");
            mainRight.setText("▶");
            autoReadHandler.removeCallbacks(autoReadRunnable);
        }
        else if(status == 3)
        {
            autoScoll = 3;
            //autoScollNowSec = autoScollSec;
            mainLeft.setText("▶");
            mainRight.setText(String.valueOf(autoScollNowSec));
            autoReadHandler.removeCallbacks(autoReadRunnable);
        }
    }

	public void novelScroll(LinearLayout layout, ScrollView scroll, Boolean isAuto)
	{
		if(((WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() + scroll.getScrollY() + 40 >= layout.getMeasuredHeight() || (smartScroll.equals("关闭") && !isAuto))
		{
			try
			{
			    novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
                List<String> novelpage = new ArrayList<String>(Arrays.asList(novellist.getString("page").split("▒")));
                novelpage.set(p - 1, String.valueOf(Integer.valueOf(novelpage.get(p - 1)).intValue() + 1));
                novellist.put("page", join(novelpage.toArray(new String[novelpage.size()]), "▒"));
			    String text = fileOpen.novelReader(0);
                Log.i("Debug", text);
                if(!text.equals(""))
                {
                    textView.setText(text);
                    mainScrollView.fullScroll(View.FOCUS_UP);
                    editor.putString("novelList", novellist.toString());
                    editor.commit();
                    mainHint.setText(getHintText(sharedPreferences));
                }
                else//看完了
                {
                    ArrayList<String> novelComplete = new ArrayList<String>(Arrays.asList(sharedPreferences.getString("novelComplete", "").split("▒")));
                    if(!novelComplete.contains(filepath + filename))
                    {
                        novelComplete.add(filepath + filename);
                        editor.putString("novelComplete", join(novelComplete.toArray(new String[novelComplete.size()]), "▒"));
                        editor.commit();
                    }
                    Toast.makeText(ctx, "你已经看完这本小说啦！在“我的小说”里已经贴上了记号", Toast.LENGTH_SHORT).show();
                }
			}
			catch(JSONException e)
			{
				Toast.makeText(ctx, "json错误" + e.toString(), Toast.LENGTH_SHORT).show();
			}
			catch(Exception e)
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
        if(pass == 1 && sharedPreferences.getString("touchHideText", "关闭").equals("开启"))
        {
            textView.setTextColor(Color.argb(0, 0, 0, 0));
            isalpha = 1;
			if(mode == 1)
			{
				MainActivity.mainLeft.setVisibility(View.INVISIBLE);
				MainActivity.mainRight.setVisibility(View.INVISIBLE);
				MainActivity.mainHint.setVisibility(View.INVISIBLE);
			}
            if(autoScoll == 1) autoReadChange(3);
            if(sharedPreferences.getString("displayTime", "关闭").equals("开启"))
            {
                ((TextView)findViewById(R.id.mainTime)).setText(new SimpleDateFormat("HH:mm").format(new Date()));
                findViewById(R.id.mainTime).setVisibility(View.VISIBLE);
            }
        }
        else if(pass == 0)
        {
            Intent passwordint = new Intent(ctx, passwordAct.class);
            startActivity(passwordint);
        }
    }

    public void textLongClick()
    {
        if(pass == 1)//无密码或已解锁
        {
            if(isalpha == 0)//调出菜单
            {
                cho = 0;
                Intent menuint = new Intent(ctx, menuAct.class);
                startActivity(menuint);
            }
            else//显示文字
            {
                textView.setTextColor(Color.argb(light * 40 - 10, 255, 255, 255));
                isalpha = 0;
				if(mode == 1)
				{
					MainActivity.mainLeft.setVisibility(View.VISIBLE);
					MainActivity.mainRight.setVisibility(View.VISIBLE);
					MainActivity.mainHint.setVisibility(View.VISIBLE);
				}
                if(autoScoll == 3) autoReadChange(1);
                if(sharedPreferences.getString("displayTime", "关闭").equals("开启"))
                {
                    findViewById(R.id.mainTime).setVisibility(View.GONE);
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
			ArrayList<String> novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));
            if(cuffMode.equals("开启"))
            {
			    return new SimpleDateFormat("HH:mm").format(new Date()) + "\n" + (Integer.valueOf(novelpage.get(p - 1)).intValue() + 1) + "页\n" + batteryLevel + "%";
            }
            return new SimpleDateFormat("HH:mm").format(new Date()) + "\n" + (Integer.valueOf(novelpage.get(p - 1)).intValue() + 1) + "页  " + batteryLevel + "%";
		}
		catch(JSONException e)
		{
			return "";
		}
	}

    public void stRequestedOrientation()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    
    public static String join(String[] strs, String splitter)
    {
		if(strs.length != 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(strs[0]);
			for(int i = 1; i < strs.length; i++)
			{
				//Toast.makeText(fileselectCtx, strs[i], Toast.LENGTH_SHORT);
				sb.append(splitter + strs[i]);
			}
			return sb.toString();
		}
		else
		{
			return "";
		}
    }

	public void batterylevel()
	{
        if(batteryLevelReceiver == null)
        {
            batteryLevelReceiver = new BroadcastReceiver()
            {
                public void onReceive(Context context, Intent intent)
                {
                    if(mode == 1)
                    {
                        int rawlevel = intent.getIntExtra("level", -1);//获得当前电量
                        int scale = intent.getIntExtra("scale", -1);//获得总电量
                        batteryLevel = -1;
                        if(rawlevel >= 0 && scale > 0)
                        {
                            batteryLevel = (rawlevel * 100) / scale;
                        }
                        mainHint.setText(getHintText(sharedPreferences));
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
		if(batteryLevelReceiver != null) unregisterReceiver(batteryLevelReceiver);
		//ctx.unregisterReceiver(this);
        if(mode == 1) try
        {
            novelReader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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



