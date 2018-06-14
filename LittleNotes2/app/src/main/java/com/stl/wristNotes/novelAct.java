package com.stl.wristNotes;

import android.app.*;
import android.graphics.Color;
import android.os.*;
import android.widget.*;

import java.lang.reflect.Array;
import java.util.*;
import org.json.*;
import android.content.*;
import java.io.*;
import android.view.*;
import android.view.View.*;
import android.widget.AdapterView.*;

import com.stl.wristNotes.method.*;

public class novelAct extends Activity
{
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	JSONObject novellist;
	ArrayList<String> novelname;
	ArrayList<String> novelpath;
	ArrayList<String> novelpage;
	Button novelbutton;
	ArrayList<String> nameList;
	ArrayList<String> pageList;
	nAdapter simpleAdapter;
	ListView listView;

	int choose = -1;
    int isHeadview = 1;
	Context ctx = this;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novel);

		nameList = new ArrayList<String>();
		pageList = new ArrayList<String>();
		listView = (ListView) findViewById(R.id.myNovel);
		LayoutInflater infla = LayoutInflater.from(this);
		View footView = infla.inflate(R.layout.mynoveltext, null);
		novelbutton = (Button) footView.findViewById(R.id.mynoveltextButton);
		novelbutton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View p1)
				{
					new AlertDialog.Builder(ctx)
						.setMessage("要清除所有小说历史记录吗")
						.setPositiveButton("确定", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								if (MainActivity.mode == 0)
								{
									editor.putString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}");
									editor.commit();
									Toast.makeText(ctx, "记录已清除！", Toast.LENGTH_SHORT).show();
									finish();
								}
								else
								{
									Toast.makeText(ctx, "不能在小说模式下清除小说观看纪录！请先打开其它文档再试", Toast.LENGTH_LONG).show();
								}
							}
						})
						.setNegativeButton("取消", null).show();
				}
			});
		
		View headView2 = infla.inflate(R.layout.widget_title, null);
		((TextView) headView2.findViewById(R.id.title)).setText("我的小说");
		listView.addHeaderView(headView2, null, true);
		
		try
		{
			sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
			novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
			novelname = new ArrayList<String>(Arrays.asList(novellist.getString("name").split("▒")));
			novelpath = new ArrayList<String>(Arrays.asList(novellist.getString("path").split("▒")));
			novelpage = new ArrayList<String>(Arrays.asList(novellist.getString("page").split("▒")));

			for (int i = 0; i < novelname.size(); i++)
			{
				nameList.add(novelname.get(i));
				pageList.add("看到第" + (Integer.valueOf(novelpage.get(i)).intValue() + 1) + "页\n大概一共有" + (int)Math.ceil(new File(novelpath.get(i)).length() / 1250) + "页");
			}
		}
        catch (JSONException e)
        {
            Toast.makeText(ctx, "小说观看记录解析错误，请尝试重启应用程序或重新安装(=_=)", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(ctx, "未知错误喵。。", Toast.LENGTH_LONG).show();
		}
		
		simpleAdapter = new nAdapter(getLayoutInflater(), nameList, pageList, novelpath);
		if(sharedPreferences.getString("function", "00000").split("")[4].equals("0"))//功能提醒
		{
            final View menuClickBg = findViewById(R.id.novelClickBg);
            final View menuClickBu = findViewById(R.id.novelClickBu);

            ((Button)menuClickBg.findViewById(R.id.clickBg1)).getLayoutParams().height = 79;
            ((Button)menuClickBg.findViewById(R.id.clickBg2)).getLayoutParams().height = 100;
            ((LinearLayout)menuClickBu.findViewById(R.id.clickBu1)).setPadding(0, 79, 0, 0);

            ((TextView)menuClickBu.findViewById(R.id.clickBu2)).setText("长按小说查看更多选项喵~");

            menuClickBg.setVisibility(View.VISIBLE);
            menuClickBu.setVisibility(View.VISIBLE);

            menuClickBu.findViewById(R.id.clickBu3).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View p1)
                    {
                        String[] function = sharedPreferences.getString("function", "00000").split("");
                        function[4] = "1";
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
		listView.addFooterView(footView, null, true);
		listView.setAdapter(simpleAdapter);

		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> l, View v, final int position, long id)
			{
				if(position != 0)
				{
					if(new File(novelpath.get(position - isHeadview)).exists())
					{
						fileOpen.openNovel(ctx, sharedPreferences, editor,
										   novelpath.get(position - isHeadview).substring(0, novelpath.get(position - isHeadview).length() - novelpath.get(position - isHeadview).split("/")[novelpath.get(position - isHeadview).split("/").length - 1].length()),
										   novelpath.get(position - isHeadview).split("/")[novelpath.get(position - isHeadview).split("/").length - 1]);
						menuAct.ctx.finish();
						finish();
					}
					else
					{
						new AlertDialog.Builder(ctx)
							.setMessage("该小说原文件已被删除！是否删除该小说记录？")
							.setPositiveButton("删除", new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									if(MainActivity.p - 1 > choose)
									{
										MainActivity.p--;
										editor.putInt("p", MainActivity.p);
										editor.commit();
									}
									deleteNovel(position - isHeadview);
									choose = -1;
								}
							})
							.setNegativeButton("取消", null).show();
					}
				}
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id)
			{
				if(position != 0)
				{
					MainActivity.cho = 8;
					Intent intent = new Intent(ctx, menuAct.class);
					intent.putExtra("path", novelpath.get(position - 1));
					startActivityForResult(intent, 0);
					choose = position - isHeadview;
					//startActivity(intent);
                    return true;
				}
				return false;
			}
		});
	}

	public void deleteNovel(int j)
	{
		try
		{
			//novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
			//novelname = new ArrayList<String>(Arrays.asList(novellist.getString("name").split("▒")));
			//novelpath = new ArrayList<String>(Arrays.asList(novellist.getString("path").split("▒")));
			//novelpage = new ArrayList<String>(Arrays.asList(novellist.getString("page").split("▒")));
			novelname.remove(j);
			novelpath.remove(j);
			novelpage.remove(j);

			novellist.put("name", MainActivity.join(novelname.toArray(new String[novelname.size()]), "▒"));
			novellist.put("path", MainActivity.join(novelpath.toArray(new String[novelpath.size()]), "▒"));
			novellist.put("page", MainActivity.join(novelpage.toArray(new String[novelpage.size()]), "▒"));
			editor.putString("novelList", novellist.toString());
			editor.commit();

			nameList.remove(j);
			pageList.remove(j);
			simpleAdapter.notifyDataSetChanged();
			//simpleAdapter = new SimpleAdapter(this, listItems, R.layout.mynovelitem, new String[]{"header","second"}, new int[]{R.id.mynovelitemTextView1, R.id.mynovelitemTextView2});
			//listView.setAdapter(simpleAdapter);
			Toast.makeText(ctx, "已删除该记录！", Toast.LENGTH_LONG).show();
		}
		catch (JSONException e)
		{
			Toast.makeText(ctx, "小说观看记录解析错误，请尝试重启应用程序或重新安装(=_=)", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(ctx, "未知错误喵。。", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(data.getIntExtra("info", -1) == 1 && choose != -1)
		{
			if(choose == MainActivity.p - 1)
			{
				Toast.makeText(ctx, "这个文件正在被打开呢！", Toast.LENGTH_SHORT).show();
			}
			else
			{
				new AlertDialog.Builder(ctx)
					.setMessage("要清除该条小说历史记录吗（原文件不会被删除）")
					.setPositiveButton("确定", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							if(MainActivity.p - 1 > choose)
							{
								MainActivity.p--;
								editor.putInt("p", MainActivity.p);
								editor.commit();
							}
							deleteNovel(choose);
							choose = -1;
						}
					})
					.setNegativeButton("取消", null).show();
			}
		}
		else if(data.getIntExtra("info", -1) == 2 && choose != -1)
		{
			MainActivity.helpor = 4;
			Intent intent = new Intent(ctx, helpAct.class);
			intent.putExtra("string", new fileAttributes(novelname.get(choose), novelpath.get(choose)).getFileAttributes());
			startActivity(intent);
		}
		else if(data.getIntExtra("info", -1) == 3 && choose != -1)
		{
			ArrayList<String> novelComplete = new ArrayList<String>(Arrays.asList(sharedPreferences.getString("novelComplete", "").split("▒")));
			if(!novelComplete.contains(novelpath.get(choose)))
			{
				novelComplete.add(novelpath.get(choose));
				editor.putString("novelComplete", MainActivity.join(novelComplete.toArray(new String[novelComplete.size()]), "▒"));
				editor.commit();
			}
			Toast.makeText(ctx, "你已经看完这本小说啦！在“我的小说”里已经贴上了记号", Toast.LENGTH_SHORT).show();
			simpleAdapter.notifyDataSetChanged();
		}
		else if(data.getIntExtra("info", -1) == 4 && choose != -1)
		{
			ArrayList<String> novelComplete = new ArrayList<String>(Arrays.asList(sharedPreferences.getString("novelComplete", "").split("▒")));
			if(novelComplete.contains(novelpath.get(choose)))
			{
                novelComplete.remove(novelpath.get(choose));
				editor.putString("novelComplete", MainActivity.join(novelComplete.toArray(new String[novelComplete.size()]), "▒"));
				editor.commit();
			}
			Toast.makeText(ctx, "已去掉标记", Toast.LENGTH_SHORT).show();
			simpleAdapter.notifyDataSetChanged();
		}
	}
}

class nAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private View layoutview;

	private ArrayList<String> name;
	private ArrayList<String> page;
	private ArrayList<String> path;

	private TextView nameTextview;
	private TextView pageTextview;
	private ImageView completeImg;

	public nAdapter(LayoutInflater inflater, ArrayList<String> nameList, ArrayList<String> pageList, ArrayList<String> pathList)
	{
		mInflater = inflater;
		name = nameList;
		page = pageList;
		path = pathList;
	}

	@Override
	public int getCount()
	{
		return name.size();
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
		layoutview = mInflater.inflate(R.layout.mynovelitem, null);

		nameTextview = (TextView) layoutview.findViewById(R.id.mynovelitemTextView1);
		pageTextview = (TextView) layoutview.findViewById(R.id.mynovelitemTextView2);

		nameTextview.setText(name.get(position));
		pageTextview.setText(page.get(position));
		ArrayList<String> novelComplete = new ArrayList<String>(Arrays.asList(MainActivity.sharedPreferences.getString("novelComplete", "").split("▒")));
		if(novelComplete.contains(path.get(position)))
		{
			completeImg = (ImageView) layoutview.findViewById(R.id.novelComplete);
			completeImg.setVisibility(View.VISIBLE);
		}

		return layoutview;
	}
}
