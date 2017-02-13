package com.tata.adapterAndListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tata.R;
import com.tata.activity.ShowMyPicture;
import com.tata.activity.ShowPicActivity;
import com.tata.adapterAndListener.ShareAdapter.Holder;
import com.tata.adapterAndListener.ShareAdapter.MyAdapter;
import com.tata.adapterAndListener.ShareAdapter.MyOnitemClickListener;
import com.tata.bean.Route;
import com.tata.bean.TimeItem;
import com.tata.utils.Constants;

public class RouteAdapter extends BaseAdapter {

	private List<Route> list;
	private RouteItemHolder holder;
	private Context mContext;
	DisplayImageOptions options;

	static class RouteItemHolder {
		public TextView whichday;
		private TextView route;
		private TextView describe;
		private TextView remark;
		private GridView gridView;
		private ImageView picture;
	}

	public RouteAdapter(List<Route> list, Context context,
			DisplayImageOptions options) {
		this.list = list;
		mContext = context;
		this.options = options;
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
		Route item = list.get(position);
		holder = new RouteItemHolder();
		if(item.getImages().size()==4){
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.route_four_item, null);
			holder.gridView = (GridView) convertView.findViewById(R.id.picture);
		}else if (item.getImages().size()==3||item.getImages().size()==2) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.route_morepic_item, null);
			holder.gridView = (GridView) convertView.findViewById(R.id.picture);
		}
		else if(item.getImages().size()==1){
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.route_onlypic_item, null);
			holder.picture=(ImageView) convertView.findViewById(R.id.picture);
		}else {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.route_item, null);
		}
			holder.whichday = (TextView) convertView
					.findViewById(R.id.whichDay);
			holder.route = (TextView) convertView.findViewById(R.id.route);
			holder.describe = (TextView) convertView
					.findViewById(R.id.describe);
			holder.remark = (TextView) convertView.findViewById(R.id.remark);
		holder.whichday.setText("day"+item.getDayID());
		holder.route.setText(item.getGeneralize());
		if(item.getDescribe().equals("")){
			holder.describe.setVisibility(View.GONE);
		}else {
			holder.describe.setText("    "+item.getDescribe());
		}
		holder.remark.setText(item.getRemarkr());
		if(item.getImages().size()==1){
			ImageLoader.getInstance().displayImage(item.getImages().get(0),
					holder.picture, options);
			holder.picture.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					byte[] result;
					List<byte[]> data = new ArrayList<byte[]>();
					Drawable drawable = ((ImageView) v).getDrawable();
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
					Constants.data = data;
					Intent intent = new Intent(mContext,
							ShowPicActivity.class);
					intent.putExtra("position", 0);
					mContext.startActivity(intent);
				}
			});
		}else if(item.getImages().size()!=0){
			holder.gridView.setAdapter(new MyAdapter(mContext, holder.gridView,item.getImages()));
			holder.gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
			holder.gridView
			.setOnItemClickListener(new MyOnitemClickListener(item
					.getImages().size()));
		}
		
		return convertView;
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
	
	class MyAdapter extends BaseAdapter {

		private List<String> shareImg = null;
		private Context context = null;
		private LayoutInflater inflater = null;
		GridView gridView;
		public MyAdapter(Context context,GridView gridView, List<String> shareImg) {
			this.shareImg = shareImg;
			this.context = context;
			inflater = LayoutInflater.from(context);
			this.gridView=gridView;
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
//			if (shareImg.size() == 1) {
//				gridView.setNumColumns(1);
//				convertView = inflater.inflate(R.layout.share_gridview_onlypic,
//						null);
//				ImageView imageView = (ImageView) convertView
//						.findViewById(R.id.gridview_img);
//				ImageLoader.getInstance().displayImage(shareImg.get(0),
//						imageView, options);
//				return convertView;
//			} else 
				if (shareImg.size() == 2) {
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
			}else if (shareImg.size() == 3) {
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
}
