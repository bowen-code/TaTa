package com.tata.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.adapterAndListener.MyShareAdapter;
import com.tata.adapterAndListener.TimeAdapter;
import com.tata.bean.ShareMessage;
import com.tata.bean.TimeItem;
import com.tata.utils.Constants;
import com.tata.utils.MainApplication;
import com.tata.utils.ToastUtil;
import com.tata.utils.jsonTools;
import com.tata.view.CircleImageView;
import com.tata.view.MyDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class PersonActivity extends BaseActivity implements OnItemClickListener{

	private String phoneNumber;
	private String myphoneNumber;
	private String userName;
    private ListView shareListView;
    private List<TimeItem> list;
	private Dialog dialog;
	CircleImageView figure;
	private TextView username;
	protected LinkedList<ShareMessage> myWriteData;
	private DisplayImageOptions options;
	private int index;
	protected TimeAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_person);
		phoneNumber=getIntent().getStringExtra("phoneNumber");
		userName=getIntent().getStringExtra("userName");
		myphoneNumber=getSharedPreferences(
				"user", Activity.MODE_PRIVATE).getString("phoneNumber", "");
		if(myphoneNumber.equals(phoneNumber)){
			setTopText("我的主页");
		}else {
			setTopText(userName+"的主页");
		}
		Constants.deleteList.clear();
		Constants.collectList.clear();
		init();
		dialog = new MyDialog(this).myDialog();
		dialog.show();
		AsyncHttpClient httpClient=new AsyncHttpClient();
		RequestParams params=new RequestParams();
		params.put("phoneNumber", phoneNumber);
		params.put("num",1);
		String url="http://120.24.254.127/tata/data/getMyShare.php";
		httpClient.post(url, params, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				System.out.println(arg2);
				if(myphoneNumber.equals("")){
					dialog.dismiss();
					ToastUtil.show(PersonActivity.this,"请先登录");
					 Intent intent = new Intent(PersonActivity.this, LoginActivity.class);
                	 startActivity(intent);
                	 overridePendingTransition(R.anim.slide_left_in,
                			 R.anim.slide_left_out);
                	 finish();
					return;
				}
				ShareMessage shareMessage;
				myWriteData=jsonTools.getMyShareMessages(arg2);
				if(myWriteData.get(0).getShareID()!=-1){
				for (int i = 0; i < myWriteData.size(); i++) {
					shareMessage=myWriteData.get(i);
					String time=shareMessage.getTime().substring(0, 10).replace('-', '/');
					if(shareMessage.getType()==1){
						list.add(new TimeItem(1, time.substring(0, 5), time.substring(5), shareMessage.getContent(), shareMessage.getShareImg()));
					}else {
						list.add(new TimeItem(0, time.substring(0, 5), time.substring(5), shareMessage.getContent()));
					}
				}
				}
				ImageLoader.getInstance().displayImage(myWriteData.get(0).getUserImg(), figure, options);
				
				 adapter=new TimeAdapter(list, PersonActivity.this,options);
			       shareListView.setAdapter(adapter);
				dialog.dismiss();
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
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
	private void init() {
		username=(TextView) findViewById(R.id.username);
		username.setText(userName);
		list=new ArrayList<TimeItem>();
       shareListView=(ListView) findViewById(R.id.shareList);
       shareListView.setOnItemClickListener(this);
       figure=(CircleImageView) findViewById(R.id.figure);
	}
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, DetailShareActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("share", myWriteData.get(position));
		intent.putExtras(bundle);
		index=position;
		startActivityForResult(intent, position);
		overridePendingTransition(R.anim.slide_left_in,
				R.anim.slide_left_out);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==index&&Constants.delete&&phoneNumber.equals(myphoneNumber)){
			list.remove(index);
			myWriteData.remove(index);
			adapter.notifyDataSetChanged();
			Constants.deleteList.add(myWriteData.get(index).getShareID());
			Intent mIntent = new Intent("delete.data.person.myself");
			sendBroadcast(mIntent);
		}
	}


}
