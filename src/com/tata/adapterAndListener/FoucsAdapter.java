package com.tata.adapterAndListener;

import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tata.R;
import com.tata.adapterAndListener.FoucsAdapter.FoucsHolder;
import com.tata.adapterAndListener.NearAdapter.NearHolder;
import com.tata.bean.FoucsInfo;
import com.tata.bean.NearTravel;
import com.tata.utils.TimeDeal;
import com.tata.view.MyDialog;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FoucsAdapter extends BaseAdapter{

	private List<FoucsInfo> info;
	private FoucsHolder holder;
	private Context mContext;
	DisplayImageOptions options;
	private String myPhoneNumber;
	public FoucsAdapter(List<FoucsInfo> info,Context context,DisplayImageOptions options) {
		this.info = info;
		mContext=context;
		this.options=options;
		myPhoneNumber = context.getSharedPreferences("user",
				Context.MODE_PRIVATE).getString("phoneNumber", "");
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return info.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return info.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		FoucsInfo item = info.get(position);
		if (convertView == null) {
			holder = new FoucsHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.myfoucs_item, null);
			holder.figure=(ImageView) convertView.findViewById(R.id.figure);
			holder.delete=(ImageView) convertView.findViewById(R.id.delete);
			holder.username=(TextView) convertView.findViewById(R.id.username);
			holder.shareMsg=(TextView) convertView.findViewById(R.id.shareMsg);
			convertView.setTag(holder);
		}else {
			holder=(FoucsHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(item.getUserImg(), holder.figure, options);
		holder.username.setText(item.getUsername());
		holder.shareMsg.setText("¹Ø×¢Ê±¼ä£º"+TimeDeal.getTime(item.getTime()));
		holder.delete.setOnClickListener(new myonclickListener(item.getPhoneNumber(),position));
		return convertView;
	}
	
	class myonclickListener implements OnClickListener {

		private String phoneNumber;
		private Dialog dialog;
        private int position;
		public myonclickListener(String phoneNumber,int position) {
			this.phoneNumber = phoneNumber;
			this.position = position;
		}

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.delete:
				dialog = new MyDialog(mContext).myDialog();
				AsyncHttpClient httpClient = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("phoneNumberZ", myPhoneNumber);
				params.put("phoneNumberB", phoneNumber);
				String url = "http://120.24.254.127/tata/deleteAttention";
				httpClient.post(url, params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println(arg2);
						info.remove(position);
						dialog.dismiss();
						notifyDataSetChanged();
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						System.out.println(arg2);
						dialog.dismiss();
					}
				});
				break;

			}
		}
	}

	
	
	
	static class FoucsHolder {
		public ImageView figure;
		public ImageView delete;
		public TextView username;
		private TextView shareMsg;
	}

}
