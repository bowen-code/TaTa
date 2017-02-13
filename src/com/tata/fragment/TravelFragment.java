package com.tata.fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tata.R;
import com.tata.activity.BanjiActivity;
import com.tata.activity.GraduateActivity;
import com.tata.activity.HotActivity;
import com.tata.activity.NearActivity;
import com.tata.activity.PrivateorderActivity;
import com.tata.activity.RentcarActivity;
import com.tata.activity.ScoreActivity;
import com.tata.activity.SearchActivity;
import com.tata.activity.StarActivity;
import com.tata.adapterAndListener.GuideAdapter;
import com.tata.adapterAndListener.GuidePageChangeListener;
import com.tata.utils.ACache;
import com.tata.utils.HttpUtils;
import com.tata.utils.MyHttpClient;
import com.tata.utils.NetWorkHelper;
import com.tata.utils.ToastUtil;
import com.tata.utils.jsonTools;
import com.tata.view.MyViewPager;
import com.tata.view.MyViewPager.OnTouchListener;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TravelFragment extends Fragment implements OnClickListener {
	private MyViewPager viewPager;
	private ImageView[] imageViews = new ImageView[4];
	private ArrayList<View> advPics;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	Timer mTimer;
	TimerTask mTask;
	int pageIndex = 1;
	boolean isTaskRun;
	private LinearLayout hotActivity, nearActivity, banjiActivity,
			graduateActivity, privateorderActivity, rentcarActivity,
			starActivity, scoreActivity;
	protected String url="http://120.24.254.127/tata/data/getHomeImg.php";
	protected String imgJson="";
	public String[] data;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.travel, container, false);// ���������ļ�
		initView(rootView);
		initData(rootView);
		initOption();
//		if(NetWorkHelper.checkNetState(getActivity())){
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				imgJson=HttpUtils.getJsonString(url);
				mHandler.sendEmptyMessage(1);
			}
		}).start();
//		}else {
//			ACache mCache=ACache.get(getActivity());
//			String data=mCache.getAsString("advImgs");
//			if(data!=null){
//				imgJson=data;
//				mHandler.sendEmptyMessage(1);
//			}
//			Toast.makeText(getActivity(), "��������", Toast.LENGTH_SHORT).show();
//			}
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


		return rootView;
	}

	private void initOption() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.blank) // ����ͼƬ�������ڼ���ʾ��ͼƬ
		.showImageForEmptyUri(R.drawable.blank)// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
		.showImageOnFail(R.drawable.blank) // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
		.cacheInMemory(true)// �������ص�ͼƬ�Ƿ񻺴����ڴ���
		.cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����  
		.considerExifParams(true) // �Ƿ���JPEGͼ��EXIF��������ת����ת��
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// ����ͼƬ����εı��뷽ʽ��ʾ
		.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)// ����ͼƬ�Ľ�������//
		// .decodingOptions(android.graphics.BitmapFactory.Options
		// decodingOptions)//����ͼƬ�Ľ�������
		// .delayBeforeLoading(int delayInMillis)//int
		// delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
		// ����ͼƬ���뻺��ǰ����bitmap��������
