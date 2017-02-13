package com.tata.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.baidu.android.bbalbs.common.a.c;
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
import com.tata.utils.NetWorkHelper;
import com.tata.utils.jsonTools;
import com.tata.view.BanjiPullTask;
import com.tata.view.MyDialog;
import com.tata.view.NearPullTask;
import com.tata.view.PullToRefreshListView;
import com.tata.view.PullToRefreshView;
import com.tata.view.PullToRefreshBase.OnRefreshListener;
import com.tata.view.PullToRefreshView.OnFooterRefreshListener;
import com.tata.view.PullToRefreshView.OnHeaderRefreshListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BanjiActivity extends BaseActivity implements OnItemClickListener,OnHeaderRefreshListener, OnFooterRefreshListener{
	private ListView listView;
	private List<NearTravel> list;
	private DisplayImageOptions options;
	private Dialog dialog;
	private String phoneNumber;
	private EditText search;
    private TextView start;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopText("�༶��");
		View view=getLayoutInflater().inflate(R.layout.activity_banji, null);
		setCenterView(view);
		dialog=new MyDialog(this).myDialog();
		Constants.BanjiNum=1;
		init(view);
		listView.setOnItemClickListener(this);
		phoneNumber=getSharedPreferences(
				"user", Activity.MODE_PRIVATE).getString("phoneNumber", "");
	}
	
//	OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
//		public void onRefresh() {
//			BanjiPullTask pullTask = new BanjiPullTask(BanjiActivity.this,
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
				Intent intent = new Intent(BanjiActivity.this, SearchActivity.class);
				intent.putExtra("destination", destination);
				intent.putExtra("typeID",3);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_left_out);
				}
			}
		});
		listView = (ListView) view
				.findViewById(R.id.travelList);
		list=new ArrayList<NearTravel>();
		if(NetWorkHelper.checkNetState(this)){
		dialog.show();
		String url = "http://120.24.254.127/tata/data/getProduct.php";
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("typeID", 3);
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
                        list = jsonTools.getNearTravels(arg2);
                        adapter= new NearAdapter(list, options,
            					BanjiActivity.this);
            			listView.setAdapter(adapter);
//            			mPullToRefreshListView
//						.setOnRefreshListener(mOnrefreshListener);
            			ACache mCache = ACache.get(BanjiActivity.this);
            			mCache.put("banji", arg2);
			}

		});
		}else {
//			ACache mCache = ACache.get(BanjiActivity.this);
//			String jsonString=mCache.getAsString("banji");
//			list = jsonTools.getNearTravels(jsonString);
//			 adapter= new NearAdapter(list, options,
// 					BanjiActivity.this);
// 			listView.setAdapter(adapter);
// 			mPullToRefreshListView
//			.setOnRefreshListener(mOnrefreshListener);
		}
	}
	private void initOption() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.scene1) // ����ͼƬ�������ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.scene1)// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.scene1) // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
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
		Toast.makeText(this, "��������", Toast.LENGTH_SHORT).show();
	}
	}
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		BanjiPullTask pullTask = new BanjiPullTask(BanjiActivity.this,
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
