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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;



import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tata.R;
import com.tata.utils.Bimp;
import com.tata.utils.Constants;
import com.tata.utils.FileUtils;
import com.tata.utils.ImageItem;
import com.tata.utils.MainApplication;
import com.tata.utils.ToastUtil;
import com.tata.view.MyDialog;
import com.tata.view.PublicWay;
import com.tata.view.Res;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WriteShare extends BaseActivity {
	private View parentView;
	private PopupWindow pop;
	private LinearLayout ll_popup;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	public static Bitmap bimap;
	private EditText text;
	private SharedPreferences mySharedPreferences;
	private static String phoneNumber;
	private static String content;
	private Dialog dialog;
	private CheckBox check;
	private static String shareurl = "http://120.24.254.127/tata/data/share.php";
    private TextView location;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopText("写分享");
		setRightButton("发表");
		setRightSize(16);
		setRightClick();
		parentView = getLayoutInflater().inflate(R.layout.activity_write_share,
				null);
		setCenterView(parentView);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);
		// PublicWay.activityList.add(this);
		text = (EditText) findViewById(R.id.content);
		text.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
                 if(text.getText().toString().length()>=260){
					ToastUtil.show(WriteShare.this,"最多260个字");
				}
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		mySharedPreferences = getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		phoneNumber = mySharedPreferences.getString("phoneNumber", "");
		Init();
	}

	public void Init() {
		location=(TextView) findViewById(R.id.location);
		location.setText(MainApplication.location);
		pop = new PopupWindow(WriteShare.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				photo();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(WriteShare.this, AlbumActivity.class);
				startActivity(intent);
				// overridePendingTransition(R.anim.activity_translate_in,
				// R.anim.activity_translate_out);
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

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							WriteShare.this, R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(WriteShare.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
		
		check=(CheckBox) findViewById(R.id.check);
		

	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 4) {
				return 4;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 4) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
						.getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			case 2:
				dialog.dismiss();
				Constants.freshFlag=true;
				finish();
				break;
			case 3:
				dialog.dismiss();
				setRightClick();
				break;
			}
			super.handleMessage(msg);
		}
	};
	protected ArrayList<String> list;

	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.tempSelectBitmap.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						Bimp.max += 1;
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
					}
				}
			}
		}).start();
	}

	// 每次返回时更新
	@Override
	protected void onResume() {
		adapter.update();
		super.onRestart();
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 4 && resultCode == RESULT_OK) {

//				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
//				FileUtils.saveBitmap(bm, fileName);

				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				takePhoto.camera=true;
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			for (int i = 0; i < PublicWay.activityList.size(); i++) {
//				if (null != PublicWay.activityList.get(i)) {
//					PublicWay.activityList.get(i).finish();
//				}
//			}
//			Bimp.tempSelectBitmap.clear();
//			Bimp.max = 0;
//			finish();
//		}
//		return true;
//	}

	public void Return(View v) {
		for (int i = 0; i < PublicWay.activityList.size(); i++) {
			if (null != PublicWay.activityList.get(i)) {
				PublicWay.activityList.get(i).finish();
			}
		}
		Bimp.tempSelectBitmap.clear();
		Bimp.max = 0;
		finish();
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}

	public void onClick(View v) {
		if(mySharedPreferences.getString("phoneNumber", "").equals("")){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			ToastUtil.show(this, "请先登录！");
			return;
		}
		content = text.getText().toString().trim();
		if (content.equals("") && Bimp.tempSelectBitmap.size() == 0) {
			Toast.makeText(this, "分享内容为空！", Toast.LENGTH_SHORT).show();
		} else {
			setnoRightClick();
			dialog = new MyDialog(this).loadupload();
			new Thread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					list = new ArrayList<String>();
					for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
						if(Bimp.tempSelectBitmap.get(i).camera){
							String fileName = String.valueOf(System.currentTimeMillis());
							 saveMyBitmap(fileName,Bimp.tempSelectBitmap.get(i).getBitmap());
								list.add(getFilesDir()+"/"+fileName);
						}else {
							
						String path= Bimp.tempSelectBitmap.get(i).getImagePath();
						 String filename = path.substring(path.lastIndexOf("/") + 1);
						 saveMyBitmap(filename,FileUtils.getSmallBitmap(path));
						list.add(getFilesDir()+"/"+filename);
						}
					}
					Looper.prepare();
					upload(list);
					Looper.loop();
				}
			}).start();
		}

	}

	public void upload(List<String> mlist) {
		List<String> list = mlist; // 要上传的文件名,如：d:\haha.doc.
		try {
			String enterNewline = "\r\n";
			String fix="--";
			String boundary="######";
			URL url = new URL(shareurl);// 自己写的PHP接口接口存储图片
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
			
			
			if(check.isChecked()){
				sb.append(fix+boundary+enterNewline);
				sb.append("Content-Disposition: form-data; name=\""
						+ "shareLocation" + "\"\r\n\r\n");
				sb.append(MainApplication.location);
				sb.append("\r\n");
			sb.append(fix+boundary+enterNewline);
			sb.append("Content-Disposition: form-data; name=\""
					+ "locationX" + "\"\r\n\r\n");
			sb.append(MainApplication.Lng);
			sb.append("\r\n");
			
			sb.append(fix+boundary+enterNewline);
			sb.append("Content-Disposition: form-data; name=\""
					+ "locationY" + "\"\r\n\r\n");
			sb.append(MainApplication.Lat);
			sb.append("\r\n");
			}
			
			if (!content.equals("")) {
				sb.append(fix+boundary+enterNewline);
				sb.append("Content-Disposition: form-data; name=\""
						+ "shareContent" + "\"\r\n\r\n");
				sb.append(content);
				sb.append("\r\n");
			}
			out.write(sb.toString().getBytes("utf-8"));
			byte[] end_data = (fix+boundary+fix+enterNewline).getBytes();// 定义最后数据分隔线
			File file=null;
			for (int i = 1; i <= list.size(); i++) {
				String fname = list.get(i - 1);
				file = new File(fname);
				StringBuilder sb2 = new StringBuilder(fix+boundary+enterNewline);
				sb2.append("Content-Disposition: form-data;name=\"shareImg" + i
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
			}
			out.write(end_data);
			out.flush();
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				ToastUtil.show(this, "分享成功");
				for (int i = 0; i < list.size(); i++) {
					String fname = list.get(i);
					file = new File(fname);
					if(file!=null)
						file.delete();
				}
				for (int i = 0; i < PublicWay.activityList.size(); i++) {
					if (null != PublicWay.activityList.get(i)) {
						PublicWay.activityList.get(i).finish();
					}
				}
				Bimp.tempSelectBitmap.clear();
				Bimp.max = 0;
				handler.sendEmptyMessage(2);
			} else {
				ToastUtil.show(this, "分享失败");
//				if(file!=null)
//				file.delete();
				handler.sendEmptyMessage(3);
			}
			// 定义BufferedReader输入流来读取URL的响应
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
			out.flush();
		} catch (Exception e) {
			handler.sendEmptyMessage(3);
			e.printStackTrace();
		}
	}
	@SuppressLint("NewApi")
	public void saveMyBitmap(String filename, Bitmap bit) {
		FileOutputStream outStream;
		try {
			outStream = openFileOutput(filename, Context.MODE_PRIVATE);
			bit.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();  
			outStream.close();  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!text.getText().toString().trim().equals("")||Bimp.tempSelectBitmap.size()!=0){
		final AlertDialog dlg3 = new AlertDialog.Builder(this).create();
		dlg3.show();
		Window window3 = dlg3.getWindow();
		window3.setContentView(R.layout.exit_sharewindow);
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
						overridePendingTransition(0, R.anim.slide_right_out); 
						for (int i = 0; i < list.size(); i++) {
							String fname = list.get(i);
							File file = new File(fname);
							if(file!=null)
								file.delete();
						}
						for (int i = 0; i < PublicWay.activityList.size(); i++) {
							if (null != PublicWay.activityList.get(i)) {
								PublicWay.activityList.get(i).finish();
							}
						}
						Bimp.tempSelectBitmap.clear();
						Bimp.max = 0;
						WriteShare.super.onBackPressed();
					}
				});
		}else {
			super.onBackPressed();
		}
	}
	
//	@Override
//	public void finish() {
//		for (int i = 0; i < list.size(); i++) {
//			String fname = list.get(i - 1);
//			File file = new File(fname);
//			if(file!=null)
//				file.delete();
//		}
//		super.finish();
//	}
}
