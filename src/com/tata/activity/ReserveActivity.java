package com.tata.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.bean.NearTravel;
import com.tata.utils.Constants;
import com.tata.utils.ToastUtil;
import com.tata.utils.Tools;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;
import com.tata.view.WLQQTimePicker;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReserveActivity extends BaseActivity {

	private ImageView minus, plus,overFlow;
	private TextView title,numTextView, submitReservation, date, amount,addPerson;
	private Dialog dialog;
	private LinearLayout personsInfo;
	private NearTravel data;
	private int danjia;
	private Intent intent;
	private EditText name,idcard,LXRphoneNumber,LXRname;
	private int num;
	private SharedPreferences mySharedPreferences;
	private String phoneNumber,userName;
	private ArrayList<String> timeData;
	static String[][] travellers;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater()
				.inflate(R.layout.activity_reserve, null);
		intent=getIntent();
		data = (NearTravel) intent.getSerializableExtra("product");
//		danjia = data.getPriceTwo();
		timeData = new ArrayList<String>();
		for (int i = 0; i < data.getDays().size(); i++) {
			timeData.add(data.getDays().get(i).replace('-', '/'));
		}
		if(data.getPriceTwo()==0){
			danjia=data.getPrice();
		}else {
			danjia=data.getPriceTwo();
		}
		mySharedPreferences = getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		phoneNumber = mySharedPreferences.getString("phoneNumber", "");
		userName = mySharedPreferences.getString("userName", "");
		init(view);
		Constants.completeFlag=false;
		Constants.travelers.clear();
	}

	private void init(View view) {
		setTopText("订单填写");
		setCenterView(view);
//		date = (TextView) findViewById(R.id.date);
//		if(!Constants.startTime.equals("")){
//			date.setText(Constants.startTime);
//		}
//		date.setOnClickListener(this);
		LXRname=(EditText) findViewById(R.id.LXRname);
		LXRname.setText(userName);
		LXRphoneNumber=(EditText) findViewById(R.id.LXRphoneNumber);
		LXRphoneNumber.setText(phoneNumber);
		name=(EditText) findViewById(R.id.name);
		idcard=(EditText) findViewById(R.id.idcard);
		overFlow=(ImageView) findViewById(R.id.overFlow);
		overFlow.setOnClickListener(this);
		minus = (ImageView) findViewById(R.id.minus);
		plus = (ImageView) findViewById(R.id.plus);
		numTextView = (TextView) findViewById(R.id.num);
		submitReservation = (TextView) findViewById(R.id.submitReservation);
		minus.setOnClickListener(this);
		plus.setOnClickListener(this);
		addPerson=(TextView) findViewById(R.id.addPerson);
		addPerson.setOnClickListener(this);
		personsInfo=(LinearLayout) findViewById(R.id.personsInfo);
		title=(TextView) findViewById(R.id.title);
		title.setText(data.getTitle());
		date=(TextView) findViewById(R.id.date);
		if(intent.getStringExtra("date").equals("")){
			date.setText("请选择出发时间");
		}else {
			date.setText(intent.getStringExtra("date")+"出发");
		}
		amount=(TextView) findViewById(R.id.amount);
		amount.setText("￥"+danjia);
		submitReservation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.overFlow){
			Intent intent=new Intent(ReserveActivity.this,MoreDate.class);
			intent.putExtra("date",timeData);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out); 
			return;
		}
		num = Integer.parseInt(this.numTextView.getText().toString());
		Intent intent;
		switch (v.getId()) {
		case R.id.minus:
			if (num != 1) {
				num--;
				this.numTextView.setText(num + "");
				if(num>Constants.travelers.size()){
					if(num-1-Constants.travelers.size()==0){
						addPerson.setText("点击添加剩余旅客信息");
					}else {
						addPerson.setText("点击添加剩余"+(num-1-Constants.travelers.size())+"人旅客信息");
					}
				}else {
					addPerson.setText("查看或修改旅客信息");
				}
				amount.setText("￥" + num * danjia);
			}
			break;
		case R.id.plus:
			num++;
			this.numTextView.setText(num + "");
			if(num>Constants.travelers.size()){
				if(Constants.completeFlag){
					addPerson.setText("点击添加剩余"+(num-Constants.travelers.size())+"人旅客信息");
				}else {
					addPerson.setText("点击添加剩余"+(num-1-Constants.travelers.size())+"人旅客信息");
				}
			}
			amount.setText("￥" + num * danjia);
			break;
		case R.id.submitReservation:
			if(date.getText().toString().equals("请选择出发时间")){
				Toast.makeText(this,"请选择出发日期！", Toast.LENGTH_SHORT).show();
				break;
			}
			if(name.getText().toString().equals("")||idcard.getText().toString().equals("")){
				Toast.makeText(this,"请填写旅客信息！", Toast.LENGTH_SHORT).show();
				break;
			}else if(!new Tools().personIdValidation(idcard.getText().toString().trim())){
		    	ToastUtil.show(ReserveActivity.this,"旅客身份证号格式错误");
		    	break;
			}
			
				if(num!=1&&num!=Constants.travelers.size()){
					Toast.makeText(this,"你选择的旅客人数与填写的个数不同", Toast.LENGTH_SHORT).show();
					break;
				}
				
				
			
			if(LXRname.getText().toString().equals("")){
				Toast.makeText(this,"请填写联系人姓名！", Toast.LENGTH_SHORT).show();
				break;
			}
			if(LXRphoneNumber.getText().toString().equals("")){
				Toast.makeText(this,"请填写联系人电话！", Toast.LENGTH_SHORT).show();
				break;
			}else if(!new Tools().isCellphoneNumber(LXRphoneNumber.getText().toString().trim())){
				Toast.makeText(this,"联系人电话号码格式错误！", Toast.LENGTH_SHORT).show();
				break;
			}
			
//			dialog=new MyDialog(this).xiadan();
//			dialog.show();
			if(num==1){
				travellers=new String[1][2];
				travellers[0][0]=name.getText().toString().trim();
				travellers[0][1]=idcard.getText().toString().trim();
			}else {
				int size=Constants.travelers.size();
				travellers=new String[size][2];
				for (int i = 0; i < size; i++) {
					travellers[i][0]=Constants.travelers.get(i).getName();
					travellers[i][1]=Constants.travelers.get(i).getIdcard();
				}
			}
//			submitReservation.setOnClickListener(null);
			Intent intent2 = new Intent(ReserveActivity.this, ConfirmActivity.class);
			intent2.putExtra("title", data.getTitle());
			intent2.putExtra("phoneNumber",phoneNumber);
			intent2.putExtra("productID",data.getProductID());
			intent2.putExtra("ticketNum",num);
			intent2.putExtra("price",danjia*num);
			intent2.putExtra("contactsName",LXRname.getText().toString().trim());
			intent2.putExtra("contactsPho",LXRphoneNumber.getText().toString().trim());
			intent2.putExtra("startDate",date.getText().toString().substring(0, 10));
			startActivity(intent2);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			
			break;
		case R.id.addPerson:
			if(num==1){
				ToastUtil.show(this,"无剩余旅客");
				return;
			}else if (name.getText().toString().trim().equals("")||idcard.getText().toString().trim().equals("")) {
				ToastUtil.show(this,"请先填写好个人信息");
				return;
			}else if(!new Tools().personIdValidation(idcard.getText().toString().trim())){
		    	ToastUtil.show(ReserveActivity.this,"旅客身份证号格式错误");
		    	return;
			}
			intent = new Intent(this, TravelerActivity.class);
			intent.putExtra("firstName",name.getText().toString().trim());
			intent.putExtra("firstIdcardtext", idcard.getText().toString().trim());
			intent.putExtra("personNum",num);
			startActivityForResult(intent, 0);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			break;
	}
	}
//	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==0&&Constants.travelers.size()!=num){
			Constants.completeFlag=false;
			return;
		}
		if(requestCode==0&&Constants.completeFlag){
			addPerson.setText("查看或修改旅客信息");
		}
			if (requestCode == 1 && !Constants.startTime.equals("")) {
				date.setText(Constants.startTime.replace('/', '-')+"出发");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
