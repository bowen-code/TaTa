package com.tata.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.utils.Constants;
import com.tata.utils.ToastUtil;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PrivateorderActivity extends BaseActivity {

	private EditText destination, startPlace, budget,
			require;
	private TextView  dayNum, personNum;
	private ImageView minusdayNum,plusdayNum;
	private ImageView minuspersonNum,pluspersonNum;
	private TextView submit, startTime;
	private String dest, sP, sT, rq;
	private int dN, pN;
	private int bd;
	private SharedPreferences mySharedPreferences;
	private String phoneNumber;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopText("私人订制");
		View view = getLayoutInflater().inflate(R.layout.activity_privateorder,
				null);
		setCenterView(view);
		mySharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
		init(view);
	}

	private void init(View view) {
		destination = (EditText) view.findViewById(R.id.destination);
		startPlace = (EditText) view.findViewById(R.id.startPlace);
		startTime = (TextView) view.findViewById(R.id.startTime);
		startTime.setOnClickListener(this);
		dayNum = (TextView) view.findViewById(R.id.dayNum);
		personNum = (TextView) view.findViewById(R.id.personNum);
		budget = (EditText) view.findViewById(R.id.budget);
		require = (EditText) view.findViewById(R.id.require);
		submit = (TextView) view.findViewById(R.id.submit);
		submit.setOnClickListener(this);
		minusdayNum=(ImageView) findViewById(R.id.minusdayNum);
		minuspersonNum=(ImageView) findViewById(R.id.minuspersonNum);
		plusdayNum=(ImageView) findViewById(R.id.plusdayNum);
		pluspersonNum=(ImageView) findViewById(R.id.pluspersonNum);
		minusdayNum.setOnClickListener(this);
		pluspersonNum.setOnClickListener(this);
		plusdayNum.setOnClickListener(this);
		minuspersonNum.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		int num;
		switch (v.getId()) {
		case R.id.minusdayNum:
			num = Integer.parseInt(dayNum.getText().toString());
			if (num != 1) {
				num--;
				dayNum.setText(num + "");
			}
			break;
		case R.id.plusdayNum:
			num = Integer.parseInt(dayNum.getText().toString());
			num++;
			dayNum.setText(num + "");
			break;
		case R.id.minuspersonNum:
			num = Integer.parseInt(personNum.getText().toString());
			if (num != 1) {
				num--;
				personNum.setText(num + "");
			}
			break;
		case R.id.pluspersonNum:
			num = Integer.parseInt(personNum.getText().toString());
			num++;
			personNum.setText(num + "");
			break;
		case R.id.startTime:
			intent = new Intent(this, CalendarActivity.class);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;

		case R.id.submit:
			phoneNumber = mySharedPreferences.getString("phoneNumber", "");
			if(phoneNumber.equals("")){
				ToastUtil.show(this, "请先登陆！");
				intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_left_out);
				break;
			}
			dest = destination.getText().toString().trim();
			sP = startPlace.getText().toString().trim();
			sT = startTime.getText().toString().trim();
			rq = require.getText().toString().trim();
			if (dest.equals("")) {
				ToastUtil.show(this, "请填写目的地");
				return;
			} else if (sP.equals("")) {
				ToastUtil.show(this, "请填写出发地");
				return;
			} else if (sT.equals("")) {
				ToastUtil.show(this, "请选择出发日期");
				return;
			}
			dN = Integer.valueOf(dayNum.getText().toString().trim());
			pN = Integer.valueOf(personNum.getText().toString().trim());
			try {
				bd = Integer.valueOf(budget.getText().toString().trim());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				ToastUtil.show(this, "请填写正确的预算金额");
				e.printStackTrace();
				return;
			}
			if (bd <= 0) {
				ToastUtil.show(this, "请填写正确的预算金额");
				return;
			}

			dialog = new MyDialog(this).loadDialog1();
			dialog.show();
			AsyncHttpClient httpClient = new AsyncHttpClient();
			String url = "http://120.24.254.127/tata/data/privateIndent.php";
			RequestParams params = new RequestParams();
			params.put("phoneNumber", phoneNumber);
			params.put("destination", dest);
			params.put("startPlace ", sP);
			params.put("startDate ", sT);
			params.put("days", dN);
			params.put("personNum", pN);
			params.put("budget", bd);
			params.put("description", rq);
			httpClient.post(url, params, new TextHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					try {
						if(new JSONObject(arg2).getInt("code")==1){
							ToastUtil.show(PrivateorderActivity.this, "提交成功");
							finish();
						}else {
							ToastUtil.show(PrivateorderActivity.this, "提交失败，请重试");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					ToastUtil.show(PrivateorderActivity.this, "提交失败 请重试");
				}
			});

			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1 && !Constants.startTime.equals("")) {
			startTime.setText(Constants.startTime);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
