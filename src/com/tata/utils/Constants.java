package com.tata.utils;

import java.util.ArrayList;
import java.util.List;

import com.tata.activity.NewPwdActivity;
import com.tata.activity.TravelerActivity;
import com.tata.bean.ShareMessage;
import com.tata.bean.Traveler;

import android.R.integer;
import android.os.Environment;

public class Constants {
	public static boolean SHOW_LOG = true;
	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/tata/";
	public static String startTime="";
	public static String endTime="";
	public static String userName="";
	public static String version;
	public static String versionName;
	public static List<byte[]> data;
    public static boolean freshFlag=false;
    public static boolean completeFlag=false;
    public static boolean delete=false;
    public static List<Traveler> travelers=new ArrayList<Traveler>();
	public static int BanjiNum=1;
	public static int NearNum=1;
	public static int collect_product=1;
	public static int collect_share=1;
	public static List<Integer> deleteList=new ArrayList<Integer>();
	public static List<Integer> collectList=new ArrayList<Integer>();
	public static List<Integer> mydeletecollectShare=new ArrayList<Integer>();
}
