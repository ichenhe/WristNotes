package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.stl.wristNotes.method.*;
import java.io.*;
import java.util.*;
import android.graphics.*;
import antlr.*;


public class fileselectAct extends Activity
{
	Context ctx = this;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static Activity fileselectCtx;
    Intent intent;
    public static File fileselectwillfile;
    TextView fileselecttitle;
    public static String[] filelist;
	String[] filelist2;
	public static ArrayList<String> filelistToAdapter;
	ListView fileselectView;
	public static zAdapter adapter;

    public static View headView;
    static View filedo1;
    static View filedo2;
    static View filedo3;
    View filejt1;
    View filejt2;
    ListView filedolist;
    zAdapter filedoAdapter2;

	int tip = 1;
    public static int doSelect = 0;
    public static int sortRule = 0;
    public static boolean sortReverse = false;
    public static boolean[] selectItem;

    public static ArrayList<String> fileCopyClip;
    public static ArrayList<String> fileCopyClipName;
    public static int fileCopyClipMode;
    //0无 1复制 2剪贴

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileselect);

        fileselectCtx = this;
        sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

		fileselectView = (ListView) findViewById(R.id.fileselectList);
		LayoutInflater infla = LayoutInflater.from(this);

		View headView2 = infla.inflate(R.layout.widget_title, null);
		fileselecttitle = (TextView) headView2.findViewById(R.id.title);
		fileselectView.addHeaderView(headView2, null, true);
        
        sortRule = sharedPreferences.getInt("sortRule", 0);
        sortReverse = sharedPreferences.getBoolean("sortReverse", false);

        if(MainActivity.filewillpath.equals(""))
        {
            MainActivity.filewillpath = sharedPreferences.getString("filepath", Environment.getExternalStorageDirectory().toString() + "/");
            fileCopyClip = new ArrayList<String>();
            fileCopyClipName = new ArrayList<String>();
            fileCopyClipMode = 0;
        }
        fileselecttitle.setText(MainActivity.filewillpath);
        fileselecttitle.setClickable(true);
        fileselecttitle.setOnClickListener(new View.OnClickListener()
			{
				//@Override
				public void onClick(View p1)
				{
					try
					{
						if(!fileselecttitle.getText().equals("/"))
						{
							String[] filelist3 = MainActivity.filewillpath.split("/");
							String filelist4 = "";
                            if(filelist3 != null)
                            {
                                for(int i = 0; i < filelist3.length - 1; i++)
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
					catch(Exception e)
					{
						Toast.makeText(ctx, "你没有获取系统文件夹文件的权限╮(ˉ▽ˉ)╭", Toast.LENGTH_LONG).show();
					}
				}
			});

        //fileselectwillfile.list();
        try
        {
            fileselectwillfile = new File(MainActivity.filewillpath);
            if(fileselectwillfile.exists())
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
		catch(Exception e)
        {
            MainActivity.filewillpath = Environment.getExternalStorageDirectory().toString() + "/";
            filelist = Environment.getExternalStorageDirectory().list();
            Toast.makeText(fileselectCtx, "你没有获取系统文件夹文件的权限╮(ˉ▽ˉ)╭", Toast.LENGTH_LONG).show();
            //Toast.makeText(fileselectCtx, e.toString(), Toast.LENGTH_SHORT).show();
        }
		try
		{
       		Arrays.sort(filelist, comparator);
		}
		catch(Exception e)
		{
			MainActivity.filewillpath = Environment.getExternalStorageDirectory().toString() + "/";
			filelist = Environment.getExternalStorageDirectory().list();
            fileselecttitle.setText(MainActivity.filewillpath);
			Arrays.sort(filelist, comparator);
			Toast.makeText(ctx, "你没有获取系统文件夹文件的权限╮(ˉ▽ˉ)╭", Toast.LENGTH_LONG).show();
		}
		try
		{
            doSelect = 0;
			filelistToAdapter = new ArrayList<String>(Arrays.asList(filelist));
            selectItem = new boolean[filelistToAdapter.size()];
			adapter = new zAdapter(filelistToAdapter, getLayoutInflater(), MainActivity.filewillpath, selectItem);
			if(sharedPreferences.getString("function", "00000").split("")[5].equals("0"))//功能提醒
			{
                final View menuClickBg = findViewById(R.id.fileClickBg);
                final View menuClickBu = findViewById(R.id.fileClickBu);

                fileselectView.scrollTo(0, 140);
                ((Button)menuClickBg.findViewById(R.id.clickBg1)).getLayoutParams().height = 2;//42 + 40 - 100
                ((Button)menuClickBg.findViewById(R.id.clickBg2)).getLayoutParams().height = 90;//title高度
                ((LinearLayout)menuClickBu.findViewById(R.id.clickBu1)).setPadding(0, 79, 0, 0);

                ((TextView)menuClickBu.findViewById(R.id.clickBu2)).setText("长按文件和文件夹\n查看更多选项喵~");

                menuClickBg.setVisibility(View.VISIBLE);
                menuClickBu.setVisibility(View.VISIBLE);

                menuClickBu.findViewById(R.id.clickBu3).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View p1)
                        {
                            String[] function = sharedPreferences.getString("function", "00000").split("");
                            function[5] = "1";
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
		}
		catch(Exception e)
		{
			Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
		}


        headView = infla.inflate(R.layout.widget_file, null);
        fileselectView.addHeaderView(headView, null, true);
        tip = 2;

        filedo1 = headView.findViewById(R.id.filedo1);
        filedo2 = headView.findViewById(R.id.filedo2);
        filedo3 = headView.findViewById(R.id.filedo3);
        filejt1 = findViewById(R.id.filejt1);
        filejt2 = findViewById(R.id.filejt2);
        filedolist = (ListView) findViewById(R.id.filemenulist);

        final ArrayList<String> filedot1 = new ArrayList<String>(Arrays.asList("新建文件", "新建文件夹"));
        final ArrayList<Integer> filedoi1 = new ArrayList<Integer>(Arrays.asList(R.drawable.icon_lnewfile, R.drawable.icon_lnewfor));
        final ArrayList<String> filedot2 = new ArrayList<String>(Arrays.asList("以名称排序", "以类型排序", "以时间排序", "以大小排序", "倒序"));
        final ArrayList<Integer> filedoi2 = new ArrayList<Integer>(Arrays.asList(0, 0, 0, 0, 0));
        final zAdapter filedoAdapter1 = new zAdapter(getLayoutInflater(), filedot1, filedoi1);

        filedo1.setClickable(true);
        filedo2.setClickable(true);
        filedo3.setClickable(true);

        statusChange(ctx);
        headView.findViewById(R.id.filedo).setOnClickListener(null);
        filedo1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    if(fileCopyClipMode == 0)
                    {
                        if(doSelect == 1)//取消选择1
                        {
                            doSelect = 0;
                            filedo1.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
                            filejt1.setVisibility(View.INVISIBLE);
                            filedolist.setVisibility(View.GONE);
                        }
                        else//选择1
                        {
                            doSelect = 1;
                            fileselectView.scrollTo(0, 0);
                            filedo1.setBackground(ctx.getDrawable(R.drawable.bg_filedo_scl));
                            filedo2.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
                            filedo3.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));

                            filejt1.setVisibility(View.VISIBLE);
                            filejt2.setVisibility(View.INVISIBLE);

                            filedolist.setAdapter(filedoAdapter1);
                            filedolist.setVisibility(View.VISIBLE);

                            adapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        try
                        {
                            for(int i = 0; i < fileCopyClip.size(); i++)
                            {
                                if(!new File(fileCopyClip.get(i)).isDirectory())
                                {
                                    file.copyFile(new File(fileCopyClip.get(i)), new File(MainActivity.filewillpath + fileCopyClipName.get(i)));
                                }
                                else
                                {
                                    file.copyFolder(fileCopyClip.get(i), MainActivity.filewillpath + fileCopyClipName.get(i));
                                }
                                filelistToAdapter.add(0, fileCopyClipName.get(i));
                            }
                            for(int i = 0; i < fileCopyClip.size(); i++)
                            {
                                if(fileCopyClipMode == 2) file.deleteDir(new File(fileCopyClip.get(i)));
                            }
                            adapter.notifyDataSetChanged(); 
                            Toast.makeText(ctx, "粘贴完成了喵~", Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e)
                        {
                            Toast.makeText(ctx, "粘贴失败，请检查原文件是否存在，或稍后重试喵", Toast.LENGTH_SHORT).show();
                        }
                        finally
                        {
                            fileCopyClip = new ArrayList<String>();
                            fileCopyClipName = new ArrayList<String>();
                            fileCopyClipMode = 0;
                        }
                        statusChange(ctx);
                    }
                }
            });
        filedo2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    if(fileCopyClipMode == 0)
                    {
                        if(doSelect == 2)
                        {
                            doSelect = 0;
                            filedo2.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
                            filejt2.setVisibility(View.INVISIBLE);
                            filedolist.setVisibility(View.GONE);
                        }
                        else
                        {
                            doSelect = 2;
                            fileselectView.scrollTo(0, 0);
                            filedo1.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
                            filedo2.setBackground(ctx.getDrawable(R.drawable.bg_filedo_scl));
                            filedo3.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));

                            filejt1.setVisibility(View.INVISIBLE);
                            filejt2.setVisibility(View.VISIBLE);

                            filedoAdapter2 = new zAdapter(getLayoutInflater(), filedot2, filedoi2);
                            filedolist.setAdapter(filedoAdapter2);
                            filedolist.setVisibility(View.VISIBLE);

                            adapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        fileCopyClip = new ArrayList<String>();
                        fileCopyClipName = new ArrayList<String>();
                        fileCopyClipMode = 0;
                        statusChange(ctx);
                    }
                }
            });
        filedo3.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View p1)
                {
                    if(doSelect == 3)
                    {
                        doSelect = 0;
                        filedo3.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
                        adapter.notifyDataSetChanged();
                    }
                    else//选中3
                    {
                        doSelect = 3;
                        //fileselectView.scrollTo(0, 0);
                        filedo1.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
                        filedo2.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
                        filedo3.setBackground(ctx.getDrawable(R.drawable.bg_filedo_scl));

                        filejt1.setVisibility(View.INVISIBLE);
                        filejt2.setVisibility(View.INVISIBLE);

                        filedolist.setVisibility(View.GONE);

                        selectItem = new boolean[filelistToAdapter.size()];
                        adapter = new zAdapter(filelistToAdapter, getLayoutInflater(), MainActivity.filewillpath, selectItem);
                        //adapter.notifyDataSetChanged();
                        fileselectView.setAdapter(adapter);
                    }
                }
            });


        filedolist.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> l, View v, int position, long id)
				{
                    if(doSelect == 1)
                    {
                        if(position == 0)
                        {
                            MainActivity.inputtitle = "输入文件名";
                            MainActivity.inputset = "文件.txt";
                            MainActivity.inputhite = "带扩展名";
                            MainActivity.inputkeys = "";
                            Intent intent = new Intent(ctx, inputAct.class);
                            intent.putExtra("po", 1);
                            startActivityForResult(intent, 0);
                        }
                        else if(position == 1)
                        {
                            MainActivity.inputtitle = "输入文件夹名";
                            MainActivity.inputset = "文件夹";
                            MainActivity.inputhite = "";
                            MainActivity.inputkeys = "";
                            Intent intent = new Intent(ctx, inputAct.class);
                            intent.putExtra("po", 2);
                            startActivityForResult(intent, 0);
                        }
                    }
                    else if(doSelect == 2)
                    {
                        if(position <= 3)
                        {
                            sortRule = position;
                            doSelect = 0;
                            filedo2.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
                            filejt2.setVisibility(View.INVISIBLE);
                            filedolist.setVisibility(View.GONE);

                            filelist = fileselectwillfile.list();
                            Arrays.sort(filelist, comparator);
                            filelistToAdapter = new ArrayList<String>(Arrays.asList(filelist));
                            adapter = new zAdapter(filelistToAdapter, getLayoutInflater(), MainActivity.filewillpath, selectItem);
                            fileselectView.setAdapter(adapter);
                            
                            editor.putInt("sortRule", sortRule);
                            editor.commit();
                        }
                        else if(position == 4)//倒序
                        {
                            sortReverse = !sortReverse;
                            doSelect = 0;
                            filedo2.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
                            filejt2.setVisibility(View.INVISIBLE);
                            filedolist.setVisibility(View.GONE);

                            filelist = fileselectwillfile.list();
                            Arrays.sort(filelist, comparator);
                            filelistToAdapter = new ArrayList<String>(Arrays.asList(filelist));
                            adapter = new zAdapter(filelistToAdapter, getLayoutInflater(), MainActivity.filewillpath, selectItem);
                            fileselectView.setAdapter(adapter);
                            
                            editor.putBoolean("sortReverse", sortReverse);
                            editor.commit();
                        }
                    }
                }
            });


        fileselectView.setAdapter(adapter);

        fileselectView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> l, View v, int position, long id)
				{
					position -= tip;
					String s = filelistToAdapter.get(position);
                    if(doSelect != 3)
                    {
                        if(new File(MainActivity.filewillpath + s + "/").isDirectory())
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
                            if(new File(fileselecttitle.getText().toString() + s).length() < 1024 * 90)//90kb
                            {
                                fileOpen.openFile(fileselectCtx, editor, fileselecttitle.getText().toString(), s);
                                MainActivity.filepath = fileselecttitle.getText().toString();
                                MainActivity.filename = s;
                                finish();
                            }
                            else
                            {
                                fileOpen.bigFile(fileselectCtx, sharedPreferences, editor, fileselecttitle.getText().toString(), s);
                            }
                        }
                    }
                    else
                    {
                        selectItem[position] = !selectItem[position];
                        ((CheckBox) v.findViewById(R.id.menulistcb)).setChecked(selectItem[position]);
                        //adapter.notifyDataSetChanged();
                    }
				}
			});

        fileselectView.setOnItemLongClickListener(new OnItemLongClickListener()
			{
				@Override
				public boolean onItemLongClick(AdapterView<?> l, View v, int position, long id)
				{
					position -= tip;
					String s = filelistToAdapter.get(position);
                    if(doSelect != 3)
                    {
                        if(!new File(MainActivity.filewillpath + s + "/").isDirectory())
                        {
                            try
                            {
                                MainActivity.filedofile = s;
                                MainActivity.filedopath = MainActivity.filewillpath;
                                Intent intent = new Intent(fileselectCtx, filetodoAct.class);
                                intent.putExtra("po", 0);
                                intent.putExtra("selectPosition", position);
                                startActivity(intent);
                            }
                            catch(Exception e)
                            {
                                Toast.makeText(fileselectCtx, "错误错误错误了！-_-#", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            MainActivity.filedofile = s;
                            MainActivity.filedopath = MainActivity.filewillpath;
                            Intent intent = new Intent(fileselectCtx, filetodoAct.class);
                            intent.putExtra("po", 1);
                            startActivity(intent);
                        }
                    }
                    else//多选
                    {
                        if(selectItem[position])
                        {
                            MainActivity.filedopath = MainActivity.filewillpath;
                            Intent helpint = new Intent(fileselectCtx, filetodoAct.class);
                            helpint.putExtra("po", 9);
                            startActivity(helpint);
                            return true;
                        }
                        else return false;
                    }
					return true;
				}
			});
    }

    public static void statusChange(Context ctx)
    {
        if(fileCopyClipMode == 1 || fileCopyClipMode == 2)//复制&剪贴
        {
            ((ImageView) headView.findViewById(R.id.filedoimg1)).setImageResource(R.drawable.file_stage_paste);
            ((TextView) headView.findViewById(R.id.filedotext1)).setVisibility(View.VISIBLE);
            ((ImageView) headView.findViewById(R.id.filedoimg2)).setImageResource(R.drawable.file_stage_cancel);
            ((TextView) headView.findViewById(R.id.filedotext2)).setVisibility(View.VISIBLE);
            filedo3.setVisibility(View.GONE);
        }
        else
        {
            filedo1.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
            filedo2.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
            filedo3.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));

            ((ImageView) headView.findViewById(R.id.filedoimg1)).setImageResource(R.drawable.file_stage_new);
            ((ImageView) headView.findViewById(R.id.filedoimg2)).setImageResource(R.drawable.file_stage_sort);
            ((ImageView) headView.findViewById(R.id.filedoimg3)).setImageResource(R.drawable.file_stage_more);
            ((TextView) headView.findViewById(R.id.filedotext1)).setVisibility(View.GONE);
            ((TextView) headView.findViewById(R.id.filedotext2)).setVisibility(View.GONE);
            filedo3.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if(resultCode == 1 || resultCode == 2)//input 新建文件
            {
                if(!data.getStringExtra("info").equals(""))
                {
                    int success;
                    success = file.create(resultCode, new File(MainActivity.filewillpath + data.getStringExtra("info")));

                    if(success == 1)
                    {
                        Toast.makeText(ctx, "创建成功！", Toast.LENGTH_SHORT).show();
                        filelistToAdapter.add(data.getStringExtra("info"));
                        adapter.notifyDataSetChanged();
                    }
                    else if(success == 0)
                    {
                        Toast.makeText(ctx, "创建失败..", Toast.LENGTH_SHORT).show();
                    }
                    else if(success == 2)
                    {
                        Toast.makeText(ctx, "已存在...", Toast.LENGTH_SHORT).show();
                    }

                    doSelect = 0;
                    filedo1.setBackground(ctx.getDrawable(R.drawable.bg_filedo_noscl));
                    filejt1.setVisibility(View.INVISIBLE);
                    filedolist.setVisibility(View.GONE);
                }
            }
        }
        catch(Exception e)
        {
            Toast.makeText(ctx, "回调" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static Comparator<String> comparator = new Comparator<String>()
    {
        @Override
        public int compare(String o1, String o2)
        {
            int Return = 0;
            String q1 = MainActivity.filewillpath + o1;
            String q2 = MainActivity.filewillpath + o2;
            if(sortRule == 0)//名字
            {
                Return = o1.compareToIgnoreCase(o2);
            }
            else if(sortRule == 1)//类型
            {
                String exten1 = "";
                String exten2 = "";
                if(o1.contains(".")) exten1 = o1.split("[.]")[o1.split("[.]").length - 1];
                if(o2.contains(".")) exten1 = o2.split("[.]")[o2.split("[.]").length - 1];

                if(!exten1.equals(exten2)) Return = exten1.compareToIgnoreCase(exten2) > 0 ? exten1.compareToIgnoreCase(exten2) + 1 : exten1.compareToIgnoreCase(exten2) - 1;
                else Return = o1.compareToIgnoreCase(o2);
            }
            else if(sortRule == 2)//时间
            {
                if(new File(q1).lastModified() > new File(q2).lastModified()) Return = 1;
                else if(new File(q1).lastModified() < new File(q2).lastModified()) Return = -1;
                else Return = 0;
            }
            else if(sortRule == 3)//大小
            {
                if(new File(q1).length() > new File(q2).length()) Return = -1;
                else if(new File(q1).length() < new File(q2).length()) Return = 1;
                else Return = 0;
            }

            if(sortReverse) Return *= -1;
            return Return;
        }
    };
}

class zAdapter extends BaseAdapter
{

	private ArrayList<String> mData;//定义数据。
	private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。
	private String path;
    private ArrayList<Integer> mImg;

    private ImageView image;
    private TextView name;
    private ImageView imggo;
    private ToggleButton imgswi;
    private TextView tip;
    private View layoutview;

    private CheckBox checkbox;
    private boolean[] select;

    private int lay;

	public zAdapter(ArrayList<String> data, LayoutInflater inflater, String path, boolean[] select)
	{
		mData = data;
		mInflater = inflater;
		this.path = path;
        this.lay = 1;
        this.select = select;
	}

    public zAdapter(LayoutInflater inflater, ArrayList<String> data, ArrayList<Integer> img)
    {
        mInflater = inflater;
        mData = data;
        mImg = img;
        this.lay = 2;
    }

	@Override
	public int getCount()
	{
		return mData.size();
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
        if(lay == 1)
        {
            //获得ListView中的view
            layoutview = mInflater.inflate(R.layout.menulist, null);

            //获得自定义布局中每一个控件的对象。
            image = (ImageView) layoutview.findViewById(R.id.menulistimg);
            name = (TextView) layoutview.findViewById(R.id.menulistText);
            imggo = (ImageView) layoutview.findViewById(R.id.menulistgo);
            imgswi = (ToggleButton) layoutview.findViewById(R.id.menulistswi);
            tip = (TextView) layoutview.findViewById(R.id.menulisttip);

            imggo.setVisibility(View.GONE);
            imgswi.setVisibility(View.GONE);

            //将数据一一添加到自定义的布局中。
            name.setText(mData.get(position));

            //获取文件拓展名
            if(new File(path + mData.get(position)).isDirectory())
            {
                image.setImageResource(R.drawable.folder);
            }
            else
            {
                if(mData.get(position).contains("."))
                {
                    String exten = mData.get(position).split("[.]")[mData.get(position).split("[.]").length - 1];
                    //name.setText(name.getText().toString() + "&" + exten);
                    //String exten = "jpg";
                    if(exten.equals("jpg") || exten.equals("png") || exten.equals("jpge") || exten.equals("gif") || exten.equals("bmp") || exten.equals("tif") || exten.equals("pic"))
                    {
                        image.setImageResource(R.drawable.imgfile);
                    }
                    else if(exten.equals("apk"))
                    {
                        image.setImageResource(R.drawable.apkfile);
                    }
                    else if(exten.equals("wav") || exten.equals("aif") || exten.equals("au") || exten.equals("mp3") || exten.equals("wma") || exten.equals("amr") || exten.equals("flac") || exten.equals("aac"))
                    {
                        image.setImageResource(R.drawable.audio);
                    }
                    else if(exten.equals("doc") || exten.equals("xls") || exten.equals("xlsx") || exten.equals("ppt") || exten.equals("docx") || exten.equals("pptx") || exten.equals("pdf"))
                    {
                        image.setImageResource(R.drawable.doc);
                    }
                    else if(exten.equals("java") || exten.equals("py") || exten.equals("xml") || exten.equals("html") ||
						exten.equals("js") || exten.equals("css") || exten.equals("bat") || exten.equals("com") || exten.equals("class"))
                    {
                        image.setImageResource(R.drawable.profile);
                    }
                    else if(exten.equals("txt"))
                    {
                        if(new File(path + mData.get(position)).length() < 1024 * 90)
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
            if(MainActivity.filepath.equals(path) && MainActivity.filename.equals(mData.get(position)))
            {
                if(MainActivity.mode == 0)
                {
                    tip.setText("已作为文本打开");
                }
                else
                {
                    tip.setText("已作为小说打开");
                }
            }
            else
            {
                tip.setVisibility(View.GONE);
            }

            if(fileselectAct.doSelect == 3)
            {
                layoutview.findViewById(R.id.menulistcb).setVisibility(View.VISIBLE);
                if(select[position]) ((CheckBox) layoutview.findViewById(R.id.menulistcb)).setChecked(true);
            }

            return layoutview;
        }
        else if(lay == 2)
        {
            layoutview = mInflater.inflate(R.layout.item_filedo, null);

            image = (ImageView) layoutview.findViewById(R.id.filedoimg);
            name = (TextView) layoutview.findViewById(R.id.filedotext);
            checkbox = (CheckBox) layoutview.findViewById(R.id.filedocb);

            name.setText(mData.get(position));

            if(mImg.get(position) == 0) image.setVisibility(View.GONE);
            else image.setImageResource(mImg.get(position));

            if(mImg.get(position) == 0 && position == fileselectAct.sortRule) name.setTextColor(Color.rgb(68, 196, 238));

            if(mData.get(position).equals("倒序"))
            {
                layoutview.findViewById(R.id.filedocb).setVisibility(View.VISIBLE);
                ((CheckBox) layoutview.findViewById(R.id.filedocb)).setChecked(fileselectAct.sortReverse);
            }
            else if(mData.get(position).equals("只用于此文件夹"))
            {
                layoutview.findViewById(R.id.filedocb).setVisibility(View.VISIBLE);
            }
            return layoutview;
        }
        return null;
	}
}
