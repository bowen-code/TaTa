package com.tata.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.activity.MoreDate.Holder;
import com.tata.utils.Constants;
import com.tata.utils.ToastUtil;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MoreDate extends BaseActivity implements OnItemClickListener{

	private ArrayList<String> data;
    private GridView date;
    int selectDay=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_more_date);
		setTopText("选择日期");
		data=(ArrayList<String>)getIntent().getSerializableExtra("date");
		System.out.println(data.toString());
		date=(GridView) findViewById(R.id.date);
		date.setSelector(new ColorDrawable(Color.TRANSPARENT));
		date.setOnItemClickListener(this);
		date.setAdapter(new MyAdapter(data));
	}
	
	class MyAdapter extends BaseAdapter {

		private List<String> data;
		private Holder holder;
		public MyAdapter(List<String> data) {
			this.data = data;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
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
				if (convertView == null) {
					convertView = getLayoutInflater().inflate(
							R.layout.date_textview, null);
					holder = new Holder();
					holder.time = (TextView) convertView
							.findViewById(R.id.time);

					convertView.setTag(holder);

				} else {
					holder = (Holder) convertView.getTag();
				}
				holder.time.setText(data.get(position));
				return convertView;
			}
	}
	
	static class Holder{
		private TextView time;
	}

	public void onItemClick(AdapterView<?> parent, View view, final int position,
			long id) {
		if(selectDay!=-1){
			parent.getChildAt(selectDay).setBackgroundColor(getResources().getColor(R.color.time_bg));
		}
		parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.gray));
		selectDay=position;
		final AlertDialog dlg3 = new AlertDialog.Builder(this).create();
		dlg3.show();
		Window window3 = dlg3.getWindow();
		window3.setContentView(R.layout.confirm_time_window);
		window3.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						dlg3.dismiss();
					}
				});
		window3.findViewById(R.id.confirm).setOnClickListener(
				new OnClickListener() {

					public void onClick(View v) {
						dlg3.dismiss();
						ToastUtil.show(MoreDate.this,"选择了"+data.get(position));
						Constants.startTime=data.get(position);
						finish();
					}
				});
		
	}


}
