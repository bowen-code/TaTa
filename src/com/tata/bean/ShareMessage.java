package com.tata.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.smssdk.statistics.NewAppReceiver;

import android.R.integer;
import android.graphics.Bitmap;

public class ShareMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int type = 0;// 0代表文字分享 1代表图片分享
	private Bitmap figure;
	private String shareImg1;
	private String shareImg2;
	private int age;
	private String phoneNumber;
	private int shareID=-1;
    private List<Dianzan> dianzan=new ArrayList<Dianzan>();
    private List<ShareComment> shareComment=new ArrayList<ShareComment>();
    private boolean showAllComment=false;
    

	public boolean isShowAllComment() {
		return showAllComment;
	}

	public void setShowAllComment(boolean showAllComment) {
		this.showAllComment = showAllComment;
	}

	public List<ShareComment> getShareComment() {
		return shareComment;
	}

	public void setShareComment(List<ShareComment> shareComment) {
		this.shareComment = shareComment;
	}

	public List<Dianzan> getDianzan() {
		return dianzan;
	}

	public void setDianzan(List<Dianzan> dianzan) {
		this.dianzan = dianzan;
	}

	public int getMyfouce() {
		return myfouce;
	}

	public void setMyfouce(int myfouce) {
		this.myfouce = myfouce;
	}

	public int getMycollect() {
		return mycollect;
	}

	public void setMycollect(int mycollect) {
		this.mycollect = mycollect;
	}

	private int myfouce;// 0否 1
	private int mycollect;// 0否 1

	public int getShareID() {
		return shareID;
	}

	public void setShareID(int shareID) {
		this.shareID = shareID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getShareImg1() {
		return shareImg1;
	}

	public void setShareImg1(String shareImg1) {
		this.shareImg1 = shareImg1;
	}

	public String getShareImg2() {
		return shareImg2;
	}

	public void setShareImg2(String shareImg2) {
		this.shareImg2 = shareImg2;
	}

	public String getShareImg3() {
		return shareImg3;
	}

	public void setShareImg3(String shareImg3) {
		this.shareImg3 = shareImg3;
	}

	public String getShareImg4() {
		return shareImg4;
	}

	public void setShareImg4(String shareImg4) {
		this.shareImg4 = shareImg4;
	}

	private String shareImg3;
	private String shareImg4;

	private String name;
	private int gender;
	private String birthday;
	private String time;
	private String content;
	private String userImg;

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	private Bitmap picture;
	private String distance;
	private List<String> shareImg;

	public List<String> getShareImg() {
		return shareImg;
	}

	public void setShareImg(List<String> shareImg) {
		this.shareImg = shareImg;
	}

	private String locationXY="";
	private String shareLocation;

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getLocationXY() {
		return locationXY;
	}

	public void setLocationXY(String locationXY) {
		this.locationXY = locationXY;
	}

	public String getShareLocation() {
		return shareLocation;
	}

	public void setShareLocation(String shareLocation) {
		this.shareLocation = shareLocation;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Bitmap getFigure() {
		return figure;
	}

	public void setFigure(Bitmap figure) {
		this.figure = figure;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public ShareMessage(Bitmap figure, String name, int gender, int age,
			String time, String content, Bitmap picture, String distance) {
		this.figure = figure;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.time = time;
		this.content = content;
		this.picture = picture;
		this.distance = distance;
	}

	public ShareMessage(Bitmap figure, String name, int gender, int age,
			String time, String content, String distance) {
		this.figure = figure;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.time = time;
		this.content = content;
		this.distance = distance;
	}

	public ShareMessage() {
	}

}
