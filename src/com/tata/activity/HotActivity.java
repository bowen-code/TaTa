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
		setTopText("̤̤����");
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

			/* �����ֶ�����ʱ��λ�� */
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

			/* state: 0���У�1�ǻ����У�2������� */
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
	 * ������ʱ����
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
		mTimer.schedule(mTask, 3 * 1000, 3 * 1000);// ���������Զ��л���ʱ�䣬��λ�Ǻ��룬2*1000��ʾ2��
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
	 * ����Page���л��߼�
	 */
	private void setCurrentItem() {
		if (isTaskRun) {
			if (pageIndex == 0) {
				pageIndex = 3;
			} else if (pageIndex == 4) {
				pageIndex = 0;
			}
		}
		viewPager.setCurrentItem(pageIndex, false);// ȡ������
	}

	/**
	 * ֹͣ��ʱ����
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
				.showImageOnLoading(R.drawable.blank) // ����ͼƬ�������ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.blank)// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.blank) // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
				.cacheInMemory(true)// �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisc(true)// �������ص�ͼƬ�Ƿ񻺴���SD����
				.considerExifParams(true) // �Ƿ���JPEGͼ��EXIF��������ת����ת��
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// ����ͼƬ����εı��뷽ʽ��ʾ
				.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)// ����ͼƬ�Ľ�������//
				// .decodingOptions(android.graphics.BitmapFactory.Options
				// decodingOptions)//����ͼƬ�Ľ�������
				// .delayBeforeLoading(int delayInMillis)//int
				// delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
				// ����ͼƬ���뻺��ǰ����bitmap��������
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// ����ͼƬ������ǰ�Ƿ����ã���λ
				.displayer(new RoundedBitmapDisplayer(20))// �Ƿ�����ΪԲ�ǣ�����Ϊ����
				.displayer(new FadeInBitmapDisplayer(100))// �Ƿ�ͼƬ���غú���Ķ���ʱ��
				.build();// �������

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
			// ���holder�Լ�holder������tv��img�����ʵ��
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
			// Ϊholder�е�tv��img��������
			holder.tv.setText(item.getTitle());
			ImageLoader.getInstance().displayImage(item.getImages()[0],
					holder.img, options);
			// ע�� Ĭ��Ϊ����null,����÷���convertView��ͼ
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
			Toast.makeText(this, "��������", Toast.LENGTH_SHORT).show();
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
