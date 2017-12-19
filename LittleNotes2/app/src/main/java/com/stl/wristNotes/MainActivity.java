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
    public static int cho = 0;
    String startHideText;
    public static int mode = 0;
    //0 普通 1 小说
    public static int p = 0;
    //当前小说在列表中的位置
    JSONObject novellist;
	BroadcastReceiver batteryLevelReceiver;
	public static int batteryLevel;//电量
	IntentFilter batteryLevelFilter;
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
        p = sharedPreferences.getInt("p", 0);
		filewillpath = Environment.getExternalStorageDirectory().toString() + "/";
        try
        {
            novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
        }
        catch (JSONException e)
        {
            Toast.makeText(ctx, "小说观看记录解析错误，请尝试重启应用程序或重新安装喵", Toast.LENGTH_LONG).show();
        }
        mainLeft = (Button) findViewById(R.id.mainButtonLeft);
        mainRight = (Button) findViewById(R.id.mainButtonRight);
        mainHint = (TextView) findViewById(R.id.mainHint);
        mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);

        Intent intent = getIntent();
        String action = intent.getAction();
        if(intent.ACTION_VIEW.equals(action))
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

            if(new File(filepath + filename).length() < 716800)
            {
                editor.putString("filename", filename);
                editor.putString("filepath", filepath);
                editor.commit();
                mode = 0;
                Toast.makeText(ctx, "成功打开文件:" + filename, Toast.LENGTH_SHORT).show();
            }
            else
            {
                bigFile(ctx, sharedPreferences, editor, filepath, filename);
                mode = 1;
            }
        }

        //createFloatView()

		
        textView = (TextView) findViewById(R.id.mainTextView);

        PackageManager pm = ctx.getPackageManager();//context为当前Activity上下文
        try
        {
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
            if(sharedPreferences.getInt("isUpdated", 0) < pi.versionCode)
            {
                Intent startint = new Intent(ctx, updated.class);
                startActivity(startint);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
			Toast.makeText(ctx, "应用版本信息获取失败", Toast.LENGTH_LONG).show();
        }

        if(mode == 0)
        {
            mainLeft.setVisibility(View.INVISIBLE);
            mainRight.setVisibility(View.INVISIBLE);
            mainHint.setVisibility(View.INVISIBLE);
        }
        else if(mode == 1)
        {
            try
            {
                //Toast.makeText(ctx, filepath + filename + "   " + mode, Toast.LENGTH_SHORT).show();
                textView.setText(novelReader(filepath + filename, Integer.valueOf(novellist.getString("page").split("▒")[p - 1]).intValue()));
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
            if(!new File(filepath + filename).exists())
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
                if(mode == 0) textView.setText(fileReader(filepath + filename));
            }
        }
        catch (Exception e)
        {
            Toast.makeText(ctx, "未知错误喵。。", Toast.LENGTH_SHORT).show();
        }

        if(sharedPreferences.getString("startHideText", "关闭").equals("开启"))
        {
            textView.setTextColor(Color.argb(0, 0, 0, 0));
            isalpha = 1;
        }
        else
        {
            if(!new File(filepath + filename).exists())
            {
                textView.setTextColor(Color.argb(255, 203, 203, 203));
            }
            else
            {
                textView.setTextColor(Color.argb(255, light * 8, light * 8, light * 8));
            }
            isalpha = 0;
        }

        if(!sharedPreferences.getString("password", "").equals(""))
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
                try
                {
                    novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
                    List<String> novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));
                    novelpage.set(p - 1, String.valueOf(Integer.valueOf(novelpage.get(p - 1)).intValue() - 1));
                    novellist.put("page", join(novelpage.toArray(new String[novelpage.size()]), "▒"));
                    textView.setText(novelReader(filepath + filename, Integer.valueOf(novelpage.get(p - 1)).intValue()));
                    editor.putString("novelList", novellist.toString());
                    editor.commit();
					mainHint.setText(getHintText(sharedPreferences));
					//batteryLevel();
                }
                catch (Exception e)
                {
                    if(e.toString().contains("charCount")) Toast.makeText(ctx, "已是第一页！", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(ctx, "发生未知错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mainRight.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View p1)
            {
                try
                {
                    novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
                    List<String> novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));
                    novelpage.set(p - 1, String.valueOf(Integer.valueOf(novelpage.get(p - 1)).intValue() + 1));
                    novellist.put("page", join(novelpage.toArray(new String[novelpage.size()]), "▒"));
                    textView.setText(novelReader(filepath + filename, Integer.valueOf(novelpage.get(p - 1)).intValue()));
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
        if(pass == 1 && sharedPreferences.getString("touchHideText", "关闭").equals("开启"))
        {
            textView.setTextColor(Color.argb(0, 0, 0, 0));
            isalpha = 1;
        }
        else if(pass == 0)
        {
            Intent passwordint = new Intent(ctx, passwordAct.class);
            startActivity(passwordint);
        }
    }

    public void textLongClick()
    {
        if(pass == 1)
        {
            if(isalpha == 0)
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
        //textView.setTextColor(Color.argb(255, light * 8, light * 8, light * 8));
        return temp.toString();
    }
	
	public static void bigFile(final Context ctx, final SharedPreferences sp, final SharedPreferences.Editor ed, final String path, final String name)
    {
        new AlertDialog.Builder(ctx)
                .setMessage("您打开的文件过大，使用普通模式打开会导致应用卡死，是否使用小说模式打开？\n（您也可以长按文件用小说模式打开）")
                .setPositiveButton("小说模式", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        openNovel(ctx, sp, ed, path, name);
                    }
                })
                .setNegativeButton("普通模式", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Toast.makeText(ctx, "正在打开。。前方应用即将卡死，实在不行就卸了重装吧→_→", Toast.LENGTH_SHORT).show();
                        openFile(ctx, ed, path, name);
                    }
                }).show();
    }
	
	public static void openNovel(final Context ctx, SharedPreferences sp, SharedPreferences.Editor ed, String path, String name)
    {
        try
        {
            MainActivity.p = 0;
            JSONObject novellist = new JSONObject(sp.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
			List<String> novelname = new ArrayList<>();
			if (!novellist.getString("name").equals("")) novelname = new ArrayList(Arrays.asList(novellist.getString("name").split("▒")));
            List<String> novelpath = new ArrayList<>();
            if (!novellist.getString("path").equals("")) novelpath = new ArrayList(Arrays.asList(novellist.getString("path").split("▒")));
			List<String> novelpage = new ArrayList<>();
			if (!novellist.getString("page").equals("")) novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));

            for (int i = 0; i < novelpath.size(); i++)
            {
				//Toast.makeText(fileselectCtx, "第" + i + "个，内容是" + novelpath.get(i) + "原地址是" + path + "%%" + name, Toast.LENGTH_LONG).show();
                if (novelpath.get(i).equals(path + name))
                {
                    MainActivity.p = i + 1;
                }
            }

			//Toast.makeText(fileselectCtx, MainActivity.p + "rrr" + novelpath.size(), Toast.LENGTH_LONG).show();
            if (MainActivity.p == 0)//第一次打开
            {
				String nname = name.substring(0, name.length()-name.split("[.]")[name.split("[.]").length-1].length()-1);
				novelname.add(nname);
                novelpath.add(path + name);
				novelpage.add("0");
				
                novellist.put("name", join(novelname.toArray(new String[novelname.size()]), "▒"));
				novellist.put("path", join(novelpath.toArray(new String[novelpath.size()]), "▒"));
				novellist.put("page", join(novelpage.toArray(new String[novelpage.size()]), "▒"));
				
				MainActivity.textView.setText(MainActivity.novelReader(path + name, 0));
                ed.putString("novelList", novellist.toString());
                MainActivity.p = novelname.size();

                Toast.makeText(ctx, "已打开小说 " + nname, Toast.LENGTH_SHORT).show();
            }
            else
            {
                MainActivity.textView.setText(MainActivity.novelReader(path + name, Integer.valueOf(novellist.getString("page").split("▒")[MainActivity.p - 1]).intValue()));
                Toast.makeText(ctx, "已跳转至上次观看位置", Toast.LENGTH_SHORT).show();
            }
            MainActivity.mode = 1;
            ed.putInt("mode", 1);
            ed.putInt("p", MainActivity.p);
			ed.putString("filepath", path);
			ed.putString("filename", name);
            ed.commit();
            MainActivity.filename = name;
            MainActivity.filepath = path;
            MainActivity.mainLeft.setVisibility(View.VISIBLE);
            MainActivity.mainRight.setVisibility(View.VISIBLE);
            MainActivity.mainHint.setVisibility(View.VISIBLE);
			mainHint.setText(getHintText(sp));
        } catch (Exception e)
        {
            Toast.makeText(ctx, "打开文件错误！" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }
	
	public static void openFile(Context ctx, SharedPreferences.Editor editor, String path, String name)
    {
        try
        {
            MainActivity.textView.setText(MainActivity.fileReader(path + name));
            editor.putString("filename", name);
            editor.putString("filepath", path);
            editor.putInt("mode", 0);
            editor.commit();
            MainActivity.mode = 0;
            MainActivity.mainLeft.setVisibility(View.INVISIBLE);
            MainActivity.mainRight.setVisibility(View.INVISIBLE);
            MainActivity.mainHint.setVisibility(View.INVISIBLE);
            Toast.makeText(ctx, "成功打开文件:" + name + "喵", Toast.LENGTH_SHORT).show();
        } catch (IOException e)
        {
            Toast.makeText(ctx, "打开文件错误！", Toast.LENGTH_SHORT).show();
        }
    }
	
	public static String getHintText(SharedPreferences sp)
	{
		try
		{
			JSONObject novellist = new JSONObject(sp.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
			ArrayList<String> novelpath = new ArrayList(Arrays.asList(novellist.getString("path").split("▒")));
			ArrayList<String> novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));
			return new SimpleDateFormat("HH:mm").format(new Date()) + "\n" + (Integer.valueOf(novelpage.get(p - 1)).intValue() + 1) + "/" + (int)Math.ceil(new File(novelpath.get(p - 1)).length() / 500 + 1) + "  " + batteryLevel + "%";
		}
		catch (JSONException e)
		{
			return "";
		}
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
        if(batteryLevelReceiver == null)
        {
            batteryLevelReceiver = new BroadcastReceiver()
            {
                public void onReceive(Context context, Intent intent)
                {
                    int rawlevel = intent.getIntExtra("level", -1);//获得当前电量
                    int scale = intent.getIntExtra("scale", -1);//获得总电量
                    batteryLevel = -1;
                    if(rawlevel >= 0 && scale > 0)
                    {
                        batteryLevel = (rawlevel * 100) / scale;
                    }
                    if(mode == 1)
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
		if(batteryLevelReceiver != null) unregisterReceiver(batteryLevelReceiver);
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



