package com.stl.wristNotes;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.view.View.*;
import android.graphics.*;

public class passwordAct extends Activity
{
	Context passctx=this;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	String passtext;
	EditText passedit;
	TextView passview;
	Intent passint;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);

		sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		passtext = sharedPreferences.getString("passtext", "输入密码");
		
		passview = (TextView) findViewById(R.id.passwordText);
		passview.setText(passtext);
		passedit = (EditText) findViewById(R.id.passwordEdit);
		Button passbutt = (Button) findViewById(R.id.passwordButton);
		passbutt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1)
				{
					if (!passedit.getText().toString().equals(""))
					{
						if (passview.getText().toString().equals("  设定新密码  ") /*&& sharedPreferences.getString("password", "").equals("")*/)
						{
							MainActivity.passwdsp = passedit.getText().toString();
							editor.putString("passtext", "  再次输入  ");
							editor.commit();
							Toast.makeText(passctx, "请再次输入密码", Toast.LENGTH_SHORT).show();
							passint = new Intent(passctx, passwordAct.class);
							startActivity(passint);
							finish();
						}
						else if (passview.getText().toString().equals("  再次输入  "))
						{
							if (passedit.getText().toString().equals(MainActivity.passwdsp))
							{
								editor.putString("password", passedit.getText().toString());
								editor.putString("passtext", "输入密码");
								editor.commit();
								Toast.makeText(passctx, "创建密码成功！", Toast.LENGTH_SHORT).show();
								finish();
							}
							else
							{
								Toast.makeText(passctx, "两次输入的不一样..", Toast.LENGTH_SHORT).show();
								passedit.setText("");
							}
						}
						else if(passview.getText().toString().equals("  输入原密码  "))
						{
							if (passedit.getText().toString().equals(sharedPreferences.getString("password", "")))
							{
								editor.putString("passtext", "  设定新密码  ");
								editor.commit();
								passint = new Intent(passctx, passwordAct.class);
								startActivity(passint);
								Toast.makeText(passctx, "请输入新密码！", Toast.LENGTH_SHORT).show();
								finish();
							}
							else
							{
								Toast.makeText(passctx, "密码错误..", Toast.LENGTH_SHORT).show();
								passedit.setText("");
							}
						}
						else if(passview.getText().toString().equals(" 输入原密码 "))
						{
							if (passedit.getText().toString().equals(sharedPreferences.getString("password", "")))
							{
								editor.putString("passtext", "");
								editor.putString("password", "");
								editor.commit();
								Toast.makeText(passctx, "已关闭密码", Toast.LENGTH_SHORT).show();
								finish();
							}
							else
							{
								Toast.makeText(passctx, "密码错误..", Toast.LENGTH_SHORT).show();
								passedit.setText("");
							}
						}
						else
						{
							if (passedit.getText().toString().equals(sharedPreferences.getString("password", "")))
							{
								//Intent passint = new Intent(passwordAct.this,MainActivity.class);
								MainActivity.pass = 1;
								MainActivity.textView.setTextColor(Color.argb(255, MainActivity.light*8, MainActivity.light*8, MainActivity.light*8));
								finish();
							}
							else
							{
								Toast.makeText(passctx, "密码错错错错误！", Toast.LENGTH_SHORT).show();
								passedit.setText("");
							}
						}
					}
					else
					{
						Toast.makeText(passctx, "这里明明什么都没有啊→_→", Toast.LENGTH_SHORT).show();
					}
				}
			});
		passbutt.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View p1)
				{

					return true;
				}
			});
	}
}
