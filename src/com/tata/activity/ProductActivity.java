package com.tata.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tata.R;
import com.tata.adapterAndListener.GuideAdapter;
import com.tata.bean.NearTravel;
import com.tata.bean.Route;
import com.tata.utils.Constants;
import com.tata.utils.MainApplication;
import com.tata.utils.ToastUtil;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class ProductActivity extends Activity implements
		android.view.View.OnClickListener {

	private LinearLayout detailRoute, up, down;
	private RelativeLayout rule, detailPrice;
	private TextView title, yuanPriceType, salePriceType, yuanPrice, salePrice,
			time, route1, remarkp, moreDay, day1, day2, day3, day4, day5, day6,
			day7;
	private Intent intent;
	private ViewPager productBg;
	private NearTravel data;
	private ArrayList<View> advPics;
	private Dialog dialog;
	private ImageView[] imageViews = new ImageView[3];
	private DisplayImageOptions options;
	private ImageView picture,collect;
	Timer mTimer;
	TimerTask mTask;
	int pageIndex = 1;
	boolean isTaskRun;
	private TextView[] days;
	private ArrayList<String> timeData;
	private SharedPreferences sp;
	private boolean isCollect;
	public static List<Route> routeData;
    int selectDay=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		initOption();
		isCollect=getIntent().getBooleanExtra("isCollect", false);
		if(getIntent().getBooleanExtra("isAgainProduct",false)){
			dialog=new MyDialog(this).myDialog();
			dialog.show();
			AsyncHttpClient httpClient=new AsyncHttpClient();
			RequestParams params=new RequestParams();
			params.put("productID", getIntent().getStringExtra("productID"));
			httpClient.post("http://120.24.254.127/tata/data/getproductshow.php", params, new TextHttpResponseHandler() {
				
				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					// TODO Auto-generated method stub
					System.out.println(arg2);
					data=jsonTools.getAgainProduct(arg2);
					routeData = data.getRouteList();
					initView();
					dialog.dismiss();
				}
				
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					// TODO Auto-generated method stub
					System.out.println(arg2);
					dialog.dismiss();
				}
			});
		}else {
			data = (NearTravel) getIntent().getSerializableExtra("product");
			routeData = data.getRouteList();
			initView();
		}
		// dialog = new MyDialog(this).myDialog();
		Constants.startTime="";
