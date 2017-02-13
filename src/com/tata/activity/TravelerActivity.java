package com.tata.activity;

import java.util.zip.Inflater;

import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.bean.Traveler;
import com.tata.utils.ToastUtil;
import com.tata.utils.Tools;

import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TravelerActivity extends BaseActivity {

	private int num;
	private String firstName,firstIdcard;
	private TextView addPerson,name,idcard;
	private LinearLayout editInputLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_traveler);
		setTopText("信息填写");
		Intent intent=getIntent();
		num=intent.getIntExtra("personNum", 1);
		firstName=intent.getStringExtra("firstName");
		firstIdcard=intent.getStringExtra("firstIdcardtext");
		addPerson=(TextView) findViewById(R.id.addPerson);
		addPerson.setText("点击添加剩余"+(num-1)+"名旅客信息");
		name=(TextView) findViewById(R.id.name);
		name.setText(firstName);
		idcard=(TextView) findViewById(R.id.idcard);
		idcard.setText(firstIdcard);
		editInputLayout=(LinearLayout) findViewById(R.id.editInputLayout);
		if(com.tata.utils.Constants.completeFlag){
			int size=com.tata.utils.Constants.travelers.size();
			if(num<size){
				for (int i = num; i < size; i++) {
					com.tata.utils.Constants.travelers.remove(i);
				}
				addPerson.setText("点击完成");
			}else if(num==size){
				addPerson.setText("点击完成");
			}else{
				addPerson.setText("点击添加剩余"+(num-size)+"名旅客信息");
			}
		}
			for (int i = 1; i < com.tata.utils.Constants.travelers.size(); i++) {
				View addView=LayoutInflater.from(TravelerActivity.this).inflate(R.layout.person_info_input, null);
				EditText name=(EditText)addView.findViewById(R.id.name);
				name.setText(com.tata.utils.Constants.travelers.get(i).getName());
				EditText idcard=(EditText) addView.findViewById(R.id.idcard);
				idcard.setText(com.tata.utils.Constants.travelers.get(i).getIdcard());
				addView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			    editInputLayout.addView(addView);
			}
			final Tools tools=new Tools();
	    addPerson.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				for (int i = 0; i < editInputLayout.getChildCount(); i++) {
					EditText name=(EditText) editInputLayout.getChildAt(i).findViewById(R.id.name);
					EditText idcard=(EditText) editInputLayout.getChildAt(i).findViewById(R.id.idcard);
				    if(name.getText().toString().trim().equals("")||idcard.getText().toString().trim().equals("")){
				    	ToastUtil.show(TravelerActivity.this,"请先填写完该旅客信息");
				    	return;
				    }else if(!tools.personIdValidation(idcard.getText().toString().trim())){
				    	ToastUtil.show(TravelerActivity.this,"该旅客身份证号格式错误");
				    	return;
					}
				}
				if(addPerson.getText().equals("点击完成")){
					com.tata.utils.Constants.completeFlag=true;
					com.tata.utils.Constants.travelers.clear();
					for (int i = 0; i < editInputLayout.getChildCount(); i++) {
						EditText name=(EditText) editInputLayout.getChildAt(i).findViewById(R.id.name);
						EditText idcard=(EditText) editInputLayout.getChildAt(i).findViewById(R.id.idcard);
						com.tata.utils.Constants.travelers.add(new Traveler(name.getText().toString().trim(), idcard.getText().toString().trim()));
					}
					finish();
					return;
				}
				
				
				View addView=LayoutInflater.from(TravelerActivity.this).inflate(R.layout.person_info_input, null);
				addView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//				int num2=editInputLayout.getChildCount()+1;
//				addView.findViewById(R.id.name).setId(num2);
//				addView.findViewById(R.id.idcard).setId(num2+1);
				editInputLayout.addView(addView);
				if(editInputLayout.getChildCount()==num){
					addPerson.setText("点击完成");
				}else {
					addPerson.setText("点击添加剩余"+(num-editInputLayout.getChildCount())+"名旅客信息");
				}
			}
		});
	}


}
