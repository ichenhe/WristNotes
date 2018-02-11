package com.stl.wristNotes;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.view.*;

import java.io.*;

import android.content.*;
import com.stl.wristNotes.method.*;


public class filetodoAct extends Activity
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context ctx = this;
	String[] todo;
	int[] img;

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

        todo = new String[]{ "用隐私模式打开", "用小说模式打开", "打开为...(*)", "重命名(*)", "分享(*)", "删除", "属性" };
		img = new int[]{ R.drawable.txtfile, R.drawable.novelfile, 0, 0, 0, 0, 0 };
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
						if(s.equals("用隐私模式打开"))
						{
							try
							{
								MainActivity.textView.setText(fileOpen.fileReader(MainActivity.filedopath + MainActivity.filedofile));
								Toast.makeText(ctx, "隐私模式成功打开文件:" + s + ",要看什么的话。。小心身后哦♪(´▽｀)", Toast.LENGTH_SHORT).show();
								fileselectAct.fileselectCtx.finish();
								finish();
							}
							catch(IOException e)
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
						else
						{
							Toast.makeText(ctx, "很抱歉，该功能正在开发中，请等待版本更新！(tan90˚)", Toast.LENGTH_SHORT).show();
						}
					}
				}
			});
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
