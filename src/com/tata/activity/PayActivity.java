package com.tata.activity;

import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pingplusplus.android.PaymentActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.bean.NearTravel;
import com.tata.utils.AppManager;
import com.tata.utils.ToastUtil;
import com.tata.utils.Tools;
import com.tata.view.MyDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PayActivity extends BaseActivity {

	private ImageView[] image = new ImageView[5];
	private RelativeLayout[] layout = new RelativeLayout[5];
	private Bitmap checkBitmap;
	private Bitmap uncheckBitmap;
	private String channel = "alipay";
	private int amount;
	private TextView price, reserveNumber, productIDTextView;
	private TextView pay, titleTextView, payMoney, ticketNum;
	private String productID;
	private String title;
	private int num;
	private int reserveID;
	private Dialog dialog;
	public static final String URL = "http://120.24.254.127/tata/data/MN/pay.php";
	private static final int REQUEST_CODE_PAYMENT = 1;
	private boolean isDirectPay = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.activity_pay, null);
		Intent intent = getIntent();
		productID = intent.getStringExtra("productID");
		amount = intent.getIntExtra("amount", 0);
		num = intent.getIntExtra("num", 0);
		reserveID = intent.getIntExtra("reserveID", 0);
		title = intent.getStringExtra("title");
		isDirectPay = intent.getBooleanExtra("isDirectPay", true);
		init(view);
	}

	private void init(View view) {
		setTopText("订单支付");
		setCenterView(view);
		image[0] = (ImageView) view.findViewById(R.id.zfbImg);
		image[1] = (ImageView) view.findViewById(R.id.wxImg);
		image[2] = (ImageView) view.findViewById(R.id.bdImg);
		image[3] = (ImageView) view.findViewById(R.id.jsImg);
		image[4] = (ImageView) view.findViewById(R.id.cftImg);
		layout[0] = (RelativeLayout) view.findViewById(R.id.zfb);
		layout[0].setOnClickListener(this);
		layout[1] = (RelativeLayout) view.findViewById(R.id.wx);
		layout[1].setOnClickListener(this);
		layout[2] = (RelativeLayout) view.findViewById(R.id.bd);
		layout[2].setOnClickListener(this);
		layout[3] = (RelativeLayout) view.findViewById(R.id.jsyh);
		layout[3].setOnClickListener(this);
		layout[4] = (RelativeLayout) view.findViewById(R.id.jdpay_wap);
		layout[4].setOnClickListener(this);
		checkBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.checkbox_checked);
		uncheckBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.checkbox_uncheck);
		price = (TextView) view.findViewById(R.id.price);
		price.setText("￥" + amount);
		reserveNumber = (TextView) view.findViewById(R.id.reserveNumber);
		reserveNumber.setText(reserveID + "");
		titleTextView = (TextView) view.findViewById(R.id.title);
		titleTextView.setText("游玩内容：" + title);
		payMoney = (TextView) view.findViewById(R.id.payMoney);
		payMoney.setText("￥" + amount);
		pay = (TextView) view.findViewById(R.id.pay);
		pay.setOnClickListener(this);
		ticketNum = (TextView) findViewById(R.id.ticketNum);
		ticketNum.setText("票数：" + num + "张");
		productIDTextView = (TextView) findViewById(R.id.productID);
		productIDTextView.setText("产品编号：" + productID);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zfb:
			image[0].setImageBitmap(checkBitmap);
			image[1].setImageBitmap(uncheckBitmap);
			image[2].setImageBitmap(uncheckBitmap);
			image[3].setImageBitmap(uncheckBitmap);
			image[4].setImageBitmap(uncheckBitmap);
			channel = "alipay";
			break;
		case R.id.wx:
			image[0].setImageBitmap(uncheckBitmap);
			image[1].setImageBitmap(checkBitmap);
			image[2].setImageBitmap(uncheckBitmap);
			image[3].setImageBitmap(uncheckBitmap);
			image[4].setImageBitmap(uncheckBitmap);
			channel = "wx";
			break;
		case R.id.bd:
			image[0].setImageBitmap(uncheckBitmap);
			image[1].setImageBitmap(uncheckBitmap);
			image[2].setImageBitmap(checkBitmap);
			image[3].setImageBitmap(uncheckBitmap);
			image[4].setImageBitmap(uncheckBitmap);
			channel = "bfb";
			break;
		case R.id.jsyh:
			image[0].setImageBitmap(uncheckBitmap);
			image[1].setImageBitmap(uncheckBitmap);
			image[2].setImageBitmap(uncheckBitmap);
			image[3].setImageBitmap(checkBitmap);
			image[4].setImageBitmap(uncheckBitmap);
			channel = "upacp";
			break;
		case R.id.jdpay_wap:
			image[0].setImageBitmap(uncheckBitmap);
			image[1].setImageBitmap(uncheckBitmap);
			image[2].setImageBitmap(uncheckBitmap);
			image[3].setImageBitmap(uncheckBitmap);
			image[4].setImageBitmap(checkBitmap);
			channel = "jdpay_wap";
			break;
		case R.id.pay:
			pay.setOnClickListener(null);
			dialog = new MyDialog(this).loadDialog1();
			dialog.show();
			AsyncHttpClient httpClient = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("channel", channel);
			params.put("amount", amount);
			params.put("indentID", reserveID);
			httpClient.post(URL, params, new TextHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					// TODO Auto-generated method stub
					System.out.println(arg2);
					dialog.dismiss();
					if (arg2 == null) {
						ToastUtil.show(PayActivity.this, "支付失败，请重试");
						pay.setOnClickListener(PayActivity.this);
						return;
					}
					Intent intent = new Intent();
					String packageName = getPackageName();
					ComponentName componentName = new ComponentName(
							packageName, packageName
									+ ".wxapi.WXPayEntryActivity");
					intent.setComponent(componentName);
					intent.putExtra(PaymentActivity.EXTRA_CHARGE, arg2);
					startActivityForResult(intent, REQUEST_CODE_PAYMENT);
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					System.out.println(arg2);
					dialog.dismiss();
					ToastUtil.show(PayActivity.this, "支付失败，请重试");
					pay.setOnClickListener(PayActivity.this);
				}
			});
			break;

		}
	}

	/**
	 * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。 最终支付成功根据异步通知为准
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		pay.setOnClickListener(this);

		// 支付页面返回处理
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getExtras().getString("pay_result");
				if (result.equals("success")) {
					ToastUtil.show(this, "支付成功");
					Intent intent = new Intent(this, MyReserveActivity.class);
					intent.putExtra("success", true);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_left_in,
							R.anim.slide_left_out);
					if (isDirectPay) {
						AppManager.getAppManager().finishLastThreeActivitys();
					} else {
						AppManager.getAppManager().finishLastTwoActivitys();
					}
				} else if (result.equals("fail")) {
					ToastUtil.show(this, "支付失败，请重试");
				} else if (result.equals("cancel")) {
					ToastUtil.show(this, "支付失败，手机没安装该支付软件");
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		final AlertDialog dlg3 = new AlertDialog.Builder(this).create();
		dlg3.show();
		Window window3 = dlg3.getWindow();
		window3.setContentView(R.layout.unpay_window);
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
						// TODO Auto-generated method stub
						if (isDirectPay) {
							AppManager.getAppManager()
									.finishLastThreeActivitys();
							Intent intent = new Intent(PayActivity.this, MyReserveActivity.class);
							intent.putExtra("success", false);
							startActivity(intent);
							overridePendingTransition(R.anim.slide_left_in,
									R.anim.slide_left_out);
						} else {
							finish();
							overridePendingTransition(0, R.anim.slide_right_out);
						}
					}
				});
	}
}
