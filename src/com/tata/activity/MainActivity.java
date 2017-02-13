package com.tata.activity;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.Header;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.adapterAndListener.MyFragmentPagerAdapter;
import com.tata.fragment.PersonFragment;
import com.tata.fragment.ShareFragment;
import com.tata.fragment.ShopFragment;
import com.tata.fragment.TravelFragment;
import com.tata.utils.AppManager;
import com.tata.utils.Constants;
import com.tata.utils.MainApplication;
import com.tata.utils.ToastUtil;
import com.tata.version.Common;
import com.tata.version.DBOpenHelper;
import com.tata.version.DownloadReceiver;
import com.tata.view.MyDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private ViewPager viewPager;
	private LinearLayout travelLayout, shareLayout, shopLayout, personLayout;
	private ArrayList<Fragment> fragmentList;
	private TextView travel, share, shop, person;
	private TextView textView[] = new TextView[4];
	private long exitTime;
	private LocationClient mLocationClient;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppManager.getAppManager().addActivity(this);
		sp=getSharedPreferences("user", MODE_PRIVATE);
		findLocation();
		viewPager = (ViewPager) findViewById(R.id.fragmentPager);
		initView();
		initFragment();
	}

	private void initView() {
		travelLayout = (LinearLayout) findViewById(R.id.TravelLayout);
		shareLayout = (LinearLayout) findViewById(R.id.ShareLayout);
		shopLayout = (LinearLayout) findViewById(R.id.ShopLayout);
		personLayout = (LinearLayout) findViewById(R.id.PersonLayout);
		travelLayout.setOnClickListener(this);
		shareLayout.setOnClickListener(this);
		shopLayout.setOnClickListener(this);
		personLayout.setOnClickListener(this);
		travel = (TextView) findViewById(R.id.travel);
		share = (TextView) findViewById(R.id.share);
		shop = (TextView) findViewById(R.id.shop);
		person = (TextView) findViewById(R.id.person);
		textView[0] = travel;
		textView[1] = share;
		textView[2] = shop;
		textView[3] = person;
	}

	private void initFragment() {
		fragmentList = new ArrayList<Fragment>();
		Fragment firstFragment = new TravelFragment();
		Fragment secondFragment = new ShareFragment();
		Fragment thirdFragment = new ShopFragment();
		Fragment fourthFragment = new PersonFragment();
		fragmentList.add(firstFragment);
		fragmentList.add(secondFragment);
		fragmentList.add(thirdFragment);
		fragmentList.add(fourthFragment);

		// 给ViewPager设置适配器
		viewPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		viewPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
        viewPager.setOffscreenPageLimit(3);
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
			for (int i = 0; i < 4; i++) {
				if (i == arg0) {
					textView[arg0].setTextColor(Color.BLUE);
				} else {
					textView[i].setTextColor(Color.BLACK);
				}
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.TravelLayout:
			viewPager.setCurrentItem(0);
			break;
		case R.id.ShareLayout:
			viewPager.setCurrentItem(1);
			break;
		case R.id.ShopLayout:
			viewPager.setCurrentItem(2);
			break;
		case R.id.PersonLayout:
			viewPager.setCurrentItem(3);
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				ToastUtil.show(this, "再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				if(getSharedPreferences("user", MODE_PRIVATE).getBoolean("Updating", false)){
					 Dialog dialog = new AlertDialog.Builder(this).setTitle("确认退出？").setMessage("apk正在下载中")  
					            // 设置内容  
					            .setPositiveButton("确认",// 设置确定按钮  
					                    new DialogInterface.OnClickListener() {  
					                        public void onClick(DialogInterface dialog,  
					                                int which) { 
					            					File dir = Environment.getExternalStorageDirectory();//文件保存目录
					            					File file=new File(dir, Constants.versionName);
					            					if (file.exists()) {
														file.delete();
													}
					            					new DBOpenHelper(MainActivity.this).getWritableDatabase().execSQL("DROP TABLE IF EXISTS filedownlog");
					            					getSharedPreferences("user", MODE_PRIVATE).edit().putBoolean("Updating", false).commit();
					            					DownloadReceiver.mNotificationManager.cancel(DownloadReceiver.id);
					            					AppManager.getAppManager().AppExit(MainActivity.this);
					            				}
					                    })  
					            .setNegativeButton("取消",  
					                    new DialogInterface.OnClickListener() {  
					                        public void onClick(DialogInterface dialog,  
					                                int whichButton) {  
					                            // 点击"取消"按钮之后退出程序  
					                        }  
					                    }).create();// 创建  
					    // 显示对话框  
					    dialog.show();  
				}else {
					AppManager.getAppManager().AppExit(MainActivity.this);
				}
				}
//				AsyncHttpClient httpClient=new AsyncHttpClient();
//				RequestParams params=new RequestParams();
//				String url="http://120.24.254.127/tata/userExit";
//				params.put("phoneNumber", sp.getString("phoneNumber",""));
//				httpClient.post(url, params, new TextHttpResponseHandler() {
//					
//					@Override
//					public void onSuccess(int arg0, Header[] arg1, String arg2) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
			}
//		}
		return true;

	}

	private void findLocation() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		mLocationClient.registerLocationListener(new MyLocationListener());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}
	
	public class MyLocationListener implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer sb = new StringBuffer(256);
			sb.append(location.getAddrStr());
			String place=sb.toString();
			if(place.substring(0, 2).equals("中国")){
				MainApplication.location=place.substring(2);
			}else {
				MainApplication.location=place;
			}
			MainApplication.location_city=location.getCity();
			MainApplication.Lng=location.getLongitude()+"";
			MainApplication.Lat=location.getLatitude()+"";
			System.out.println(MainApplication.Lat);
			System.out.println(MainApplication.Lng);
			if(!MainApplication.location.equals("")){
				mLocationClient.stop();
			}
		}
	}
	protected void onStop() {
		super.onStop();
		if(mLocationClient.isStarted()){
		mLocationClient.stop();
		}
	}
	protected void onPause() {
		super.onPause();
		if(mLocationClient.isStarted()){
			mLocationClient.stop();
			}
	}
	
}
