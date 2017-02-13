package com.tata.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tata.R;
import com.tata.activity.MyReserveActivity.MyOnPageChangeListener;
import com.tata.adapterAndListener.CollectShareAdapter;
import com.tata.adapterAndListener.FoucsAdapter;
import com.tata.adapterAndListener.GuideAdapter;
import com.tata.adapterAndListener.MyCollectAdapter;
import com.tata.adapterAndListener.MyShareAdapter;
import com.tata.adapterAndListener.NearAdapter;
import com.tata.adapterAndListener.ReservePayAdapter;
import com.tata.adapterAndListener.ReservePingAdapter;
import com.tata.adapterAndListener.ReserveTravelAdapter;
import com.tata.adapterAndListener.ShareAdapter;
import com.tata.bean.FoucsInfo;
import com.tata.bean.NearTravel;
import com.tata.bean.Reserve;
import com.tata.bean.ShareMessage;
import com.tata.utils.Constants;
import com.tata.utils.ToastUtil;
import com.tata.utils.jsonTools;
import com.tata.view.CollectSharePullTask;
import com.tata.view.MyDialog;
import com.tata.view.NearPullTask;
import com.tata.view.PullToRefreshListView;
import com.tata.view.PullToRefreshView;
import com.tata.view.PullToRefreshBase.OnRefreshListener;
import com.tata.view.PullToRefreshView.OnFooterRefreshListener;
import com.tata.view.PullToRefreshView.OnHeaderRefreshListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCollectActivity extends BaseActivity implements OnHeaderRefreshListener, OnFooterRefreshListener{
	private PullToRefreshListView mPullToRefreshListView_share;
	private TextView myWrite, myCollect;
	private RelativeLayout myCollectLayout, myWriteLayout;
	private ViewPager viewPager;
	private TextView[] textView = new TextView[2];
	private List<View> list = new ArrayList<View>();
	private ListView myWriteListView, myCollectListView, myFoucsListView;
	private List<ShareMessage> myWriteData = new ArrayList<ShareMessage>();
	private List<NearTravel> myCollectData = new ArrayList<NearTravel>();
//	private List<FoucsInfo> myFoucsData = new ArrayList<FoucsInfo>();
	private DisplayImageOptions options;
	private SharedPreferences mySharedPreferences;
	private String phoneNumber;
	private View mywriteView;
	private View mycollectView;
	private Dialog dialog;
	protected PopupWindow popupWindow;
	protected NearAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopText("我的收藏");
		setCenterView(R.layout.activity_mycollect);
		initOption();
		Constants.collect_product=1;
		Constants.collect_share=1;
		Constants.mydeletecollectShare.clear();
		mySharedPreferences = getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		phoneNumber = mySharedPreferences.getString("phoneNumber", "");
		init();
		dialog = new MyDialog(this).myDialog();
		dialog.show();
	}
	
