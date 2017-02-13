package com.tata.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.adapterAndListener.GuideAdapter;
import com.tata.utils.Tools;
import com.tata.view.PhotoView;
import com.tata.view.ViewPagerFixed;
import com.tata.view.PhotoViewAttacher.OnPhotoTapListener;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShowMyPicture extends Activity implements OnPhotoTapListener{

	private String[] imageUrls;
	private ViewPagerFixed viewPager;
	private TextView number;
	private int num;
	private int position;
	private List<View> views=new ArrayList<View>();
	private DisplayImageOptions options;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageUrls=getIntent().getStringArrayExtra("imageUrls");
		num=imageUrls.length;
		setContentView(R.layout.activity_show_pic);
		viewPager=(ViewPagerFixed) findViewById(R.id.imagePager);
		initView();
		viewPager.setAdapter(new GuideAdapter(views));
		viewPager.setCurrentItem(0);
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
	
	public void savePic(View v){
//		if(Tools.hasSdcard()){
//			String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/踏踏旅游";
//		    File file=new File(path);
//		    
//		    if(!file.exists()){
//					file.mkdir();
//		    }else {
//		    	String picname=new Date().getTime()+".jpg";
//				File picFile=new File(path,picname);
//				try {
//					BufferedOutputStream bos = new BufferedOutputStream(
//							new FileOutputStream(picFile));
//					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//				bos.flush();
//				bos.close();
//				Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
	}

	private void initView() {
		number=(TextView) findViewById(R.id.number);
		number.setText((position+1)+"/"+num);
		ImageView picture;
		for (int i = 0; i < imageUrls.length; i++) {
//		FrameLayout layout=new FrameLayout(this);
//		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT));
//		final ProgressBar progressBar=(ProgressBar) LayoutInflater.from(this).inflate(R.layout.myprogressbar, null);
        PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		ImageLoader.getInstance().displayImage(imageUrls[i],
				img, options);
		img.setOnPhotoTapListener(this);
        views.add(img);
				} 
	}

	public void onPhotoTap(View view, float x, float y) {
		// TODO Auto-generated method stub
		finish();
	}

	private void initOption() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.blank) // 设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.blank)// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.blank) // 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
		.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)// 设置图片的解码类型//
		// .decodingOptions(android.graphics.BitmapFactory.Options
		// decodingOptions)//设置图片的解码配置
		// .delayBeforeLoading(int delayInMillis)//int
		// delayInMillis为你设置的下载前的延迟时间
		// 设置图片加入缓存前，对bitmap进行设置
		// .preProcessor(BitmapProcessor preProcessor)
		.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
		.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
		.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
		.build();// 构建完成
	}		

}
