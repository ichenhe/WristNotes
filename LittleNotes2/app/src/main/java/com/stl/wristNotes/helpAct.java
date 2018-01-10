package com.stl.wristNotes;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.text.*;


public class helpAct extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		
		TextView helptitel = (TextView) findViewById(R.id.helpTextView2);
		TextView helptext = (TextView) findViewById(R.id.helpTextView1);
		if(MainActivity.helpor == 2)
		{
			helptitel.setText("展望未来");
			helptext.setText("计划在之后的版本加入： \n \n1.对不用这个软件作弊的人加入情景模式，可定制背景颜色，字体大小和颜色等，方便看小说或看其他的文件 \n \n2.加入小说模式，将更好的兼容小说等长文本，支持记录观看进度，分页等等 \n \n3.对用这个作弊的同学，还要加入翻腕翻页，翻腕隐藏文字，吹气翻页（测试中）等功能，总之要能在另一只手不碰到手表的情况下完成作弊过程，因为另一只手翻页什么的容易被怀疑。 \n \n4.作弊模式下检测到心跳突然过快就隐藏文字或锁定应用（滑稽） \n \n5.（重要）好吧我承认这个界面丑爆了，下一版的界面会用TicWatch官方提供的TicDesign重新设计界面 \n \n6.重新制作密码功能，密码功能是为了能在被怀疑作弊后老师进入这个应用后不看到小抄，而不是看到要输入密码后逼你输入密码然后看到小抄。 \n \n7.利用起手表显示文字两边空出的空间，可以自定义快捷按钮。 \n \n8.完善文件选择器和操作文件菜单（这个应用其实是个文件浏览器（滑稽））");
			MainActivity.helpor = 1;
		}
		else if(MainActivity.helpor == 3)
		{
			helptitel.setText("更新日志");
			helptext.setText("v1.40\n◆优化小说翻页体验，支持智能滚动\n◆增加蓝牙传输文件的教程\n◆增加新功能的提示\n◆支持了小说页面的跳转\n◆优化字体亮度，有了更大选择的区间\n◆修复若干BUG\n\nv1.30\n◆小！说！模！式！，分页，进度保存，我看过的小说等等功能应有尽有\n◆第一次下载应用会有不一样的启动界面（如果你是更新来的，你可以卸载应用再重装试试(滑稽)）\n◆其他功能对小说模式的优化\n◆打开文件时若文件过大，提示是否用小说模式打开\n◆支持删除文件了！请长按文件后选择“删除”\n◆大概统一了一下提示语的语气，变成了一个腹黑可爱会卖萌的妹子\n\nv1.20\n◆增加FTP功能，可以用电脑或手机向手表传文件\n◆可以用本应用直接从其它文件管理器中打开文件，或直接打开蓝牙传来的文件\n◆可以用挠挠控制文字隐藏显示等（限Ticwatch）\n◆优化应用界面\n◆修复一大堆BUG\n\nv1.14\n◆增加更新日志和更新欢迎界面\n◆优化应用逻辑\n◆修复若干BUG");
			MainActivity.helpor = 1;
		}
		else if(MainActivity.helpor == 4)
		{
			helptitel.setText("文件属性");
			helptext.setText(Html.fromHtml(getIntent().getStringExtra("string")));
			MainActivity.helpor = 1;
		}
		else if(MainActivity.helpor == 5)
		{
			helptitel.setText("蓝牙教程");
			helptext.setText(Html.fromHtml("<b>教程作者：Aries小痕，欢迎关注他的B站账号~</b><br/><br/><b>一、准备工作</b><br/>1. 首先确认手机和手表已正常配对，手机设置的蓝牙设置里已显示你的手表。<br/>2. 打开tic助手，确认已和手表连接。<br/><br/><b>二、开始蓝牙传输</b><br/>1. 打开文件浏览器，找到准备好的txt文件，长按并在菜单中选择共享（分享）选项。<br/>2. 分享中选择蓝牙选项<br/>3. 选择你的手表<br/>4. 开始传输，同时手表会产生提示，在手表中点击接收<br/>5. 等待文件传输成功后，用小纸条打开即可。文件位置在/Bluetooth文件夹"));
			MainActivity.helpor = 1;
		}
	}
}
