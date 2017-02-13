package com.tata.adapterAndListener;

import java.util.List;
import java.util.zip.Inflater;

import com.tata.R;
import com.tata.activity.PayActivity;
import com.tata.activity.ProductActivity;
import com.tata.activity.ReserveDetailActivity;
import com.tata.adapterAndListener.ReservePingAdapter.ViewHolder;
import com.tata.bean.Reserve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReservePingAdapter extends BaseAdapter {
 
	private List<Reserve> list;
	private Context mContext;
	private ViewHolder holder;
	public ReservePingAdapter(List<Reserve> list,Context context) {
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.reserve_history_item, null);
			holder.time=(TextView) convertView.findViewById(R.id.time);
			holder.location=(TextView) convertView.findViewById(R.id.location);
			holder.ticketName=(TextView) convertView.findViewById(R.id.ticketName);
			holder.ticketsum=(TextView) convertView.findViewById(R.id.ticketSum);
			holder.validTime=(TextView) convertView.findViewById(R.id.validTime);
			holder.price=(TextView) convertView.findViewById(R.id.price);
			holder.ping=(TextView) convertView.findViewById(R.id.ping);
			holder.state=(TextView) convertView.findViewById(R.id.state);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		holder.time.setText(item.getIndentTime());
		if(item.gettitle().length()>15){
			holder.location.setText(item.gettitle().substring(0, 15)+"����");
		}else {
			holder.location.setText(item.gettitle());
		}
		if(item.gettitle().length()>20){
			holder.ticketName.setText(item.gettitle().substring(0, 20)+"����");
		}else {
			holder.ticketName.setText(item.gettitle());
		}
		holder.ticketsum.setText(item.getTicketsum()+"��");
		holder.validTime.setText("");
		holder.price.setText("��"+item.getPrice());
		holder.ping.setText("�鿴����");
		if(item.getIndentState()==3){
			holder.state.setText("���˿�");
		}else {
			holder.state.setText("�ѳ���");
		}
		holder.ping.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(mContext, ReserveDetailActivity.class);
	             intent.putExtra("reserve",item);
				 mContext.startActivity(intent);
	               ((Activity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
//				Intent intent=new Intent(mContext, ProductActivity.class);
//				intent.putExtra("productID",item.getProductID()); 
//				intent.putExtra("isAgainProduct",true);
//				mContext.startActivity(intent);
//				((Activity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
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
        public TextView state;  
        public TextView ping;  
    }  
}