//		initOption();
		sp=getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
	}

	private void initOption() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.blank) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.blank)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.blank) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)// 设置图片的解码类型//
				// .decodingOptions(android.graphics.BitmapFactory.Options
				// decodingOptions)//设置图片的解码配置
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillis为你设置的下载前的延迟时间
				// 设置图片加入缓存前，对bitmap进行设置
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成

	}

	private void initView() {
		collect=(ImageView) findViewById(R.id.collect);
		String id=data.getProductID();
		System.out.println(MainApplication.collectProducts.toString());
		for (int i = 0; i < MainApplication.collectProducts.size(); i++) {
			if(id.equals(MainApplication.collectProducts.get(i))){
//				collect.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.collected));
				isCollect=true;
				break;
			}
		}
		if(isCollect){
			collect.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.collected));
		}
		up = (LinearLayout) findViewById(R.id.up);
		down = (LinearLayout) findViewById(R.id.down);
		picture = (ImageView) findViewById(R.id.picture);
		ImageLoader.getInstance().displayImage(data.getImages()[0], picture,
				options);
		time = (TextView) findViewById(R.id.time);
		title = (TextView) findViewById(R.id.title);
		moreDay = (TextView) findViewById(R.id.moreDay);
		day1 = (TextView) findViewById(R.id.first);
		day2 = (TextView) findViewById(R.id.second);
		day2.setOnClickListener(this);
		day3 = (TextView) findViewById(R.id.third);
		day4 = (TextView) findViewById(R.id.forth);
		day5 = (TextView) findViewById(R.id.fifth);
		day6 = (TextView) findViewById(R.id.sixth);
		day7 = (TextView) findViewById(R.id.seventh);
		moreDay = (TextView) findViewById(R.id.moreDay);
		day1.setOnClickListener(this);
		day3.setOnClickListener(this);
		day4.setOnClickListener(this);
		day5.setOnClickListener(this);
		day6.setOnClickListener(this);
		day7.setOnClickListener(this);
		days = new TextView[8];
		days[0] = day1;
		days[1] = day2;
		days[2] = day3;
		days[3] = day4;
		days[4] = day5;
		days[5] = day6;
		days[6] = day7;
		days[7] = moreDay;
		remarkp = (TextView) findViewById(R.id.remarkp);
		remarkp.setText(data.getRemarkp());
		route1 = (TextView) findViewById(R.id.route1);
		if(data.getRouteList().size()!=0)
		route1.setText(data.getRouteList().get(0).getGeneralize());
		yuanPriceType = (TextView) findViewById(R.id.yuanPriceType);
		yuanPrice = (TextView) findViewById(R.id.yuanPrice);
		salePriceType = (TextView) findViewById(R.id.salePriceType);
		salePrice = (TextView) findViewById(R.id.salePrice);
		title.setText(data.getTitle());
		if (data.getPriceTwo() != 0) {
			salePrice.setText("￥" + data.getPriceTwo());
			yuanPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			salePriceType.setVisibility(View.GONE);
			salePrice.setVisibility(View.GONE);
			yuanPriceType.setText("价格：");
		}
		yuanPrice.setText("￥" + data.getPrice());
		productBg = (ViewPager) findViewById(R.id.productBg);
		imageViews[0] = (ImageView) findViewById(R.id.img1);
		imageViews[1] = (ImageView) findViewById(R.id.img2);
		imageViews[2] = (ImageView) findViewById(R.id.img3);
		advPics = new ArrayList<View>();
		ImageView view1 = new ImageView(this);
		ImageLoader.getInstance().displayImage(data.getImages()[0], view1,
				options);
		view1.setScaleType(ScaleType.FIT_XY);
		advPics.add(view1);
		ImageView view2 = new ImageView(this);
		ImageLoader.getInstance().displayImage(data.getImages()[1], view2,
				options);
		view2.setScaleType(ScaleType.FIT_XY);
		advPics.add(view2);
		ImageView view3 = new ImageView(this);
		ImageLoader.getInstance().displayImage(data.getImages()[2], view3,
				options);
		view3.setScaleType(ScaleType.FIT_XY);
		advPics.add(view3);
		// dialog.show();
		productBg.setAdapter(new GuideAdapter(advPics));
		productBg.setOnPageChangeListener(new OnPageChangeListener() {

			/* 更新手动滑动时的位置 */
			public void onPageSelected(int position) {
				pageIndex = position;
				for (int i = 0; i < imageViews.length; i++) {
					imageViews[pageIndex]
							.setImageResource(R.drawable.ic_page_selected);
					if (pageIndex != i) {
						imageViews[i]
								.setImageResource(R.drawable.ic_page_normal);
					}

				}
			}

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			/* state: 0空闲，1是滑行中，2加载完毕 */
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				if (state == 0 && !isTaskRun) {
					setCurrentItem();
					startTask();
				} else if (state == 1 && isTaskRun)
					stopTask();
			}

		});
		detailRoute = (LinearLayout) findViewById(R.id.detailRoute);
		rule = (RelativeLayout) findViewById(R.id.rule);
		detailPrice = (RelativeLayout) findViewById(R.id.detailPrice);
		detailRoute.setOnClickListener(this);
		detailPrice.setOnClickListener(this);
		rule.setOnClickListener(this);
		dealDays();
	}

	private void dealDays() {
		timeData = new ArrayList<String>();
		for (int i = 0; i < data.getDays().size(); i++) {
			timeData.add(data.getDays().get(i).replace('-', '/'));
		}
		System.out.println(data.getDays().size());
		if (timeData.size() <= 4) {
			down.setVisibility(View.GONE);
			for (int i = 0; i < timeData.size(); i++) {
				days[i].setText(timeData.get(i));
			}
			for (int i = timeData.size(); i < 4; i++) {
				days[i].setVisibility(View.INVISIBLE);
			}
		} else if (timeData.size() < 8) {
			for (int i = 0; i < timeData.size(); i++) {
				days[i].setText(timeData.get(i));
			}
			for (int i = timeData.size(); i < 8; i++) {
				days[i].setVisibility(View.INVISIBLE);
			}
		} else if (timeData.size() == 8) {
			for (int i = 0; i < 8; i++) {
				days[i].setText(timeData.get(i));
			}
			moreDay.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					selectDay=8;
					moreDay.setBackgroundColor(getResources().getColor(R.color.gray));
					for (int i = 0; i < days.length; i++) {
						if(i!=7)
						days[i].setBackgroundColor(getResources().getColor(R.color.time_bg));
					}
					date=data.getDays().get(7);
//					ToastUtil.show(ProductActivity.this,timeData.get(7));
				}

			});
		} else {
			for (int i = 0; i < 7; i++) {
				days[i].setText(timeData.get(i));
			}
			moreDay.setText("……");
			moreDay.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
//					selectDay=8;
					Intent intent=new Intent(ProductActivity.this,MoreDate.class);
					intent.putExtra("date",timeData);
					startActivityForResult(intent, 1);
					overridePendingTransition(R.anim.slide_left_in,
							R.anim.slide_left_out);                       
				}

			});
		}
	}

	public void reserve(View v) {
		if (!sp.getString("phoneNumber", "").equals("")) {
			intent = new Intent(this, ReserveActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("product", data);
			intent.putExtras(bundle);
			intent.putExtra("date", date);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
		} else {
			ToastUtil.show(this, "请先登陆！");
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
		}
	}


	/**
	 * 开启定时任务
	 */
	private void startTask() {
		// TODO Auto-generated method stub
		isTaskRun = true;
		mTimer = new Timer();
		mTask = new TimerTask() {
			@Override
			public void run() {
				pageIndex++;
				mHandler.sendEmptyMessage(0);
			}
		};
		mTimer.schedule(mTask, 3 * 1000, 3 * 1000);// 这里设置自动切换的时间，单位是毫秒，2*1000表示2秒
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			setCurrentItem();
		}
	};
	private String date="";
	private PopupWindow pop;
	private LinearLayout ll_popup;

	/**
	 * 停止定时任务
	 */
	private void stopTask() {
		// TODO Auto-generated method stub
		isTaskRun = false;
		mTimer.cancel();
	}

	/**
	 * 处理Page的切换逻辑
	 */
	private void setCurrentItem() {
		if (isTaskRun) {
			if (pageIndex == 0) {
				pageIndex = 3;
			} else if (pageIndex == 4) {
				pageIndex = 0;
			}
		}
		if(productBg!=null)
		productBg.setCurrentItem(pageIndex, false);// 取消动画
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setCurrentItem();
		startTask();
	}

	@Override
	public void onPause() {
		super.onPause();
		stopTask();
	}

	public void collect(View v) {
		if(sp.getString("phoneNumber", "").equals("")){
			ToastUtil.show(this, "请先登陆！");
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			return;
		}
		AsyncHttpClient httpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("phoneNumber",sp.getString("phoneNumber", ""));
		params.put("productID",data.getProductID());
		String url;
		if(isCollect){
			System.out.println(sp.getString("phoneNumber", ""));
			System.out.println(data.getProductID());
			url="http://120.24.254.127/tata/data/deletecollectproduct.php";
			collect.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.collect));
		}else {
			url="http://120.24.254.127/tata/data/collectProduct.php";
			collect.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.collected));
		}
		httpClient.post(url, params,new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				System.out.println(arg2);
				try {
					JSONObject object=new JSONObject(arg2);
					if(object.getInt("code")==1){
//						System.out.println("kkkkkkk");
						if(!isCollect){
							MainApplication.collectProducts.add(data.getProductID());
						}else {
							MainApplication.collectProducts.remove(data.getProductID());
						}
						isCollect=!isCollect;
					}else if(object.getInt("code")==0){
						ToastUtil.show(ProductActivity.this, "网络出错，请稍后再试！");
					}else if (object.getInt("code")==2) {
						if(!MainApplication.collectProducts.contains(data.getProductID()))
						MainApplication.collectProducts.add(data.getProductID());
						isCollect=true;
						collect.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.collected));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				ToastUtil.show(ProductActivity.this, "网络出错，请稍后再试！");
			}
		});
		
	}

	public void call(View v) {
		pop = new PopupWindow(ProductActivity.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(
				ProductActivity.this, R.anim.activity_translate_in));
		pop.showAtLocation(v.getRootView(), Gravity.BOTTOM, 0, 0);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		final Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		bt1.setText(data.getServiceTel1());
		final Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		bt2.setText(data.getServiceTel2());
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}

		});
		bt1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+bt1.getText()));  
	            startActivity(intent); 
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+bt2.getText()));  
	            startActivity(intent); 
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
	}

	public void back(View v) {
		finish();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(0, R.anim.slide_right_out);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1 && !Constants.startTime.equals("")) {
			date=Constants.startTime.replace('/', '-');
			int index=timeData.indexOf(Constants.startTime);
			if(index<=7){
//				if(index!=selectDay-1){
				if(selectDay!=-1)
					days[selectDay-1].setBackgroundColor(getResources().getColor(R.color.time_bg));
				    days[index].setBackgroundColor(getResources().getColor(R.color.gray));
				    selectDay=index+1;
//				}
			}else if(selectDay!=-1){
				days[selectDay-1].setBackgroundColor(getResources().getColor(R.color.time_bg));
				selectDay=8;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.first:
			selectDay=1;
			day1.setBackgroundColor(getResources().getColor(R.color.gray));
			for (int i = 0; i < days.length; i++) {
				if(i!=0)
				days[i].setBackgroundColor(getResources().getColor(R.color.time_bg));
			}
			date=data.getDays().get(0);
//			ToastUtil.show(this,timeData.get(0));
			break;
		case R.id.second:
			selectDay=2;
			day2.setBackgroundColor(getResources().getColor(R.color.gray));
			for (int i = 0; i < days.length; i++) {
				if(i!=1)
				days[i].setBackgroundColor(getResources().getColor(R.color.time_bg));
			}
			date=data.getDays().get(1);
//			ToastUtil.show(this,timeData.get(1));
			break;
		case R.id.third:
			selectDay=3;
			day3.setBackgroundColor(getResources().getColor(R.color.gray));
			for (int i = 0; i < days.length; i++) {
				if(i!=2)
				days[i].setBackgroundColor(getResources().getColor(R.color.time_bg));
			}
			date=data.getDays().get(2);
//			ToastUtil.show(this,timeData.get(2));
			break;
		case R.id.forth:
			selectDay=4;
			day4.setBackgroundColor(getResources().getColor(R.color.gray));
			for (int i = 0; i < days.length; i++) {
				if(i!=3)
				days[i].setBackgroundColor(getResources().getColor(R.color.time_bg));
			}
			date=data.getDays().get(3);
//			ToastUtil.show(this,timeData.get(3));
			break;
		case R.id.fifth:
			selectDay=5;
			day5.setBackgroundColor(getResources().getColor(R.color.gray));
			for (int i = 0; i < days.length; i++) {
				if(i!=4)
				days[i].setBackgroundColor(getResources().getColor(R.color.time_bg));
			}
			date=data.getDays().get(4);
//			ToastUtil.show(this,timeData.get(4));
			break;
		case R.id.sixth:
			selectDay=6;
			day6.setBackgroundColor(getResources().getColor(R.color.gray));
			for (int i = 0; i < days.length; i++) {
				if(i!=5)
				days[i].setBackgroundColor(getResources().getColor(R.color.time_bg));
			}
			date=data.getDays().get(5);
//			ToastUtil.show(this,timeData.get(5));
			break;
		case R.id.seventh:
			selectDay=7;
			day7.setBackgroundColor(getResources().getColor(R.color.gray));
			for (int i = 0; i < days.length; i++) {
				if(i!=6)
				days[i].setBackgroundColor(getResources().getColor(R.color.time_bg));
			}
			date=data.getDays().get(6);
//			ToastUtil.show(this,timeData.get(6));
			break;
		case R.id.rule:
			intent = new Intent(this, RuleActivity.class);
			intent.putExtra("refund",data.getRefund());
			intent.putExtra("booking",data.getbooking());
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.detailPrice:
			intent = new Intent(this, DetailPriceActivity.class);
			intent.putExtra("include",data.getInclude());
			intent.putExtra("noContain",data.getNoContain());
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.detailRoute:
			intent = new Intent(this, DetailRouteActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;

		}
	}

	public void toDetailRoute(View v) {
		intent = new Intent(this, DetailRouteActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}
}
