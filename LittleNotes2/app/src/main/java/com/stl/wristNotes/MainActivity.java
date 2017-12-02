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
import org.json.*;

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
    //外部-fileSelect
    String filepath;
    String filename;
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
    public static int cho = 0;
    String startHideText;
    public static int mode = 0;
    //0 普通
    //1 小说

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);

        sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        filepath = sharedPreferences.getString("filepath", Environment.getExternalStorageDirectory() + "/0学习文档/");
        filename = sharedPreferences.getString("filename", "1.txt");
        light = sharedPreferences.getInt("light", 6);
        startHideText = sharedPreferences.getString("startHideText", "关闭");
        mode = sharedPreferences.getInt("mode", 0);
		try
		{
			JSONObject novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
		}
		catch (JSONException e)
		{}
		mainLeft = (Button) findViewById(R.id.mainButtonLeft);
		mainRight = (Button) findViewById(R.id.mainButtonRight);
		mainHint = (TextView) findViewById(R.id.mainHint);
		mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);

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

			if (new File(filepath + filename).length() < 716800)
			{
				editor.putString("filename", filename);
				editor.putString("filepath", filepath);
				editor.commit();
				Toast.makeText(ctx, "成功打开文件:" + filename, Toast.LENGTH_SHORT).show();
			}
			else
			{
				fileselectAct.bigFile(ctx, sharedPreferences, editor, filepath, filename);
			}
        }

        //createFloatView()

        textView = (TextView) findViewById(R.id.mainTextView);

        PackageManager pm = ctx.getPackageManager();//context为当前Activity上下文
        try
        {
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
            if (sharedPreferences.getInt("isUpdated", 0) < pi.versionCode)
            {
                editor.putInt("isUpdated", pi.versionCode);
                editor.commit();
                Intent startint = new Intent(ctx, updated.class);
                startActivity(startint);
            }
        }
		catch (PackageManager.NameNotFoundException e)
        {
        }

		if (mode == 0)
		{
			mainLeft.setVisibility(View.INVISIBLE);
			mainRight.setVisibility(View.INVISIBLE);
			mainHint.setVisibility(View.INVISIBLE);
		}

        try
        {
            if (!new File(filepath + filename).exists())
            {
                textView.setText("\n\n你当前没有打开任何文档\n长按这里进入菜单，点击文档选择可以打开文档\n\n\n");
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
                textView.setText(fileReader(filepath + filename));
            }
        }
		catch (Exception e)
        {
            Toast.makeText(ctx, "错误！" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        if (sharedPreferences.getString("startHideText", "关闭").equals("开启"))
        {
            textView.setTextColor(Color.argb(0, 0, 0, 0));
            isalpha = 1;
        }
        else
        {
            if (!new File(filepath + filename).exists())
            {
                textView.setTextColor(Color.argb(255, 203, 203, 203));
            }
            else
            {
                textView.setTextColor(Color.argb(255, light * 8, light * 8, light * 8));
            }
            isalpha = 0;
        }

        if (!sharedPreferences.getString("password", "").equals(""))
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

				}
			});

        mainRight.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					
				}
			});

		mainHint.setClickable(true);
		mainHint.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{

				}
			});


	}

	public void textClick()
	{
		if (pass == 1 && sharedPreferences.getString("touchHideText", "关闭").equals("开启"))
		{
			textView.setTextColor(Color.argb(0, 0, 0, 0));
			isalpha = 1;
		}
		else if (pass == 0)
		{
			Intent passwordint = new Intent(ctx, passwordAct.class);
			startActivity(passwordint);
		}
	}

	public void textLongClick()
	{
		if (pass == 1)
		{
			if (isalpha == 0)
			{
				cho = 0;
				Intent menuint = new Intent(ctx, menuAct.class);
				startActivity(menuint);
			}
			else
			{
				textView.setTextColor(Color.argb(255, light * 8, light * 8, light * 8));
				isalpha = 0;
			}
		}
		else
		{
			Intent passwordint = new Intent(ctx, passwordAct.class);
			startActivity(passwordint);
		}
	}

	public static String fileReader(String path) throws IOException
	{
		FileReader reader = new FileReader(path);
		BufferedReader bReader = new BufferedReader(reader);
		StringBuffer temp = new StringBuffer();
		String temp1 = "";
		while ((temp1 = bReader.readLine()) != null)
		{
			//Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_LONG).show();
			temp.append(temp1 + "\n");
		}
		bReader.close();
		textView.setTextColor(Color.argb(255, light * 8, light * 8, light * 8));
		return temp.toString();
	}

	public static String novelReader(String path, int page) throws IOException
	{
		FileReader reader = new FileReader(path);
		BufferedReader bReader = new BufferedReader(reader);
		StringBuffer temp = new StringBuffer();
		bReader.skip(page * 500);
		char[] ch = new char[500];
		bReader.read(ch, 0, 500);
		for (char b : ch) temp.append(b);
		bReader.close();
		textView.setTextColor(Color.argb(255, light * 8, light * 8, light * 8));
		return temp.toString();
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


	private boolean isAdded = false; // 是否已增加悬浮窗
	private static WindowManager wm;
	private static WindowManager.LayoutParams params;
	private Button btn_floatView;

	/**
	 * 创建悬浮窗
	 */
	private void createFloatView()
	{
		btn_floatView = new Button(getApplicationContext());
		//btn_floatView.setBackground();

		wm = (WindowManager) getApplicationContext()
			.getSystemService(Context.WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();

		// 设置window type
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		/*
		 * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE;
		 * 那么优先级会降低一些, 即拉下通知栏不可见
		 */

		params.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

		// 设置Window flag
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
			| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 下面的flags属性的效果形同“锁定”。
		 * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
		 | LayoutParams.FLAG_NOT_FOCUSABLE
		 | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

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
	}

}



