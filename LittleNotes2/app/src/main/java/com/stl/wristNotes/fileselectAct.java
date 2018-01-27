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
import com.stl.wristNotes.method.*;


public class fileselectAct extends Activity
{
	Context ctx = this;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static Activity fileselectCtx;
    Intent intent;
    File fileselectwillfile;
    TextView fileselecttitle;
    String[] filelist;
    String[] filelist2;
	ListView fileselectView;


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
        fileselectView = (ListView) findViewById(R.id.fileselectList);
        fileselecttitle.setText(MainActivity.filewillpath);
        fileselecttitle.setClickable(true);
        fileselecttitle.setOnClickListener(new View.OnClickListener()
			{
				//@Override
				public void onClick(View p1)
				{
					try
					{
						if (!fileselecttitle.getText().equals("/"))
						{
							String[] filelist3 = MainActivity.filewillpath.split("/");
							String filelist4 = "";
                            if (filelist3 != null)
                            {
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
                                Toast.makeText(ctx, "你没有获取系统文件夹文件的权限╮(ˉ▽ˉ)╭", Toast.LENGTH_LONG).show();
                            }
						}
						else
						{
							MainActivity.cho = 0;
							intent = new Intent(fileselectCtx, menuAct.class);
							startActivity(intent);
							finish();
						}
					}
					catch (Exception e)
					{
						Toast.makeText(ctx, "你没有获取系统文件夹文件的权限╮(ˉ▽ˉ)╭", Toast.LENGTH_LONG).show();
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
                filelist = Environment.getExternalStorageDirectory().list();
                Toast.makeText(fileselectCtx, "你没有获取系统文件夹文件的权限╮(ˉ▽ˉ)╭", Toast.LENGTH_LONG).show();
            }
        }
		catch (Exception e)
        {
            MainActivity.filewillpath = Environment.getExternalStorageDirectory().toString() + "/";
            filelist = Environment.getExternalStorageDirectory().list();
            Toast.makeText(fileselectCtx, "你没有获取系统文件夹文件的权限╮(ˉ▽ˉ)╭", Toast.LENGTH_LONG).show();
            //Toast.makeText(fileselectCtx, e.toString(), Toast.LENGTH_SHORT).show();
        }
		try
		{
       		Arrays.sort(filelist);
		}
		catch (Exception e)
		{
			MainActivity.filewillpath = Environment.getExternalStorageDirectory().toString() + "/";
			filelist = Environment.getExternalStorageDirectory().list();
			Arrays.sort(filelist);
			Toast.makeText(ctx, "你没有获取系统文件夹文件的权限╮(ˉ▽ˉ)╭", Toast.LENGTH_LONG).show();
		}
		try
		{
			zAdapter adapter = new zAdapter(filelist, getLayoutInflater(), MainActivity.filewillpath);
			if(sharedPreferences.getString("function", "0000").split("")[2].equals("0"))//功能提醒
			{
				LayoutInflater infla = LayoutInflater.from(this);
				final View headView = infla.inflate(R.layout.widget_newfunction, null);
				fileselectView.addHeaderView(headView, null, true);

				((TextView) findViewById(R.id.functiontext)).setText("长按文件查看更多文件选项喵~");
				LinearLayout button = (LinearLayout) headView.findViewById(R.id.functionbutton);
				button.setClickable(true);
				button.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View p1)
					{
						fileselectView.removeHeaderView(headView);
						String[] function = sharedPreferences.getString("function", "0000").split("");
						function[2] = "1";
						editor.putString("function", MainActivity.join(function, ""));
						editor.commit();
					}
				});
			}
			fileselectView.setAdapter(adapter);
		}
		catch(Exception e)
		{
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
		}

        fileselectView.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> l, View v, int position, long id)
				{
					String s = filelist[position];
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
						if (new File(fileselecttitle.getText().toString() + s).length() < 512000)
						{
							fileOpen.openFile(fileselectCtx, editor, fileselecttitle.getText().toString(), s);
							finish();
						}
						else
						{
							fileOpen.bigFile(fileselectCtx, sharedPreferences, editor, fileselecttitle.getText().toString(), s);
						}
					}
				}
			});

        fileselectView.setOnItemLongClickListener(new OnItemLongClickListener()
			{
				@Override
				public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id)
				{
					String s = filelist[position];
					if (!new File(MainActivity.filewillpath + s + "/").isDirectory())
					{
						try
						{
							MainActivity.filedofile = s;
							MainActivity.filedopath = MainActivity.filewillpath;
							Intent helpint = new Intent(fileselectCtx, filetodoAct.class);
							startActivity(helpint);

						}
						catch (Exception e)
						{
							Toast.makeText(fileselectCtx, "错误错误错误了！-_-#", Toast.LENGTH_SHORT).show();
						}
					}
					return true;
				}
			});
    }

}

class zAdapter extends BaseAdapter
{

	private String[] mData;//定义数据。
	private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。
	private String path;
    private ImageView image;
    private TextView name;
    private ImageView imggo;
    private ToggleButton imgswi;
    private TextView tip;
    private View layoutview;

	/*
	 定义构造器，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
	 */
	public zAdapter(String[] data, LayoutInflater inflater, String path)
	{
		mData = data;
		mInflater = inflater;
		this.path = path;
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
		
		//将数据一一添加到自定义的布局中。
		name.setText(mData[position]);

		try{
		//获取文件拓展名
		if(new File(path + mData[position]).isDirectory())
		{
			image.setImageResource(R.drawable.folder);
		}
		else
		{
			if(mData[position].contains("."))
			{
				String exten = mData[position].split("[.]")[mData[position].split("[.]").length - 1];
				//name.setText(name.getText().toString() + "&" + exten);
				//String exten = "jpg";
				if(exten.equals("jpg")||exten.equals("png")||exten.equals("jpge")||exten.equals("gif")||exten.equals("bmp")||exten.equals("tif")||exten.equals("pic"))
				{
					image.setImageResource(R.drawable.imgfile);
				}
				else if(exten.equals("apk"))
				{
					image.setImageResource(R.drawable.apkfile);
				}
				else if(exten.equals("wav")||exten.equals("aif")||exten.equals("au")||exten.equals("mp3")||exten.equals("wma")||exten.equals("amr")||exten.equals("flac")||exten.equals("aac"))
				{
					image.setImageResource(R.drawable.audio);
				}
				else if(exten.equals("doc")||exten.equals("xls")||exten.equals("xlsx")||exten.equals("ppt")||exten.equals("docx")||exten.equals("pptx")||exten.equals("pdf"))
				{
					image.setImageResource(R.drawable.doc);
				}
				else if(exten.equals("txt"))
				{
					if(new File(path + mData[position]).length() < 102400)
					{
						image.setImageResource(R.drawable.txtfile);
					}
					else
					{
						image.setImageResource(R.drawable.novelfile);
					}
				}
				else
				{
					image.setImageResource(R.drawable.unknowfile);
				}
			}
			else
			{
				image.setImageResource(R.drawable.unknowfile);
			}
		}
		}
		catch(Exception e)
		{
			
		}
		return layoutview;
	}
}
