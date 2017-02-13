package com.tata.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.runner.Version;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * @ClassName HistroyUserPreferences.java
 * @Description 保存历史用户,并记录有无记住密码
 * @author
 * @date 2013-1-10
 * 
 */
public class RecordPreferences {

	private static RecordPreferences instance;
	private Context context;

	private SharedPreferences sharedPreferences; // 临时
	private SharedPreferences sharedHistory;
	private SharedPreferences shareClientPreferences; //
	private SharedPreferences shareNotifyPreferences;
	private SharedPreferences shareIsFirstEnter;//是否第一次进入程序
	private SharedPreferences shareMdData; // MdData
	private final static String AUTO_LOGIN = "AUTO_LOGIN";
	private final static String FIRST_START = "FIRST_START";
	private final static String VERSION_NAME = "VERSION_NAME";
	private final static String CURRENT_ACCOUNT = "CURRENT_ACCOUNT";
	private final static String LAST_LOGIN = "0";
	private String versionName; // 版本号

	private RecordPreferences() {
	}

	private RecordPreferences(Context context) {
		this.context = context;
//		shareNotifyPreferences = context.getSharedPreferences(
//				Constants.SHARE_NOTIFY, Context.MODE_PRIVATE);
//		sharedHistory = context.getSharedPreferences(
//				Constants.SHARED_HISTORY_USER, Context.MODE_PRIVATE);
//		shareClientPreferences = context.getSharedPreferences(
//				Constants.SHARE_DATA, Context.MODE_PRIVATE);
//		shareMdData = context.getSharedPreferences(Constants.SHARE_MDNUM,
//				Context.MODE_PRIVATE);
		shareIsFirstEnter = context.getSharedPreferences("first",
				Context.MODE_PRIVATE);
//		versionName = Version.getAppVersionName(context);
	}

	public static RecordPreferences getInstance(Context context) {
		if (instance == null) {
			instance = new RecordPreferences(context);
		}
		return instance;
	}

	/**
	 * 创建 Or 获取 SharedPreferences
	 * 
	 * @param name
	 * @param mode
	 */
	public SharedPreferences getSharedPreferences(String name, int mode) {
		if (TextUtils.isEmpty(name))
			return null;
		sharedPreferences = context.getSharedPreferences(name, mode);
		return sharedPreferences;
	}

	/**
	 * 添加键值对
	 * 
	 * @param name
	 * @param key
	 * @param value
	 */
	public void putSharedValue(String sharedName, String key, Object value) {
		this.putSharedValue(sharedName, new String[] { key },
				new Object[] { value });
	}

	/**
	 * 添加键值对
	 * 
	 * @param name
	 * @param key
	 * @param value
	 */
	public void putSharedValue(String sharedName, String[] key, Object[] value) {
		if (TextUtils.isEmpty(sharedName) || key == null || value == null) {
			return;
		}
		if (sharedPreferences == null) {
			sharedPreferences = getSharedPreferences(sharedName,
					Context.MODE_PRIVATE);
		}
		Editor editor = sharedPreferences.edit();
		for (int i = 0; i < key.length; i++) {
			if (key[i] == null || value[i] == null) {
				return;
			}
			if (value[i].getClass() == String.class) {
				editor.putString(key[i], value[i].toString());
			} else if (value[i].getClass() == Integer.class) {
				editor.putInt(key[i], Integer.parseInt(value[i].toString()));
			} else if (value[i].getClass() == Long.class) {
				editor.putLong(key[i], Long.parseLong(value[i].toString()));
			} else if (value[i].getClass() == Boolean.class) {
				editor.putBoolean(key[i],
						Boolean.parseBoolean(value[i].toString()));
			} else if (value[i].getClass() == Float.class) {
				editor.putFloat(key[i], Float.parseFloat(value[i].toString()));
			}
		}
		editor.commit();
	}

