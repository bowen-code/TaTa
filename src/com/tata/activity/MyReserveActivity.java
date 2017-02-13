package com.tata.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.R.color;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.adapterAndListener.GuideAdapter;
import com.tata.adapterAndListener.ReservePayAdapter;
import com.tata.adapterAndListener.ReservePingAdapter;
import com.tata.adapterAndListener.ReserveTravelAdapter;
import com.tata.bean.Reserve;
import com.tata.utils.ToastUtil;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MyReserveActivity extends BaseActivity implements OnItemClickListener{

	private TextView toPay, toTravel, toPing;
	private ViewPager viewPager;
	private TextView[] textView = new TextView[3];
	private List<View> list = new ArrayList<View>();
	private ListView topayListView, totravelListView, topingListView;
	private List<Reserve> topayData = new ArrayList<Reserve>();
	private List<Reserve> totravelData = new ArrayList<Reserve>();
	private List<Reserve> TravelledData = new ArrayList<Reserve>();
	SharedPreferences sp;
	private Dialog dialog;
	private boolean success;
	private boolean isRefund;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.activity_my_reserve,
				null);
		setTopText("¶©µ¥");
		sp = getSharedPreferences("user", MODE_PRIVATE);
		success=getIntent().getBooleanExtra("success", false);
		isRefund=getIntent().getBooleanExtra("isRefund", false);
		dialog = new MyDialog(this).myDialog();
		init(view);

	}


	private void init(View view) {
		setCenterView(view);
		toPay = (TextView) view.findViewById(R.id.toPay);
		toTravel = (TextView) view.findViewById(R.id.toTravel);
		toPing = (TextView) view.findViewById(R.id.toPing);
		viewPager = (ViewPager) view.findViewById(R.id.fragmentPager);
		toPay.setOnClickListener(this);
		toTravel.setOnClickListener(this);
		toPing.setOnClickListener(this);
		textView[0] = toPay;
		textView[1] = toTravel;
		textView[2] = toPing;
		LayoutInflater inflater = getLayoutInflater();
		View topayView = inflater.inflate(R.layout.topay, null);
		topayListView = (ListView) topayView.findViewById(R.id.payList);
		topayListView.setOnItemClickListener(this);
		View totravelView = inflater.inflate(R.layout.totravel, null);
		totravelListView = (ListView) totravelView
				.findViewById(R.id.travelList);
		View topingView = inflater.inflate(R.layout.toping, null);
		topingListView = (ListView) topingView.findViewById(R.id.pingList);

		list.add(topayView);
		list.add(totravelView);
		list.add(topingView);
		viewPager.setAdapter(new GuideAdapter(list));
		if(success){
		viewPager.setCurrentItem(1);
		textView[0].setTextColor(Color.WHITE);
		textView[0].setBackgroundColor(getResources().getColor(
				R.color.headColor));
		textView[1].setBackgroundColor(Color.WHITE);
		textView[1].setTextColor(getResources().getColor(
				R.color.headColor));
		}
		if(isRefund){
			viewPager.setCurrentItem(2);
			textView[0].setTextColor(Color.WHITE);
			textView[0].setBackgroundColor(getResources().getColor(
					R.color.headColor));
			textView[2].setBackgroundColor(Color.WHITE);
			textView[2].setTextColor(getResources().getColor(
					R.color.headColor));
		}
		viewPager.setOffscreenPageLimit(3);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		dialog.show();
		String url = "http://120.24.254.127/tata/data/getindent.php";
		AsyncHttpClient httpClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("phoneNumber", sp.getString("phoneNumber", ""));
		httpClient.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				System.out.println(arg2);
				topayData = jsonTools.getNoPayReserve(arg2);
				totravelData = jsonTools.getNoTravelReserve(arg2);
				TravelledData = jsonTools.getTravelledReserve(arg2);
				topayListView.setAdapter(new ReservePayAdapter(topayData,
						MyReserveActivity.this));
				totravelListView.setAdapter(new ReserveTravelAdapter(
						totravelData, MyReserveActivity.this));
				topingListView.setAdapter(new ReservePingAdapter(TravelledData,
						MyReserveActivity.this));
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
		case R.id.toPay:
			viewPager.setCurrentItem(0);
			break;
		case R.id.toTravel:
			viewPager.setCurrentItem(1);
			break;
		case R.id.toPing:
			viewPager.setCurrentItem(2);
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
			for (int i = 0; i < 3; i++) {
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

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, ReserveDetailActivity.class);
		intent.putExtra("reserve",topayData.get(position));
		startActivity(intent);
	}
}
