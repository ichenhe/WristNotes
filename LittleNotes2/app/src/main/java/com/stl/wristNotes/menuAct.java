package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

import java.io.*;
import org.json.*;
import android.graphics.drawable.*;
import android.graphics.drawable.shapes.*;

public class menuAct extends Activity
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static Activity ctx;
    TextView menutitle;
    ListView listView;

    public static ListAdapter adapter;
    Intent menuintent;
    Intent passint;

    String[] menuList;
    int[] menuimg;
    int[] menubut;
    public static String[][] menutip;

    int tip = 1;
    Intent i;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        ctx = this;
        sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //menutitle = (TextView) findViewById(R.id.menuText);
        listView = (ListView) findViewById(R.id.menuList);
        //LinearLayout footViewLayout = (LinearLayout) LayoutInflater.from(ctx).inflate(R.layout.menu, null);
        //listView.addHeaderView(footViewLayout);

        LayoutInflater infla = LayoutInflater.from(this);

        View headView2 = infla.inflate(R.layout.widget_title, null);
        menutitle = (TextView) headView2.findViewById(R.id.title);
        listView.addHeaderView(headView2, null, true);

        menutitle.setClickable(true);
        menutitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View p1)
            {
                //Toast.makeText(ctx, p1.getHeight() + "", Toast.LENGTH_SHORT).show();
                if(MainActivity.cho == 2 || MainActivity.cho == 3 || MainActivity.cho == 4 || MainActivity.cho == 6)
                {
                    MainActivity.cho = 0;
                    menuintent = new Intent(ctx, menuAct.class);
                    startActivity(menuintent);
                    finish();
                }
                else if(MainActivity.cho == 1 || MainActivity.cho == 5)
                {
                    MainActivity.cho = 2;
                    menuintent = new Intent(ctx, menuAct.class);
                    startActivity(menuintent);
                    finish();
                }
                else if(MainActivity.cho == 999)
                {
                    MainActivity.cho = 2;
                    menuintent = new Intent(ctx, menuAct.class);
                    startActivity(menuintent);
                    finish();
                }
            }
        });

        if(MainActivity.cho == 0)
        {
            menuList = new String[]{"打开文档", "编辑文档", "我的小说", "显示设置", "偏好设置", "文件传输", "会员购", "帮助", "关于"};
            menuimg = new int[]{R.drawable.files, R.drawable.edit, R.drawable.novelfile, R.drawable.xs, R.drawable.preference, R.drawable.ftp, R.drawable.favicon, R.drawable.helps, R.drawable.about};
            menubut = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1};
            menutip = new String[][]{{""}, {""}, {""}, {""}, {""}, {""}, {""}, {""}, {""}};
            menutitle.setText("设置");
        }
        else if(MainActivity.cho == 1)
        {
            menuList = new String[]{"1", "2", "3", "4", "5", "6"};
            menuimg = new int[]{0, 0, 0, 0, 0, 0};
            menubut = new int[]{-1, -1, -1, -1, -1, -1};
            menutip = new String[][]{{""}, {""}, {""}, {""}, {""}, {""}};
            menutitle.setText("亮度调整");
        }
        else if(MainActivity.cho == 2)
        {
            menuList = new String[]{"袖口模式", "调整亮度：" + sharedPreferences.getInt("light", 5), "字号选择：" + sharedPreferences.getInt("bs", 14)};
            menuimg = new int[]{0, 0, 0, R.drawable.theme};
            menubut = new int[]{getState("cuffMode", "关闭", "关闭"), 2, 2, -1};
            menutip = new String[][]{{"已关闭", "已开启\n只在右半边显示"}, {"更改文字亮度"}, {"更改文字大小"}, {""}};
            menutitle.setText("显示设置");
        }
        else if(MainActivity.cho == 3)
        {
            menuList = new String[]{"触摸隐藏文字", "启动应用隐藏文字", "重置新功能提示", "隐藏文字显示时间", "密码保护", "更改密码", "密码入口伪装"};
            menuimg = new int[]{0, 0, 0, 0, 0, 0, 0};
            menubut = new int[]{getState("touchHideText", "关闭", "关闭"), getState("startHideText", "关闭", "关闭"), -1, getState("displayTime", "关闭", "关闭"), getState("password", "", ""), 2, 2};
            menutip = new String[][]{{"已关闭", "已开启\n长按可重新显示文字"}, {"已关闭", "已开启\n要先长按使文字显示"}, {"重新显示新功能提示"}, {"已关闭", "已开启\n点击隐藏文字后屏幕会显示当前时间"}, {"已关闭", "已开启"}, {"更改你的密码"}, {"更改密码输入界面标题栏的文字"}};
            menutitle.setText("偏好设置");
        }
        else if(MainActivity.cho == 5)
        {
            menuList = new String[]{"8", "10", "12", "14", "16"};
            menuimg = new int[]{0, 0, 0, 0, 0};
            menubut = new int[]{-1, -1, -1, -1, -1};
            menutip = new String[][]{{""}, {""}, {""}, {""}, {""}};
            menutitle.setText("字号调整");
        }
        else if(MainActivity.cho == 6)
        {
            menuList = new String[]{"FTP文件传输", "蓝牙传输"};
            menuimg = new int[]{R.drawable.filecs, R.drawable.bluetooth};
            menubut = new int[]{-1, -1};
            menutip = new String[][]{{"在手表上建立一个FTP服务器传输文件"}, {"使用蓝牙传输文件"}};
            menutitle.setText("文件传输");
        }
        else if(MainActivity.cho == 7)
        {
            menuList = new String[]{"跳转页数", "智能翻页", "自动翻页", "自动翻页速度"};
            menuimg = new int[]{0, 0, 0, 0};
            menubut = new int[]{2, getState("smartScroll", "开启", "关闭"), 0, 2};
            menutip = new String[][]{{"跳转到任意一页"}, {"已关闭", "已开启\n翻页键先滚动到页面底部再翻页"}, {"已关闭", "已开启\n系统会自动翻页"}, {MainActivity.autoScollSec + "秒"}};
            menutitle.setText("阅读菜单");
            if(MainActivity.autoScoll != 0) menubut[2] = 1;
        }
        else if(MainActivity.cho == 8)
        {
            i = new Intent();
            i.putExtra("info", -1);
            setResult(0, i);
            menuList = new String[]{"删除该小说记录", "文件属性"};
            menuimg = new int[]{R.drawable.rubb, R.drawable.about};
            menubut = new int[]{-1, -1};
            menutip = new String[][]{{""}, {""}};
            menutitle.setText("小说记录");
        }

        adapter = new mAdapter(menuList, menuimg, menubut, getLayoutInflater(), menutip);

        if(MainActivity.cho != 7 && MainActivity.cho != 8 && MainActivity.cho != 0 && sharedPreferences.getString("function", "00000").split("")[3].equals("0"))
        {
            final View menuClickBg = findViewById(R.id.menuClickBg);
            final View menuClickBu = findViewById(R.id.menuClickBu);
            
            ((Button)menuClickBg.findViewById(R.id.clickBg1)).getLayoutParams().height = 0;
            ((Button)menuClickBg.findViewById(R.id.clickBg2)).getLayoutParams().height = 79;//title高度
            
            ((TextView)menuClickBu.findViewById(R.id.clickBu2)).setText("点击上方标题栏\n可以回到上一层喵~");
            
            menuClickBg.setVisibility(View.VISIBLE);
            menuClickBu.setVisibility(View.VISIBLE);
            
            menuClickBu.findViewById(R.id.clickBu3).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View p1)
                    {
                        String[] function = sharedPreferences.getString("function", "00000").split("");
                        function[3] = "1";
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

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id)
            {
                position -= tip;
                String s = menuList[position];
                if(s.equals("打开文档"))
                {
                    MainActivity.filewillpath = "";
                    menuintent = new Intent(ctx, filetodoAct.class);
                    menuintent.putExtra("po", 2);
                    startActivity(menuintent);
                    finish();
                }
                else if(s.equals("编辑文档"))
                {
                    if(MainActivity.mode == 0)
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
                else if(s.equals("显示设置") || s.equals("偏好设置"))
                {
                    if(s.equals("显示设置")) MainActivity.cho = 2;
                    if(s.equals("偏好设置")) MainActivity.cho = 3;
                    menuintent = new Intent(ctx, menuAct.class);
                    startActivity(menuintent);
                    finish();
                }
                else if(s.equals("会员购"))
                {
                    MainActivity.helpor = 7;
                    Intent intent = new Intent(ctx, helpAct.class);
                    startActivity(intent);
                }
                else if(s.equals("帮助"))
                {
                    Intent helpint = new Intent(ctx, filetodoAct.class);
                    helpint.putExtra("po", 7);
                    startActivity(helpint);
                }
                else if(s.equals("关于"))
                {
                    Intent helpint = new Intent(ctx, aboutAct.class);
                    startActivity(helpint);
                    //menuintent = new Intent(ctx, aboutAct.class);
                    //startActivity(menuintent);
                }
                else if(s.indexOf("调整亮度") != -1)
                {
                    MainActivity.cho = 1;
                    menuintent = new Intent(ctx, menuAct.class);
                    startActivity(menuintent);
                    finish();
                }
                else if(s.equals("1") || s.equals("2") || s.equals("3") || s.equals("4") || s.equals("5") || s.equals("6"))
                {
					int light = Integer.parseInt(s) * 45;
                    if(light > 255) light = 255;
                    MainActivity.textView.setTextColor(Color.argb(Integer.parseInt(s) * 40, 255, 255, 255));
					MainActivity.mainLeft.setTextColor(Color.argb(light, 255, 255, 255));
					MainActivity.mainRight.setTextColor(Color.argb(light, 255, 255, 255));
					MainActivity.mainHint.setTextColor(Color.argb(light, 255, 255, 255));
                    editor.putInt("light", Integer.parseInt(s));
                    editor.commit();
                    Toast.makeText(ctx, "已调整亮度", Toast.LENGTH_SHORT).show();
                    MainActivity.light = Integer.parseInt(s);
                    finish();
                }
                else if(s.equals("8") || s.equals("10") || s.equals("12") || s.equals("14") || s.equals("16"))
                {
                    MainActivity.textView.setTextSize(Integer.parseInt(s));
                    editor.putInt("bs", Integer.parseInt(s));
                    editor.commit();
                    Toast.makeText(ctx, "已调整字号", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(s.indexOf("字号选择") != -1)
                {
                    MainActivity.cho = 5;
                    menuintent = new Intent(ctx, menuAct.class);
                    startActivity(menuintent);
                    finish();
                }
                else if(s.indexOf("密码保护") != -1)
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
                        ((ToggleButton) v.findViewById(R.id.menulistswi)).setChecked(false);
                        ((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][0]);
                        menubut[0] = 0;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    else
                    {
                        editor.putString("touchHideText", "开启");
                        ((ToggleButton)  v.findViewById(R.id.menulistswi)).setChecked(true);
                        ((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][1]);
                        menubut[0] = 1;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    editor.commit();
                }
                else if(s.indexOf("启动应用隐藏文字") != -1)
                {
                    if(sharedPreferences.getString("startHideText", "关闭").equals("开启"))
                    {
                        editor.putString("startHideText", "关闭");
                        //((ToggleButton) v.findViewById(R.id.menulistswi)).setChecked(false);
                        //((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][0]);
                        menubut[1] = 0;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    else
                    {
                        editor.putString("startHideText", "开启");
                        //((ToggleButton) v.findViewById(R.id.menulistswi)).setChecked(true);
                        //((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][1]);
                        menubut[1] = 1;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    editor.commit();
                }
                else if(s.equals("隐藏文字显示时间"))
                {
                    if(sharedPreferences.getString("displayTime", "关闭").equals("开启"))
                    {
                        editor.putString("displayTime", "关闭");
                        ((ToggleButton) v.findViewById(R.id.menulistswi)).setChecked(false);
                        ((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][0]);
                        menubut[3] = 0;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    else
                    {
                        editor.putString("displayTime", "开启");
                        ((ToggleButton) v.findViewById(R.id.menulistswi)).setChecked(true);
                        ((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][1]);
                        menubut[3] = 1;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    editor.commit();
                }
                else if(s.equals("FTP文件传输"))
                {
                    passint = new Intent(ctx, ftpAct.class);
                    startActivity(passint);
                }
                else if(s.equals("我的小说"))
                {
                    if(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}").equals("{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"))
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
                        ((ToggleButton) v.findViewById(R.id.menulistswi)).setChecked(false);
                        ((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][0]);
                        menubut[1] = 0;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    else if(sharedPreferences.getString("smartScroll", "开启").equals("关闭"))
                    {
                        editor.putString("smartScroll", "开启");
                        MainActivity.smartScroll = "开启";
                        ((ToggleButton)  v.findViewById(R.id.menulistswi)).setChecked(true);
                        ((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][1]);
                        menubut[1] = 1;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    editor.commit();
                }
                else if(s.equals("删除该小说记录"))
                {
                    i.putExtra("info", 1);
                    setResult(0, i);
                    finish();
                }
                else if(s.equals("文件属性"))
                {
                    i.putExtra("info", 2);
                    setResult(0, i);
                    finish();
                }
                else if(s.equals("重置新功能提示"))
                {
                    editor.putString("function", "00000");
                    editor.commit();
                    Toast.makeText(ctx, "已重置！重启应用就能正常查看喵~", Toast.LENGTH_SHORT).show();
                }
                else if(s.equals("蓝牙传输"))
                {
                    MainActivity.helpor = 5;
                    menuintent = new Intent(ctx, helpAct.class);
                    startActivity(menuintent);
                }
                else if(s.equals("自动翻页"))
                {
                    if(MainActivity.autoScoll == 1 || MainActivity.autoScoll == 3)
                    {
                        MainActivity.autoReadChange(2);
                        editor.putInt("autoScoll", 0);
                        ((ToggleButton) v.findViewById(R.id.menulistswi)).setChecked(false);
                        ((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][0]);
                        menubut[2] = 0;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    else
                    {
                        MainActivity.autoScollNowSec = MainActivity.autoScollSec;
                        MainActivity.autoReadChange(3);
                        editor.putInt("autoScoll", 1);
                        ((ToggleButton) v.findViewById(R.id.menulistswi)).setChecked(true);
                        ((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][1]);
                        menubut[2] = 1;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    editor.commit();
                }
                else if(s.equals("自动翻页速度"))
                {
                    menuintent = new Intent(ctx, filetodoAct.class);
                    menuintent.putExtra("po", 8);
                    startActivity(menuintent);
                }
                else if(s.equals("袖口模式"))
                {
                    if(sharedPreferences.getString("cuffMode", "开启").equals("开启"))
                    {
                        editor.putString("cuffMode", "关闭");
                        MainActivity.cuffMode = "关闭";
                        ((ToggleButton) v.findViewById(R.id.menulistswi)).setChecked(false);
                        ((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][0]);
                        menubut[0] = 0;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    else if(sharedPreferences.getString("cuffMode", "开启").equals("关闭"))
                    {
                        editor.putString("cuffMode", "开启");
                        MainActivity.cuffMode = "开启";
                        ((ToggleButton)  v.findViewById(R.id.menulistswi)).setChecked(true);
                        ((TextView) v.findViewById(R.id.menulisttip)).setText(menutip[position][1]);
                        menubut[0] = 1;
                        ((BaseAdapter)adapter).notifyDataSetChanged();
                    }
                    editor.commit();
                    MainActivity.cuffModeChange(ctx);
                }
            }
        });
    }

    
    private String mima()
    {
        if(sharedPreferences.getString("password", "").equals(""))
        {
            return "关闭";
        }
        else
        {
            return "开启";
        }
    }

    private int getState(String sp, String de, String close)
    {
        if(sharedPreferences.getString(sp, de).equals(close))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }


}

class mAdapter extends BaseAdapter
{

    private String[] mData;//定义数据。
    private int[] mImg;
    private int[] mBut;
    private String[][] mTip;
    private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。

    private ImageView image;
    private TextView name;
    private ImageView imggo;
    private ToggleButton imgswi;
    private TextView tip;
    private View layoutview;

    public mAdapter(String[] data, int[] img, int[] but, LayoutInflater inflater, String[][] tip)
    {
        mData = data;
        mImg = img;
        mInflater = inflater;
        mBut = but;
        mTip = tip;

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
        layoutview = mInflater.inflate(R.layout.menulist, null);
        //获得自定义布局中每一个控件的对象。
        image = (ImageView) layoutview.findViewById(R.id.menulistimg);
        name = (TextView) layoutview.findViewById(R.id.menulistText);
        imggo = (ImageView) layoutview.findViewById(R.id.menulistgo);
        imgswi = (ToggleButton) layoutview.findViewById(R.id.menulistswi);
        tip = (TextView) layoutview.findViewById(R.id.menulisttip);
        //将数据一一添加到自定义的布局中。
        name.setText(mData[position]);

        //左侧图标
        if(mImg[position] == 0) image.setVisibility(View.GONE);
        else image.setImageResource(mImg[position]);

        //开关和go按钮
        if(mBut[position] == -1)
        {
            imggo.setVisibility(View.GONE);
            imgswi.setVisibility(View.GONE);
        }
        else if(mBut[position] == 2)
        {
            imgswi.setVisibility(View.GONE);
        }
        else
        {
            if(mBut[position] == 0)
            {
                imgswi.setChecked(false);
            }
            else if(mBut[position] == 1)
            {
                imgswi.setChecked(true);
            }
            imggo.setVisibility(View.GONE);
            imgswi.setTag(0);
        }

        //小字
        if(mTip[position][0].equals(""))
        {
            tip.setVisibility(View.GONE);
        }
        else if(mBut[position] == 0 || mBut[position] == 1)
        {
            tip.setText(mTip[position][mBut[position]]);
        }
        else
        {
            tip.setText(mTip[position][0]);
        }
		
		if(MainActivity.cho == 1)//亮度调整
		{
			int l = Integer.valueOf(mData[position]).intValue() * 40;
			//name.setTextColor(Color.argb(255, l, l, l));
			//name.setBackgroundColor(Color.argb(255, 0, 0, 0));
            TextView text = (TextView) layoutview.findViewById(R.id.menulisttext);
            text.setVisibility(View.VISIBLE);
            text.setTextColor(Color.argb(l, 255, 255, 255));
            
            image.setImageResource(R.drawable.shape_black);
		}
		else if(MainActivity.cho == 5)
		{
			//name.setTextSize(Integer.valueOf(mData[position]).intValue());
            TextView text = (TextView) layoutview.findViewById(R.id.menulisttext);
            text.setVisibility(View.VISIBLE);
            text.setTextSize(Math.round((Integer.valueOf(mData[position]).intValue() - 8) * 1.5 + 22));
		}
        else if(MainActivity.cho == 0)
        {
            layoutview.findViewById(R.id.menulistred).setVisibility(View.VISIBLE);
        }


        return layoutview;
    }
}
