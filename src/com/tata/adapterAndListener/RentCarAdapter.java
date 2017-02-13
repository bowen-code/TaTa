package com.tata.adapterAndListener;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tata.R;
import com.tata.bean.NearTravel;
import com.tata.bean.RentCar;

public class RentCarAdapter extends BaseAdapter{

	private List<RentCar> list;
	private RentCarHolder holder;
	private Context mContext;
	static class RentCarHolder {
		public ImageView scene;
		public TextView title;
		private TextView saleNum;
		private TextView commentNum;
		private TextView price;
	}
	public RentCarAdapter(List<RentCar> list,Context context) {
		this.list = list;
		mContext=context;
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
		RentCar item = list.get(position);
		if (convertView == null) {
			holder = new RentCarHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.rent_car_item, null);
			holder.scene=(ImageView) convertView.findViewById(R.id.scene);
			holder.title=(TextView) convertView.findViewById(R.id.title);
			holder.saleNum=(TextView) convertView.findViewById(R.id.saleNum);
			holder.commentNum=(TextView) convertView.findViewById(R.id.commentNum);
			holder.price=(TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		}else {
			holder=(RentCarHolder) convertView.getTag();
		}
		
		holder.scene.setImageBitmap(item.getScene());
		holder.title.setText(item.getTitle());
		holder.saleNum.setText("月销"+item.getSaleNum()+"笔/");
		holder.commentNum.setText(item.getCommentNum()+"+评论");
		holder.price.setText(item.getPrice()+"元");
		return convertView;
	}
	
}
