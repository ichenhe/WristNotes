package com.stl.wristNotes;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;


public class inputAct extends Activity
{
	Context ctx = this;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	
	TextView inputtitle;
	EditText inputedit;
	Button inputbutton;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input);

		sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		
		inputtitle = (TextView) findViewById(R.id.inputText);
		inputtitle.setText(MainActivity.inputtitle);
		inputedit = (EditText) findViewById(R.id.inputEdit);
		inputedit.setHint(MainActivity.inputhite);
		inputedit.setText(MainActivity.inputset);
		inputbutton = (Button) findViewById(R.id.inputButton);
		inputbutton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					if(MainActivity.inputact == 1)//密码伪装
					{
						if(!inputedit.getText().toString().equals(""))
						{
							editor.putString("passtext", inputedit.getText().toString());
							editor.commit();
							Toast.makeText(ctx, "设置成功！", Toast.LENGTH_SHORT).show();
							finish();
						}
						else
						{
							editor.putString("passtext", "输入密码");
							editor.commit();
							Toast.makeText(ctx, "恢复默认：“输入密码”", Toast.LENGTH_SHORT).show();
							finish();
						}
					}
				}
			});
	}
}
