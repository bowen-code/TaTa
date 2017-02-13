package com.tata.utils;



import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.R.integer;
import android.app.Application;
import android.graphics.Bitmap.CompressFormat;
public class MainApplication extends Application {
    public static String location;
    public static List<String> collectProducts=new ArrayList<String>();
    public static List<Integer> collectShares=new ArrayList<Integer>();
    public static String Lng;//经度
    public static String Lat;//纬度
    public static String location_city;
    public static int num;
    public static String IP="http://120.24.254.127";
    public static boolean longClick=false;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
        location="武汉大学";
        num=1;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration  
        	    .Builder(getApplicationContext())  
        	    .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
        	    .threadPriority(Thread.NORM_PRIORITY - 2)  
        	    .denyCacheImageMultipleSizesInMemory()  
        	    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
        	    .memoryCacheSize(2 * 1024 * 1024)    
        	    .discCacheSize(50 * 1024 * 1024)    
        	    .tasksProcessingOrder(QueueProcessingType.FIFO)  
        	    .discCacheFileCount(100) //缓存的文件数量  
        	    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
        	    .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
        	    .writeDebugLogs() // Remove for release app  
        	    .build();//开始构建  
        	    // Initialize ImageLoader with configuration.  
		ImageLoader.getInstance().init(config);// 全局初始化此配置
	}
}
