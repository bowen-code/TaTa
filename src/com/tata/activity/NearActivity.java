package com.tata.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.adapterAndListener.NearAdapter;
import com.tata.bean.NearTravel;
import com.tata.utils.ACache;
import com.tata.utils.Constants;
import com.tata.utils.MainApplication;
import com.tata.utils.MyHttpClient;
import com.tata.utils.NetWorkHelper;
import com.tata.utils.jsonTools;
import com.tata.view.BanjiPullTask;
import com.tata.view.MyDialog;
import com.tata.view.NearPullTask;
import com.tata.view.PullTask;
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
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NearActivity extends BaseActivity implements OnItemClickListener,OnHeaderRefreshListener, OnFooterRefreshListener {

	private PullToRefreshListView mPullToRefreshListView;
	private ListView listView;
	private List<NearTravel> list;
	protected String result;
	private DisplayImageOptions options;
	private Dialog dialog;
	private String phoneNumber;
	private EditText search;
    private TextView start;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopText("周边游");
		setRightButton(MainApplication.location_city);
		View view = getLayoutInflater().inflate(R.layout.activity_near, null);
		setCenterView(view);
		dialog=new MyDialog(this).myDialog();
		Constants.NearNum=1;
		initOption();
		init(view);
		listView.setOnItemClickListener(this);
		phoneNumber=getSharedPreferences(
				"user", Activity.MODE_PRIVATE).getString("phoneNumber", "");
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

	
//	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
//		public void onRefresh() {
//			NearPullTask pullTask = new NearPullTask(NearActivity.this,
//					mPullToRefreshListView,
//					mPullToRefreshListView.getRefreshType(), adapter, list,
//					phoneNumber);
//			pullTask.getShare();
//		}
//	};
	protected NearAdapter adapter;
	private PullToRefreshView mPullToRefreshView;
	private void init(View view) {
		// TODO Auto-generated method stub
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		search=(EditText) findViewById(R.id.search);
		start=(TextView) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String destination=search.getText().toString().trim();
				if(!TextUtils.equals(destination, "")){
				Intent intent = new Intent(NearActivity.this, SearchActivity.class);
				intent.putExtra("destination", destination);
				intent.putExtra("typeID",2);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_left_out);
				}
			}
		});
//		mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.travelList);
		listView=(ListView) view.findViewById(R.id.travelList);
		list = new ArrayList<NearTravel>();
		if(NetWorkHelper.checkNetState(this)){
		dialog.show();
		String url = "http://120.24.254.127/tata/data/getProduct.php";
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("typeID", 2);
		params.put("num", 1);
		params.put("phoneNumber",phoneNumber);
		client.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				System.out.println(arg2);
                        list = jsonTools.getNearTravels(arg2);
                        dialog.dismiss();
                       adapter= new NearAdapter(list, options,
            					NearActivity.this);
            			listView.setAdapter(adapter);
//            			mPullToRefreshListView
//						.setOnRefreshListener(mOnrefreshListener);
//            			ACache mCache = ACache.get(NearActivity.this);
//            			mCache.put("nearActivity", arg2);
			}

		});
		}else {
//			ACache mCache = ACache.get(NearActivity.this);
//			String jsonString=mCache.getAsString("nearActivity");
//			list = jsonTools.getNearTravels(jsonString);
//			 adapter= new NearAdapter(list, options,
// 					NearActivity.this);
// 			listView.setAdapter(adapter);
//			mPullToRefreshListView
//			.setOnRefreshListener(mOnrefreshListener);
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(NetWorkHelper.checkNetState(this)){
		NearTravel data=list.get(position);
		Intent intent = new Intent(this, ProductActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("product", data);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}else {
		Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
	}
	}

	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		NearPullTask pullTask = new NearPullTask(NearActivity.this,
				mPullToRefreshView,
				0, adapter, list,
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

}
