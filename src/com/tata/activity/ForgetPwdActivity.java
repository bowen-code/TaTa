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
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.activity.RegisterActivity.MyTimerTask;
import com.tata.utils.AppManager;
import com.tata.utils.MyHttpClient;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgetPwdActivity extends BaseActivity {

	private Button regist_next_btn, regist_getchecknumber_btn;
	private EditText phoneNumber, regist_checknumber_et;
	private Timer timer = new Timer();
	private Dialog pd;
	private MyTimerTask mTimerTask;
	private String phoneNumberString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_forget_pwd);
		setTopText("忘记密码");
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
		regist_next_btn = (Button) findViewById(R.id.regist_next_btn);
		regist_getchecknumber_btn = (Button) findViewById(R.id.regist_getchecknumber_btn);
		regist_next_btn.setOnClickListener(this);
		regist_getchecknumber_btn.setOnClickListener(this);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		regist_checknumber_et = (EditText) findViewById(R.id.regist_checknumber_et);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.regist_getchecknumber_btn:
			phoneNumberString = phoneNumber.getText().toString().trim();
			if(phoneNumberString.equals("")){
				Toast.makeText(this, "电话号码不能为空", 1000).show();
				break;
			}
			if(!isCellphoneNumber(phoneNumberString)){
				Toast.makeText(this, "该电话号码不存在", 1000).show();
				break;
			}
			if (isCellphoneNumber(phoneNumberString)) {
				SMSSDK.getVerificationCode("86", phoneNumberString);
				if (mTimerTask != null) {
					mTimerTask.cancel(); // 将原任务从队列中移除
					mTimerTask = null;
				}
				time = 60;
				mTimerTask = new MyTimerTask(); // 新建一个任务
				timer = new Timer();

				timer.schedule(mTimerTask, 1000, 1000);
				regist_getchecknumber_btn.setEnabled(false);
			}
			break;

		case R.id.regist_next_btn:
			String confirmCode = regist_checknumber_et.getText().toString().trim();
			if (!confirmCode.equals("")) {
				pd = new MyDialog(ForgetPwdActivity.this)
				.loadDialog1();
				String verificationCode = confirmCode;
				if (!TextUtils.isEmpty(verificationCode)) {
					SMSSDK.submitVerificationCode("86", phoneNumberString,
							verificationCode);
				}
			} else {
				Toast.makeText(this, "验证码不能为空", 1000).show();
			}
			break;
		}
	}

	private boolean isCellphoneNumber(String number) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
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
				if (result == SMSSDK.RESULT_COMPLETE) {
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						if (((HashMap<String, String>) data).get("phone")
								.equals(phoneNumberString)) {
							Toast.makeText(ForgetPwdActivity.this, "验证码验证成功",
									1000).show();
							pd.dismiss();
							Intent intent = new Intent(ForgetPwdActivity.this, NewPwdActivity.class);
							intent.putExtra("phoneNumber", phoneNumberString);
							startActivity(intent);
							overridePendingTransition(R.anim.slide_left_in,
									R.anim.slide_left_out);
						} else {
							Toast.makeText(ForgetPwdActivity.this, "验证码验证失败",
									1000).show();
							pd.dismiss();
							break;
						}

					} else {
						if (pd != null) {
							pd.dismiss();
						}
					}
				} else {
					if (pd != null) {
						pd.dismiss();
					}
					Toast.makeText(ForgetPwdActivity.this, "验证码无效", 1000)
							.show();
				}
				break;
		}
		}
	};

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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}
}
