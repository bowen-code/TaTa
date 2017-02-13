package com.tata.view;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.bean.NearTravel;
import com.tata.bean.ShareMessage;
import com.tata.utils.Constants;
import com.tata.utils.MainApplication;
import com.tata.utils.MyHttpClient;
import com.tata.utils.NetWorkHelper;
import com.tata.utils.jsonTools;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

public class BanjiPullTask {

	private PullToRefreshView pullToRefreshListView; // 实现下拉刷新与上拉加载的ListView
	private int pullState; // 记录判断，上拉与下拉动作
	private BaseAdapter baseAdapter; // ListView适配器，用于提醒ListView数据已经更新
	private List<NearTravel> linkedList;
	private Context context;
	private String phoneNumber;

	public BanjiPullTask(Context mContext,
			PullToRefreshView pullToRefreshListView, int pullState,
			BaseAdapter baseAdapter, List<NearTravel> linkedList,
			String phone) {
		this.pullToRefreshListView = pullToRefreshListView;
		this.pullState = pullState;
		this.baseAdapter = baseAdapter;
		this.linkedList = linkedList;
		context = mContext;
		phoneNumber = phone;
	}

	private RequestParams param;
	String url;

	public void getShare() {
		if (NetWorkHelper.checkNetState(context)) {
			AsyncHttpClient httpClient = new AsyncHttpClient();
			param = new RequestParams();
				Constants.BanjiNum++;
				param.put("num", Constants.BanjiNum);
				param.put("phoneNumber", phoneNumber);
				param.put("typeID", 3);
			httpClient.post("http://120.24.254.127/tata/data/getProduct.php", param, new TextHttpResponseHandler() {

				@Override
				public void onSuccess(int arg0, Header[] arg1, String result) {
					// TODO Auto-generated method stub
											// 上拉
						List<NearTravel> newData = jsonTools
								.getNearTravels(result);
						for (int i = 0; i < newData.size(); i++) {
							linkedList.add(newData.get(i));
							baseAdapter.notifyDataSetChanged();
						}
					pullToRefreshListView.onFooterRefreshComplete();
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2,
						Throwable arg3) {
//					System.out.println(arg2);
					pullToRefreshListView.onFooterRefreshComplete();
				}
			});
		}else {
			pullToRefreshListView.onFooterRefreshComplete();
		}
	}
		

}
