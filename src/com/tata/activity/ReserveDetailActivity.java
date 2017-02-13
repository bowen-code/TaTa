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
import com.tata.utils.AppManager;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReserveDetailActivity extends BaseActivity {
	Reserve reserve;
	private ImageView overflow;
	private TextView title, num, date, pay, nameTextView1, idcardTextView1,
			nameTextView2, idcardTextView2, price,LXRname,LXRphoneNumber;
	private LinearLayout personsInfo,secondTraveller;
    private boolean flag;
    String[][] travellers;
    int ticketNum,amount;
    private String titleString,dateString,contactsName,contactsPho;
	private String phoneNumber;
	private String productID;
	private Dialog dialog;
	private RelativeLayout toProduct;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_reserve_detail);
		setTopText("订单详情");
		SharedPreferences mySharedPreferences = getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		phoneNumber = mySharedPreferences.getString("phoneNumber", "");
		reserve=(Reserve) getIntent().getSerializableExtra("reserve");
		travellers=reserve.getTraveller();
		productID=reserve.getProductID();
		title = (TextView) findViewById(R.id.title);
		toProduct=(RelativeLayout) findViewById(R.id.toProduct);
		if(reserve.getProductState()==1){
		toProduct.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent intent=new Intent(ReserveDetailActivity.this, ProductActivity.class);
	             intent.putExtra("productID",reserve.getProductID());
	             intent.putExtra("isAgainProduct",true);
	             ReserveDetailActivity.this.startActivity(intent);
	             overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
		}
		titleString=reserve.gettitle();
		title.setText(titleString);
		LXRname = (TextView) findViewById(R.id.LXRname);
		contactsName=reserve.getContactsName();
		LXRname.setText(contactsName);
		LXRphoneNumber = (TextView) findViewById(R.id.LXRphoneNumber);
		contactsPho=reserve.getContactsPho();
		LXRphoneNumber.setText(contactsPho);
		num = (TextView) findViewById(R.id.num);
		ticketNum=reserve.getTicketsum();
		num.setText(ticketNum+"");
		overflow = (ImageView) findViewById(R.id.overflow);
		if(travellers!=null&&travellers.length>2){
		overflow.setOnClickListener(this);
		}else {
			overflow.setVisibility(View.GONE);
		}
		date = (TextView) findViewById(R.id.date);
		dateString=reserve.getStartTime();
		date.setText(dateString);
		price = (TextView) findViewById(R.id.amount);
		amount=reserve.getPrice();
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
		pay = (TextView) findViewById(R.id.pay);
		if(reserve.getIndentState()==2){
			pay.setText("申请退款");
			pay.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog=new MyDialog(ReserveDetailActivity.this).tuikuan();
					dialog.show();
					AsyncHttpClient httpClient=new AsyncHttpClient();
					RequestParams params=new RequestParams();
					params.put("indentID",reserve.getIndentID());
					params.put("id",reserve.getId());
					params.put("description","就是要退款");
					httpClient.post("http://120.24.254.127/tata/data/MN/_refund.php", params, new TextHttpResponseHandler() {
						
						@Override
						public void onSuccess(int arg0, Header[] arg1, String arg2) {
							// TODO Auto-generated method stub
							pay.setOnClickListener(ReserveDetailActivity.this);
//							try {
//								if(new JSONObject(arg2).getInt("code")==1){
							System.out.println(arg2);
									dialog.dismiss();
									Toast.makeText(ReserveDetailActivity.this,"退款成功", Toast.LENGTH_SHORT).show();
									Intent intent=new Intent(ReserveDetailActivity.this,MyReserveActivity.class);
									intent.putExtra("isRefund",true);
									startActivity(intent);
									overridePendingTransition(R.anim.slide_left_in,
											R.anim.slide_left_out);
									AppManager.getAppManager().finishLastTwoActivitys();
//								}else {
//									dialog.dismiss();
//									Toast.makeText(ReserveDetailActivity.this,"退款失败，请重试", Toast.LENGTH_SHORT).show();
//								}
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							pay.setOnClickListener(ReserveDetailActivity.this);
							Toast.makeText(ReserveDetailActivity.this,"下单失败，请重试", Toast.LENGTH_SHORT).show();
						}
		
					});
				}
			});
		}else if (reserve.getIndentState()==3||reserve.getIndentState()==4) {
			if(reserve.getProductState()==0){
				pay.setText("产品已下架");
			}else {
			pay.setText("再来一单");
			pay.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(ReserveDetailActivity.this, ProductActivity.class);
					intent.putExtra("productID",reserve.getProductID()); 
					intent.putExtra("isAgainProduct",true);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				}
			});
			}
		}else {
			if(reserve.getProductState()==0){
				pay.setText("产品已下架");
			}else {
			pay.setOnClickListener(this);
			}
		}
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

		case R.id.pay:
			pay.setOnClickListener(null);
//			dialog=new MyDialog(this).xiadan();
//			dialog.show();
			Intent intent = new Intent(ReserveDetailActivity.this, PayActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("productID",productID);
	     	bundle.putInt("amount",amount);
	     	bundle.putInt("num",ticketNum);
	     	bundle.putInt("reserveID",reserve.getIndentID());
	     	bundle.putString("title",titleString);
	     	intent.putExtras(bundle);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
//			AsyncHttpClient httpClient=new AsyncHttpClient();
//			RequestParams params=new RequestParams();
//			params.put("phoneNumber", phoneNumber);
//			params.put("indentState", 1);
//			params.put("productID",productID);
//			params.put("ticketNum", ticketNum);
//			params.put("startDate",dateString);
//			params.put("price",amount);
//			params.put("contactsName",contactsName);
//			params.put("contactsPho",contactsPho);
//			params.put("travellers",jsonTools.toJSONString(travellers));
//			httpClient.post("http://120.24.254.127/tata/data/reserve.php", params, new TextHttpResponseHandler() {
//				
//				@Override
//				public void onSuccess(int arg0, Header[] arg1, String arg2) {
//					// TODO Auto-generated method stub
//					pay.setOnClickListener(ReserveDetailActivity.this);
//					try {
//						if(new JSONObject(arg2).getInt("code")==1){
//							dialog.dismiss();
//							Toast.makeText(ReserveDetailActivity.this,"下单成功", Toast.LENGTH_SHORT).show();
//							Intent intent = new Intent(ReserveDetailActivity.this, PayActivity.class);
//							Bundle bundle=new Bundle();
//							bundle.putString("productID",productID);
//					     	bundle.putInt("amount",amount);
//					     	bundle.putInt("num",ticketNum);
//					     	bundle.putInt("reserveID",new JSONObject(arg2).getInt("data"));
//					     	bundle.putString("title",titleString);
//					     	intent.putExtras(bundle);
//							startActivity(intent);
//							overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//						}else {
//							dialog.dismiss();
//							Toast.makeText(ReserveDetailActivity.this,"下单失败，请重试", Toast.LENGTH_SHORT).show();
//						}
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//				@Override
//				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
//					// TODO Auto-generated method stub
//					dialog.dismiss();
//					pay.setOnClickListener(ReserveDetailActivity.this);
//					Toast.makeText(ReserveDetailActivity.this,"下单失败，请重试", Toast.LENGTH_SHORT).show();
//				}
//
//			});
//			break;
		}

	}
}
