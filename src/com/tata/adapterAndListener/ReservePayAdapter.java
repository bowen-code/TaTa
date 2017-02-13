package com.tata.adapterAndListener;

import java.util.List;
import java.util.zip.Inflater;

import com.tata.R;
import com.tata.activity.PayActivity;
import com.tata.adapterAndListener.ReservePayAdapter.ViewHolder;
import com.tata.bean.Reserve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReservePayAdapter extends BaseAdapter {
 
	private List<Reserve> list;
	private Context mContext;
	private ViewHolder holder;
	public ReservePayAdapter(List<Reserve> list,Context context) {
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
		final Reserve item=list.get(position);
		holder=new ViewHolder();
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.reserve_pay_item, null);
			holder.time=(TextView) convertView.findViewById(R.id.time);
			holder.location=(TextView) convertView.findViewById(R.id.location);
			holder.ticketName=(TextView) convertView.findViewById(R.id.ticketName);
			holder.ticketsum=(TextView) convertView.findViewById(R.id.ticketSum);
			holder.validTime=(TextView) convertView.findViewById(R.id.validTime);
			holder.price=(TextView) convertView.findViewById(R.id.price);
			holder.pay=(TextView) convertView.findViewById(R.id.pay);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		holder.time.setText(item.getIndentTime());
		if(item.gettitle().length()>15){
			holder.location.setText(item.gettitle().substring(0, 15)+"°≠°≠");
		}else {
			holder.location.setText(item.gettitle());
		}
		if(item.gettitle().length()>20){
			holder.ticketName.setText(item.gettitle().substring(0, 20)+"°≠°≠");
		}else {
			holder.ticketName.setText(item.gettitle());
		}
		holder.ticketsum.setText(item.getTicketsum()+"’≈");
		holder.validTime.setText(item.getStartTime()+"÷Æ«∞");
		holder.price.setText("£§"+item.getPrice());
		holder.pay.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
                Intent intent=new Intent(mContext, PayActivity.class);
                Bundle bundle=new Bundle();
             	bundle.putString("productID",item.getProductID());
             	bundle.putInt("reserveID",item.getIndentID());
             	bundle.putString("title",item.gettitle());
             	bundle.putInt("num",item.getTicketsum());
             	bundle.putInt("amount",item.getPrice());
             	bundle.putBoolean("isDirectPay",false);
             	intent.putExtras(bundle);
                mContext.startActivity(intent);
               ((Activity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
		return convertView;
	}
	static class ViewHolder {  
        public TextView time;  
        public TextView location;  
        public TextView ticketName;  
        public TextView ticketsum;  
        public TextView validTime;  
        public TextView price;  
        public TextView pay;  
    }  
}
