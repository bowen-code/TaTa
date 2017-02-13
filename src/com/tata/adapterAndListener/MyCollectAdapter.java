package com.tata.adapterAndListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.R.integer;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tata.R;
import com.tata.activity.ShowPicActivity;
import com.tata.adapterAndListener.ShareAdapter.Holder;
import com.tata.adapterAndListener.ShareAdapter.MyAdapter;
import com.tata.adapterAndListener.ShareAdapter.MyOnitemClickListener;
import com.tata.adapterAndListener.ShareAdapter.ShareHolder;
import com.tata.adapterAndListener.ShareAdapter.myonclickListener;
import com.tata.bean.ShareMessage;
import com.tata.utils.Constants;
import com.tata.utils.ToastUtil;
import com.tata.view.MyDialog;

public class MyCollectAdapter extends BaseAdapter {
	private List<ShareMessage> list;
	private ShareHolder holder;
	private Context mContext;
	private TextView foucs;
	private TextView collect;
	private PopupWindow popupWindow;
	public int[] pictures;
	DisplayImageOptions options;
	private String myphoneNumber;

	static class ShareHolder {
		private ImageView figure;
		private ImageView picture;
		private ImageView gender;
		private ImageView overflow;
		private TextView nickName;
		private TextView sharelocation;
		private TextView distance;
		private TextView time;
		private TextView content;
		private GridView gridView;

	}

	public MyCollectAdapter(List<ShareMessage> list, Context context,
			DisplayImageOptions options) {
		this.list = list;
		mContext = context;
		this.options = options;
		myphoneNumber = context.getSharedPreferences("user",
				Context.MODE_PRIVATE).getString("phoneNumber", "");
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ShareMessage item = list.get(position);
		// if (convertView == null) {
		holder = new ShareHolder();
		if (item.getType() == 0) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.share_item, null);
		} else if (item.getType() == 1) {

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.share_item_morepicture, null);
			holder.gridView = (GridView) convertView.findViewById(R.id.picture);
			holder.gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		}
		holder.figure = (ImageView) convertView.findViewById(R.id.figure);
		holder.nickName = (TextView) convertView.findViewById(R.id.nickName);
		holder.gender = (ImageView) convertView.findViewById(R.id.gender);
		holder.overflow = (ImageView) convertView.findViewById(R.id.overflow);
		holder.time = (TextView) convertView.findViewById(R.id.time);
		holder.content = (TextView) convertView.findViewById(R.id.content);
		holder.distance = (TextView) convertView.findViewById(R.id.distance);
		holder.sharelocation = (TextView) convertView
				.findViewById(R.id.sharelocation);
		ImageLoader.getInstance().displayImage(item.getUserImg(),
				holder.figure, options);
		holder.nickName.setText(item.getName());
		if (item.getGender() == 0) {
			holder.gender.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.male));
		} else {
			holder.gender.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.female));
		}

		holder.overflow.setBackgroundDrawable(mContext.getResources()
				.getDrawable(R.drawable.delete));
		holder.time.setText(item.getTime());
		holder.content.setText(item.getContent());
//		holder.sharelocation.setText(item.getShareLocation());
		 holder.distance.setText(item.getShareLocation());
		if (item.getType() == 1) {
			holder.gridView.setAdapter(new MyAdapter(mContext, item
					.getShareImg()));
			holder.gridView.setOnItemClickListener(new MyOnitemClickListener(
					item.getShareImg().size()));
		}
		holder.overflow.setOnClickListener(new myonclickListener(myphoneNumber,
				item.getShareID(),position));
		return convertView;
	}

	static class Holder {

		ImageView img = null;
	}

	class MyAdapter extends BaseAdapter {

		private List<String> shareImg = null;
		private Context context = null;
		private LayoutInflater inflater = null;

		public MyAdapter(Context context, List<String> shareImg) {
			this.shareImg = shareImg;
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return shareImg.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// 获得holder以及holder对象中tv和img对象的实例
			if (shareImg.size() == 1) {
				convertView = inflater.inflate(R.layout.share_gridview_onlypic,
						null);
				ImageView imageView = (ImageView) convertView
						.findViewById(R.id.gridview_img);
				ImageLoader.getInstance().displayImage(shareImg.get(0),
						imageView, options);
				return convertView;
			} else if (shareImg.size() == 2) {
				Holder holder;
				if (convertView == null) {
					holder = new Holder();
					convertView = inflater.inflate(
							R.layout.share_gridview_twopic, null);
					holder.img = (ImageView) convertView
							.findViewById(R.id.gridview_img);
					convertView.setTag(holder);
				} else {
					holder = (Holder) convertView.getTag();
				}
				ImageLoader.getInstance().displayImage(shareImg.get(position),
						holder.img, options);
				return convertView;
			} else {

				Holder holder;
				if (convertView == null) {
					convertView = inflater.inflate(
							R.layout.share_gridview_pic_item, null);
					holder = new Holder();
					holder.img = (ImageView) convertView
							.findViewById(R.id.gridview_img);

					convertView.setTag(holder);

				} else {
					holder = (Holder) convertView.getTag();

				}
				ImageLoader.getInstance().displayImage(shareImg.get(position),
						holder.img, options);
				// 注意 默认为返回null,必须得返回convertView视图
				return convertView;
			}
		}
	}

	class myonclickListener implements OnClickListener {

		private String phoneNumber;
		private String myPhoneNumber;
		private int shareID;
		private int position;
		private Dialog dialog;

		public myonclickListener(String myPhoneNumber, int shareID,int position) {
			this.myPhoneNumber = myPhoneNumber;
			this.shareID = shareID;
			this.position = position;
		}

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.overflow:
				dialog = new MyDialog(mContext).myDialog();
				AsyncHttpClient httpClient = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("phoneNumber", myPhoneNumber);
				params.put("shareID", shareID);
				String url = "http://120.24.254.127/tata/deleteCollect";
				httpClient.post(url, params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println(arg2);
						list.remove(position);
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

	class MyOnitemClickListener implements OnItemClickListener {
		private int size;

		public MyOnitemClickListener(int size) {
			this.size = size;
		}

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			byte[] result;
			List<byte[]> data = new ArrayList<byte[]>();
			for (int i = 0; i < size; i++) {
				Drawable drawable = ((ImageView) (parent.getChildAt(i)
						.findViewById(R.id.gridview_img))).getDrawable();
				BitmapDrawable bd = (BitmapDrawable) drawable;
				Bitmap bitmap = bd.getBitmap();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, output);
				result = output.toByteArray();
				data.add(result);
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Constants.data = data;
			Intent intent = new Intent(mContext, ShowPicActivity.class);
			intent.putExtra("position", position);
			mContext.startActivity(intent);
		}
	}
}
