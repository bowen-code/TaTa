package com.tata.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeDeal {
	/** ׼����һ��ģ�壬���ַ�������ȡ���������� */
	private static String pat1 = "yyyy-MM-dd HH:mm:ss";
	/** ׼���ڶ���ģ�壬����ȡ����������ֱ�Ϊָ���ĸ�ʽ */
	private static String pat2 = "yyyy��MM��dd�� HH:mm:ss";
	/** ʵ����ģ����� */
	private static SimpleDateFormat sdf1 = new SimpleDateFormat(pat1);
	private static SimpleDateFormat sdf2 = new SimpleDateFormat(pat2);

//	public static String {
//		Date dates = Dates();
//		String string = sdf1.format(dates);
//		System.out.println(string);
//		String time = "2013-01-29 19:38:21";
//		System.out.println(getTime(time));
//	}

	public static Long farmatTime(String string) {
		Date date = null;
		try {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = Date(sf.parse(string));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}

	public static Date Date(Date date) {
		Date datetimeDate;
		datetimeDate = new Date(date.getTime());
		return datetimeDate;
	}

	public static Date Dates() {
		Date datetimeDate;
		Long dates = 1361515285070L;
		datetimeDate = new Date(dates);
		return datetimeDate;
	}

	public static String getTime(String commitDate) {
		// TODO Auto-generated method stub
		// ����ҳ�������õ���ʱ��
		Date nowTime = new Date();
		sdf1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String currDate = sdf1.format(nowTime);
		Date date = null;
		try {
			// ���������ַ����е�������ȡ����
			date = sdf1.parse(commitDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int nowDate = Integer.valueOf(currDate.substring(8, 10));
		int commit = Integer.valueOf(commitDate.substring(8, 10));
		String monthDay = sdf2.format(date).substring(5, 12);
		String yearMonthDay = sdf2.format(date).substring(0, 12);
		int month = Integer.valueOf(monthDay.substring(0, 2));
		int day = Integer.valueOf(monthDay.substring(3, 5));
		if (month < 10 && day < 10) {
			monthDay = monthDay.substring(1, 3) + monthDay.substring(4);
		} else if (month < 10) {
			monthDay = monthDay.substring(1);
		} else if (day < 10) {
			monthDay = monthDay.substring(0, 3) + monthDay.substring(4);
		}
		int yearMonth = Integer.valueOf(yearMonthDay.substring(5, 7));
		int yearDay = Integer.valueOf(yearMonthDay.substring(8, 10));
		if (yearMonth < 10 && yearDay < 10) {
			yearMonthDay = yearMonthDay.substring(0, 5)
					+ yearMonthDay.substring(6, 8) + yearMonthDay.substring(9);
		} else if (yearMonth < 10) {
			yearMonthDay = yearMonthDay.substring(0, 5)
					+ yearMonthDay.substring(6);
		} else if (yearDay < 10) {
			yearMonthDay = yearMonthDay.substring(0, 8)
					+ yearMonthDay.substring(9);
		}
		String str = " 00:00:00";
		float currDay = farmatTime(currDate.substring(0, 10) + str);
		float commitDay = farmatTime(commitDate.substring(0, 10) + str);
		int currYear = Integer.valueOf(currDate.substring(0, 4));
		int commitYear = Integer.valueOf(commitDate.substring(0, 4));
		int flag = (int) (farmatTime(currDate) / 1000 - farmatTime(commitDate) / 1000);
		String des = null;
		String hourMin = commitDate.substring(11, 16);
		int temp = flag;
		if (temp < 60) {
			if (commitDay < currDay) {
				des = "����  " + hourMin;
			} else {
				des = "�ո�";
			}
		} else if (temp < 60 * 60) {
			if (commitDay < currDay) {
				des = "����  " + hourMin;
			} else {
				des = temp / 60 + "������ǰ";
				System.out.println(des);
			}
		} else if (temp < 60 * 60 * 24) {
			int hour = temp / (60 * 60);
			if (commitDay < currDay) {
				des = "����  " + hourMin;
			} else {
				if (hour < 6) {
					des = hour + "Сʱǰ";
				} else {
					des = hourMin;
				}
			}
		} else if (temp < (60 * 60 * 24 * 2)) {
			if (nowDate - commit == 1) {
				des = "����  " + hourMin;
			} else {
				des = "ǰ��  " + hourMin;
			}
		} else if (temp < 60 * 60 * 60 * 3) {
			if (nowDate - commit == 2) {
				des = "ǰ��  " + hourMin;
			} else {
				if (commitYear < currYear) {
					des = yearMonthDay + hourMin;
				} else {
					des = monthDay + hourMin;
				}
			}
		} else {
			if (commitYear < currYear) {
				des = yearMonthDay + hourMin;
			} else {
				des = monthDay + hourMin;
			}
		}
		if (des == null) {
			des = commitDate;
		}
		return des;
	}

	public static Date Date() {
		Date datetimeDate;
		Long dates = 1361514787384L;
		datetimeDate = new Date(dates);
		return datetimeDate;
	}
}
