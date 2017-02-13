package com.tata.activity;

import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.utils.ToastUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AdviceActivity extends BaseActivity {

	private EditText productType,content;
	private TextView phoneNumberTextView;
	private String phoneNumber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=getLayoutInflater().inflate(R.layout.activity_advice, null);
		phoneNumber=getSharedPreferences("user", MODE_PRIVATE).getString("phoneNumber", "");
		init(view);
	}

	private void init(View view) {
		setTopText("�������");
		setRightButton("�ύ");
		setCenterView(view);
		productType=(EditText) findViewById(R.id.productType);
		phoneNumberTextView=(TextView) findViewById(R.id.phoneNumber);
		phoneNumberTextView.setText(phoneNumber);
		content=(EditText) findViewById(R.id.content);
		setRightClick();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(phoneNumber.equals("")){
			ToastUtil.show(this,"���ȵ�¼");
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			return;
		}
		if(productType.getText().toString().equals("")){
			ToastUtil.show(this,"����д��������");
			return;
		}
		if(content.getText().toString().equals("")){
			ToastUtil.show(this,"����д�������");
			return;
		}
		ToastUtil.show(this,"�ύ�ɹ�");
		finish();
	}


}
