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
import java.util.List;

import android.content.*;

import com.stl.wristNotes.method.*;

import org.apache.log4j.chainsaw.Main;

import static android.R.id.hint;


public class filetodoAct extends Activity
{
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context ctx = this;
    Intent intent;
    Intent i;
    ArrayList<String> todo;
    ArrayList<Integer> img;
    ArrayList<String> hint;
    int po;
    String[] starpath;
    fAdapter adapter;

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
        title.setText(MainActivity.filedofile);

        intent = getIntent();
        po = intent.getIntExtra("po", 0);

        if(po == 3 || po == 4)
        {
            i = new Intent();
            i.putExtra("info", -1);
            setResult(0, i);
        }
        if(po == 0 || po == 3)//文件属性
        {
            todo = new ArrayList<String>(Arrays.asList("用隐私模式打开", "用小说模式打开", "收藏该文件", "新建...", "打开为...(*)", "重命名(*)", "分享...", "删除", "属性"));
            img = new ArrayList<Integer>(Arrays.asList(R.drawable.txtfile, R.drawable.novelfile, R.drawable.star, 0, 0, 0, 0, 0, 0));
            hint = new ArrayList<String>(Arrays.asList("", "", "收藏至快速访问", "在当前目录下新建", "查找其他应用打开", "", "分享文件至其他应用", "", ""));
            if(po == 3)
            {
                todo.set(2, "取消收藏");
                img.set(2, R.drawable.nostar);
                hint.set(2, "从快速访问移除该文件");
            }
        }
        else if(po == 1 || po == 4)//文件夹属性
        {
            todo = new ArrayList<String>(Arrays.asList("收藏该文件夹", "新建...", "删除整个文件夹", "属性(*)"));
            img = new ArrayList<Integer>(Arrays.asList(R.drawable.star, 0, 0, 0));
            hint = new ArrayList<String>(Arrays.asList("收藏至快速访问", "在选中目录下新建", "", ""));
            if(po == 4)
            {
                todo.set(0, "取消收藏");
                img.set(0, R.drawable.nostar);
                hint.set(0, "从快速访问移除该文件夹");
            }
        }
        else if(po == 2)//？
        {
            starpath = new String[]{"▒▒▒▒▒"};
            if(!sharedPreferences.getString("star", "").equals(""))
                starpath = sharedPreferences.getString("star", "").split("▒");

            todo = new ArrayList<String>();
            img = new ArrayList<Integer>();
            hint = new ArrayList<String>();
            todo.add("上次打开目录");
            todo.add("储存卡根目录");
            img.add(R.drawable.sdfiles);
            img.add(R.drawable.files);
            hint.add("最后一次打开文件的路径");
            hint.add("");
            title.setText("快速访问");

            if(!starpath[0].equals("▒▒▒▒▒"))
            {
                for (int i = 0; i < starpath.length; i++)
                {
                    todo.add(starpath[i].split("/")[starpath[i].split("/").length - 1]);
                    if(new File(starpath[i]).isDirectory()) img.add(R.drawable.starfor);
                    else img.add(R.drawable.star);
                    hint.add(starpath[i].subSequence(0, starpath[i].length() - starpath[i].split("/")[starpath[i].split("/").length - 1].length()).toString());
                }
            }
            else
            {
                todo.add("暂无收藏");
                img.add(0);
                hint.add("长按文件或文件夹可以添加至这里喵~");
            }
        }
        else if(po == 5)//新建
        {
            todo = new ArrayList<String>(Arrays.asList("新建文件", "新建文件夹"));
            img = new ArrayList<Integer>(Arrays.asList(R.drawable.createfile, R.drawable.createfor));
            hint = new ArrayList<String>(Arrays.asList("", ""));
            title.setText("新建...");
        }
        adapter = new fAdapter(todo, img, hint, getLayoutInflater());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> l, View v, int position, long id)
            {
                if(position != 0)
                {
                    String s = todo.get(position - 1);
                    if(po == 0 || po == 3)
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
                                                fileselectAct.fileselectCtx.finish();
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
                        else if(s.equals("收藏该文件"))
                        {
                            try
                            {
                                List<String> starlist = new ArrayList<>();
                                if(!sharedPreferences.getString("star", "").equals(""))
                                    starlist = new ArrayList(Arrays.asList(sharedPreferences.getString("star", "").split("▒")));
                                starlist.add(MainActivity.filedopath + MainActivity.filedofile);
                                editor.putString("star", MainActivity.join((starlist.toArray(new String[starlist.size()])), "▒"));
                                editor.commit();
                                Toast.makeText(ctx, "已收藏该文件！以后可以在快速访问中打开！", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(ctx, "收藏失败？？", Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(s.equals("取消收藏"))
                        {
                            i.putExtra("info", 1);
                            setResult(0, i);
                            finish();
                        }
                        else if(s.equals("新建..."))
                        {
                            Intent intent = new Intent(ctx, filetodoAct.class);
                            intent.setClass(ctx, filetodoAct.class);
                            intent.putExtra("po", 5);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(ctx, "很抱歉，该功能正在开发中，请等待版本更新！(tan90˚)", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(po == 1 || po == 4)
                    {
                        if(s.equals("收藏该文件夹"))
                        {
                            try
                            {
                                List<String> starlist = new ArrayList<>();
                                if(!sharedPreferences.getString("star", "").equals(""))
                                    starlist = new ArrayList(Arrays.asList(sharedPreferences.getString("star", "").split("▒")));
                                starlist.add(MainActivity.filedopath + MainActivity.filedofile);
                                editor.putString("star", MainActivity.join((starlist.toArray(new String[starlist.size()])), "▒"));
                                editor.commit();
                                Toast.makeText(ctx, "已收藏该文件夹！以后可以在快速访问中打开！", Toast.LENGTH_LONG).show();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(ctx, "收藏失败？？", Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(s.equals("删除整个文件夹"))
                        {
                            new AlertDialog.Builder(ctx)
                                    .setMessage("确认删除该文件夹以及子文件吗吗？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            if(file.deleteDir(new File(MainActivity.filedopath + MainActivity.filedofile)))
                                            {
                                                Toast.makeText(ctx, "删除成功！~(≥▽≤)~", Toast.LENGTH_SHORT).show();
                                                finish();
                                                fileselectAct.fileselectCtx.finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(ctx, "删除失败？？喵喵喵？ºΔº", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("取消", null).show();
                        }
                        else if(s.equals("取消收藏"))
                        {
                            i.putExtra("info", 1);
                            setResult(0, i);
                            finish();
                        }
                        else if(s.equals("新建..."))
                        {
                            Intent intent = new Intent(ctx, filetodoAct.class);
                            intent.setClass(ctx, filetodoAct.class);
                            intent.putExtra("po", 5);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(ctx, "很抱歉，该功能正在开发中，请等待版本更新！(tan90˚)", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(po == 2)
                    {
                        if(position - 1 == 0)
                        {
                            MainActivity.filewillpath = "";
                            Intent intent = new Intent(ctx, fileselectAct.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(position - 1 == 1)
                        {
                            MainActivity.filewillpath = Environment.getExternalStorageDirectory() + "/";
                            Intent intent = new Intent(ctx, fileselectAct.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(!starpath[0].equals("▒▒▒▒▒"))
                        {
                            File file = new File(starpath[position - 1 - 2]);
                            if(file.exists())
                            {
                                if(file.isDirectory())
                                {
                                    MainActivity.filewillpath = starpath[position - 1 - 2] + "/";
                                    Intent intent = new Intent(ctx, fileselectAct.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    if(file.length() < 1024 * 90)//90kb
                                    {
                                        fileOpen.openFile(ctx, editor, hint.get(position - 1), todo.get(position - 1));
                                        MainActivity.filepath = hint.get(position - 1);
                                        MainActivity.filename = todo.get(position - 1);
                                        finish();
                                    }
                                    else
                                    {
                                        fileOpen.bigFile(ctx, sharedPreferences, editor, hint.get(position - 1), todo.get(position - 1));
                                    }
                                }
                            }
                            else
                            {
                                delete(position - 1);
                            }
                        }
                    }
                    else if(po == 5)
                    {
                        if(s.equals("新建文件"))
                        {
                            MainActivity.inputtitle = "输入文件名";
                            MainActivity.inputset = "文件.txt";
                            MainActivity.inputhite = "带扩展名";
                            MainActivity.inputkeys = "";
                            Intent intent = new Intent(ctx, inputAct.class);
                            intent.putExtra("po", 1);
                            startActivityForResult(intent, 0);
                        }
                        else if(s.equals("新建文件夹"))
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
                    String s = todo.get(position - 1);
                    if(po == 2 && !starpath[0].equals("▒▒▒▒▒"))
                    {
                        if(position > 2)//排除掉前两个和标题
                        {
                            if(new File(hint.get(position - 1) + todo.get(position - 1)).exists())
                            {
                                if(!new File(hint.get(position - 1) + todo.get(position - 1)).isDirectory())
                                {
                                    MainActivity.filedopath = hint.get(position - 1);
                                    MainActivity.filedofile = todo.get(position - 1);
                                    Intent intent = new Intent(ctx, filetodoAct.class);
                                    intent.setClass(ctx, filetodoAct.class);
                                    intent.putExtra("po", 3);
                                    MainActivity.filedopo = position - 3;
                                    startActivityForResult(intent, 0);
                                }
                                else
                                {
                                    MainActivity.filedopath = hint.get(position - 1);
                                    MainActivity.filedofile = todo.get(position - 1);
                                    Intent intent = new Intent(ctx, filetodoAct.class);
                                    intent.setClass(ctx, filetodoAct.class);
                                    intent.putExtra("po", 4);
                                    MainActivity.filedopo = position - 3;
                                    startActivityForResult(intent, 0);
                                }
                            }
                            else
                            {
                                delete(position - 3);
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

    private void delete(final int po)
    {
        new AlertDialog.Builder(ctx)
                .setMessage("未找到文件或文件夹，是否删除该收藏？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        deleteStar(po);
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    private void deleteStar(int po)
    {
        ArrayList<String> tstarpath = new ArrayList(Arrays.asList(sharedPreferences.getString("star", "").split("▒")));
        tstarpath.remove(po);
        editor.putString("star", MainActivity.join(tstarpath.toArray(new String[tstarpath.size()]), "▒"));
        editor.commit();
        todo.remove(po + 2);
        img.remove(po + 2);
        hint.remove(po + 2);
        adapter.notifyDataSetChanged();
    }

    // 調用系統方法分享文件
    public static void shareFile(Context context, File file)
    {
        if(null != file && file.exists())
        {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_STREAM, file.toURI());
            share.setType(getMimeType(file.getAbsolutePath()));
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share, "分享文件"));
        }
        else
        {
            Toast.makeText(context, "分享文件不存在", Toast.LENGTH_SHORT);
        }
    }

    // 根据文件后缀名获得对应的MIME类型。
    private static String getMimeType(String filePath)
    {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "*/*";
        if(filePath != null)
        {
            try
            {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            }
            catch (IllegalStateException e)
            {
                return mime;
            }
            catch (IllegalArgumentException e)
            {
                return mime;
            }
            catch (RuntimeException e)
            {
                return mime;
            }
        }
        return mime;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if(resultCode == 0)//删除
            {
                if(data.getIntExtra("info", -1) == 1)
                {
                    deleteStar(MainActivity.filedopo);
                    Toast.makeText(ctx, "删除成功！", Toast.LENGTH_SHORT).show();
                }
            }
            else if(resultCode == 1 || resultCode == 2)//input 新建文件
            {
                int success;
                if(new File(MainActivity.filedopath + MainActivity.filedofile).isDirectory())
                {
                    success = file.create(resultCode, new File(MainActivity.filedopath + MainActivity.filedofile + "/" + data.getStringExtra("info")));
                }
                else
                {
                    success = file.create(resultCode, new File(MainActivity.filedopath + data.getStringExtra("info")));
                }

                if(success == 1)
                {
                    Toast.makeText(ctx, "创建成功！", Toast.LENGTH_SHORT).show();
                }
                else if(success == 0)
                {
                    Toast.makeText(ctx, "创建失败..", Toast.LENGTH_SHORT).show();
                }
                else if(success == 2)
                {
                    Toast.makeText(ctx, "已存在...", Toast.LENGTH_SHORT).show();
                }

                fileselectAct.filelistToAdapter.add(data.getStringExtra("info"));
                fileselectAct.adapter.notifyDataSetChanged();
                finish();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(ctx, "回调" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}

class fAdapter extends BaseAdapter
{

    private ArrayList<String> mData;//定义数据。
    private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。
    private ImageView image;
    private TextView name;
    private ImageView imggo;
    private ToggleButton imgswi;
    private TextView tip;
    private View layoutview;
    private ArrayList<Integer> mImg;
    private ArrayList<String> mHint;

    /*
     定义构造器，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
     */
    public fAdapter(ArrayList<String> data, ArrayList<Integer> img, ArrayList<String> hint, LayoutInflater inflater)
    {
        mData = data;
        mInflater = inflater;
        mImg = img;
        mHint = hint;
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
        //获得ListView中的view
        layoutview = mInflater.inflate(R.layout.menulist, null);

        //获得自定义布局中每一个控件的对象。
        image = (ImageView) layoutview.findViewById(R.id.menulistimg);
        name = (TextView) layoutview.findViewById(R.id.menulistText);
        imggo = (ImageView) layoutview.findViewById(R.id.menulistgo);
        imgswi = (ToggleButton) layoutview.findViewById(R.id.menulistswi);
        tip = (TextView) layoutview.findViewById(R.id.menulisttip);

        //将数据一一添加到自定义的布局中。
        name.setText(mData.get(position));
        if(mImg.get(position) == 0) image.setVisibility(View.GONE);
        else image.setImageResource(mImg.get(position));

        if(mHint.get(position) == "") tip.setVisibility(View.GONE);
        else tip.setText(mHint.get(position));

        imggo.setVisibility(View.GONE);
        imgswi.setVisibility(View.GONE);
        return layoutview;
    }
}
