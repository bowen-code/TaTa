package com.tata.activity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.statistics.NewAppReceiver;

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
import com.tata.utils.Bimp;
import com.tata.utils.FileUtils;
import com.tata.utils.ImageItem;
import com.tata.utils.MainApplication;
import com.tata.utils.ToastUtil;
import com.tata.utils.Tools;
import com.tata.view.CircleImageView;
import com.tata.view.MyDialog;
import com.tata.view.WLQQTimePicker;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class XiuGaiActivity extends BaseActivity {

	private ImageView figure;
	private TextView phone, username,school;
	private TextView gender, birthday, unlogin, changePwd;
	private Button complete;
	private String phoneNumber;
	private String userName;
	private SharedPreferences mySharedPreferences;
	private int genderInt;
	private String birthdayString = "";
	private PopupWindow pop;
	private LinearLayout ll_popup;
	private String genderString;
	private Dialog dialog;
	private DisplayImageOptions options;
	private String userImgUrl;
	private RelativeLayout figureLayout, userNameLayout, genderLayout,
			birthdayLayout,schoolLayout;
	private String schoolstring;
	private static final int CAMERA_REQUEST_CODE = 2;
	private static final int RESULT_REQUEST_CODE = 3;
	private static final int IMAGE_REQUEST_CODE = 4;
	private static  String IMAGE_FILE_NAME = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopText("个人信息");
		setCenterView(R.layout.activity_xiu_gai);
		IMAGE_FILE_NAME=new Date().getTime()+".jpg";
		mySharedPreferences = getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		userName = mySharedPreferences.getString("userName", "");
		schoolstring = mySharedPreferences.getString("school", "");
		phoneNumber = mySharedPreferences.getString("phoneNumber", "");
		birthdayString = mySharedPreferences.getString("birthday", "");
		genderInt = mySharedPreferences.getInt("gender", 3);
		userImgUrl = mySharedPreferences.getString("userImgUrl", "");
		if (genderInt == 0) {
			genderString = "男";
		} else if (genderInt == 1) {
			genderString = "女";
		} else {

		}
		initOption();
		init();
	}

	private void init() {
		figure = (ImageView) findViewById(R.id.figure);
		ImageLoader.getInstance().displayImage(userImgUrl, figure, options);
		figure.setOnClickListener(this);
		phone = (TextView) findViewById(R.id.phone);
		phone.setText(Tools.showPhoneNum(phoneNumber));
		username = (TextView) findViewById(R.id.username);
		username.setText(userName);
		school = (TextView) findViewById(R.id.school);
		school.setText(schoolstring);
		gender = (TextView) findViewById(R.id.gender);
		gender.setText(genderString);
		birthday = (TextView) findViewById(R.id.birthday);
		birthday.setText(birthdayString);
		unlogin = (TextView) findViewById(R.id.unLogin);
		unlogin.setOnClickListener(this);
		changePwd = (TextView) findViewById(R.id.changePwd);
		changePwd.setOnClickListener(this);
		figureLayout = (RelativeLayout) findViewById(R.id.figureLayout);
		figureLayout.setOnClickListener(this);
		userNameLayout = (RelativeLayout) findViewById(R.id.userNameLayout);
		userNameLayout.setOnClickListener(this);
		genderLayout = (RelativeLayout) findViewById(R.id.genderLayout);
		genderLayout.setOnClickListener(this);
		birthdayLayout = (RelativeLayout) findViewById(R.id.birthdayLayout);
		birthdayLayout.setOnClickListener(this);
		schoolLayout = (RelativeLayout) findViewById(R.id.schoolLayout);
		schoolLayout.setOnClickListener(this);
	}

	private void initOption() {
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.face) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.face)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.face) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(android.graphics.Bitmap.Config.RGB_565)// 设置图片的解码类型//
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
				.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();// 构建完成
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.figure:
			break;
		case R.id.changePwd:
			Intent changePwd = new Intent(this, ChangepwdActivity.class);
			startActivity(changePwd);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			break;
		case R.id.figureLayout:
			pop = new PopupWindow(this);

			View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
					null);

			ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

			pop.setWidth(LayoutParams.MATCH_PARENT);
			pop.setHeight(LayoutParams.WRAP_CONTENT);
			pop.setBackgroundDrawable(new BitmapDrawable());
			pop.setFocusable(true);
			pop.setOutsideTouchable(true);
			pop.setContentView(view);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.activity_translate_in));
			pop.showAtLocation(figureLayout.getRootView(), Gravity.BOTTOM, 0, 0);
			RelativeLayout parent = (RelativeLayout) view
					.findViewById(R.id.parent);
			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			parent.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					pop.dismiss();
					ll_popup.clearAnimation();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					pop.dismiss();
					ll_popup.clearAnimation();
				}
			});
			//相册
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					pop.dismiss();
					ll_popup.clearAnimation();
					Intent intentFromGallery = new Intent();
					intentFromGallery.setType("image/*"); // 设置文件类型
					intentFromGallery
							.setAction(Intent.ACTION_GET_CONTENT);
					if(isIntentAvailable(new Intent("com.android.camera.action.CROP"))){
						startActivityForResult(intentFromGallery,
								IMAGE_REQUEST_CODE);
					}else {
						System.out.println("相册图片");
						intentFromGallery.putExtra("return-data", true);
						startActivityForResult(intentFromGallery,RESULT_REQUEST_CODE);
					}
				}
			});
			//照相机
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					pop.dismiss();
					ll_popup.clearAnimation();
					Intent intentFromCapture = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					// 判断存储卡是否可以用，可用进行存储
					if (Tools.hasSdcard()) {

						intentFromCapture.putExtra(
								MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(Environment
										.getExternalStorageDirectory(),
										IMAGE_FILE_NAME)));
					}
					if(isIntentAvailable(new Intent("com.android.camera.action.CROP"))){
						startActivityForResult(intentFromCapture,
								CAMERA_REQUEST_CODE);
					}else {
						intentFromCapture.putExtra("return-data", true);
						startActivityForResult(intentFromCapture,RESULT_REQUEST_CODE);
					}
				}
			});
			break;
		case R.id.userNameLayout:
			Intent intent = new Intent(this, XiugaiName.class);
			intent.putExtra("userName", userName);
			intent.putExtra("phoneNumber", phoneNumber);
			intent.putExtra("gender", genderInt);
			intent.putExtra("birthday", birthdayString);
			intent.putExtra("school", schoolstring);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);

			break;
		case R.id.schoolLayout:
			Intent intent2 = new Intent(this, SchoolActivity.class);
			intent2.putExtra("userName", userName);
			intent2.putExtra("phoneNumber", phoneNumber);
			intent2.putExtra("gender", genderInt);
			intent2.putExtra("birthday", birthdayString);
			intent2.putExtra("school", schoolstring);
			startActivityForResult(intent2, 5);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			
			break;
		case R.id.genderLayout:
			final AlertDialog dlg = new AlertDialog.Builder(this).create();
			dlg.show();
			Window window = dlg.getWindow();
			window.setContentView(R.layout.gender_window);
			RelativeLayout maleLayout = (RelativeLayout) window
					.findViewById(R.id.maleLayout);
			RelativeLayout femaleLayout = (RelativeLayout) window
					.findViewById(R.id.femaleLayout);
			if (genderInt == 0) {
				maleLayout.findViewById(R.id.maleOk)
						.setVisibility(View.VISIBLE);
			} else {
				femaleLayout.findViewById(R.id.femaleOk).setVisibility(
						View.VISIBLE);
			}
			dlg.show();
			maleLayout.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					genderInt = 0;
					gender.setText("男");
					RequestParams params = new RequestParams();
					params.put("phoneNumber", phoneNumber);
					params.put("userName", userName);
					params.put("gender", genderInt);
					params.put("birthday", birthdayString);
					params.put("school", schoolstring);
					changeData(params);
					dlg.dismiss();
				}
			});
			femaleLayout.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					genderInt = 1;
					gender.setText("女");
					RequestParams params = new RequestParams();
					params.put("phoneNumber", phoneNumber);
					params.put("userName", userName);
					params.put("gender", genderInt);
					params.put("birthday", birthdayString);
					params.put("school", schoolstring);
					changeData(params);
					dlg.dismiss();
				}
			});
			break;
		case R.id.birthdayLayout:
			final WLQQTimePicker timePicker = new WLQQTimePicker(this);
			timePicker.setDate(new Date().getTime());
			final AlertDialog dlg2 = new AlertDialog.Builder(this).create();
			dlg2.show();
			Window window2 = dlg2.getWindow();
			window2.setContentView(timePicker);
			window2.findViewById(R.id.cancel).setOnClickListener(
					new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							dlg2.cancel();
						}
					});
			window2.findViewById(R.id.confirm).setOnClickListener(
					new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							dlg2.cancel();
							birthday.setText(XiuGaiActivity.this.getString(
									R.string.picker_title,
									timePicker.getmYear(),
									timePicker.getmMonth() + 1,
									timePicker.getmDay()));
							birthdayString = birthday.getText().toString();
							RequestParams params = new RequestParams();
							params.put("phoneNumber", phoneNumber);
							params.put("userName", userName);
							params.put("gender", genderInt);
							params.put("birthday", birthdayString);
							params.put("school", schoolstring);
							changeData(params);
						}
					});
			break;
		case R.id.unLogin:
			final AlertDialog dlg3 = new AlertDialog.Builder(this).create();
			dlg3.show();
			Window window3 = dlg3.getWindow();
			window3.setContentView(R.layout.unlogin_window);
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
							// TODO Auto-generated method stub
							AsyncHttpClient httpClient = new AsyncHttpClient();
							RequestParams params = new RequestParams();
							String url = "http://120.24.254.127/tata/data/userExit.php";
							params.put("phoneNumber", phoneNumber);
							httpClient.post(url, params,
									new TextHttpResponseHandler() {

										@Override
										public void onSuccess(int arg0,
												Header[] arg1, String arg2) {
											// TODO Auto-generated method stub
											dlg3.dismiss();
											try {
												if (new JSONObject(arg2).getInt("code")==0) {
													ToastUtil.show(XiuGaiActivity.this,"退出失败，请重试");
													return;
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											mySharedPreferences.edit().clear()
													.commit();
											MainApplication.collectProducts.clear();
											MainApplication.collectShares.clear();
											Intent mIntent = new Intent(
													"data.person.myself");
											sendBroadcast(mIntent);
											Intent intent = new Intent(
													XiuGaiActivity.this,
													LoginActivity.class);
											startActivity(intent);
											overridePendingTransition(
													R.anim.slide_left_in,
													R.anim.slide_left_out);
											finish();
										}

										@Override
										public void onFailure(int arg0,
												Header[] arg1, String arg2,
												Throwable arg3) {
											// TODO Auto-generated method stub
											dlg3.dismiss();
											ToastUtil.show(XiuGaiActivity.this,"退出失败，请重试");
										}
									});
						}
					});
			break;


		}
	}

	public void changeData(RequestParams params) {
		AsyncHttpClient httpClient = new AsyncHttpClient();
		String url = "http://120.24.254.127/tata/data/updateUserInformation.php";
		httpClient.post(url, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				System.out.println(arg2);
				Toast.makeText(XiuGaiActivity.this, "修改成功", Toast.LENGTH_SHORT)
						.show();
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putString("userName", userName);
				editor.putString("phoneNumber", phoneNumber);
				editor.putInt("gender", genderInt);
				editor.putString("birthday", birthdayString);
				editor.putString("school",schoolstring);
				editor.commit();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				System.out.println(arg2);
				Toast.makeText(XiuGaiActivity.this, "修改失败", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == 1) {
			userName=mySharedPreferences.getString("userName", "");
			username.setText(userName);
		    return;
		}
		if (requestCode == 5) {
			schoolstring=mySharedPreferences.getString("school", "");
			school.setText(schoolstring);
			return;
		}
		
		switch (requestCode) {
		case IMAGE_REQUEST_CODE:
			startPhotoZoom(data.getData());
			break;
		case CAMERA_REQUEST_CODE:
			if (Tools.hasSdcard()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory()
								+ IMAGE_FILE_NAME);
//				dd
				startPhotoZoom(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(XiuGaiActivity.this, "未找到存储卡，无法存储照片！",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case RESULT_REQUEST_CODE:
			if (data != null) {
				//返回imageurl
				dialog=new MyDialog(this).loadDialog1();
				dialog.show();
//				Bundle extras = data.getExtras();
//				if (extras != null) {
//					Bitmap photo = extras.getParcelable("data");
//					IMAGE_FILE_NAME=new Date().getTime()+".jpg";
//					System.out.println("llllll");
//					saveMyBitmap(IMAGE_FILE_NAME, photo);
//					
//				}else {
//					System.out.println("llfffff");
//				}
				Uri uri = data.getData();    
				ContentResolver  cr = XiuGaiActivity.this.getContentResolver();    
	                Bitmap bitmap;
					try {
						bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
						IMAGE_FILE_NAME=new Date().getTime()+".jpg";
						saveMyBitmap(IMAGE_FILE_NAME, bitmap);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}    
	                new Thread(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();
						upload(getFilesDir()+"/"+IMAGE_FILE_NAME);
						Looper.loop();
					}
				}).start();
				
			}
			break;
		}
	}
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				try {
					JSONObject object=new JSONObject((String)msg.obj);
					System.out.println((String)msg.obj);
					if(object.getInt("code")==1){
						String imageurl=object.getJSONObject("data").getString("userImg");
						ImageLoader.getInstance().displayImage(imageurl, figure, options);
						dialog.dismiss();
						ToastUtil.show(XiuGaiActivity.this, "修改成功");
						mySharedPreferences.edit().putString("userImgUrl",imageurl).commit();
						 Intent mIntent = new Intent("data.person.myself");
						 sendBroadcast(mIntent);
					}else {
						dialog.dismiss();
						ToastUtil.show(XiuGaiActivity.this, "修改失败");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	};
	
	
	public void upload(String fname) {
		try {
			String enterNewline = "\r\n";
			String fix="--";
			String boundary="######";
			URL url = new URL("http://120.24.254.127/tata/data/updateUserImg.php");// 自己写的PHP接口接口存储图片
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Accept", "image/jpeg, image/pjpeg,image/png");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			StringBuilder sb = new StringBuilder(fix+boundary+enterNewline);
			sb.append("Content-Disposition: form-data; name=\"" + "phoneNumber"
					+ "\"\r\n\r\n");
			sb.append(phoneNumber);
			sb.append("\r\n");
			
			
			
			out.write(sb.toString().getBytes("utf-8"));
			byte[] end_data = (fix+boundary+fix+enterNewline).getBytes();// 定义最后数据分隔线
			File file = new File(fname);
				StringBuilder sb2 = new StringBuilder(fix+boundary+enterNewline);
				sb2.append("Content-Disposition: form-data;name=\"userImg"
						+ "\";filename=\"" + file.getName() + "\"\r\n");
				sb2.append("\r\n");
                
				byte[] data = sb2.toString().getBytes();
				out.write(data);
				DataInputStream in = new DataInputStream(new FileInputStream(
						file));
				int bytes = -1;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				out.write(enterNewline.getBytes());
				in.close();
			out.write(end_data);
			out.flush();
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line = null;
				StringBuilder result=new StringBuilder();
				while ((line = reader.readLine()) != null) {
					result.append(line);
				}
				reader.close();
//				ToastUtil.show(this, "修改成功");
				file.delete();
				Message message=handler.obtainMessage();
				message.obj=result.toString();
				message.what=1;
				handler.sendMessage(message);
			} else {
				ToastUtil.show(this, "修改失败");
				file.delete();
				dialog.dismiss();
			}
			// 定义BufferedReader输入流来读取URL的响应
			out.close();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressLint("NewApi")
	public void saveMyBitmap(String filename, Bitmap bit) {  
		FileOutputStream outStream;
		try {
			outStream = openFileOutput(filename, Context.MODE_PRIVATE);
			bit.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
			outStream.flush();  
			outStream.close();  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}


	private void startPhotoZoom(Uri data) {
		// TODO Auto-generated method stub
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}
	
	public  boolean isIntentAvailable(Intent intent) {
		PackageManager packageManager=getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

}
