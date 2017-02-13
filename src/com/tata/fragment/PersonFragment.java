package com.tata.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tata.R;
import com.tata.activity.AdviceActivity;
import com.tata.activity.LoginActivity;
import com.tata.activity.MyCollectActivity;
import com.tata.activity.OrderMessageActivity;
import com.tata.activity.MyReserveActivity;
import com.tata.activity.MyWalletActivity;
import com.tata.activity.PersonActivity;
import com.tata.activity.XiuGaiActivity;
import com.tata.utils.Constants;
import com.tata.utils.ToastUtil;
import com.tata.version.Common;
import com.tata.version.DownloadProgressListener;
import com.tata.version.DownloadReceiver;
import com.tata.version.FileDownloader;
import com.tata.view.CircleImageView;
import com.tata.view.MyDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonFragment extends Fragment implements OnClickListener {
	private RelativeLayout advice,myWallet,privateOrder,myReserve,versionUpdate;
	private LinearLayout myshare,mycollect;
    private TextView username;
	private int request_login_code=0;
	private CircleImageView figure;
	private DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private SharedPreferences mySharedPreferences;
	private String name;
	private String userImgUrl;
	private String phoneNumber;
	private boolean update;
	private ImageView newVersion;
	long m_newVerCode; //最新版的版本号
	String m_newVerName; //最新版的版本名
	Handler m_mainHandler;
	    Intent intent;
	    String m_appNameStr; //下载到本地要给这个APP命的名字
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.person, container, false);// 关联布局文件
		init(rootView);
		initOption();
		//注册广播  
        registerBoradcastReceiver(); 
		mySharedPreferences = getActivity().getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		name=mySharedPreferences.getString("userName", "");
		userImgUrl=mySharedPreferences.getString("userImgUrl", "");
		phoneNumber=mySharedPreferences.getString("phoneNumber", "");
		if(!name.equals("")){
			username.setText(name);
		}
		if(!userImgUrl.equals("")){
			ImageLoader.getInstance().displayImage(userImgUrl,
					figure, options);
		}
		m_mainHandler = new Handler();
		intent=new Intent(getActivity(),DownloadReceiver.class);
		update=mySharedPreferences.getBoolean("needUpdate", false);
		if(update){
			newVersion.setVisibility(View.VISIBLE);
		}else {
			new checkNewestVersionAsyncTask().execute();
		}
		return rootView;
	}
	
	class checkNewestVersionAsyncTask extends AsyncTask<Void, Void, Boolean>
	{
	
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(postCheckNewestVersionCommand2Server())
			{
				int vercode = Common.getVerCode(getActivity()); // 用到前面第一节写的方法  
		        System.out.println(vercode+""+m_newVerCode); 
				if (m_newVerCode > vercode) {  
		             return true;
		         } else {  
		             return false;
		         }  
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result) {//如果有最新版本
				mySharedPreferences.edit().putBoolean("needUpdate",true).commit();
				newVersion.setVisibility(View.VISIBLE);
			}else {
				mySharedPreferences.edit().putBoolean("needUpdate",false).commit();
			}
			super.onPostExecute(result);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	
	
	/**
	 * 从服务器获取当前最新版本号，如果成功返回TURE，如果失败，返回FALSE
	 * @return
	 */
	private Boolean postCheckNewestVersionCommand2Server()
	{
		StringBuilder builder = new StringBuilder();
		JSONArray jsonArray = null;
		try {
			// 构造POST方法的{name:value} 参数对
			List<NameValuePair> vps = new ArrayList<NameValuePair>();
			// 将参数传入post方法中
			vps.add(new BasicNameValuePair("action", "checkNewVersion"));
			builder = Common.post_to_server(vps);
			System.out.println(builder.toString());
			jsonArray = new JSONArray(builder.toString());
			if (jsonArray.length()>0) {
				if (jsonArray.getJSONObject(0).getInt("id") == 1) {
					m_newVerName = jsonArray.getJSONObject(0).getString("verName");
					m_newVerCode = jsonArray.getJSONObject(0).getLong("verCode");
					m_appNameStr="TaTa_"+m_newVerName+".apk";
					return true;
				}
			}
	
			return false;
		} catch (Exception e) {
//			Log.e("msg",e.getMessage());
			m_newVerName="";
			m_newVerCode=-1;
			return false;
		}
	}
	
	class checkNewestVersionAsyncTask2 extends AsyncTask<Void, Void, Boolean>
	{
	
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(postCheckNewestVersionCommand2Server())
			{
				int vercode = Common.getVerCode(getActivity()); // 用到前面第一节写的方法  
		        System.out.println(vercode+""+m_newVerCode); 
				if (m_newVerCode > vercode) {  
		             return true;
		         } else {  
		             return false;
		         }  
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if (result) {//如果有最新版本
				if(!mySharedPreferences.getBoolean("Updating", false)){
					File file=new File(Environment
			                .getExternalStorageDirectory(),m_appNameStr);
					if(file.exists()){
						dialog.dismiss();
						ToastUtil.show(getActivity(),"新版本已经下好，请安装");
						down();
						return;
					}
				doNewVersionUpdate(); // 更新新版本  
				}else {
					dialog.dismiss();
					ToastUtil.show(getActivity(),"已经在更新");
				}
			}else {
				dialog.dismiss();
				notNewVersionDlgShow(); // 提示当前为最新版本  
			}
			super.onPostExecute(result);
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	/**
	 * 提示更新新版本
	 */
	private void doNewVersionUpdate() {
		dialog.dismiss();
		int verCode = Common.getVerCode(getActivity());  
	    String verName = Common.getVerName(getActivity());  
	    
	    String str= "当前版本："+verName+" ,发现新版本："+m_newVerName+" ,是否更新？";  
	    Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("软件更新").setMessage(str)  
	            // 设置内容  
	            .setPositiveButton("更新",// 设置确定按钮  
	                    new DialogInterface.OnClickListener() {  
	                        public void onClick(DialogInterface dialog,  
	                                int which) { 
//	                            m_progressDlg.setTitle("正在下载");  
//	                            m_progressDlg.setMessage("请稍候...");  
//	                            downFile(Common.UPDATESOFTADDRESS);  //开始下载
	                        	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
	            					File dir = Environment.getExternalStorageDirectory();//文件保存目录
	            					mySharedPreferences.edit().putBoolean("Updating", true).commit();
	            					Constants.versionName=m_appNameStr;
	            					Toast.makeText(getActivity(), "开始下载", 0).show();
	            					download(Common.UPDATESOFTADDRESS+m_appNameStr, dir,1);
//	            					download(path2, dir,2); 
	            				}else{
	            					Toast.makeText(getActivity(), "没有SD卡", 1).show();
	            				}
	                        }  
	                    })  
	            .setNegativeButton("暂不更新",  
	                    new DialogInterface.OnClickListener() {  
	                        public void onClick(DialogInterface dialog,  
	                                int whichButton) {  
	                            // 点击"取消"按钮之后退出程序  
	                        }  
	                    }).create();// 创建  
	    // 显示对话框  
	    dialog.show();  
	}
	
	
	 private Handler handler = new Handler(){

			
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					int size = msg.getData().getInt("size");
//					downloadbar.setProgress(size);
//					float result = (float)downloadbar.getProgress()/ (float)downloadbar.getMax();
					int p = (int)((float)size/length*100);
					//要通知的notification中进度条的id
					int notifityId=msg.arg1;
					intent.putExtra("pro", p);
					intent.putExtra("id", notifityId);
					getActivity().sendBroadcast(intent);
					if(size==length){
						Toast.makeText(getActivity().getApplicationContext(), "下载成功", 1).show();
					    down();
					}
					break;

				case -1:
					Toast.makeText(getActivity().getApplicationContext(), "下载失败", 1).show();
					break;
				}
				
			}    	
	    };
	protected int length;
	private Dialog dialog;
	
	
	 //对于UI控件的更新只能由主线程(UI线程)负责，如果在非UI线程更新UI控件，更新的结果不会反映在屏幕上，某些控件还会出错
    private void download(final String path, final File dir,final int softid){
    	new Thread(new Runnable() {
			
			public void run() {
				try {
					
				 FileDownloader loader = new FileDownloader(getActivity(), path, dir, 3,softid);
					final FileDownloader loader2=loader;
					length = loader.getFileSize();//获取文件的长度
//					downloadbar.setMax(length);
					loader.download(new DownloadProgressListener(){
						
						public void onDownloadSize(int size) {//可以实时得到文件下载的长度
							Message msg = new Message();
							msg.what = 1;
							msg.arg1=loader2.notifityid;
							msg.getData().putInt("size", size);							
							handler.sendMessage(msg);
						}});
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = -1;
					msg.getData().putString("error", "下载失败");
					handler.sendMessage(msg);
				}
			}
		}).start();
    	
    }
		/**
		 *  提示当前为最新版本  
		 */
		private void notNewVersionDlgShow()
		{
			dialog.dismiss();
			int verCode = Common.getVerCode(getActivity());  
		    String verName = Common.getVerName(getActivity()); 
		    String str="当前版本已是最新版,无需更新!";
		    Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("软件更新")  
		            .setMessage(str)// 设置内容  
		            .setPositiveButton("确定",// 设置确定按钮  
		                    new DialogInterface.OnClickListener() {  
		                        public void onClick(DialogInterface dialog,  
		                                int which) {  
		                        }  
		                    }).create();// 创建  
		    // 显示对话框  
		    dialog.show();  
		}

		/**
		 * 告诉HANDER已经下载完成了，可以安装了
		 */
		private void down() {
	        m_mainHandler.post(new Runnable() {
	            public void run() {
//	                m_progressDlg.cancel();
	                update();
	            }
	        });
	}
		/**
		 * 安装程序
		 */
	    void update() {
	    	mySharedPreferences.edit().putBoolean("Updating", false).commit();
	    	mySharedPreferences.edit().putBoolean("needUpdate", false).commit();
	        Intent intent = new Intent(Intent.ACTION_VIEW);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        intent.setDataAndType(Uri.fromFile(new File(Environment
	                .getExternalStorageDirectory(), m_appNameStr)),
	                "application/vnd.android.package-archive");
	        startActivity(intent);
	    }
		
		
	private void initOption() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.face) // 设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.face)// 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.face) // 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
		.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)// 设置图片的解码类型//
		.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
		.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
		.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
		.build();// 构建完成
	}

	private void init(View rootView) {
		newVersion=(ImageView) rootView.findViewById(R.id.newVersion);
		username=(TextView) rootView.findViewById(R.id.username);
		advice = (RelativeLayout) rootView.findViewById(R.id.advice);
		advice.setOnClickListener(this);
		myWallet = (RelativeLayout) rootView.findViewById(R.id.myWallet);
		myWallet.setOnClickListener(this);
		privateOrder = (RelativeLayout) rootView.findViewById(R.id.privateOrder);
		privateOrder.setOnClickListener(this);
		myReserve = (RelativeLayout) rootView.findViewById(R.id.myReserve);
		myReserve.setOnClickListener(this);
		username.setOnClickListener(this);
		figure=(CircleImageView) rootView.findViewById(R.id.figure);
		figure.setOnClickListener(this);
		myshare=(LinearLayout) rootView.findViewById(R.id.myshare);
		myshare.setOnClickListener(this);
		mycollect=(LinearLayout) rootView.findViewById(R.id.mycollect);
		mycollect.setOnClickListener(this);
		versionUpdate=(RelativeLayout) rootView.findViewById(R.id.versionUpdate);
		versionUpdate.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.myshare:
			intent=new Intent(getActivity(),PersonActivity.class);
			intent.putExtra("phoneNumber",phoneNumber);
			intent.putExtra("userName", name);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.mycollect:
			intent=new Intent(getActivity(),MyCollectActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.advice:
//			ToastUtil.show(getActivity(),"敬请期待");
			intent=new Intent(getActivity(),AdviceActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.versionUpdate:
			dialog=new MyDialog(getActivity()).updateVersion();
			dialog.show();
			new checkNewestVersionAsyncTask2().execute();
			
			break;
		case R.id.myWallet:
			ToastUtil.show(getActivity(),"敬请期待");
//			intent=new Intent(getActivity(),MyWalletActivity.class);
//			startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.myReserve:
			intent=new Intent(getActivity(),MyReserveActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.privateOrder:
			intent=new Intent(getActivity(),OrderMessageActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.username:
			if(mySharedPreferences.getString("phoneNumber", "").equals("")){
				intent=new Intent(getActivity(),LoginActivity.class);
				startActivityForResult(intent, request_login_code);
				getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}else {
				intent=new Intent(getActivity(),XiuGaiActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
			break;
		case R.id.figure:
			if(!mySharedPreferences.getString("phoneNumber", "").equals("")){
				intent=new Intent(getActivity(),XiuGaiActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}else {
				intent=new Intent(getActivity(),LoginActivity.class);
				startActivityForResult(intent, request_login_code);
				getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
			break;
		}
	}
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		if(requestCode==request_login_code){
//			String name=mySharedPreferences.getString("userName", "");
//			String userImgUrl=mySharedPreferences.getString("userImgUrl", null);
//			if(!name.equals("")){
//				username.setText(name);
//			}
//			if(userImgUrl!=null){
//				ImageLoader.getInstance().displayImage(userImgUrl,
//						figure, options);
//			}
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if(action.equals("data.person.myself")){
            	mySharedPreferences = getActivity().getSharedPreferences(
            			"user", Activity.MODE_PRIVATE);
            	String name=mySharedPreferences.getString("userName", "");
            	if(name.equals("")){
            		username.setText("登录/注册");
            	}else{
            		username.setText(name);
				}
            	String userImgUrl=mySharedPreferences.getString("userImgUrl","");
    			if(userImgUrl.equals("")){
    				figure.setImageResource(R.drawable.face);
    			}else{
    				ImageLoader.getInstance().displayImage(userImgUrl,
    						figure, options);
				}
            }  
        }  
          
    };  
	
	 public void registerBoradcastReceiver(){
	        IntentFilter myIntentFilter = new IntentFilter();  
	        myIntentFilter.addAction("data.person.myself");  
	        //注册广播        
	        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);  
	    }  
}
