package com.tata.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.apache.http.HttpRequest;

import com.loopj.android.http.AsyncHttpClient;
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
import com.tata.fragment.TravelFragment;
import com.tata.utils.ACache;
import com.tata.utils.NetWorkHelper;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;
import com.tata.view.MyGridView;
import com.tata.view.PullToRefreshView;
import com.tata.view.PullToRefreshView.OnFooterRefreshListener;
import com.tata.view.PullToRefreshView.OnHeaderRefreshListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class HotActivity extends BaseActivity implements OnItemClickListener,
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private ViewPager viewPager;
	private ImageView[] imageViews = new ImageView[4];
	private ArrayList<View> advPics;
	private MyGridView gridView;
	private List<NearTravel> list=new ArrayList<NearTravel>();
	private List<NearTravel> showList=new ArrayList<NearTravel>();
	private MyAdapter adapter;
	private Dialog dialog;
	private DisplayImageOptions options;
	Timer mTimer;
	TimerTask mTask;
	int pageIndex = 1;
	boolean isTaskRun;
	private String phoneNumber;
	private int num = 1;
	String url = "http://120.24.254.127/tata/data/getProduct.php";
	private PullToRefreshView mPullToRefreshView;
	private EditText search;
    private TextView start;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopText("踏踏热门");
		View view = getLayoutInflater().inflate(R.layout.activity_hot, null);
		setCenterView(view);
		initOption();
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		phoneNumber = getSharedPreferences("user", Activity.MODE_PRIVATE)
				.getString("phoneNumber", "");
		init(view);
//		viewPager.setAdapter(new GuideAdapter(advPics));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

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
	protected String[] data;

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
		viewPager.setCurrentItem(pageIndex, false);// 取消动画
	}

	/**
	 * 停止定时任务
	 */
	private void stopTask() {
		// TODO Auto-generated method stub
		isTaskRun = false;
		mTimer.cancel();
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

	private void init(View view) {
		search=(EditText) findViewById(R.id.search);
		start=(TextView) findViewById(R.id.start);
		start.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String destination=search.getText().toString().trim();
				if(!TextUtils.equals(destination, "")){
				Intent intent = new Intent(HotActivity.this, SearchActivity.class);
				intent.putExtra("destination", destination);
				intent.putExtra("typeID",1);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_left_out);
				}
			}
		});
		viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		imageViews[0] = (ImageView) view.findViewById(R.id.img1);
		imageViews[1] = (ImageView) view.findViewById(R.id.img2);
		imageViews[2] = (ImageView) view.findViewById(R.id.img3);
		imageViews[3] = (ImageView) view.findViewById(R.id.img4);
		advPics = new ArrayList<View>();
		final ImageView view1 = new ImageView(this);
		view1.setScaleType(ScaleType.FIT_XY);
		advPics.add(view1);
		final ImageView view2 = new ImageView(this);
		view2.setScaleType(ScaleType.FIT_XY);
		advPics.add(view2);
		final ImageView view3 = new ImageView(this);
		view3.setScaleType(ScaleType.FIT_XY);
		advPics.add(view3);
		final ImageView view4 = new ImageView(this);
		view4.setScaleType(ScaleType.FIT_XY);
		advPics.add(view4);
		AsyncHttpClient httpClient=new AsyncHttpClient();
		httpClient.get("http://120.24.254.127/tata/data/getHotImg.php",new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				data=jsonTools.getImgUrls(arg2);
				viewPager.setAdapter(new GuideAdapter(advPics));
				ImageLoader.getInstance().displayImage(data[0], view1,
						options);
				ImageLoader.getInstance().displayImage(data[1], view2,
						options);
				ImageLoader.getInstance().displayImage(data[2], view3,
						options);
				ImageLoader.getInstance().displayImage(data[3], view4,
						options);
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		gridView = (MyGridView) findViewById(R.id.grid_view);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if (NetWorkHelper.checkNetState(this)) {
			dialog = new MyDialog(this).myDialog();
			dialog.show();
			// String url = "http://120.24.254.127/tata/getProduct";
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("typeID", 1);
			params.put("num", 1);
			params.put("phoneNumber", phoneNumber);
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
					dialog.dismiss();
					System.out.println(arg2);
					list = jsonTools.getNearTravels(arg2);
					adapter = new HotActivity.MyAdapter(list, HotActivity.this);
					gridView.setAdapter(adapter);
					gridView.setOnItemClickListener(HotActivity.this);
					ACache mCache = ACache.get(HotActivity.this);
					mCache.put("HotActivity", arg2);
				}

			});
		} else {
			ACache mCache = ACache.get(HotActivity.this);
			String jsonString = mCache.getAsString("HotActivity");
			list = jsonTools.getNearTravels(jsonString);
			adapter = new MyAdapter(list, HotActivity.this);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(HotActivity.this);
		}
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

	class MyAdapter extends BaseAdapter {

		private List<NearTravel> list;
		private Context context = null;
		private LayoutInflater inflater = null;

		public MyAdapter(List<NearTravel> data, Context context) {
			super();
			this.context = context;
			list = data;
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		private class Holder {

			TextView tv = null;
			ImageView img = null;

			public TextView getTv() {
				return tv;
			}

			public void setTv(TextView tv) {
				this.tv = tv;
			}

			public ImageView getImg() {
				return img;
			}

			public void setImg(ImageView img) {
				this.img = img;
			}

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// 获得holder以及holder对象中tv和img对象的实例
			NearTravel item = list.get(position);
			Holder holder;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.gridview_item, null);
				holder = new Holder();
				holder.tv = (TextView) convertView
						.findViewById(R.id.gridview_text);
				holder.img = (ImageView) convertView
						.findViewById(R.id.gridview_img);

				convertView.setTag(holder);

			} else {
				holder = (Holder) convertView.getTag();

			}
			// 为holder中的tv和img设置内容
			holder.tv.setText(item.getTitle());
			ImageLoader.getInstance().displayImage(item.getImages()[0],
					holder.img, options);
			// 注意 默认为返回null,必须得返回convertView视图
			return convertView;
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (NetWorkHelper.checkNetState(this)) {
			NearTravel data = list.get(position);
			Intent intent = new Intent(this, ProductActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("product", data);
			intent.putExtras(bundle);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
		} else {
			Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
		}
	}

	private class GetHeaderDataTask extends AsyncTask<Void, Void, String[]> {

		protected String[] doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			// rv.finishRefresh();

			super.onPostExecute(result);
		}

	}

	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		num++;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("typeID", 1);
		params.put("num", num);
		params.put("phoneNumber", phoneNumber);
		client.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				mPullToRefreshView.onFooterRefreshComplete();// TODO
																// Auto-generated
																// method stub
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) { // TODO
																			// Auto-generated
																			// method
																			// stub
				List<NearTravel> addlist = jsonTools.getNearTravels(arg2);
				for (int i = 0; i < addlist.size(); i++) {
					list.add(addlist.get(i));
				}
				if (addlist.size() != 0) {
					adapter.notifyDataSetChanged();

				}
				mPullToRefreshView.onFooterRefreshComplete();
			}

		});
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
