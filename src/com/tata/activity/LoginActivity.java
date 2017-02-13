package com.tata.activity;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.tata.R;
import com.tata.activity.ForgetPwdActivity.MyTimerTask;
import com.tata.bean.PersonInfo;
import com.tata.utils.Constants;
import com.tata.utils.MD5;
import com.tata.utils.MyHttpClient;
import com.tata.utils.ToastUtil;
import com.tata.utils.Tools;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

	private Intent intent;
	private TextView register,forgetPwd;
	private EditText phone, password;
	private Button login;
	private String passwordMD5;
	private String phoneNumber;
	private String pwd;
	private Pattern pattern;
	private Matcher matcher;
	private Dialog dialog;
	protected String result;
	private LinearLayout loginLayout;
	private Timer timer = new Timer();
	private MyTimerTask mTimerTask;
	private EditText regist_checknumber_et;
	private Button regist_getchecknumber_btn;
	private boolean isMessageLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopText("登录");
		setRightButton("注册");
		setCenterView(R.layout.activity_login);
		init();
		SMSSDK.initSDK(this, "a5a6c523f0b0", "94b5796f1df20f9b23d0953d8ecd6c18");
		EventHandler eh = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {

				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				msg.what = 2;
				timeHandler.sendMessage(msg);
			}

		};
		SMSSDK.registerEventHandler(eh);
	}

	private void init() {
		register = (TextView) findViewById(R.id.bt_right);
		forgetPwd = (TextView) findViewById(R.id.forgetPwd);
		register.setOnClickListener(this);
		forgetPwd.setOnClickListener(this);
		phone = (EditText) findViewById(R.id.phone);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(this);
		loginLayout=(LinearLayout)findViewById(R.id.loginLayout);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_right:
			intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			break;
		case R.id.forgetPwd:
			intent = new Intent(this, ForgetPwdActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			break;
		case R.id.login:
			if(!checkForm()){
				break;
			}
			if(isMessageLogin){
			if (! regist_checknumber_et.getText().toString().trim().equals("")) {
				String verificationCode =  regist_checknumber_et.getText().toString().trim();
				if (!TextUtils.isEmpty(verificationCode)) {
					SMSSDK.submitVerificationCode("86", phoneNumber,
							verificationCode);
				}
			} else {
				Toast.makeText(this, "验证码不能为空", 1000).show();
			}
			}else {
				dialog = new MyDialog(this).loadDialog2();
				dialog.show();
				new Thread(new Runnable() {
					String url = "http://120.24.254.127/tata/data/login.php";// 定义请求的地址
					NameValuePair phoneNameValuePair = new BasicNameValuePair(
							"phoneNumber", phoneNumber);
					NameValuePair pwdNameValuePair = new BasicNameValuePair(
							"password", passwordMD5);

					public void run() {
						// TODO Auto-generated method stub
						result = MyHttpClient.post(url, pwdNameValuePair,
								phoneNameValuePair);
						System.out.println(result);
						try {
							int code=new JSONObject(result).getInt("code");
							if (code== 1) {

								handler.sendEmptyMessage(0);
							} else if(code==0) {
								handler.sendEmptyMessage(1);
							}else {
								handler.sendEmptyMessage(2);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}).start();
			}
			break;
			
		case R.id.regist_getchecknumber_btn:
			phoneNumber = phone.getText().toString().trim();
			if (isCellphoneNumber(phoneNumber)) {
				isMessageLogin=true;
				SMSSDK.getVerificationCode("86", phoneNumber);
				if (mTimerTask != null) {
					mTimerTask.cancel(); // 将原任务从队列中移除
					mTimerTask = null;
				}
				time = 60;
				mTimerTask = new MyTimerTask(); // 新建一个任务
				timer = new Timer();

				timer.schedule(mTimerTask, 1000, 1000);
				regist_getchecknumber_btn.setEnabled(false);
			}else {
				Toast.makeText(this, "请输入正确的手机号码", 0).show();
			}
			break;
		}
	}
	
	
	int time = 60;
	Handler timeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				regist_getchecknumber_btn.setText(time + "秒");
				if (time < 1) {
					timer.cancel();
					regist_getchecknumber_btn.setEnabled(true);
					regist_getchecknumber_btn.setText("获取验证码");
					regist_getchecknumber_btn
							.setBackgroundResource(R.drawable.textview);
				} else {
					regist_getchecknumber_btn
							.setBackgroundResource(R.drawable.textview2);
				}
				break;
			case 2:
				int event = msg.arg1;
				int result = msg.arg2;
				Object data = msg.obj;
				System.out.println("kkkkk");
				if (result == SMSSDK.RESULT_COMPLETE) {
					System.out.println("RESULT_COMPLETE");
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						System.out.println("EVENT_SUBMIT_VERIFICATION_CODE");
						if (((HashMap<String, String>) data).get("phone")
								.equals(phoneNumber)) {
							dialog = new MyDialog(LoginActivity.this).loadDialog2();
							new Thread(new Runnable() {
								String url = "http://120.24.254.127/tata/data/loginS.php";// 定义请求的地址
								NameValuePair phoneNameValuePair = new BasicNameValuePair(
										"phoneNumber", phoneNumber);
								NameValuePair pwdNameValuePair = new BasicNameValuePair(
										"password", passwordMD5);

								public void run() {
									// TODO Auto-generated method stub
									LoginActivity.this.result = MyHttpClient.post(url, pwdNameValuePair,
											phoneNameValuePair);
									try {
										int code=new JSONObject(LoginActivity.this.result).getInt("code");
										if (code== 1) {

											handler.sendEmptyMessage(0);
										} else if(code==0) {
											handler.sendEmptyMessage(1);
										}else {
											handler.sendEmptyMessage(2);
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							}).start();
						} else {
							Toast.makeText(LoginActivity.this, "验证码无效",
									0).show();
							break;
						}

					}
//					} else {
//						Toast.makeText(LoginActivity.this, "验证码无效",
//								0).show();
//					}
//				}
				} else {
					Toast.makeText(LoginActivity.this, "验证码无效", 0)
							.show();
				}
				break;
		}
		}
	};
	
	private boolean isCellphoneNumber(String number) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog.dismiss();
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();
				PersonInfo info = jsonTools.getPersonInfo(result);
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"user", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putString("userName", info.getUserName());
				editor.putString("phoneNumber", info.getPhoneNumber());
				editor.putString("userImgUrl", info.getuserImgUrl());
				editor.putInt("gender", info.getGender());
				editor.putString("school", info.getSchool());
				editor.putString("birthday", info.getBirthday());
				editor.commit();
				Intent mIntent = new Intent("data.person.myself");
				sendBroadcast(mIntent);
				finish();
				break;
			case 1:
				dialog.dismiss();
				Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(LoginActivity.this,"此用户已在线", Toast.LENGTH_SHORT).show();
				final AlertDialog dlg3 = new AlertDialog.Builder(LoginActivity.this).create();
				dlg3.show();
				Window window3 = dlg3.getWindow();
				window3.setContentView(R.layout.messagelogin_window);
				window3.findViewById(R.id.cancel).setOnClickListener(
						new OnClickListener() {

							public void onClick(View v) {
								// TODO Auto-generated method stub
								dlg3.dismiss();
							}
						});
				window3.findViewById(R.id.confirm).setOnClickListener(
						new OnClickListener() {

							public void onClick(View v) {
								dlg3.dismiss();
								isMessageLogin=true;
								View view=getLayoutInflater().inflate(R.layout.get_message, null);
								regist_checknumber_et=(EditText) view.findViewById(R.id.regist_checknumber_et);
								regist_getchecknumber_btn=(Button) view.findViewById(R.id.regist_getchecknumber_btn);
								loginLayout.addView(view, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
								regist_getchecknumber_btn.setOnClickListener(LoginActivity.this);
							}
						});
				break;
			}
		}
	};

	public boolean checkForm() {
		phoneNumber = phone.getText().toString().trim();
		pwd = password.getText().toString().trim();
		passwordMD5 = MD5.md5(pwd);
		System.out.println(passwordMD5);
		boolean flag = false;
		if (phoneNumber.equals("")) {
			Toast.makeText(this, "电话号码不能为空！", Toast.LENGTH_SHORT).show();
			return flag;
		}
		if (!new Tools().isCellphoneNumber(phoneNumber)) {
			Toast.makeText(this, "该电话号码不存在！", Toast.LENGTH_SHORT).show();
			return flag;
		}
		if (pwd.equals("")) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
		} else {
			if (!checkPassword(pwd)) {
				Toast.makeText(this, "密码只能以字母、数字或下划线组成，且为6-18位！",
						Toast.LENGTH_SHORT).show();
			} else {
				flag = true;
			}
		}
		return flag;
	}

	public boolean checkPassword(String pwdString) {
		pattern = Pattern.compile("^[\\w]{6,11}$");
		matcher = pattern.matcher(pwdString);
		return matcher.matches();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}

	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			time--;
			Message message = new Message();
			message.what = 1;
			timeHandler.sendMessage(message);
		}

	}

}
