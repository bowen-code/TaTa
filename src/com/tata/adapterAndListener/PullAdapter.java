package com.tata.adapterAndListener;

import java.util.LinkedList;

import com.tata.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PullAdapter extends BaseAdapter {

	private LinkedList<String> linkedList;
	private LayoutInflater mInflater;
	
	public PullAdapter(LinkedList<String> linkedList, Context context) {
		mInflater = LayoutInflater.from(context);
		this.linkedList = linkedList;
	}
	
	public int getCount() {
		return linkedList.size();
	}

	public Object getItem(int position) {
		return linkedList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.layout_main_listitem, null);
			holder.textView = (TextView) convertView.findViewById(R.id.textView);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(linkedList.size()>0){
			final String dataStr = linkedList.get(position);
			holder.textView.setText(dataStr);
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView textView;        //数据显示区域
	}
}
