package com.tata.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.utils.AppManager;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewPwdActivity extends BaseActivity {

	private Button complete;
	private EditText pwdEditText,newPwdEditText;
	String pwd,newPwd;
	private String phoneNumber;
	private Dialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_new_pwd);
		setTopText("填写密码");
		phoneNumber=getIntent().getStringExtra("phoneNumber");
		complete=(Button) findViewById(R.id.complete);
		complete.setOnClickListener(this);
		pwdEditText=(EditText) findViewById(R.id.password);
		newPwdEditText=(EditText) findViewById(R.id.confirm_password);
	}

	@Override
	public void onClick(View v) {
		pwd=pwdEditText.getText().toString().trim();
		newPwd=newPwdEditText.getText().toString().trim();
		if(checkPassword(pwd)&&checkPassword(newPwd)){
			if(pwd.equals(newPwd)){
				pd = new MyDialog(this)
				.loadDialog1();
				AsyncHttpClient httpClient=new AsyncHttpClient();
				RequestParams params=new RequestParams();
				params.put("phoneNumber",phoneNumber);
				params.put("newPassword",pwd);
				httpClient.post("http://120.24.254.127/tata/updateUserPassword", params, new TextHttpResponseHandler() {
					
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						pd.dismiss();
						AppManager.getAppManager().finishLastTwoActivitys();
					}
					
					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(NewPwdActivity.this,"发生错误，请重试",Toast.LENGTH_SHORT).show();
						pd.dismiss();
					}
				});
				
			}else {
				Toast.makeText(this,"两次密码不一致",Toast.LENGTH_SHORT).show();
			}
		}else {
			Toast.makeText(this,"密码格式不对",Toast.LENGTH_SHORT).show();
		}
	}

	
	public boolean checkPassword(String pwdString) {
		Pattern pattern = Pattern.compile("^[\\w]{6,18}$");
		Matcher matcher = pattern.matcher(pwdString);
		return matcher.matches();
	}
}
