package com.tata.adapterAndListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Nickname;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tata.R;
import com.tata.R.color;
import com.tata.activity.BanjiActivity;
import com.tata.activity.LoginActivity;
import com.tata.activity.MyCollectActivity;
import com.tata.activity.PersonActivity;
import com.tata.activity.ShowPicActivity;
import com.tata.bean.Dianzan;
import com.tata.bean.ShareComment;
import com.tata.bean.ShareMessage;
import com.tata.utils.Constants;
import com.tata.utils.MainApplication;
import com.tata.utils.ToastUtil;
import com.tata.utils.Tools;
import com.tata.utils.jsonTools;
import com.tata.view.emoji.EmojiconTextView;

public class CollectShareAdapter extends BaseAdapter {

	private List<ShareMessage> list;
	private ShareHolder holder;
	private Context mContext;
	private PopupWindow popupWindow;
	public int[] pictures;
	DisplayImageOptions options;
	// private String myphoneNumber;
	LatLng locationLatLng = new LatLng(30.598428, 114.311831);
	private LatLng itemLatLng;
	private int index;
	private double distance;
	boolean flag = false;
	private Handler handler;
	// private String userName;
	private SharedPreferences sp;

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

	public CollectShareAdapter(List<ShareMessage> list, Context context,
			DisplayImageOptions options) {
		this.list = list;
		mContext = context;
		this.options = options;
		sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		// userName=context.getSharedPreferences("user",
		// Context.MODE_PRIVATE).getString("userName", "");
		// myphoneNumber = context.getSharedPreferences("user",
		// Context.MODE_PRIVATE).getString("phoneNumber", "");
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

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ShareMessage item = list.get(position);
		// if (convertView == null) {
		holder = new ShareHolder();
		if (item.getType() == 0) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.collect_share_item, null);
		} else if (item.getType() == 1) {
			if (item.getShareImg().size() == 1) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.collect_share_item_picture, null);
				holder.picture = (ImageView) convertView
						.findViewById(R.id.picture);
			}
			else {

				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.collect_share_item_morepicture, null);
				holder.gridView = (GridView) convertView
						.findViewById(R.id.picture);
				holder.gridView
						.setSelector(new ColorDrawable(Color.TRANSPARENT));
			}
		}
		holder.figure = (ImageView) convertView.findViewById(R.id.figure);
		holder.figure.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String phoneNumber = item.getPhoneNumber();
				String userName = item.getName();
				Intent intent = new Intent(mContext, PersonActivity.class);
				intent.putExtra("phoneNumber", phoneNumber);
				intent.putExtra("userName", userName);
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
		holder.nickName = (TextView) convertView.findViewById(R.id.nickName);
		holder.nickName.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phoneNumber = item.getPhoneNumber();
				String userName = item.getName();
				Intent intent = new Intent(mContext, PersonActivity.class);
				intent.putExtra("phoneNumber", phoneNumber);
				intent.putExtra("userName", userName);
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(
						R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
		holder.gender = (ImageView) convertView.findViewById(R.id.gender);
		holder.overflow = (ImageView) convertView.findViewById(R.id.overflow);
		holder.time = (TextView) convertView.findViewById(R.id.time);
		holder.content = (TextView) convertView.findViewById(R.id.content);
		holder.distance = (TextView) convertView.findViewById(R.id.distance);
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
		holder.time.setText(item.getTime());
		holder.content.setText(item.getContent());
		if(item.getShareLocation()==null){
			holder.distance.setVisibility(View.GONE);
		}else {
			holder.distance.setText(item.getShareLocation());
		}
		if (!item.getLocationXY().equals("")) {
			index = item.getLocationXY().indexOf("/");
			itemLatLng = new LatLng(Double.parseDouble(item.getLocationXY()
					.substring(0, index)), Double.parseDouble(item
					.getLocationXY().substring(index + 1)));
			distance = DistanceUtil.getDistance(itemLatLng, locationLatLng);
			int juli = (int) distance;
			if (juli < 1000) {
				holder.distance.setText(juli + "米");
			} else {
				holder.distance.setText(String.format("%.1f", juli / 1000.0)
						+ "公里");
			}
		}
		if (item.getType() == 1) {
			if (item.getShareImg().size() == 1) {
				ImageLoader.getInstance().displayImage(
						item.getShareImg().get(0), holder.picture, options);
			} else {

				holder.gridView.setAdapter(new MyAdapter(mContext,
						holder.gridView, item.getShareImg()));
				holder.gridView
						.setOnItemClickListener(new MyOnitemClickListener(item
								.getShareImg().size()));
			}
		}
		holder.overflow.setOnClickListener(new myonclickListener(sp.getString(
				"phoneNumber", ""), item));
		return convertView;
	}

	static class Holder {

		ImageView img = null;
	}

	class MyAdapter extends BaseAdapter {

		private List<String> shareImg = null;
		private Context context = null;
		private LayoutInflater inflater = null;
		GridView gridView;

		public MyAdapter(Context context, GridView gridView,
				List<String> shareImg) {
			this.shareImg = shareImg;
			this.context = context;
			inflater = LayoutInflater.from(context);
			this.gridView = gridView;
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
				gridView.setNumColumns(1);
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
					gridView.setNumColumns(2);
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
			} else if (shareImg.size() == 3) {
				Holder holder;
				if (convertView == null) {
					gridView.setNumColumns(3);
					holder = new Holder();
					convertView = inflater.inflate(
							R.layout.share_gridview_threepic, null);
					holder.img = (ImageView) convertView
							.findViewById(R.id.gridview_img);
					convertView.setTag(holder);
				} else {
					holder = (Holder) convertView.getTag();
				}
				ImageLoader.getInstance().displayImage(shareImg.get(position),
						holder.img, options);
				return convertView;
			}
			else {

				Holder holder;
				if (convertView == null) {
					gridView.setNumColumns(2);
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
		private int myfoucs;
		private int mycollect;
		private ShareMessage item;
		private Button cancel;
		private Button delete;

		public myonclickListener(String myPhoneNumber, ShareMessage item) {
			this.phoneNumber = item.getPhoneNumber();
			this.myPhoneNumber = myPhoneNumber;
			this.shareID = item.getShareID();
			this.myfoucs = item.getMyfouce();
			this.mycollect = item.getMycollect();
			this.item = item;
		}

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.overflow:
				View contentView;
				contentView = LayoutInflater.from(mContext).inflate(
						R.layout.my_popwinodw, null);
				popupWindow = new PopupWindow(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				popupWindow.setFocusable(true);
				popupWindow.setOutsideTouchable(true);
				popupWindow.setBackgroundDrawable(new BitmapDrawable());
				popupWindow.setContentView(contentView);
				popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0,
						0);
				delete = (Button) contentView.findViewById(R.id.delete);
				delete.setOnClickListener(this);
				cancel = (Button) contentView.findViewById(R.id.cancel);
				cancel.setOnClickListener(this);
				break;
			case R.id.delete:
				AsyncHttpClient httpClient = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("phoneNumber", myPhoneNumber);
				params.put("shareID", shareID);
				// String url =
				// "http://120.24.254.127/tata/data/deleteShare.php";
				httpClient.post("http://120.24.254.127/tata/data/deleteCollect.php", params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println(arg2);
						list.remove(item);
						notifyDataSetChanged();
						Constants.mydeletecollectShare.add(shareID);
						Intent mIntent = new Intent("mydeletecollectShare.data.person.myself");
						mContext.sendBroadcast(mIntent);
						ToastUtil.show(mContext, "删除成功");
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						ToastUtil.show(mContext, "删除失败 请重试！");
					}
				});
				popupWindow.dismiss();
				break;
			case R.id.cancel:
				popupWindow.dismiss();
				break;
			}
		}
	}

	class mypopWindow extends PopupWindow {

		public mypopWindow(View contentView, int width, int height) {
			super(contentView, width, height);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void dismiss() {
			// TODO Auto-generated method stub
			flag = false;
			super.dismiss();
		}
	}

	class MyOnitemClickListener implements OnItemClickListener {
		private int size;

		public MyOnitemClickListener(int size) {
			this.size = size;
		}

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			handler.sendEmptyMessage(2);
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
