package com.tata.activity;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


import java.io.Serializable;
import java.util.ArrayList;

import com.tata.R;
import com.tata.bean.DataPacket;
import com.tata.utils.AppManager;


public class BaseActivity extends Activity implements OnClickListener{
	/**
	 * 上个页面传值标志
	 */
	public static final String DATA_PACKET_NAME = "Intent_Data_Packet";

	/**
	 * 上个页面传值标志
	 */
	public static final String DATA_PACKET_SIZE = "DATA_PACKET_SIZE";
	public static final String DATA_OBJECT_SIZE = "DATA_OBJECT_SIZE";
	public TextView btn_left;//标题栏左边按钮
	public TextView btn_right;//标题栏右边按钮
	protected View layout_top;//标题栏
	private TextView tv_title;//标题栏文字
	private LinearLayout centerLayout;//中心布局
	private long exitTime = 0;// 退出时间
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_base);
		AppManager.getAppManager().addActivity(this);
		initProcess();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 加载流程
	 */
	protected void initProcess() {
		initData();
		initSeriData();
		initUI();
		initListener();
	}
	/**
	 * 初始化数据
	 */
	protected void initData() {
		Intent intent = getIntent();
		if (intent != null) {
			int dataPacketSize = intent.getIntExtra(DATA_PACKET_SIZE, -1);
			if (dataPacketSize > 0) {
				ArrayList<DataPacket> dataPackets = new ArrayList<DataPacket>();
				for (int i = 0; i < dataPacketSize; i++) {
					String tag = DATA_PACKET_NAME + i;
					DataPacket tempData = (DataPacket) intent
							.getSerializableExtra(tag);
					if (tempData != null) {
						dataPackets.add(tempData);
					}
				}
				if (dataPackets.size() > 0) {
					receiveDataFromPrevious(dataPackets);
				}
			}
		}
	}
	/**
	 *从上一个页面受到的数据 list
	 *
	 */
	protected void receiveDataFromPrevious(ArrayList<DataPacket> dataLists) {
		// TODO Auto-generated method stub

	}

	/**
	 * 从上一个页面受到的数据 object
	 * 

	 */
	protected void receiveDataFromPrevious(Object[] objects) {
		// TODO Auto-generated method stub

	}

	/**
	 * 初始化监听
	 */
	protected void initListener() {
	}
	/**
	 * 初始化serizes
	 */
	protected void initSeriData() {
		Intent intent = getIntent();
		if (intent != null) {
			int dataPacketSize = intent.getIntExtra(DATA_OBJECT_SIZE, -1);
			if (dataPacketSize > 0) {
				Object[] objects = new Object[dataPacketSize];
				for (int i = 0; i < dataPacketSize; i++) {
					String tag = DATA_PACKET_NAME + i;
					Object tempData = intent.getSerializableExtra(tag);
					if (tempData != null) {
						objects[i] = tempData;
					}
				}
				if (objects.length > 0) {
					receiveDataFromPrevious(objects);
				}
			}

		}
	}
	/**
	 * 初始化ui 子类findviewbyid都要写在这里
	 */
	protected void initUI() {
		layout_top = findViewById(R.id.title_layout);
		centerLayout = (LinearLayout) findViewById(R.id.centerlayout);
		tv_title = (TextView) layout_top.findViewById(R.id.tv_center);
		btn_left = (TextView) layout_top.findViewById(R.id.bt_left);
		btn_right = (TextView) layout_top.findViewById(R.id.bt_right);
		btn_left.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	/**
	 * 设置标题可见
	 * 
	 * @param visable
	 */
	protected void setTopVisable(int visable) {
		if (layout_top != null) {
			layout_top.setVisibility(visable);
		}
	}
	protected void setLeftVisable(int visable){
		btn_left.setVisibility(visable);
	}

	/**
	 * 隐藏标题
	 */
	protected void hideTop() {
		setTopVisable(View.GONE);
	}

	/**
	 * 显示标题
	 */
	protected void showTop() {
		setTopVisable(View.VISIBLE);
	}
	/**
	 * 设置标题栏右边按钮可见性
	 * 
	 * @param visibility
	 */
	protected void setRightButton(int visibility) {
		btn_right.setVisibility(visibility);
	}
	/**
	 * 设置标题栏右边按钮文字
	 * 
	 * @param text
	 */
	protected void setRightButton(String text) {
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText(text);
		btn_right.setTextColor(Color.WHITE);
		btn_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		btn_right.setBackgroundColor(Color.TRANSPARENT);
	}
	
	protected void setRightClick(){
		btn_right.setOnClickListener(this);
	}
	
	protected void setnoRightClick(){
		btn_right.setOnClickListener(null);
	}
	
	
	
	protected void setRightColor(int color) {
		btn_right.setTextColor(color);
	}
	
	protected void setRightSize(int size){
		btn_right.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
	}
	
	protected void setRightPic(int resid) {
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setBackgroundResource(resid);
	}
	/**
	 *设置标题文字
	 * 
	 * @param text
	 *         文字
	 */

	protected void setTopText(CharSequence text) {
		tv_title.setText(text);
	}
	public void hideSoftInput(View view) {
		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard();
					return false;
				}

			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				hideSoftInput(innerView);
			}
		}
	}

	private void hideSoftKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		View currentFocus = getCurrentFocus();
		if (currentFocus != null) {
			imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
		}
	}
	/**
	 * 获得中心布局
	 * 
	 * @return
	 */
	public LinearLayout getCenterViewlayout() {
		return centerLayout;
	}

	/**
	 * 设置中心布局
	 * 
	 * @param centerView
	 */
	public void setCenterView(View centerView) {
		centerLayout.removeAllViews();
		hideSoftInput(centerView);
		centerLayout.addView(centerView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	/**
	 *设置中心布局
	 * 
	 * @param
	 */
	public void setCenterView(int layout) {
		centerLayout.removeAllViews();
		LayoutInflater inflater = getLayoutInflater();
		View addView = inflater.inflate(layout, null);
		hideSoftInput(addView);
		centerLayout.addView(addView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	/**
	 * 跳转到另外一个activity
	 * 
	 * @param nextActivity
	 *            下个Activity
	 * @param objects
	 *            单项数据集合
	 */
	public void startToNextActivity(Class<?> nextActivity,
			Serializable... objects) {
		Intent intent = new Intent(this, nextActivity);
		if (objects != null && objects.length > 0) {
			intent.putExtra(DATA_OBJECT_SIZE, objects.length);
			for (int i = 0; i < objects.length; i++) {
				String tag = DATA_PACKET_NAME + i;
				intent.putExtra(tag, objects[i]);
			}
		}
		startActivity(intent);
	}

	/**
	 * 跳转到另外一个activity
	 * 
	 * @param nextActivity
	 *            下个Activity
	 * @param requestCode
	 *            回调请求码
	 * @param objects
	 *            单项数据集合
	 */
	public void startToNextActivityForResult(Class<?> nextActivity,
			int requestCode, Serializable... objects) {
		Intent intent = new Intent(this, nextActivity);
		if (objects != null && objects.length > 0) {
			intent.putExtra(DATA_OBJECT_SIZE, objects.length);
			for (int i = 0; i < objects.length; i++) {
				String tag = DATA_PACKET_NAME + i;
				intent.putExtra(tag, objects[i]);
			}
		}
		startActivityForResult(intent, requestCode);
	}

	/**
	 * 跳转到另外一个activity
	 * 
	 * @param nextActivity
	 *            下一个Activity
	 * @param dataPacket
	 *            数据包
	 */

	public void startToNextActivity(Class<?> nextActivity,
			DataPacket... dataPacket) {
		Intent intent = new Intent(this, nextActivity);
		if (dataPacket != null && dataPacket.length > 0) {
			intent.putExtra(DATA_PACKET_SIZE, dataPacket.length);
			for (int i = 0; i < dataPacket.length; i++) {
				String tag = DATA_PACKET_NAME + i;
				intent.putExtra(tag, dataPacket[i]);
			}
		}
		startActivity(intent);
	}

	/**
	 * 跳转到另外一个activity
	 * 
	 * @param nextActivity
	 *            下一个Activity
	 * @param requestCode
	 *            回调请求码
	 * @param dataPacket
	 *            数据包
	 */

	public void startToNextActivityForResult(Class<?> nextActivity,
			int requestCode, DataPacket... dataPacket) {
		Intent intent = new Intent(this, nextActivity);
		if (dataPacket != null && dataPacket.length > 0) {
			intent.putExtra(DATA_PACKET_SIZE, dataPacket.length);
			for (int i = 0; i < dataPacket.length; i++) {
				String tag = DATA_PACKET_NAME + i;
				intent.putExtra(tag, dataPacket[i]);
			}
		}
		startActivityForResult(intent, requestCode);
	}

	/**
	 * 跳转到下一个activity
	 * 
	 * @param nextActivity
	 */
	public void startToNextActivity(Class<?> nextActivity) {
		startToNextActivity(nextActivity, new DataPacket[] {});
	}
	/**
	 * 程序退出提示：“再按一次退出程序”
	 */
	public void toastPlayForExit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			toastPlay("再按一次退出程序", getApplicationContext());
			exitTime = System.currentTimeMillis();
		} else {
			AppManager.getAppManager().AppExit(this);
		}
	}
	/**
	 * toast消息提示
	 * 
	 * @param str
	 * @param context
	 */
	public void toastPlay(String str, Context context) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		 overridePendingTransition(0, R.anim.slide_right_out); 
	}
}
