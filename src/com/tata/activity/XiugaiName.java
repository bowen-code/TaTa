package com.tata.activity;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class XiugaiName extends Activity implements OnClickListener{
	public TextView btn_left;//标题栏左边按钮
	public TextView btn_right;//标题栏右边按钮
	protected View layout_top;//标题栏
	private String userName;
	private EditText name;
	private String phoneNumber;
	private int genderInt;
	private String birthday,schoolString;
	private SharedPreferences mySharedPreferences;
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiugai_name);
		userName=getIntent().getStringExtra("userName");
		phoneNumber=getIntent().getStringExtra("phoneNumber");
		genderInt=getIntent().getIntExtra("genderInt",0);
		birthday=getIntent().getStringExtra("birthday");
		schoolString=getIntent().getStringExtra("school");
		mySharedPreferences = getSharedPreferences("user",
				Activity.MODE_PRIVATE);
         initUI();
	}
     
	protected void initUI() {
		name=(EditText) findViewById(R.id.name);
		name.setText(userName);
		layout_top = findViewById(R.id.title_layout);
		btn_left = (TextView) layout_top.findViewById(R.id.bt_left);
		btn_right = (TextView) layout_top.findViewById(R.id.bt_right);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(0, R.anim.slide_right_out); 
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_right:
			dialog=new MyDialog(this).loadDialog1();
			dialog.show();
			final String username=name.getText().toString().trim();
			if(!username.equals("")){
				 AsyncHttpClient httpClient=new AsyncHttpClient();
				 RequestParams params=new RequestParams();
				 params.put("phoneNumber", phoneNumber);
				 params.put("userName", username);
				 params.put("gender", genderInt);
				 params.put("birthday", birthday);
				 params.put("school", schoolString);
				 String url="http://120.24.254.127/tata/data/updateUserInformation.php";
				 httpClient.post(url, params, new TextHttpResponseHandler() {
				
				 @Override
				 public void onSuccess(int arg0, Header[] arg1, String arg2) {
				 // TODO Auto-generated method stub
				 System.out.println(arg2);
				 dialog.dismiss();
				 Toast.makeText(XiugaiName.this,"修改成功",
				 Toast.LENGTH_SHORT).show();
				 SharedPreferences.Editor editor = mySharedPreferences.edit();
				 editor.putString("userName",username);
				 editor.commit();
				 Intent mIntent = new Intent("data.person.myself");
				 sendBroadcast(mIntent);
				 finish();
				 }
				
				 @Override
				 public void onFailure(int arg0, Header[] arg1, String arg2, Throwable
				 arg3) {
				 // TODO Auto-generated method stub
				 System.out.println(arg2);
				 dialog.dismiss();
				 Toast.makeText(XiugaiName.this,"修改失败",
				 Toast.LENGTH_SHORT).show();
				 }

				 });
			}
			break;
		case R.id.bt_left:
			onBackPressed();
			break;

		}
	}

}
