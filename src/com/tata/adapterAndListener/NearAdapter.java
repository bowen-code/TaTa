package com.tata.adapterAndListener;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tata.R;
import com.tata.bean.NearTravel;

public class NearAdapter extends BaseAdapter{

	private List<NearTravel> list;
	private NearHolder holder;
	private Context mContext;
	private DisplayImageOptions options;
	static class NearHolder {
		public ImageView scene;
		public TextView title;
		private TextView yuanjia;
		private TextView cuxiaojia;
	}
	public NearAdapter(List<NearTravel> list,DisplayImageOptions options,Context context) {
		this.list = list;
		mContext=context;
		this.options=options;
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
		NearTravel item = list.get(position);
		if (convertView == null) {
			holder = new NearHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.near_travel_item, null);
			holder.scene=(ImageView) convertView.findViewById(R.id.scene);
			holder.title=(TextView) convertView.findViewById(R.id.title);
			holder.cuxiaojia=(TextView) convertView.findViewById(R.id.cuxiaojia);
			holder.yuanjia=(TextView) convertView.findViewById(R.id.yuanjia);
			convertView.setTag(holder);
		}else {
			holder=(NearHolder) convertView.getTag();
		}
		holder.scene.setTag(position);
//		if(holder.scene.getTag().equals(position+""))
		if(holder.scene.getTag()!=null&&holder.scene.getTag().equals(position))
		ImageLoader.getInstance().displayImage(item.getImages()[0],
				holder.scene, options);
		holder.title.setText(item.getTitle());
		if(item.getPriceTwo()==0){
			holder.cuxiaojia.setVisibility(View.GONE);
			holder.yuanjia.setText("价格：￥"+item.getPrice());
			holder.yuanjia.setTextSize(14);
		}else {
			holder.cuxiaojia.setVisibility(View.VISIBLE);
			holder.cuxiaojia.setText("促销价：￥"+item.getPriceTwo());
			holder.yuanjia.setText("原价：￥"+item.getPrice());
		}
		return convertView;
	}
	
	
}
