package com.stl.wristNotes;

import android.app.*;
import android.os.*;
import android.widget.*;


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
			helptext.setText("v1.14\n◆增加更新日志和更新欢迎界面\n◆优化应用逻辑\n◆修复若干BUG");
			MainActivity.helpor = 1;
		}
	}
}
