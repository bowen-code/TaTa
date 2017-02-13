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
import com.tata.bean.Reserve;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmActivity extends BaseActivity {

	private ImageView overflow;
	private TextView title, num, date, confirm, nameTextView1, idcardTextView1,
			nameTextView2, idcardTextView2, price,LXRname,LXRphoneNumber;
	private LinearLayout personsInfo,secondTraveller;
    private boolean flag;
    String[][] travellers;
    int ticketNum,amount;
    private String titleString,dateString,contactsName,contactsPho;
	private String phoneNumber;
	private String productID;
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_confirm);
		setTopText("确认订单");
		Intent intent=getIntent();
		travellers=ReserveActivity.travellers;
		titleString=intent.getStringExtra("title");
		phoneNumber=intent.getStringExtra("phoneNumber");
		productID=intent.getStringExtra("productID");
		dateString=intent.getStringExtra("startDate");
		contactsName=intent.getStringExtra("contactsName");
		contactsPho=intent.getStringExtra("contactsPho");
		ticketNum=intent.getIntExtra("ticketNum",0);
		amount=intent.getIntExtra("price",0);
		overflow = (ImageView) findViewById(R.id.overflow);
		if(travellers!=null&&travellers.length>2){
		overflow.setOnClickListener(this);
		}else {
			overflow.setVisibility(View.GONE);
		}
		title = (TextView) findViewById(R.id.title);
		title.setText(titleString);
		LXRname = (TextView) findViewById(R.id.LXRname);
		LXRname.setText(contactsName);
		LXRphoneNumber = (TextView) findViewById(R.id.LXRphoneNumber);
		LXRphoneNumber.setText(contactsPho);
		num = (TextView) findViewById(R.id.num);
		num.setText(ticketNum+"");
		date = (TextView) findViewById(R.id.date);
		date.setText(dateString);
		price = (TextView) findViewById(R.id.amount);
		price.setText("￥"+amount);
		nameTextView1 = (TextView) findViewById(R.id.name);
		nameTextView1.setText("姓名：  "+travellers[0][0]);
		idcardTextView1 = (TextView) findViewById(R.id.idcard);
		idcardTextView1.setText("身份证："+travellers[0][1]);
		nameTextView2 = (TextView) findViewById(R.id.name2);
		idcardTextView2 = (TextView) findViewById(R.id.idcard2);
		personsInfo = (LinearLayout) findViewById(R.id.personsInfo);
		secondTraveller = (LinearLayout) findViewById(R.id.secondTraveller);
		if(travellers.length==1){
			secondTraveller.setVisibility(View.GONE);
		}else {
			nameTextView2.setText("姓名：  "+travellers[1][0]);
			idcardTextView2.setText("身份证："+travellers[1][1]);
		}
		confirm = (TextView) findViewById(R.id.confirm);
		confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.overflow: 
			if(!flag){
				TextView name,idcard;
				for (int i = 2; i < travellers.length; i++) {
					View view=getLayoutInflater().inflate(R.layout.traveller, null);
					name=(TextView) view.findViewById(R.id.name);
					name.setText("姓名：  "+travellers[i][0]);
					idcard=(TextView) view.findViewById(R.id.idcard);
					idcard.setText("身份证："+travellers[i][1]);
					personsInfo.addView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				}
			flag=true;
			overflow.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.time_more2));
			}else {
				personsInfo.removeViews(4,personsInfo.getChildCount()-4);
				flag=false;
				overflow.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.time_more));
			}
			
            break;

		case R.id.confirm:
			confirm.setOnClickListener(null);
			dialog=new MyDialog(this).xiadan();
			dialog.show();
			AsyncHttpClient httpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("phoneNumber", phoneNumber);
			params.put("indentState", 1);
			params.put("productID",productID);
			params.put("ticketNum", ticketNum);
			params.put("startDate",dateString);
			params.put("price",amount);
			params.put("contactsName",contactsName);
			params.put("contactsPho",contactsPho);
			params.put("travellers",jsonTools.toJSONString(travellers));
			httpClient.post("http://120.24.254.127/tata/data/reserve.php", params, new TextHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					// TODO Auto-generated method stub
					confirm.setOnClickListener(ConfirmActivity.this);
					try {
						if(new JSONObject(arg2).getInt("code")==1){
							dialog.dismiss();
							Toast.makeText(ConfirmActivity.this,"下单成功", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ConfirmActivity.this, PayActivity.class);
							Bundle bundle=new Bundle();
							bundle.putString("productID",productID);
					     	bundle.putInt("amount",amount);
					     	bundle.putInt("num",ticketNum);
					     	bundle.putInt("reserveID",new JSONObject(arg2).getInt("data"));
					     	bundle.putString("title",titleString);
					     	intent.putExtras(bundle);
							startActivity(intent);
							overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
						}else {
							Toast.makeText(ConfirmActivity.this,"下单失败，请重试", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					confirm.setOnClickListener(ConfirmActivity.this);
					Toast.makeText(ConfirmActivity.this,"下单失败，请重试", Toast.LENGTH_SHORT).show();
				}

			});
			break;
		}

	}

}
