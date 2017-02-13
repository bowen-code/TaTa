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
import com.tata.utils.MD5;
import com.tata.utils.MyHttpClient;
import com.tata.view.MyDialog;
import com.tata.utils.AppManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {

	private Button regist_getchecknumber_btn;
	private EditText regist_checknumber_et;
	private EditText regist_phone_et;
	private EditText username;
	private Button regist_next_btn;
	private Timer timer = new Timer();
	private Dialog pd;
	private MyTimerTask mTimerTask;
	private static final int RETRY_INTERVAL = 60;
	private int time = RETRY_INTERVAL;
	private String usernameString;
	private String confirmCode;
	private String phoneNumber;
	private String password;
	private EditText passwordEditText;
	private EditText cfpasswordEditText;
	private String cfpwd;
	private String passwordMD5;
	private Pattern pattern;
	private Matcher matcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTopText("注册");
		setCenterView(R.layout.activity_register);
		SMSSDK.initSDK(this, "a5a6c523f0b0", "94b5796f1df20f9b23d0953d8ecd6c18");
		init();
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
		regist_checknumber_et = (EditText) findViewById(R.id.regist_checknumber_et);
		regist_phone_et = (EditText) findViewById(R.id.regist_phone_et);
		regist_next_btn = (Button) findViewById(R.id.regist_next_btn);
		username = (EditText) findViewById(R.id.username);
		regist_getchecknumber_btn = (Button) findViewById(R.id.regist_getchecknumber_btn);
		regist_getchecknumber_btn.setOnClickListener(this);
		regist_next_btn.setOnClickListener(this);
		passwordEditText = (EditText) findViewById(R.id.password);
		cfpasswordEditText = (EditText) findViewById(R.id.confirmPwd);
	}

	private boolean isCellphoneNumber(String number) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}
	
	@Override
	public void onClick(View v) {
		if (v == regist_getchecknumber_btn) {
			if (!TextUtils.isEmpty(regist_phone_et.getText().toString())
					&& isCellphoneNumber(regist_phone_et.getText().toString().trim())) {
				SMSSDK.getVerificationCode("86", regist_phone_et.getText()
						.toString().trim());
				if (mTimerTask != null) {
					mTimerTask.cancel(); // 将原任务从队列中移除
					mTimerTask = null;
				}
				time = 60;
				mTimerTask = new MyTimerTask(); // 新建一个任务
				timer = new Timer();

				timer.schedule(mTimerTask, 1000, 1000);
				regist_getchecknumber_btn.setEnabled(false);
				// phString=regist_checknumber_et.getText().toString();
			} else {
				Toast.makeText(this, "请输入正确的手机号码", 1).show();
			}

		}

		else if (v == regist_next_btn) {
			if (!checkForm()) {
				return;
			}
			confirmCode = regist_checknumber_et.getText().toString().trim();
			phoneNumber = regist_phone_et.getText().toString().trim();

			if (!confirmCode.equals("")) {
				String verificationCode = confirmCode;
				if (!TextUtils.isEmpty(verificationCode)) {
					SMSSDK.submitVerificationCode("86", phoneNumber,
							verificationCode);
				}
			} else {
				Toast.makeText(this, "验证码不能为空", 1000).show();
			}
		}
	}

	public boolean checkForm() {
		usernameString = username.getText().toString().trim();
		password = passwordEditText.getText().toString().trim();
		cfpwd = cfpasswordEditText.getText().toString().trim();
		passwordMD5 = MD5.md5(password);
		boolean flag = false;
		if (usernameString.equals("")) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
		} else {
			if (password.equals("")) {
				Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			} else {
				if (cfpwd.equals("")) {
					Toast.makeText(this, "确认密码不能为空！", Toast.LENGTH_SHORT)
							.show();
				} else {
					if (!checkUsername(usernameString)) {
						Toast.makeText(this, "用户名只能以中文、字母、数字或下划线组成，且为3-10位！",
								Toast.LENGTH_SHORT).show();
					} else {
						if (!checkPassword(password)) {
							Toast.makeText(this, "密码只能以字母、数字或下划线组成，且为6-18位！",
									Toast.LENGTH_SHORT).show();
						} else {
							if (!password.equals(cfpwd)) {
								Toast.makeText(getApplicationContext(),
										"密码不一致", Toast.LENGTH_SHORT).show();
							} else {
								flag = true;
							}
						}
					}
				}
			}
		}
		return flag;
	}

	public boolean checkUsername(String username) {
		pattern = Pattern
				.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w]{3,10}$");
		matcher = pattern.matcher(username);
		return matcher.matches();
	}

	public boolean checkPassword(String pwdString) {
		pattern = Pattern.compile("^[\\w]{6,18}$");
		matcher = pattern.matcher(pwdString);
		return matcher.matches();
	}

	final Handler timeHandler = new Handler() {
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
				if (result == SMSSDK.RESULT_COMPLETE) {
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						if (((HashMap<String, String>) data).get("phone")
								.equals(phoneNumber)) {
//							Toast.makeText(RegisterActivity.this, "验证码验证成功",
//									1000).show();
							pd = new MyDialog(RegisterActivity.this)
									.loadDialog1();
							if (pd != null) {
								pd.show();
							}

						new Thread(new Runnable() {
							String url = "http://120.24.254.127/tata/data/register.php";// 定义请求的地址
							NameValuePair userNameValuePair = new BasicNameValuePair(
									"userName", usernameString);
							NameValuePair pwdNameValuePair = new BasicNameValuePair(
									"password", passwordMD5);
							NameValuePair phoneNameValuePair = new BasicNameValuePair(
									"phoneNumber", phoneNumber);

							public void run() {
								// TODO Auto-generated method stub
								String result = MyHttpClient.post(url,
										userNameValuePair, pwdNameValuePair,
										phoneNameValuePair);
								System.out.println(result);
								try {
									int code=new JSONObject(result).getInt("code");
									if(code==0){
										timeHandler.sendEmptyMessage(4);
									}else if (code==1) {
										timeHandler.sendEmptyMessage(5);
									}else {
										timeHandler.sendEmptyMessage(3);
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}).start();
						}
						else {
							Toast.makeText(RegisterActivity.this, "验证码无效",
									1000).show();
							break;
						}
					} 
				}
				break;

			case 3:
				if (pd != null) {
					pd.dismiss();
				}
				Toast.makeText(RegisterActivity.this, "注册成功", 1000).show();
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"user", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putString("userName",usernameString);
				editor.putString("phoneNumber",phoneNumber);
				editor.commit();
				Intent mIntent = new Intent("data.person.myself");
				sendBroadcast(mIntent);
				Intent intent=new Intent(RegisterActivity.this, XiuGaiActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_left_out);
				AppManager.getAppManager().finishLastTwoActivitys();
				break;
			case 4:
				if (pd != null) {
					pd.dismiss();
				}
				Toast.makeText(RegisterActivity.this, "注册失败", 1000).show();
				break;
			case 5:
				if (pd != null) {
					pd.dismiss();
				}
				Toast.makeText(RegisterActivity.this, "该手机号已注册", 1000).show();
				break;
			}
		}
	};

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