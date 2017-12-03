package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

import java.io.*;
import java.util.*;

import org.json.*;
import org.apache.ftpserver.util.*;


public class fileselectAct extends Activity
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static Activity fileselectCtx;
    Intent intent;
    File fileselectwillfile;
    TextView fileselecttitle;
    String[] filelist;
    String[] filelist2;
    String s;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileselect);

        fileselectCtx = this;
        sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (MainActivity.filewillpath.equals("")) MainActivity.filewillpath = sharedPreferences.getString("filepath", Environment.getExternalStorageDirectory().toString() + "/");
        fileselecttitle = (TextView) findViewById(R.id.fileselectText);
        ListView fileselectView = (ListView) findViewById(R.id.fileselectList);
        fileselecttitle.setText(MainActivity.filewillpath);
        fileselecttitle.setClickable(true);
        fileselecttitle.setOnClickListener(new View.OnClickListener()
        {
            //@Override
            public void onClick(View p1)
            {
                if (!fileselecttitle.getText().equals("/"))
                {
                    String[] filelist3 = MainActivity.filewillpath.split("/");
                    String filelist4 = "";
                    for (int i = 0; i < filelist3.length - 1; i++)
                    {
                        filelist4 += filelist3[i] + "/";
                    }
                    MainActivity.filewillpath = filelist4;
                    intent = new Intent(fileselectCtx, fileselectAct.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    MainActivity.cho = 0;
                    intent = new Intent(fileselectCtx, menuAct.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //fileselectwillfile.list();
        try
        {
            fileselectwillfile = new File(MainActivity.filewillpath);
            if (fileselectwillfile.exists())
            {
                filelist = fileselectwillfile.list();
            }
            else
            {
                MainActivity.filewillpath = Environment.getExternalStorageDirectory().toString() + "/";
                filelist = new File(Environment.getExternalStorageDirectory().toString() + "/").list();
            }
        } catch (Exception e)
        {
            filelist = new File(Environment.getExternalStorageDirectory().toString() + "/").list();
            //Toast.makeText(fileselectCtx, e.toString(), Toast.LENGTH_SHORT).show();
        }
        Arrays.sort(filelist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.menulistText, filelist);
        fileselectView.setAdapter(adapter);

        fileselectView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id)
            {
                s = (String) l.getItemAtPosition(position);
                if (new File(MainActivity.filewillpath + s + "/").isDirectory())
                {
                    MainActivity.filewillpath = MainActivity.filewillpath + s + "/";
                    Intent helpint = new Intent(fileselectCtx, fileselectAct.class);
                    startActivity(helpint);
                    finish();
                }
                else
                {
//						Intent mainint = new Intent(ctx, MainActivity.class);
//						startActivity(mainint);
                    if (new File(fileselecttitle.getText().toString() + s).length() < 716800)
                    {
                        MainActivity.openFile(fileselectCtx, editor, fileselecttitle.getText().toString(), s);
                        finish();
                    }
                    else
                    {
                        MainActivity.bigFile(fileselectCtx, sharedPreferences, editor, fileselecttitle.getText().toString(), s);
                    }
                }
            }
        });

        fileselectView.setOnItemLongClickListener(new OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id)
            {
                String s = (String) l.getItemAtPosition(position);
                if (!new File(MainActivity.filewillpath + s + "/").isDirectory())
                {
                    try
                    {
                        MainActivity.filedofile = s;
                        MainActivity.filedopath = MainActivity.filewillpath;
                        Intent helpint = new Intent(fileselectCtx, filetodoAct.class);
                        startActivity(helpint);

                    } catch (Exception e)
                    {
                        Toast.makeText(fileselectCtx, "错误！", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }
}
