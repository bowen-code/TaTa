package com.tata.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.utils.ToastUtil;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.R.string;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class ChangepwdActivity extends BaseActivity {

	private EditText comfirmPwd,newpwd,pwd;
	private String comfirmPwd_string,newpwd_string,pwd_string;
	private TextView send;
	private Pattern pattern;
	private Matcher matcher;
	private SharedPreferences mySharedPreferences;
	private String phoneNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_changepwd);
		setTopText("修改密码");
		mySharedPreferences = getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		phoneNumber = mySharedPreferences.getString("phoneNumber", "");
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		comfirmPwd=(EditText) findViewById(R.id.comfirmPwd);
		newpwd=(EditText) findViewById(R.id.newpwd);
		pwd=(EditText) findViewById(R.id.pwd);
		send=(TextView) findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {
			
			private Dialog dialog;

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pwd_string=pwd.getText().toString().trim();
				newpwd_string=newpwd.getText().toString().trim();
				comfirmPwd_string=comfirmPwd.getText().toString().trim();
				if(pwd_string.equals("")||newpwd_string.equals("")||comfirmPwd_string.equals("")){
					ToastUtil.show(ChangepwdActivity.this,"请输入完整信息");
					return;
				}else if(!newpwd_string.equals(comfirmPwd_string)){
					ToastUtil.show(ChangepwdActivity.this,"两次密码不一致");
					return;
				}else if (!checkPassword(newpwd_string)) {
					ToastUtil.show(ChangepwdActivity.this,"输入密码的格式不对");
					return;
				}
				else {
					dialog=new MyDialog(ChangepwdActivity.this).loadDialog1();
					dialog.show();
					AsyncHttpClient httpClient = new AsyncHttpClient();
					RequestParams params = new RequestParams();
					String url = "http://120.24.254.127/tata/data/updateUserPassword.php";
					params.put("phoneNumber", phoneNumber);
					params.put("newPassWord", newpwd_string);
					params.put("password", pwd_string);
					httpClient.post(url, params,
							new TextHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0,
										Header[] arg1, String arg2) {
									// TODO Auto-generated method stubne
									try {
										System.out.println(arg2);
										dialog.dismiss();
										JSONObject object=new JSONObject(arg2);
									    int code=object.getInt("code");
									    if(code==1){
											ToastUtil.show(ChangepwdActivity.this,"原密码输入错误");
											return;
									    }else if(code==2){
									    	ToastUtil.show(ChangepwdActivity.this,"密码修改成功");
									    	finish();
										}else {
									    	ToastUtil.show(ChangepwdActivity.this,"密码修改失败，请重试");
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

								@Override
								public void onFailure(int arg0,
										Header[] arg1, String arg2,
										Throwable arg3) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
				}
			}
		});
	}
	public boolean checkPassword(String pwdString) {
		pattern = Pattern.compile("^[\\w]{6,18}$");
		matcher = pattern.matcher(pwdString);
		return matcher.matches();
	}


}
