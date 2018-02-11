package com.stl.wristNotes;

import android.app.*;
import android.media.MediaMetadataRetriever;
import android.os.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.view.*;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.*;
import com.stl.wristNotes.method.*;


public class filetodoAct extends Activity
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context ctx = this;
	Intent intent = getIntent();
	String[] todo;
	int[] img;
	String[] hint;
	int po;

	TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filetodo);
        sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

		LayoutInflater infla = LayoutInflater.from(this);
		ListView listView = (ListView) findViewById(R.id.filedoList);

		View headView2 = infla.inflate(R.layout.widget_title, null);
		title = (TextView) headView2.findViewById(R.id.title);
		listView.addHeaderView(headView2, null, true);

		po = intent.getIntExtra("po", 0);
        if(po == 0)//文件属性
		{
			todo = new String[]{"用隐私模式打开", "用小说模式打开", "收藏该文件", "新建...", "打开为...(*)", "重命名(*)", "分享...", "删除", "属性"};
			img = new int[]{R.drawable.txtfile, R.drawable.novelfile, R.drawable.star, 0, 0, 0, 0, 0};
			hint = new String[]{"", "", "收藏至快速访问", "在当前目录下新建", "查找其他应用打开", "", "分享文件至其他应用", "", ""};
		}
		else if(po == 1)//文件夹属性
		{
			todo = new String[]{"新建...", "收藏该文件夹", "删除", "属性"};
			img = new int[]{0, R.drawable.starfor, 0, 0, 0, 0, 0};
			hint = new String[]{"在选中目录下新建", "收藏至快速访问", "", ""};
		}
		else if(po == 2)//？
		{
			ArrayList<String> todo = new ArrayList();
            todo.add("上一次打开目录");
            todo.add("SD卡根目录");
            if(sharedPreferences.getString("star", "").contains("▒"))
            for(int i = 2; i < )
            sharedPreferences.getInt("star", 5);
		}
        fAdapter adapter = new fAdapter(todo, img, getLayoutInflater());
        title.setText(MainActivity.filedofile);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> l, View v, int position, long id)
				{
					if(position != 0)
					{
						String s = todo[position - 1];
                    if(po == 0)
						{
							if(s.equals("用隐私模式打开"))
							{
								try
								{
									MainActivity.textView.setText(fileOpen.fileReader(MainActivity.filedopath + MainActivity.filedofile));
									Toast.makeText(ctx, "隐私模式成功打开文件:" + s + ",要看什么的话。。小心身后哦♪(´▽｀)", Toast.LENGTH_SHORT).show();
									fileselectAct.fileselectCtx.finish();
									finish();
								}
								catch (IOException e)
								{
									Toast.makeText(ctx, "打开文件错！！误！！", Toast.LENGTH_SHORT).show();
								}
							}
							else if(s.equals("用小说模式打开"))
							{
								fileOpen.openNovel(ctx, sharedPreferences, editor, MainActivity.filedopath, MainActivity.filedofile);
//						try{
//							new MainActivity().batterylevel();
//						}
//						catch(Exception e)
//						{
//							Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
//						}
								//MainActivity.batterylevel();
								fileselectAct.fileselectCtx.finish();
								finish();
							}
							else if(s.equals("删除"))
							{
								new AlertDialog.Builder(ctx)
										.setMessage("确认删除" + MainActivity.filedofile + "吗？")
										.setPositiveButton("确定", new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(DialogInterface dialog, int which)
											{
												if(new File(MainActivity.filedopath + MainActivity.filedofile).delete())
												{
													Toast.makeText(ctx, "删除成功！~(≥▽≤)~", Toast.LENGTH_SHORT).show();
													finish();
													menuAct.ctx.finish();
												}
												else
												{
													Toast.makeText(ctx, "删除失败？？喵喵喵？ºΔº", Toast.LENGTH_SHORT).show();
												}
											}
										})
										.setNegativeButton("取消", null).show();
							}
							else if(s.equals("属性"))
							{
								MainActivity.helpor = 4;
								Intent intent = new Intent(ctx, helpAct.class);
								intent.putExtra("string", new fileAttributes(MainActivity.filedofile, MainActivity.filedopath + MainActivity.filedofile).getFileAttributes());
								startActivity(intent);
							}
							else if(s.equals("分享..."))
							{
								shareFile(ctx, new File(MainActivity.filedopath + MainActivity.filedofile));
							}
							else
							{
								Toast.makeText(ctx, "很抱歉，该功能正在开发中，请等待版本更新！(tan90˚)", Toast.LENGTH_SHORT).show();
							}
						}
						else if(po == 1)
						{
							if(s.equals(""))
						}
					}
				}
			});
    }

    // 調用系統方法分享文件
    public static void shareFile(Context context, File file) {
        if (null != file && file.exists()) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_STREAM, file.toURI());
            share.setType(getMimeType(file.getAbsolutePath()));//此处可发送多种文件
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share, "分享文件"));
        } else {
            Toast.makeText(context, "分享文件不存在", Toast.LENGTH_SHORT);
        }
    }

    // 根据文件后缀名获得对应的MIME类型。
    private static String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }
}

class fAdapter extends BaseAdapter
{

	private String[] mData;//定义数据。
	private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。
    private ImageView image;
    private TextView name;
    private ImageView imggo;
    private ToggleButton imgswi;
    private TextView tip;
    private View layoutview;
	private int[] mImg;

	/*
	 定义构造器，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
	 */
	public fAdapter(String[] data, int[] img, LayoutInflater inflater)
	{
		mData = data;
		mInflater = inflater;
		mImg = img;
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
		if(mImg[position] == 0) image.setVisibility(View.GONE);
		else image.setImageResource(mImg[position]);

		imggo.setVisibility(View.GONE);
		imgswi.setVisibility(View.GONE);
		tip.setVisibility(View.GONE);
		return layoutview;
	}
}