//		 .preProcessor(BitmapProcessor preProcessor)
		.resetViewBeforeLoading(true)// ����ͼƬ������ǰ�Ƿ����ã���λ
		.displayer(new RoundedBitmapDisplayer(20))// �Ƿ�����ΪԲ�ǣ�����Ϊ����
		.displayer(new FadeInBitmapDisplayer(100))// �Ƿ�ͼƬ���غú���Ķ���ʱ��
		.build();// �������
		
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

	/**
	 * ֹͣ��ʱ����
	 */
	private void stopTask() {
		// TODO Auto-generated method stub
		isTaskRun = false;
		mTimer.cancel();
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what==0) {
				setCurrentItem();
			}else {
				advPics = new ArrayList<View>();
				data=jsonTools.getImgUrls(imgJson);
//				String[] data={};
				ImageView view1 = new ImageView(getActivity());
				view1.setScaleType(ScaleType.FIT_XY);
				ImageLoader.getInstance().displayImage(data[0],
						view1, options);
				advPics.add(view1);
				ImageView view2 = new ImageView(getActivity());
				view2.setScaleType(ScaleType.FIT_XY);
				ImageLoader.getInstance().displayImage(data[1],
						view2, options);
				advPics.add(view2);
				ImageView view3 = new ImageView(getActivity());
				view3.setScaleType(ScaleType.FIT_XY);
				ImageLoader.getInstance().displayImage(data[2],
						view3, options);
				advPics.add(view3);
				ImageView view4 = new ImageView(getActivity());
				view4.setScaleType(ScaleType.FIT_XY);
				ImageLoader.getInstance().displayImage(data[3],
						view4, options);
				advPics.add(view4);
				viewPager.setAdapter(new GuideAdapter(advPics));
				ACache mCache=ACache.get(getActivity());
				mCache.put("advImgs", imgJson);
			}
		}
	};
	private TextView startTravel;
	private EditText search;

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

	private void initView(View rootView) {
		search=(EditText) rootView.findViewById(R.id.search);
		startTravel=(TextView)rootView.findViewById(R.id.startTravel);
		hotActivity = (LinearLayout) rootView.findViewById(R.id.hotActivity);
		nearActivity = (LinearLayout) rootView.findViewById(R.id.nearActivity);
		banjiActivity = (LinearLayout) rootView
				.findViewById(R.id.banjiActivity);
		graduateActivity = (LinearLayout) rootView
				.findViewById(R.id.graduateActivity);
		privateorderActivity = (LinearLayout) rootView
				.findViewById(R.id.privateorderActivity);
		rentcarActivity = (LinearLayout) rootView
				.findViewById(R.id.rentcarActivity);
		starActivity = (LinearLayout) rootView.findViewById(R.id.starActivity);
		scoreActivity = (LinearLayout) rootView
				.findViewById(R.id.scoreActivity);
		viewPager = (MyViewPager) rootView.findViewById(R.id.viewPager);
		imageViews[0] = (ImageView) rootView.findViewById(R.id.img1);
		imageViews[1] = (ImageView) rootView.findViewById(R.id.img2);
		imageViews[2] = (ImageView) rootView.findViewById(R.id.img3);
		imageViews[3] = (ImageView) rootView.findViewById(R.id.img4);
	}

	private void initData(View rootView) {
		hotActivity.setOnClickListener(this);
		nearActivity.setOnClickListener(this);
		banjiActivity.setOnClickListener(this);
		graduateActivity.setOnClickListener(this);
		privateorderActivity.setOnClickListener(this);
		rentcarActivity.setOnClickListener(this);
		starActivity.setOnClickListener(this);
		scoreActivity.setOnClickListener(this);
		startTravel.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.hotActivity:
			intent = new Intent(getActivity(), HotActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			break;
		case R.id.nearActivity:
			intent = new Intent(getActivity(), NearActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			break;
		case R.id.banjiActivity:
			intent = new Intent(getActivity(), BanjiActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			break;
		case R.id.graduateActivity:
			intent = new Intent(getActivity(), GraduateActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			break;
		case R.id.privateorderActivity:
			intent = new Intent(getActivity(), PrivateorderActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			break;
		case R.id.rentcarActivity:
			ToastUtil.show(getActivity(),"�����ڴ�");
//			intent = new Intent(getActivity(), RentcarActivity.class);
//			startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.slide_left_in,
//					R.anim.slide_left_out);
			break;
		case R.id.starActivity:
			ToastUtil.show(getActivity(),"�����ڴ�");
//			intent = new Intent(getActivity(), StarActivity.class);
//			startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.slide_left_in,
//					R.anim.slide_left_out);
			break;
		case R.id.scoreActivity:
			ToastUtil.show(getActivity(),"�����ڴ�");
//			intent = new Intent(getActivity(), ScoreActivity.class);
//			startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.slide_left_in,
//					R.anim.slide_left_out);
			break;
		case R.id.startTravel:
			String destination=search.getText().toString().trim();
			if(TextUtils.equals(destination, "")){
				break;
			}
			intent = new Intent(getActivity(), SearchActivity.class);
			intent.putExtra("destination", destination);
			intent.putExtra("typeID", 0);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			break;
		}

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
	
}