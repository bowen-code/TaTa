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
	long m_newVerCode; //���°�İ汾��
	String m_newVerName; //���°�İ汾��
	Handler m_mainHandler;
	    Intent intent;
	    String m_appNameStr; //���ص�����Ҫ�����APP��������
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.person, container, false);// ���������ļ�
		init(rootView);
		initOption();
		//ע��㲥  
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
				int vercode = Common.getVerCode(getActivity()); // �õ�ǰ���һ��д�ķ���  
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
			if (result) {//��������°汾
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
	 * �ӷ�������ȡ��ǰ���°汾�ţ�����ɹ�����TURE�����ʧ�ܣ�����FALSE
	 * @return
	 */
	private Boolean postCheckNewestVersionCommand2Server()
	{
		StringBuilder builder = new StringBuilder();
		JSONArray jsonArray = null;
		try {
			// ����POST������{name:value} ������
			List<NameValuePair> vps = new ArrayList<NameValuePair>();
			// ����������post������
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
				int vercode = Common.getVerCode(getActivity()); // �õ�ǰ���һ��д�ķ���  
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
			if (result) {//��������°汾
				if(!mySharedPreferences.getBoolean("Updating", false)){
					File file=new File(Environment
			                .getExternalStorageDirectory(),m_appNameStr);
					if(file.exists()){
						dialog.dismiss();
						ToastUtil.show(getActivity(),"�°汾�Ѿ��ºã��밲װ");
						down();
						return;
					}
				doNewVersionUpdate(); // �����°汾  
				}else {
					dialog.dismiss();
					ToastUtil.show(getActivity(),"�Ѿ��ڸ���");
				}
			}else {
				dialog.dismiss();
				notNewVersionDlgShow(); // ��ʾ��ǰΪ���°汾  
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
	 * ��ʾ�����°汾
	 */
	private void doNewVersionUpdate() {
		dialog.dismiss();
		int verCode = Common.getVerCode(getActivity());  
	    String verName = Common.getVerName(getActivity());  
	    
	    String str= "��ǰ�汾��"+verName+" ,�����°汾��"+m_newVerName+" ,�Ƿ���£�";  
	    Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("�������").setMessage(str)  
	            // ��������  
	            .setPositiveButton("����",// ����ȷ����ť  
	                    new DialogInterface.OnClickListener() {  
	                        public void onClick(DialogInterface dialog,  
	                                int which) { 
//	                            m_progressDlg.setTitle("��������");  
//	                            m_progressDlg.setMessage("���Ժ�...");  
//	                            downFile(Common.UPDATESOFTADDRESS);  //��ʼ����
	                        	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
	            					File dir = Environment.getExternalStorageDirectory();//�ļ�����Ŀ¼
	            					mySharedPreferences.edit().putBoolean("Updating", true).commit();
	            					Constants.versionName=m_appNameStr;
	            					Toast.makeText(getActivity(), "��ʼ����", 0).show();
	            					download(Common.UPDATESOFTADDRESS+m_appNameStr, dir,1);
//	            					download(path2, dir,2); 
	            				}else{
	            					Toast.makeText(getActivity(), "û��SD��", 1).show();
	            				}
	                        }  
	                    })  
	            .setNegativeButton("�ݲ�����",  
	                    new DialogInterface.OnClickListener() {  
	                        public void onClick(DialogInterface dialog,  
	                                int whichButton) {  
	                            // ���"ȡ��"��ť֮���˳�����  
	                        }  
	                    }).create();// ����  
	    // ��ʾ�Ի���  
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
					//Ҫ֪ͨ��notification�н�������id
					int notifityId=msg.arg1;
					intent.putExtra("pro", p);
					intent.putExtra("id", notifityId);
					getActivity().sendBroadcast(intent);
					if(size==length){
						Toast.makeText(getActivity().getApplicationContext(), "���سɹ�", 1).show();
					    down();
					}
					break;

				case -1:
					Toast.makeText(getActivity().getApplicationContext(), "����ʧ��", 1).show();
					break;
				}
				
			}    	
	    };
	protected int length;
	private Dialog dialog;
	
	
	 //����UI�ؼ��ĸ���ֻ�������߳�(UI�߳�)��������ڷ�UI�̸߳���UI�ؼ������µĽ�����ᷴӳ����Ļ�ϣ�ĳЩ�ؼ��������
    private void download(final String path, final File dir,final int softid){
    	new Thread(new Runnable() {
			
			public void run() {
				try {
					
				 FileDownloader loader = new FileDownloader(getActivity(), path, dir, 3,softid);
					final FileDownloader loader2=loader;
					length = loader.getFileSize();//��ȡ�ļ��ĳ���
//					downloadbar.setMax(length);
					loader.download(new DownloadProgressListener(){
						
						public void onDownloadSize(int size) {//����ʵʱ�õ��ļ����صĳ���
							Message msg = new Message();
							msg.what = 1;
							msg.arg1=loader2.notifityid;
							msg.getData().putInt("size", size);							
							handler.sendMessage(msg);
						}});
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = -1;
					msg.getData().putString("error", "����ʧ��");
					handler.sendMessage(msg);
				}
			}
		}).start();
    	
    }
		/**
		 *  ��ʾ��ǰΪ���°汾  
		 */
		private void notNewVersionDlgShow()
		{
			dialog.dismiss();
			int verCode = Common.getVerCode(getActivity());  
		    String verName = Common.getVerName(getActivity()); 
		    String str="��ǰ�汾�������°�,�������!";
		    Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("�������")  
		            .setMessage(str)// ��������  
		            .setPositiveButton("ȷ��",// ����ȷ����ť  
		                    new DialogInterface.OnClickListener() {  
		                        public void onClick(DialogInterface dialog,  
		                                int which) {  
		                        }  
		                    }).create();// ����  
		    // ��ʾ�Ի���  
		    dialog.show();  
		}

		/**
		 * ����HANDER�Ѿ���������ˣ����԰�װ��
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
		 * ��װ����
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
		.showImageOnLoading(R.drawable.face) // ����ͼƬ�������ڼ���ʾ��ͼƬ
		.showImageForEmptyUri(R.drawable.face)// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
		.showImageOnFail(R.drawable.face) // ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
		.cacheInMemory(true)// �������ص�ͼƬ�Ƿ񻺴����ڴ���
		.cacheOnDisc(true)//�������ص�ͼƬ�Ƿ񻺴���SD����  
		.considerExifParams(true) // �Ƿ���JPEGͼ��EXIF��������ת����ת��
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// ����ͼƬ����εı��뷽ʽ��ʾ
		.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)// ����ͼƬ�Ľ�������//
		.resetViewBeforeLoading(true)// ����ͼƬ������ǰ�Ƿ����ã���λ
		.displayer(new RoundedBitmapDisplayer(20))// �Ƿ�����ΪԲ�ǣ�����Ϊ����
		.displayer(new FadeInBitmapDisplayer(100))// �Ƿ�ͼƬ���غú���Ķ���ʱ��
		.build();// �������
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
//			ToastUtil.show(getActivity(),"�����ڴ�");
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
			ToastUtil.show(getActivity(),"�����ڴ�");
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
            		username.setText("��¼/ע��");
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
	        //ע��㲥        
	        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);  
	    }  
}
