package com.tata.adapterAndListener;

import java.util.List;
import java.util.zip.Inflater;

import org.apache.http.Header;

import com.baidu.android.bbalbs.common.a.c;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.R.color;
import com.tata.activity.OrderMessageActivity;
import com.tata.activity.PayActivity;
import com.tata.adapterAndListener.CurrentPrivateAdapter.ViewHolder;
import com.tata.bean.PrivateOrder;
import com.tata.bean.Reserve;
import com.tata.utils.jsonTools;
import com.tata.view.MyDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CurrentPrivateAdapter extends BaseAdapter {
 
	private List<PrivateOrder> list;
	private Context mContext;
	private ViewHolder holder;
	private SharedPreferences sp;
	public CurrentPrivateAdapter(List<PrivateOrder> list,Context context) {
		this.list = list;
		mContext=context;
		sp = context.getSharedPreferences("user", context.MODE_PRIVATE);
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.private_current_item, null);
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
			holder.cancel=(TextView) convertView.findViewById(R.id.cancel);
			holder.price=(TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		holder.privateID.setText("订单号："+item.getPrivateID());
		holder.destination.setText(item.getDestination());
		holder.startPlace.setText(item.getStartPlace());
		holder.startTime.setText(item.getStartDate());
		holder.price.setText("￥"+item.getBudget()*item.getPersonNum());
		if(item.getPrivateIndentState()==1){
			holder.privateIndentState.setText("待处理");
			holder.pay.setBackgroundColor(Color.GRAY);
		}else {
			holder.privateIndentState.setText("已处理");
			holder.pay.setBackgroundColor(mContext.getResources().getColor(R.color.pay));
			holder.pay.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
	                Intent intent=new Intent(mContext, PayActivity.class);
	                Bundle bundle=new Bundle();
	             	bundle.putInt("privateID",item.getPrivateID());
	             	bundle.putString("priceNum", "￥"+item.getBudget()*item.getPersonNum());
	             	bundle.putString("travelTime", "出发时间："+item.getStartDate()+" 游玩"+item.getDays()+"天");
	             	intent.putExtras(bundle);
	                mContext.startActivity(intent);
	               ((Activity) mContext).overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				}
			});
		}
		holder.description.setText(item.getDescription());
		holder.privateIndentTime.setText(item.getPrivateIndentTime());
		holder.budget.setText("￥"+item.getBudget());
		holder.personNum.setText(""+item.getPersonNum());
		holder.days.setText(""+item.getDays());
		holder.cancel.setOnClickListener(new OnClickListener() {
			
			private Dialog dialog;

			public void onClick(View v) {
				String url = "http://120.24.254.127/tata/data/deletePrivateIndent.php";
				AsyncHttpClient httpClient = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("phoneNumber", sp.getString("phoneNumber", ""));
				params.put("privateID",item.getPrivateID());
				dialog = new MyDialog(mContext).myDialog();
				dialog.show();
				httpClient.post(url, params, new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						// TODO Auto-generated method stub
						System.out.println(arg2);
						list.remove(item);
						notifyDataSetChanged();
						dialog.dismiss();
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						// TODO Auto-generated method stub
						System.out.println(arg2);
						dialog.dismiss();
					}
				});
			}
		});
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
        public TextView cancel;  
    }  
}
