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
        //Toast.makeText(this, MainActivity.helpor + "", Toast.LENGTH_SHORT).show();
		if(MainActivity.helpor == 3)
		{
			helptitel.setText("更新日志");
			helptext.setText("v1.50(82)\n◆文本可以自动识别编码，文本和小说再也不会乱码了！\n◆重写了应用界面！包括菜单图标，选项开关，文件图标等\n◆增加快速访问栏，你可以收藏文件或文件夹到这里来快速访问\n◆重新写了帮助\n◆可以在文件菜单里分享文件，查找其它应用打开\n◆改了个字体。。\n◆数不清的优化，包括小说模式翻页键的亮度\n◆修复了数不清的BUG\n\nv1.40\n◆优化小说翻页体验，支持智能滚动\n◆增加蓝牙传输文件的教程\n◆增加新功能的提示\n◆支持了小说页面的跳转\n◆优化字体亮度，有了更大选择的区间\n◆修复若干BUG\n\nv1.30\n◆小！说！模！式！，分页，进度保存，我看过的小说等等功能应有尽有\n◆第一次下载应用会有不一样的启动界面（如果你是更新来的，你可以卸载应用再重装试试(滑稽)）\n◆其他功能对小说模式的优化\n◆打开文件时若文件过大，提示是否用小说模式打开\n◆支持删除文件了！请长按文件后选择“删除”\n◆大概统一了一下提示语的语气，变成了一个腹黑可爱会卖萌的妹子\n\nv1.20\n◆增加FTP功能，可以用电脑或手机向手表传文件\n◆可以用本应用直接从其它文件管理器中打开文件，或直接打开蓝牙传来的文件\n◆可以用挠挠控制文字隐藏显示等（限Ticwatch）\n◆优化应用界面\n◆修复一大堆BUG\n\nv1.14\n◆增加更新日志和更新欢迎界面\n◆优化应用逻辑\n◆修复若干BUG");
		}
		else if(MainActivity.helpor == 4)
		{
			helptitel.setText("文件属性");
			helptext.setText(Html.fromHtml(getIntent().getStringExtra("string")));
		}
		else if(MainActivity.helpor == 5)
		{
			helptitel.setText("蓝牙教程");
			helptext.setText(Html.fromHtml("<b>教程作者：Aries小痕，欢迎关注他的B站账号~<br/><br/>注：只有TicWatch可以使用蓝牙传输文件，Android Wear设备请使用FTP等</b><br/><br/><b>一、准备工作</b><br/>1. 首先确认手机和手表已正常配对，手机设置的蓝牙设置里已显示你的手表。<br/>2. 打开tic助手，确认已和手表连接。<br/><br/><b>二、开始蓝牙传输</b><br/>1. 打开文件浏览器，找到准备好的txt文件，长按并在菜单中选择共享（分享）选项。<br/>2. 分享中选择蓝牙选项<br/>3. 选择你的手表<br/>4. 开始传输，同时手表会产生提示，在手表中点击接收<br/>5. 等待文件传输成功后，用小纸条打开即可。文件位置在/Bluetooth文件夹"));
		}
        else if(MainActivity.helpor == 6)
        {
            helptitel.setText("帮助");
            helptext.setText(getIntent().getStringExtra("string"));
        }
        else if(MainActivity.helpor == 7)
        {
            helptitel.setText("腕上会员购");
            helptext.setText(Html.fromHtml("<b>腕上会员购<br/>盛大开业啦！</b><br/><br/>App三个入口，不怕你看不见！<br/>超高的价格，不怕钱没地花！<br/>60块钱的马可杯了解一下~    90块钱的泡面碗了解一下~    100块钱的挂历了解一下~    <br/><br/>什么？你说贵？每件商品上都印有<b>我们的logo和st娘腕上娘</b>哦，<b>死宅</b>们都要抢着买哦<br/>什么？你说初心？你说吃相？拜托，我们可是<b>上！市！公！司！</b>无论我们多么过分，都会有粉丝<b>原谅</b>我们对吧，对吧对吧对吧对吧对吧对吧    与其说这些还不如赶快来为情怀买买买！<br/>什么？你还是不愿意买？没关系！下次我们把按钮放主界面，有半个屏幕那么大哦！"));
        }
	}
}