	/**
	 * 通过key获取值
	 * 
	 * @param name
	 * @param key
	 * @param cls
	 *            value的类型[String,Integer,Long,Boolean,Float]
	 * @return
	 */
	public Object getSharedValue(String sharedName, String key, Class<?> cls) {
		return getSharedValue(sharedName, new String[] { key },
				new Class[] { cls }).get(key);
	}

	/**
	 * 通过key获取值
	 * 
	 * @param name
	 * @param key
	 * @param cls
	 *            value的类型[String,Integer,Long,Boolean,Float]
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getSharedValue(String sharedName, String[] key,
			Class[] cls) {
		if (key == null || sharedName == null) {
			return null;
		}
		if (sharedPreferences == null) {
			sharedPreferences = getSharedPreferences(sharedName,
					Context.MODE_PRIVATE);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < key.length; i++) {
			if (key[i] == null || cls[i] == null) {
				return null;
			}
			if (cls[i] == String.class) {
				map.put(key[i], sharedPreferences.getString(key[i], null));
			} else if (cls[i] == Integer.class) {
				map.put(key[i], sharedPreferences.getInt(key[i], 0));
			} else if (cls[i] == Long.class) {
				map.put(key[i], sharedPreferences.getLong(key[i], 0));
			} else if (cls[i] == Boolean.class) {
				map.put(key[i], sharedPreferences.getBoolean(key[i], false));
			} else if (cls[i] == Float.class) {
				map.put(key[i], sharedPreferences.getFloat(key[i], 0));
			}
		}
		return map;
	}

	/**
	 * 保存一个历史用户
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码 当
	 */
	public void recordHistoryUser(String userName, String password) {
		Editor edit = sharedHistory.edit();
		edit.putString(userName, password);
		edit.putString(CURRENT_ACCOUNT, userName);
		edit.commit();
	}

	/**
	 * 保存当前用户
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码 当
	 */
	public void recordCurrentUser(String userName, String password) {
		Editor edit = sharedHistory.edit();
		edit.putString(userName, password);
		edit.putString(CURRENT_ACCOUNT, userName);
		edit.commit();
	}

	/**
	 * 刪除当前用户（退出的時候使用）
	 * 
	 */
	public void deteleCurrentUser() {
		Editor edit = sharedHistory.edit();
		edit.putString(CURRENT_ACCOUNT, null);
		edit.commit();
	}
	/**
	 * 得到当前用户
	 * 
	 */
	public String getCurrentUser() {
		return sharedHistory.getString(CURRENT_ACCOUNT, null);
	}

	/**
	 * 获取最后一次登录的用户
	 */
	public String getLastLoginUser() {
		return sharedHistory.getString(CURRENT_ACCOUNT, null);
	}

	/**
	 * 删除最后登录用户
	 */
	public void deleteLastLoginUser(String userName) {
		if (userName.equals(sharedHistory.getString(LAST_LOGIN, null))) {
			Editor edit = sharedHistory.edit();
			edit.putString(LAST_LOGIN, null);
			edit.commit();
		}

	}

	/**
	 * 获取历史用户,并从密码是否为空来判断是否有记录密码
	 * 
	 * @return
	 */
	public Map<String, String> quallyAllHistoryUser() {
		Map<String, String> historyList = new HashMap<String, String>();
		Map<String, ?> allData = sharedHistory.getAll();
		Set<String> keySet = allData.keySet();
		for (String tempUserName : keySet) {
			if (!"".equals(tempUserName) && !"0".equals(tempUserName)) {
				String password = sharedHistory.getString(tempUserName, null);
				historyList.put(tempUserName, password);
			}
		}
		return historyList;
	}

	/**
	 * 清楚所有历史用户
	 */
	public void clearAllHistoryUser() {
		Editor edit = sharedHistory.edit();
		edit.clear();
		edit.commit();
	}

