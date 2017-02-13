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
import com.tata.utils.NetWorkHelper;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;
import com.tata.view.NearPullTask;
import com.tata.view.PullToRefreshListView;
import com.tata.view.PullToRefreshView;
import com.tata.view.PullToRefreshBase.OnRefreshListener;
import com.tata.view.PullToRefreshView.OnFooterRefreshListener;
import com.tata.view.PullToRefreshView.OnHeaderRefreshListener;

import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends BaseActivity implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	private ListView listView;
	private List<NearTravel> list = new ArrayList<NearTravel>();
	private List<NearTravel> showList = new ArrayList<NearTravel>();
	private DisplayImageOptions options;
	private Dialog dialog;
	private String destination;
	private int upNum = 0;
	private EditText search;
	private TextView start, productNum;
	private String phoneNumber;
	private int typeID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		destination = getIntent().getStringExtra("destination");
		typeID = getIntent().getIntExtra("typeID", 0);
		setTopText("�������");
		View view = getLayoutInflater().inflate(R.layout.activity_search, null);
		setCenterView(view);
		dialog = new MyDialog(this).myDialog();
		phoneNumber = getSharedPreferences("user", Activity.MODE_PRIVATE)
				.getString("phoneNumber", "");
		init(view);
		listView.setOnItemClickListener(this);
	}

	// private void init(View view) {
	// // TODO Auto-generated method stub
	// listView=(ListView) view.findViewById(R.id.travelList);
	// list=new ArrayList<NearTravel>();
	// list.add(new NearTravel(BitmapFactory.decodeResource(getResources(),
	// R.drawable.scene1), "���������������������ҹ������", "����770����", "����2��"));
	// list.add(new NearTravel(BitmapFactory.decodeResource(getResources(),
	// R.drawable.scene2), "���������������������ҹ������", "����770����", "����2��"));
	// }

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

	// OnRefreshListener mOnrefreshListener = new OnRefreshListener() {
	// public void onRefresh() {
	// if(mPullToRefreshListView.getRefreshType()==2){
	// upNum++;
	// if(list.size()>10*upNum){
	// if(list.size()<=10*(upNum+1)){
	// for (int i = 10*upNum; i < list.size(); i++) {
	// showList.add(list.get(i));
	// }
	// }else {
	// for (int i = 10*upNum; i < 10*(upNum+1); i++) {
	// showList.add(list.get(i));
	// }
	// }
	// adapter.notifyDataSetChanged();
	// }
	// }
	// mPullToRefreshListView.onRefreshComplete();
	// }
	// };
	protected NearAdapter adapter;
	private PullToRefreshView mPullToRefreshView;

	private void init(View view) {
		// TODO Auto-generated method stub
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		search = (EditText) findViewById(R.id.search);
		start = (TextView) findViewById(R.id.start);
		productNum = (TextView) findViewById(R.id.productNum);
		start.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String destination = search.getText().toString().trim();
				if (!destination.equals("")) {
					if (NetWorkHelper.checkNetState(SearchActivity.this)) {
						dialog.show();
						String url = "http://120.24.254.127/tata/data/search.php";
						AsyncHttpClient client = new AsyncHttpClient();
						RequestParams params = new RequestParams();
						params.put("typeID", typeID);
						params.put("keyWord", destination);
						params.put("phoneNumber", phoneNumber);
						client.post(url, params, new TextHttpResponseHandler() {

							@Override
							public void onFailure(int arg0, Header[] arg1,
									String arg2, Throwable arg3) {
								// TODO Auto-generated method stub
								System.out.println(arg2);
								dialog.dismiss();
							}

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									String arg2) {
								// TODO Auto-generated method stub
								System.out.println(arg2);
								list.clear();
								List<NearTravel> data = jsonTools
										.getNearTravels(arg2);
								for (int i = 0; i < data.size(); i++) {
									list.add(data.get(i));
								}
								if (list.size() == 0) {
									productNum.setText("����ز�Ʒ");
								} else {
									productNum.setText("��������" + list.size()
											+ "����ز�Ʒ");
								}
								showList.clear();
								if (list.size() > 10) {
									for (int i = 0; i < 10; i++) {
										showList.add(list.get(i));
									}
									adapter.notifyDataSetChanged();
								} else {
									for (int i = 0; i < list.size(); i++) {
										showList.add(list.get(i));
									}
									adapter.notifyDataSetChanged();
								}
								dialog.dismiss();
								upNum = 0;
								// listView.setAdapter(adapter);
								// mPullToRefreshListView
								// .setOnRefreshListener(mOnrefreshListener);
							}

						});
					}
				}
			}
		});
		// mPullToRefreshListView = (PullToRefreshListView) view
		// .findViewById(R.id.travelList);
		listView = (ListView) view.findViewById(R.id.travelList);
		// listView = mPullToRefreshListView.getRefreshableView();
		if (NetWorkHelper.checkNetState(this)) {
			dialog.show();
			String url = "http://120.24.254.127/tata/data/search.php";
			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();
			params.put("typeID", typeID);
			params.put("keyWord", destination);
			params.put("phoneNumber", phoneNumber);
			client.post(url, params, new TextHttpResponseHandler() {

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2,
						Throwable arg3) {
					// TODO Auto-generated method stub
					System.out.println(arg2);
					dialog.dismiss();
				}

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					// TODO Auto-generated method stub
					System.out.println(arg2);
					List<NearTravel> data = jsonTools.getNearTravels(arg2);
					for (int i = 0; i < data.size(); i++) {
						list.add(data.get(i));
					}
					if (list.size() == 0) {
						productNum.setText("����ز�Ʒ");
					} else {
						productNum.setText("��������" + list.size() + "����ز�Ʒ");
					}
					if (list.size() > 10) {
						for (int i = 0; i < 10; i++) {
							showList.add(list.get(i));
						}
						adapter = new NearAdapter(showList, options,
								SearchActivity.this);
					} else {
						for (int i = 0; i < list.size(); i++) {
							showList.add(list.get(i));
						}
						adapter = new NearAdapter(showList, options,
								SearchActivity.this);
					}
					dialog.dismiss();
					listView.setAdapter(adapter);
					// mPullToRefreshListView
					// .setOnRefreshListener(mOnrefreshListener);
				}

			});
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
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

	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		mPullToRefreshView.postDelayed(new Runnable() {

			public void run() {
				upNum++;
				if (list.size() > 10 * upNum) {
					if (list.size() <= 10 * (upNum + 1)) {
						for (int i = 10 * upNum; i < list.size(); i++) {
							showList.add(list.get(i));
						}
					} else {
						for (int i = 10 * upNum; i < 10 * (upNum + 1); i++) {
							showList.add(list.get(i));
						}
					}
					adapter.notifyDataSetChanged();
				}
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);
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
