package com.tata.adapterAndListener;

import java.util.List;
import java.util.zip.Inflater;

import com.tata.R;
import com.tata.activity.PayActivity;
import com.tata.adapterAndListener.HistoryPrivateAdapter.ViewHolder;
import com.tata.bean.PrivateOrder;
import com.tata.bean.Reserve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryPrivateAdapter extends BaseAdapter {
 
	private List<PrivateOrder> list;
	private Context mContext;
	private ViewHolder holder;
	public HistoryPrivateAdapter(List<PrivateOrder> list,Context context) {
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
		final PrivateOrder item=list.get(position);
		holder=new ViewHolder();
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.private_history_item, null);
			holder.privateID=(TextView) convertView.findViewById(R.id.privateID);
			holder.destination=(TextView) convertView.findViewById(R.id.destination);
			holder.startPlace=(TextView) convertView.findViewById(R.id.startPlace);
			holder.startTime=(TextView) convertView.findViewById(R.id.startDate);
			holder.description=(TextView) convertView.findViewById(R.id.description);
			holder.privateIndentTime=(TextView) convertView.findViewById(R.id.time);
			holder.privateIndentState=(TextView) convertView.findViewById(R.id.privateIndentState);
			holder.personNum=(TextView) convertView.findViewById(R.id.personNum);
			holder.days=(TextView) convertView.findViewById(R.id.days);
			holder.budget=(TextView) convertView.findViewById(R.id.budget);
			holder.pay=(TextView) convertView.findViewById(R.id.pay);
			holder.price=(TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		holder.privateID.setText("¶©µ¥ºÅ£º"+item.getPrivateID());
		holder.destination.setText(item.getDestination());
		holder.startPlace.setText(item.getStartPlace());
		holder.startTime.setText(item.getStartDate());
		holder.price.setText("£¤"+item.getBudget()*item.getPersonNum());
		holder.description.setText(item.getDescription());
		holder.privateIndentTime.setText(item.getPrivateIndentTime());
		holder.budget.setText("£¤"+item.getBudget());
		holder.personNum.setText(""+item.getPersonNum());
		holder.days.setText(""+item.getDays());
		return convertView;
	}
	static class ViewHolder {  
        public TextView privateID;  
        public TextView destination;  
        public TextView startPlace;  
        public TextView startTime;  
        public TextView description;  
        public TextView privateIndentTime;  
        public TextView privateIndentState;  
        public TextView personNum;  
        public TextView days;  
        public TextView budget;  
        public TextView pay;  
        public TextView price;  
    }  
}
