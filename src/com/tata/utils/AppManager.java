package com.tata.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * Ӧ�ó���Activity�����ࣺ����Activity�����Ӧ�ó����˳�
 * 
 * @author LiangZiChao
 * @2013-4-27
 * @����02:48:14
 */
public class AppManager {

	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * ��һʵ��
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * ���Activity����ջ
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * ��ȡ��ǰActivity����ջ�����һ��ѹ��ģ�
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * ����ǰActivity����ջ�����һ��ѹ��ģ�
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}
	public void finishLastTwoActivitys() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
		Activity activity2 = activityStack.lastElement();
		finishActivity(activity2);
	}
	public void finishLastThreeActivitys() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
		Activity activity2 = activityStack.lastElement();
		finishActivity(activity2);
		Activity activity3 = activityStack.lastElement();
		finishActivity(activity3);
	}

	/**
	 * ����ָ��Activity�������Actiivty
	 */
	public void finishActivityByPosition(Activity activity) {
		int position = activityStack.search(activity);
		position = (activityStack.size() - position);
		List<Activity> activities = new ArrayList<Activity>();
		for (int i = position + 1; i < activityStack.size(); i++) {
			if (null != activityStack.get(i)) {
				Activity activity1 = activityStack.get(i);
				activity1.finish();
				activities.add(activity1);
			}
		}
		activityStack.removeAll(activities);
	}

	/**
	 * ����ָ����Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			if (!activity.isFinishing())
				activity.finish();
			activity = null;
		}
	}

	/**
	 * ����ָ�������Activity
	 */
	public void finishActivity(Class<?> cls) {
		Stack<Activity> tempStack = new Stack<Activity>();
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				if (!activity.isFinishing())
					activity.finish();
				tempStack.add(activity);
			}
		}
		activityStack.removeAll(tempStack);
	}

	/**
	 * ��������Activity
	 */
	public void finishAllActivity() {
		while (true) {
			if (activityStack.size() == 0) {
				break;
			}
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			finishActivity(activity);
		}
	}

	/**
	 * 退出app
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();

			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			int currentVersion = android.os.Build.VERSION.SDK_INT;
			if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(startMain);
				activityMgr.killBackgroundProcesses(context.getPackageName());
			} else {// android2.1
				activityMgr.restartPackage(context.getPackageName());
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			activityMgr.killBackgroundProcesses(context.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}