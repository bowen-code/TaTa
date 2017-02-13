package com.tata.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.conn.util.InetAddressUtils;

import com.tata.R;
import com.tata.bean.Dianzan;
import com.tata.bean.ShareComment;
import com.tata.bean.ShareMessage;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class Tools {
	private Handler handler;
	private int end;

	public Tools(Handler handler) {
		this.handler = handler;
	}

	public Tools() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 显示用户手机号码 中间4个＊
	 * 
	 * @param
	 * @return
	 */
	public static String showPhoneNum(String str) {
		String result = null;
		result = str.substring(0, str.length() - (str.substring(3)).length())
				+ "****" + str.substring(7);
		return result;

	}

	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public SpannableString getClickableSpan(int a, final int commentNumber,
			final ShareMessage shareMessage, final String myphonenumber,
			String text) {
		final ShareComment comment = shareMessage.getShareComment().get(
				commentNumber);
		View.OnClickListener l = new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("点击评论人SpannableString click");
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("phoneNumber", comment.getPhoneNumberC());
				bundle.putString("userName", comment.getUserNameC());
				message.setData(bundle);
				message.what = 3;
				handler.sendMessage(message);
			}
		};
		View.OnClickListener lll = new View.OnClickListener() {
			public void onClick(View v) {
				System.out.println("点击回复人SpannableString click");
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("phoneNumber", comment.getParPhoneNumber());
				bundle.putString("userName", comment.getParName());
				message.setData(bundle);
				message.what = 3;
				handler.sendMessage(message);
			}
		};
		View.OnClickListener ll = new View.OnClickListener() {
			public void onClick(View v) {
				ShareComment comment = shareMessage.getShareComment().get(
						commentNumber);
				if (!comment.getPhoneNumberC().equals(myphonenumber)) {
					Message message = new Message();
					Bundle bundle = new Bundle();
					bundle.putInt("shareID", shareMessage.getShareID());
					bundle.putInt("parid", comment.getCommentID());
					bundle.putString("ParName", comment.getUserNameC());
					message.setData(bundle);
					message.what = 1;
					handler.sendMessage(message);
				}
			}
		};
		// View.OnLongClickListener longClickListener = new
		// View.OnLongClickListener(){
		// public boolean onLongClick(View v)
		// {
		// View contentView = LayoutInflater.from(mContext).inflate(
		// R.layout.my_popwinodw, null);
		// // contentView.setBackgroundColor(Color.argb(200, 214, 216,
		// // 214));
		// PopupWindow popupWindow = new PopupWindow(LayoutParams.MATCH_PARENT,
		// LayoutParams.WRAP_CONTENT);
		// popupWindow.setFocusable(true);
		// popupWindow.setOutsideTouchable(true);
		// popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// popupWindow.setContentView(contentView);
		// popupWindow.showAtLocation(v.getRootView(), Gravity.CENTER, 0,
		// 0);
		// System.out.println("jjjjjjjjj");
		// return false;
		// }
		// };

		SpannableString spanableInfo = new SpannableString(text);
		// spanableInfo.setSpan(underlineSpan, 0, spanableInfo.length(),
		// Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		if (a == 0) {
			int index = comment.getUserNameC().length();
			spanableInfo.setSpan(new Clickable(l), 0, index,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			spanableInfo.setSpan(
					new ForegroundColorSpan(Color.rgb(0, 154, 206)), 0, index,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			spanableInfo.setSpan(new Clickable(ll), index,
					spanableInfo.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

		} else {
			int index = comment.getUserNameC().length();
			spanableInfo.setSpan(new Clickable(l), 0, index,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			spanableInfo.setSpan(
					new ForegroundColorSpan(Color.rgb(0, 154, 206)), 0, index,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			spanableInfo.setSpan(new Clickable(ll), index, index + 2,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			int index1 = text.indexOf("：");
			spanableInfo.setSpan(new Clickable(lll), index + 2, index1,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			spanableInfo.setSpan(
					new ForegroundColorSpan(Color.rgb(0, 154, 206)), index + 2,
					index1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			spanableInfo.setSpan(new Clickable(ll), index1,
					spanableInfo.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		return spanableInfo;
	}

	class Clickable extends ClickableSpan implements OnClickListener {
		private View.OnClickListener mListener;

		public Clickable(OnClickListener mListener) {
			this.mListener = mListener;
		}

		@Override
		public void onClick(View v) {
			if (!MainApplication.longClick) {
				mListener.onClick(v);
			} else {
				MainApplication.longClick = false;
			}
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			// TODO Auto-generated method stub
			ds.setColor(Color.BLACK);
			ds.setUnderlineText(false);
		}

	}

	public SpannableString getDianzanSpannableString(List<Dianzan> dianzans) {
		StringBuffer name = new StringBuffer();
		for (int i = 0; i < dianzans.size(); i++) {
			if (i == 10) {
				name.append("等" + dianzans.size() + "人");
				break;
			}
			if (i == 0) {
				name.append(dianzans.get(i).getUserName());
			} else {
				name.append("、" + dianzans.get(i).getUserName());
			}
		}
		SpannableString spanableInfo = new SpannableString(name.toString());
		int index = 0;
		int index1 = 0;
		for (int i = 0; i < dianzans.size(); i++) {
			if (dianzans.size() == 1) {
				spanableInfo.setSpan(
						new Clickable(new dianzanListener(dianzans.get(i))), 0,
						spanableInfo.length(),
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				spanableInfo.setSpan(
						new ForegroundColorSpan(Color.rgb(0, 154, 206)), 0,
						spanableInfo.length(),
						Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				break;
			}
			index = name.toString().indexOf("、");
			if (i != dianzans.size() - 1) {
				index1 = name.toString().substring(index + 1).indexOf("、");
			} else {
				if (dianzans.size() > 10) {
					index1 = name.toString().length() - 1;
				} else {
					index1 = name.toString().length();
				}
			}
			if (i == 0) {
				spanableInfo.setSpan(
						new Clickable(new dianzanListener(dianzans.get(i))), 0,
						index, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				spanableInfo.setSpan(
						new ForegroundColorSpan(Color.rgb(0, 154, 206)), 0,
						index + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			} else {
				spanableInfo.setSpan(
						new Clickable(new dianzanListener(dianzans.get(i))),
						index + 1, index1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				spanableInfo.setSpan(
						new ForegroundColorSpan(Color.rgb(0, 154, 206)),
						index + 1, index1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

			}

		}
		return spanableInfo;
	}

	class dianzanListener implements OnClickListener {

		private Dianzan dianzan;

		public Dianzan getDianzan() {
			return dianzan;
		}

		public void setDianzan(Dianzan dianzan) {
			this.dianzan = dianzan;
		}

		public dianzanListener(Dianzan dianzan) {
			super();
			this.dianzan = dianzan;
		}

		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("bbbbbbbbb");
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("phoneNumber", dianzan.getPraisePhoneNumber());
			bundle.putString("userName", dianzan.getUserName());
			message.setData(bundle);
			message.what = 3;
			handler.sendMessage(message);
		}

	}

	// 得到本机ip地址
	public String getLocalHostIp() {
		String ipaddress = "";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			// 遍历所用的网络接口
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
				Enumeration<InetAddress> inet = nif.getInetAddresses();
				// 遍历每一个接口绑定的所有ip
				while (inet.hasMoreElements()) {
					InetAddress ip = inet.nextElement();
					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress())) {
						return ip.getHostAddress();
					}
				}

			}
		} catch (SocketException e) {
			System.out.print("获取本地ip地址失败");
			e.printStackTrace();
		}
		return ipaddress;

	}

	/**
	 * 验证身份证号是否符合规则
	 * 
	 * @param text
	 *            身份证号
	 * @return
	 */
	public boolean personIdValidation(String text) {
		String regx = "[0-9]{17}x";
		String reg1 = "[0-9]{15}";
		String regex = "[0-9]{18}";
		return text.matches(regx) || text.matches(reg1) || text.matches(regex);
	}

	public boolean isCellphoneNumber(String number) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}

}
