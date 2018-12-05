package com.stl.wristNotes;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class clockAct extends Activity
{
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);
        ctx = this;

        if(isAvilible(ctx, "cn.luern0313.wristnotesclock"))
        {
            File screenshot = new File(Environment.getExternalStorageDirectory() + "/腕上小纸条表盘截图");
            if((!screenshot.exists()) || screenshot.isFile())
                screenshot.mkdir();
            findViewById(R.id.clockDef).setVisibility(View.GONE);
            findViewById(R.id.clockDown).setVisibility(View.VISIBLE);
            if(MainActivity.mainScreen.getVisibility() == View.VISIBLE)
                ((Button) findViewById(R.id.clockScreen)).setText("关闭截图按钮");
        }
    }

    public void buttonMarket(View view)
    {
        ComponentName componentName = new ComponentName("com.mobvoi.ticwear.watchface.market", "com.mobvoi.ticwear.watchface.market.WatchFaceMarketActivity");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        startActivity(intent);
    }

    public void buttonOpenScreen(View view)
    {
        if(((Button) view).getText().toString().contains("开启"))
        {
            MainActivity.mainScreen.setVisibility(View.VISIBLE);
            ((Button) view).setText("关闭截图按钮");
        }
        else
        {
            MainActivity.mainScreen.setVisibility(View.GONE);
            ((Button) view).setText("开启截图按钮");
        }

    }

    public void buttonMana(View view)
    {
        MainActivity.filewillpath = Environment.getExternalStorageDirectory() + "/腕上小纸条表盘截图/";
        Intent intent = new Intent(ctx, fileselectAct.class);
        startActivity(intent);
    }

    private boolean isAvilible(Context context, String packageName)
    {
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if(pinfo != null)
        {
            for (int i = 0; i < pinfo.size(); i++)
            {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }
}
