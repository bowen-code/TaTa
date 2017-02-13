package com.tata.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tata.R;
import com.tata.adapterAndListener.GuideAdapter;
import com.tata.utils.Constants;
import com.tata.utils.Tools;
import com.tata.view.MyViewPager;
import com.tata.view.PhotoView;
import com.tata.view.MyViewPager.OnTouchListener;
import com.tata.view.PhotoViewAttacher.OnPhotoTapListener;
import com.tata.view.ViewPagerFixed;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowPicActivity extends Activity implements OnPhotoTapListener{

	private ViewPagerFixed viewPager;
	private List<View> views=new ArrayList<View>();
	Bitmap bitmap;
	private TextView number;
	List<byte[]> data;
	private int position;
	private int num;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Intent intent=getIntent();
			data=Constants.data;
			num=data.size();
			position=intent.getIntExtra("position", 0);
			setContentView(R.layout.activity_show_pic);
			viewPager=(ViewPagerFixed) findViewById(R.id.imagePager);
			initView();
			viewPager.setAdapter(new GuideAdapter(views));
			viewPager.setCurrentItem(position);
			viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					number.setText((arg0+1)+"/"+num);
					PhotoView photoView=(PhotoView) views.get(arg0);
					photoView.zoomTo(1.0f, 0.5f, 0.5f);
				}
				
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					
				}
				
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}

		private void initView() {
			number=(TextView) findViewById(R.id.number);
			number.setText((position+1)+"/"+num);
			ImageView picture;
			for (int i = 0; i < data.size(); i++) {
//            picture=new ImageView(this);
            bitmap=BitmapFactory.decodeByteArray(data.get(i), 0, data.get(i).length);
//            picture.setImageBitmap(bitmap);
            PhotoView img = new PhotoView(this);
    		img.setBackgroundColor(0xff000000);
    		img.setImageBitmap(bitmap);
    		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
    				LayoutParams.MATCH_PARENT));
    		img.setOnPhotoTapListener(this);
            views.add(img);
					} 
		}

		public void savePic(View v){
			if(Tools.hasSdcard()){
				String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/踏踏旅游";
			    File file=new File(path);
			    
			    if(!file.exists()){
						file.mkdir();
			    }else {
			    	String picname=new Date().getTime()+".jpg";
					File picFile=new File(path,picname);
					try {
						BufferedOutputStream bos = new BufferedOutputStream(
								new FileOutputStream(picFile));
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					bos.flush();
					bos.close();
					Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		public void onPhotoTap(View view, float x, float y) {
			// TODO Auto-generated method stub
			finish();
		}


}
