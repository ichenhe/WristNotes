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

	Context ctx = this;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.novel);

		List<Map<String, Object>> listItems = new ArrayList<Map<String,Object>>();
		final ListView listView = (ListView) findViewById(R.id.myNovel);
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
				Map<String, Object> listItem=new HashMap<String,Object>();
				listItem.put("header", novelname.get(i));
				listItem.put("second", "看到第" + (Integer.valueOf(novelpage.get(i)).intValue() + 1) + "页，共" + (int)Math.ceil(new File(novelpath.get(i)).length() / 500 + 1) + "页\n" + novelpath.get(i));
				listItems.add(listItem);
			}

		}
        catch (JSONException e)
        {
            Toast.makeText(ctx, "小说观看记录解析错误，请尝试重启应用程序或重新安装(=_=)", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
		}
		
		SimpleAdapter simpleAdapter=new SimpleAdapter(this, listItems, R.layout.mynovelitem, new String[]{"header","second"}, new int[]{R.id.mynovelitemTextView1, R.id.mynovelitemTextView2});
		listView.addFooterView(footView, null, true);
		listView.setAdapter(simpleAdapter);

		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id)
			{
				fileOpen.openNovel(ctx,
								   sharedPreferences,
								   editor,
								   novelpath.get(position).substring(0, novelpath.get(position).length() - novelpath.get(position).split("/")[novelpath.get(position).split("/").length - 1].length() - 1) ,
								   novelpath.get(position).split("/")[novelpath.get(position).split("/").length - 1]);
				finish();
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id)
			{
				
				return true;
			}
		});
	}
}
