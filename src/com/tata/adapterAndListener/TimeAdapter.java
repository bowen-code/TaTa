package com.tata.adapterAndListener;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tata.R;
import com.tata.activity.ShowMyPicture;
import com.tata.bean.TimeItem;

public class TimeAdapter extends BaseAdapter{

	private List<TimeItem> list;
	private TimeItemHolder holder;
	private Context mContext;
	DisplayImageOptions options;
	static class TimeItemHolder {
		public ImageView image;
		public TextView year;
		private TextView m_d;
		private TextView word;
		private TextView pic_sum;
	}
	public TimeAdapter(List<TimeItem> list,Context context,DisplayImageOptions options) {
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
		final TimeItem item = list.get(position);
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.time_pic_item, null);
		    holder=new TimeItemHolder();
		    holder.year=(TextView) convertView.findViewById(R.id.year);
		    holder.m_d=(TextView) convertView.findViewById(R.id.m_d);
		    holder.word=(TextView) convertView.findViewById(R.id.word);
		    holder.pic_sum=(TextView) convertView.findViewById(R.id.pic_sum);
		    holder.image=(ImageView) convertView.findViewById(R.id.image);
		    convertView.setTag(holder);
		}else {
				holder=(TimeItemHolder) convertView.getTag();
		}
		 holder.image.setTag(position);
		holder.year.setText(item.getYear());
		holder.m_d.setText(item.getM_d());
		if(!item.getWord().equals("")){
			if(item.getWord().length()>50){
				holder.word.setText(item.getWord().substring(0, 46)+"......");
			}else {
				holder.word.setText(item.getWord());
			}
			
		}
		if(item.getType()==0){
			holder.image.setVisibility(View.GONE);
			holder.pic_sum.setVisibility(View.GONE);
		}else {
			holder.image.setVisibility(View.VISIBLE);
			holder.pic_sum.setVisibility(View.VISIBLE);
			holder.pic_sum.setText("π≤"+item.getImages().size()+"’≈");
//			ImageView imageView1=new ImageView(mContext);
//			imageView1.setScaleType(ScaleType.CENTER_CROP);
			if((Integer)holder.image.getTag()==position)
			ImageLoader.getInstance().displayImage(item.getImages().get(0),
					holder.image, options);
			holder.image.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(mContext,ShowMyPicture.class);
					String data[]=new String[item.getImages().size()];
					for (int i = 0; i < item.getImages().size(); i++) {
						data[i]=item.getImages().get(i);
					}
					intent.putExtra("imageUrls",data);
					mContext.startActivity(intent);
					((Activity)mContext).overridePendingTransition(R.anim.slide_left_in,
							R.anim.slide_left_out);
				}
			});
//			holder.image.addView(imageView1,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		
		
		return convertView;
	}
	
}