//	OnRefreshListener mOnrefreshListener_product = new OnRefreshListener() {
//		public void onRefresh() {
//			NearPullTask pullTask = new NearPullTask(true,MyCollectActivity.this,
//					mPullToRefreshView,
//					0, adapter, myCollectData,
//					phoneNumber);
//			pullTask.getShare();
//		}
//	};
	OnRefreshListener mOnrefreshListener_share = new OnRefreshListener() {
		public void onRefresh() {
			CollectSharePullTask pullTask = new CollectSharePullTask(MyCollectActivity.this,
					mPullToRefreshListView_share,
					mPullToRefreshListView_share.getRefreshType(), share_adapter, myWriteData,
					phoneNumber);
			pullTask.getShare();
		}
	};
	protected CollectShareAdapter share_adapter;
	private PullToRefreshView mPullToRefreshView;

	private void init() {
		myCollectLayout=(RelativeLayout) findViewById(R.id.myCollectLayout);
		myWriteLayout=(RelativeLayout) findViewById(R.id.myWriteLayout);
		myCollectLayout.setOnClickListener(this);
		myWriteLayout.setOnClickListener(this);
		myWrite = (TextView)findViewById(R.id.myWrite);
		myCollect = (TextView)findViewById(R.id.myCollect);
		viewPager = (ViewPager)findViewById(R.id.fragmentPager);
		myWrite.setOnClickListener(this);
		myCollect.setOnClickListener(this);
		textView[1] = myWrite;
		textView[0] = myCollect;
		LayoutInflater inflater = getLayoutInflater();
		mywriteView = inflater.inflate(R.layout.youji_share, null);
		mPullToRefreshListView_share = (PullToRefreshListView) mywriteView.findViewById(R.id.youjiList);
		myWriteListView=mPullToRefreshListView_share.getRefreshableView();
		mycollectView = inflater.inflate(R.layout.collect_product, null);
		mPullToRefreshView = (PullToRefreshView) mycollectView.findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		myCollectListView = (ListView) mycollectView
				.findViewById(R.id.travelList);
		myCollectListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NearTravel data=myCollectData.get(position);
				Intent intent = new Intent(MyCollectActivity.this, ProductActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("product", data);
				intent.putExtras(bundle);
				intent.putExtra("isCollect",true);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);			
			}
		});
		myCollectListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			private Button delete;
			private Button cancel;

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				
				View contentView;
				contentView = LayoutInflater.from(MyCollectActivity.this).inflate(
						R.layout.my_popwinodw, null);
				popupWindow = new PopupWindow(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				popupWindow.setFocusable(true);
				popupWindow.setOutsideTouchable(true);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.setContentView(contentView);
				popupWindow.showAtLocation(view.getRootView(), Gravity.CENTER, 0,
						0);
				contentView.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popupWindow.dismiss();
					}
				});
				delete = (Button) contentView.findViewById(R.id.delete);
				delete.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AsyncHttpClient httpClient=new AsyncHttpClient();
						RequestParams params=new RequestParams();
						params.put("phoneNumber", phoneNumber);
						params.put("productID", myCollectData.get(position).getProductID());
						String url="http://120.24.254.127/tata/data/deletecollectproduct.php";
						httpClient.post(url, params, new TextHttpResponseHandler() {

							@Override
							public void onFailure(int arg0, Header[] arg1,
									String arg2, Throwable arg3) {
								// TODO Auto-generated method stub
								System.out.println(arg2);
								popupWindow.dismiss();
								ToastUtil.show(MyCollectActivity.this, "删除失败，请重试");
							}

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									String arg2) {
								// TODO Auto-generated method stub
								int code;
								try {
									code = new JSONObject(arg2).getInt("code");
									if(code==0){
										popupWindow.dismiss();
										ToastUtil.show(MyCollectActivity.this, "删除失败，请重试");
									}else if (code==1) {
										ToastUtil.show(MyCollectActivity.this, "删除成功");
										myCollectData.remove(position);
										popupWindow.dismiss();
										adapter.notifyDataSetChanged();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							
						});
					}
				});
				cancel = (Button) contentView.findViewById(R.id.cancel);
				cancel.setOnClickListener(MyCollectActivity.this);
				return false;
			}
		});
		list.add(mycollectView); 
		list.add(mywriteView);
		viewPager.setAdapter(new GuideAdapter(list));
		viewPager.setOffscreenPageLimit(3);
		dialog = new MyDialog(this).myDialog();
		AsyncHttpClient httpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("phoneNumber", phoneNumber);
		params.put("num",1);
		String url="http://120.24.254.127/tata/data/getCollect.php";
		httpClient.post(url, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				System.out.println(arg2);
				myWriteData=jsonTools.getMyCollectMessages(arg2);
				share_adapter=new CollectShareAdapter(myWriteData,
						MyCollectActivity.this,options);
				myWriteListView.setAdapter(share_adapter);
				mPullToRefreshListView_share.setOnRefreshListener(mOnrefreshListener_share);
				dialog.dismiss();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		String url2="http://120.24.254.127/tata/data/getMyProduct.php";
		httpClient.post(url2, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
//				myCollectData=jsonTools.getMyCollectMessages(arg2);
				System.out.println(arg2);
				myCollectData=jsonTools.getNearTravels(arg2);
				adapter=new NearAdapter(myCollectData,
						options,MyCollectActivity.this);
				myCollectListView.setAdapter(adapter);
//				mPullToRefreshListView_product
//				.setOnRefreshListener(mOnrefreshListener_product);
				dialog.dismiss();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
	}
	
	private void initOption() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.blank) // 设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.blank)// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.blank) // 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.myWrite:
			viewPager.setCurrentItem(1);
			break;
		case R.id.myCollect:
			viewPager.setCurrentItem(0);
			break;
		case R.id.myWriteLayout:
			viewPager.setCurrentItem(1);
			break;
		case R.id.myCollectLayout:
			viewPager.setCurrentItem(0);
			break;
		case R.id.cancel:
			popupWindow.dismiss();
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
					textView[arg0].setTextColor(getResources().getColor(
							R.color.headColor));
				} else {
					textView[i].setTextColor(Color.WHITE);
				}
			}
		}
	}

	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		NearPullTask pullTask = new NearPullTask(true,MyCollectActivity.this,
				mPullToRefreshView,
				0, adapter, myCollectData,
				phoneNumber);
		pullTask.getShare();
	}

	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		mPullToRefreshView.postDelayed(new Runnable() {

			public void run() {
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 1000);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}


}