	/**
	 * 判断用户是否第一次登陆
	 * 
	 * @param user
	 * @return
	 */
	public boolean checkUserFirstLogin(String user) {
		boolean result = false;
		if (user != null && "".equals(user)) {
			Map<String, ?> allData = sharedHistory.getAll();
			Set<String> keySet = allData.keySet();
			for (String string : keySet) {
				if (user.equals(string)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 自动登录（以用户名为key，存储是否登录）
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param isAutoLogin
	 *            是否自动登录
	 */
	public void autoLoginUser(String userName, boolean isGraris) {
		Editor edit = sharedHistory.edit();
		edit.putBoolean(userName, isGraris);
		edit.putString(AUTO_LOGIN, userName);
		LogHelper.i("isGraris" + isGraris);
		edit.commit();
	}

	/**
	 * 获取自动登录用户的类别（是否为行业用户 ）
	 */
	public boolean IsGraris(String userName) {
		return shareClientPreferences.getBoolean(userName, false);
	}

	/**
	 * 获取勾选自动登录的用户名
	 * 
	 * @return
	 */
	public String getAutoUser() {
		return shareClientPreferences.getString(AUTO_LOGIN, null);
	}

	/**
	 * 添加注销记录
	 */
	public void addLogout(boolean flag) {
		Editor edit = shareClientPreferences.edit();
		edit.putBoolean("Logout", flag);
		edit.commit();
	}

	/**
	 * 获取注销记录
	 */
	public boolean getLogout() {
		return shareClientPreferences.getBoolean("Logout", false);
	}

	/**
	 * 添加首次启动应用记录
	 */
	public void firstStart() {
		Editor edit = shareClientPreferences.edit();
		edit.putBoolean(FIRST_START, false);
		edit.commit();
	}

	/**
	 * 添加版本到本地
	 */
	public void addVersionName() {
		Editor edit = shareClientPreferences.edit();
		edit.putString(VERSION_NAME, versionName);
		edit.commit();
	}

	/**
	 * 获取应用是否首次启动
	 */
	public boolean getFirstStart() {
		return shareClientPreferences.getBoolean(FIRST_START, true);
	}

	/**
	 * 获取记录中保存的版本号
	 */
	public String getVersionName() {
		return shareClientPreferences.getString(VERSION_NAME, "");
	}

	/**
	 * 添加Md5数据
	 * 
	 * @param mdDataName
	 *            保存文件名
	 * @param mdDataValue
	 *            保存文件值
	 */
	public void addMdData(String mdDataName, String mdDataValue) {
		Editor editor = shareMdData.edit();
		editor.putString(mdDataName, mdDataValue);
		editor.commit();
	}

	/**
	 * 获取MD5值
	 * 
	 * @param mdDataName
	 *            名称
	 * @param defaultValue
	 *            默认值
	 * @return
	 */
	public String getMdData(String mdDataName, String defaultValue) {
		return shareMdData.getString(mdDataName, defaultValue);
	}

	/**
	 * 清楚所有MD5值
	 */
	public void clearAllMdData() {
		Editor edit = shareMdData.edit();
		edit.clear();
		edit.commit();
	}

	/**
	 * 存储最近的一次通知收款用户（当通知用户为当前用户时）
	 */
	public void recordLastNotifyMember(String currentAccount,
			String lastNotifyMember) {
		Editor editor = shareNotifyPreferences.edit();
		editor.putString(currentAccount, lastNotifyMember);
		editor.commit();
	}

	/**
	 * 获取最近一次，通知收款记录
	 */
	public String getLastNotifyMember(String currentAccount) {
		return shareNotifyPreferences.getString(currentAccount, null);
	}
	/**
	 * 获取是否第一次进入程序
	 */
	public String getFirstEnter() {
		return shareIsFirstEnter.getString("FirstEnter", null);
	}
	/**
	 * 设置第一次进入程序
	 */
	public void setFirstEnter() {
		Editor edit = shareIsFirstEnter.edit();
		edit.clear();
		edit.putString("FirstEnter", "true");
		edit.commit();
	}
}
