package com.tata.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

/**
 * ��־����
 * 
 * @author ����
 */
@SuppressLint("DefaultLocale")
public final class LogHelper {
	private static final String CLASS_METHOD_LINE_FORMAT = "%s.%s()  Line:%d";
	private static final String LOG_TAG = "InfoData";

	/**
	 * ��ӡ��־
	 * 
	 * @param objects
	 *            ��Ҫ��ӡ����
	 */
	public static void trace(Object... objects) {
		if (Constants.SHOW_LOG) {		StackTraceElement traceElement = Thread.currentThread()
					.getStackTrace()[3];// �Ӷ�ջ��Ϣ�л�ȡ��ǰ�����õķ�����Ϣ
			String logText = String.format(CLASS_METHOD_LINE_FORMAT,
					traceElement.getClassName(), traceElement.getMethodName(),
					traceElement.getLineNumber());
			if (objects != null && objects.length > 0) {
				for (int i = 0; i < objects.length; i++) {
					logText += "\n    Log:" + objects[i];
				}
			}
			Log.d(LOG_TAG, logText);// Log
		}
	}

	public static boolean isEmulator() {
		return (Build.MODEL.equals("sdk"))
				|| (Build.MODEL.equals("google_sdk"));
	}

	/*
	 * �Զ���tag
	 */
	public static void v(String tag, String msg) {
		if (Constants.SHOW_LOG) {
			Log.v(tag, msg);
		}
	}

	public static void v(String tag, String msg, Throwable tr) {
		if (Constants.SHOW_LOG) {
			Log.v(tag, msg, tr);
		}
	}

	public static void d(String tag, String msg) {
		if (Constants.SHOW_LOG) {
			Log.d(tag, msg);
		}
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (Constants.SHOW_LOG) {
			Log.d(tag, msg, tr);
		}
	}

	public static void i(String tag, String msg) {
		if (Constants.SHOW_LOG) {
			Log.i(tag, msg);
		}
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (Constants.SHOW_LOG) {
			Log.i(tag, msg, tr);
		}
	}

	public static void w(String tag, String msg) {
		if (Constants.SHOW_LOG) {
			Log.w(tag, msg);
		}
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (Constants.SHOW_LOG) {
			Log.w(tag, msg, tr);
		}
	}

	public static void e(String tag, String msg) {
		if (Constants.SHOW_LOG) {
			Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (Constants.SHOW_LOG) {
			Log.e(tag, msg, tr);
		}
	}

	/*
	 * tagʹ��Ĭ��ֵ
	 */
	public static void v(String msg) {
		LogHelper.v(LOG_TAG, msg);
	}

	public static void v(String msg, Throwable tr) {
		LogHelper.v(LOG_TAG, msg, tr);
	}

	public static void d(String msg) {
		LogHelper.d(LOG_TAG, msg);
	}

	public static void d(String msg, Throwable tr) {
		LogHelper.d(LOG_TAG, msg, tr);
	}

	public static void i(String msg) {
		LogHelper.i(LOG_TAG, msg);
	}

	public static void i(String msg, Throwable tr) {
		LogHelper.i(LOG_TAG, msg, tr);
	}

	public static void w(String msg) {
		LogHelper.w(LOG_TAG, msg);
	}

	public static void w(String msg, Throwable tr) {
		LogHelper.w(LOG_TAG, msg, tr);
	}

	public static void e(String msg) {
		LogHelper.e(LOG_TAG, msg);
	}

	public static void e(String msg, Throwable tr) {
		LogHelper.e(LOG_TAG, msg, tr);
	}
}
