package com.tata.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tata.R;
import com.tata.adapterAndListener.GuideAdapter;
import com.tata.adapterAndListener.GuidePageChangeListener;
import com.tata.utils.ToastUtil;

public class GuideActivity extends Activity {

    private ViewPager viewPager;
    private ImageView[] imageViews=new ImageView[5];
    private ArrayList<View> advPics;
	private TextView enter;
	private Timer time = new Timer();
	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler.sendEmptyMessage(0);
		}
	};
	private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if(getSharedPreferences("user", 0).getString("phoneNumber", "").equals("")){
        	setContentView(R.layout.activity_guide);
            initView();
            initData();
            viewPager.setAdapter(new GuideAdapter(advPics));
            viewPager.setOnPageChangeListener(new GuidePageChangeListener(imageViews));
        }else {
        	setContentView(R.layout.guide_start2);
        	  handler = new Handler() {
      			@Override
      			public void handleMessage(Message msg) {
      					Intent intent = new Intent(GuideActivity.this,
      							MainActivity.class);
      					startActivity(intent);
      					overridePendingTransition(R.anim.slide_left_in,
      							R.anim.slide_left_out);
      					finish();
      				}
      		};
      		time.schedule(task, 1300);
		}
    }

    private void initView(){
         viewPager= (ViewPager) findViewById(R.id.viewPager);
         imageViews[0]=(ImageView) findViewById(R.id.img1);
         imageViews[1]=(ImageView) findViewById(R.id.img2);
         imageViews[2]=(ImageView) findViewById(R.id.img3);
         imageViews[3]=(ImageView) findViewById(R.id.img4);
         imageViews[4]=(ImageView) findViewById(R.id.img5);
      }

    private void initData() {
        advPics = new ArrayList<View>();
        ImageView view1 = new ImageView(this);
        view1.setBackgroundResource(R.drawable.guide1);
        advPics.add(view1);
        ImageView view2 = new ImageView(this);
        view2.setBackgroundResource(R.drawable.guide2);
        advPics.add(view2);
        ImageView view3 = new ImageView(this);
        view3.setBackgroundResource(R.drawable.guide3);
        advPics.add(view3);
        ImageView view4 = new ImageView(this);
        view4.setBackgroundResource(R.drawable.guide4);
        advPics.add(view4);
        View view5=LayoutInflater.from(this).inflate(R.layout.guide3, null);
        enter=(TextView)view5.findViewById(R.id.enter);
        advPics.add(view5);
        enter.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(GuideActivity.this,MainActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			finish();
			
			}
		});
    }


}
