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
//			String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/̤̤����";
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
//				Toast.makeText(this, "����ɹ�", Toast.LENGTH_SHORT).show();
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
		.showImageOnLoading(R.drawable.blank) // ����ͼƬ�������ڼ���ʾ��ͼƬ
		.showImageForEmptyUri(R.drawable.blank)// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
		.showImageOnFail(R.drawable.blank) // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
		.cacheInMemory(true)// �������ص�ͼƬ�Ƿ񻺴����ڴ���
		.cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����  
		.considerExifParams(true) // �Ƿ���JPEGͼ��EXIF��������ת����ת��
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// ����ͼƬ����εı��뷽ʽ��ʾ
		.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)// ����ͼƬ�Ľ�������//
		// .decodingOptions(android.graphics.BitmapFactory.Options
		// decodingOptions)//����ͼƬ�Ľ�������
		// .delayBeforeLoading(int delayInMillis)//int
		// delayInMillisΪ�����õ�����ǰ���ӳ�ʱ��
		// ����ͼƬ���뻺��ǰ����bitmap��������
		// .preProcessor(BitmapProcessor preProcessor)
		.resetViewBeforeLoading(true)// ����ͼƬ������ǰ�Ƿ����ã���λ
		.displayer(new RoundedBitmapDisplayer(20))// �Ƿ�����ΪԲ�ǣ�����Ϊ����
		.displayer(new FadeInBitmapDisplayer(100))// �Ƿ�ͼƬ���غú���Ķ���ʱ��
		.build();// �������
	}		

}
