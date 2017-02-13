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
		setTopText("搜索结果");
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
	// R.drawable.scene1), "大理吧啦吧啦吧啦三天两夜自由行", "距离770公里", "月销2笔"));
	// list.add(new NearTravel(BitmapFactory.decodeResource(getResources(),
	// R.drawable.scene2), "大理吧啦吧啦吧啦三天两夜自由行", "距离770公里", "月销2笔"));
	// }

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
									productNum.setText("无相关产品");
								} else {
									productNum.setText("共搜索到" + list.size()
											+ "个相关产品");
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
						productNum.setText("无相关产品");
					} else {
						productNum.setText("共搜索到" + list.size() + "个相关产品");
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
			Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
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
