package com.stl.wristNotes;

import android.app.*;
import android.os.*;
import android.widget.*;
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
	Map<String, Object> listItem;
	List<Map<String, Object>> listItems;
	SimpleAdapter simpleAdapter;
	ListView listView;

	int choose = -1;
    int isHeadview = 0;
	Context ctx = this;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novel);

		listItems = new ArrayList<Map<String,Object>>();
		listView = (ListView) findViewById(R.id.myNovel);
		LayoutInflater infla2 = LayoutInflater.from(this);
		View footView = infla2.inflate(R.layout.mynoveltext, null);
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
		try
		{
			sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
			novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
			novelname = new ArrayList(Arrays.asList(novellist.getString("name").split("▒")));
			novelpath = new ArrayList(Arrays.asList(novellist.getString("path").split("▒")));
			novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));

			for (int i = 0; i < novelname.size(); i++)
			{
				listItem=new HashMap<String,Object>();
				listItem.put("header", novelname.get(i));
				listItem.put("second", "看到第" + (Integer.valueOf(novelpage.get(i)).intValue() + 1) + "页\n大概一共有" + (int)Math.ceil(new File(novelpath.get(i)).length() / 1250) + "页");
				listItems.add(listItem);
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
		
		simpleAdapter = new SimpleAdapter(this, listItems, R.layout.mynovelitem, new String[]{"header","second"}, new int[]{R.id.mynovelitemTextView1, R.id.mynovelitemTextView2});
		if(sharedPreferences.getString("function", "0000").split("")[4].equals("0"))//功能提醒
		{
			LayoutInflater infla = LayoutInflater.from(this);
			final View headView = infla.inflate(R.layout.widget_newfunction, null);
			listView.addHeaderView(headView, null, true);
            isHeadview = 1;

			((TextView)findViewById(R.id.functiontext)).setText("长按小说查看更多选项喵~");
			LinearLayout button = (LinearLayout) headView.findViewById(R.id.functionbutton);
    	    button.setClickable(true);
			button.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View p1)
					{
						listView.removeHeaderView(headView);
						isHeadview = 0;
						String[] function = sharedPreferences.getString("function", "0000").split("");
						function[4] = "1";
						editor.putString("function", MainActivity.join(function, ""));
						editor.commit();
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
				if(new File(novelpath.get(position - isHeadview)).exists())
				{
                    if(isHeadview == 1)
                    {
                        fileOpen.openNovel(ctx, sharedPreferences, editor,
                                novelpath.get(position - 1).substring(0, novelpath.get(position - 1).length() - novelpath.get(position - 1).split("/")[novelpath.get(position - 1).split("/").length - 1].length()),
                                novelpath.get(position - 1).split("/")[novelpath.get(position - 1).split("/").length - 1]);
                    }
                    else
                    {
                        fileOpen.openNovel(ctx, sharedPreferences, editor,
                                novelpath.get(position).substring(0, novelpath.get(position).length() - novelpath.get(position).split("/")[novelpath.get(position).split("/").length - 1].length()),
                                novelpath.get(position).split("/")[novelpath.get(position).split("/").length - 1]);
                    }
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
								deleteNovel(position);
							}
						})
						.setNegativeButton("取消", null).show();
				}
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id)
			{
                MainActivity.cho = 8;
                Intent intent = new Intent(ctx, menuAct.class);
				//intent.putExtra("position", position);
				startActivityForResult(intent, 0);
				if(isHeadview == 1)
				{
					choose = position - 1;
				}
				else{
					choose = position;
				}
                //startActivity(intent);
				return true;
			}
		});
	}

	public void deleteNovel(int j)
	{
		try
		{
			novellist = new JSONObject(sharedPreferences.getString("novelList", "{\"name\" : \"\", \"path\" : \"\", \"page\" : \"\"}"));
			novelname = new ArrayList(Arrays.asList(novellist.getString("name").split("▒")));
			novelpath = new ArrayList(Arrays.asList(novellist.getString("path").split("▒")));
			novelpage = new ArrayList(Arrays.asList(novellist.getString("page").split("▒")));
			novelname.remove(j);
			novelpath.remove(j);
			novelpage.remove(j);

			novellist.put("name", MainActivity.join(novelname.toArray(new String[novelname.size()]), "▒"));
			novellist.put("path", MainActivity.join(novelpath.toArray(new String[novelpath.size()]), "▒"));
			novellist.put("page", MainActivity.join(novelpage.toArray(new String[novelpage.size()]), "▒"));
			editor.putString("novelList", novellist.toString());
			editor.commit();

			for (int i = 0; i < novelname.size(); i++)
			{
				listItem = new HashMap<String,Object>();
				listItems = new ArrayList<Map<String,Object>>();
				listItem.put("header", novelname.get(i));
				listItem.put("second", "看到第" + (Integer.valueOf(novelpage.get(i)).intValue() + 1) + "页\n大概一共有" + (int)Math.ceil(new File(novelpath.get(i)).length() / 1250) + "页");
				listItems.add(listItem);
			}
			//simpleAdapter.notifyDataSetChanged();
			//simpleAdapter = new SimpleAdapter(this, listItems, R.layout.mynovelitem, new String[]{"header","second"}, new int[]{R.id.mynovelitemTextView1, R.id.mynovelitemTextView2});
			//listView.setAdapter(simpleAdapter);
			finish();
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
	}
}
