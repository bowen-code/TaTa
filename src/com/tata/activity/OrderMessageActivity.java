package com.tata.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.activity.MyReserveActivity.MyOnPageChangeListener;
import com.tata.adapterAndListener.CurrentPrivateAdapter;
import com.tata.adapterAndListener.GuideAdapter;
import com.tata.adapterAndListener.HistoryPrivateAdapter;
import com.tata.adapterAndListener.ReservePayAdapter;
import com.tata.adapterAndListener.ReservePingAdapter;
import com.tata.adapterAndListener.ReserveTravelAdapter;
import com.tata.bean.PrivateOrder;
import com.tata.bean.Reserve;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class OrderMessageActivity extends BaseActivity {
	private TextView nowOrder, history;
	private ViewPager viewPager;
	private TextView[] textView = new TextView[2];
	private List<View> list = new ArrayList<View>();
	private ListView nowOrderListView, historyListView;
	private List<PrivateOrder> nowOrderData = new ArrayList<PrivateOrder>();
	private List<PrivateOrder> historyData = new ArrayList<PrivateOrder>();
	SharedPreferences sp;
	private Dialog dialog;
	protected List<PrivateOrder> data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopText("ÎÒµÄ¶©ÖÆ");
		setCenterView(R.layout.activity_order_message);
		sp = getSharedPreferences("user", MODE_PRIVATE);
		dialog = new MyDialog(this).myDialog();
		dialog.show();
		initReserve();
		init();
	}


	private void initReserve() {
		// TODO Auto-generated method stub
		
	}
	private void init() {
		nowOrder = (TextView) findViewById(R.id.nowOrder);
		history = (TextView) findViewById(R.id.history);
		viewPager = (ViewPager)findViewById(R.id.fragmentPager);
		nowOrder.setOnClickListener(this);
		history.setOnClickListener(this);
		textView[0] = nowOrder;
		textView[1] = history;
		LayoutInflater inflater = getLayoutInflater();
		View noworderView = inflater.inflate(R.layout.noworder, null);
		View historyorderlView = inflater.inflate(R.layout.historyorder, null);
		nowOrderListView = (ListView) noworderView.findViewById(R.id.nowOrder);
		historyListView = (ListView) historyorderlView
				.findViewById(R.id.historyOrder);
		list.add(noworderView);
		list.add(historyorderlView);
		viewPager.setAdapter(new GuideAdapter(list));
		viewPager.setOffscreenPageLimit(3);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		String url = "http://120.24.254.127/tata/data/getPrivateIndent.php";
		AsyncHttpClient httpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("phoneNumber", sp.getString("phoneNumber", ""));
		httpClient.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				System.out.println(arg2);
				data=jsonTools.getMyPrivateOrder(arg2);
				for (int i = 0; i < data.size(); i++) {
					int state=data.get(i).getPrivateIndentState();
					if(state==1||state==2){
						nowOrderData.add(data.get(i));
					}else {
						historyData.add(data.get(i));
					}
				}
				nowOrderListView.setAdapter(new CurrentPrivateAdapter(nowOrderData,
						OrderMessageActivity.this));
				historyListView.setAdapter(new HistoryPrivateAdapter(
						historyData, OrderMessageActivity.this));
				dialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nowOrder:
			viewPager.setCurrentItem(0);
			break;
		case R.id.history:
			viewPager.setCurrentItem(1);
			break;
		}
	}
	class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			for (int i = 0; i < 2; i++) {
				if (i == arg0) {
					textView[arg0].setBackgroundColor(Color.WHITE);
					textView[arg0].setTextColor(getResources().getColor(
							R.color.headColor));
				} else {
					textView[i].setTextColor(Color.WHITE);
					textView[i].setBackgroundColor(getResources().getColor(
							R.color.headColor));
				}
			}
		}
	}
    
}
