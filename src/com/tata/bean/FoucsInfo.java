package com.tata.bean;

import android.graphics.Bitmap;

public class FoucsInfo {

	private String figureUrl;
	private Bitmap bitmap;
	private String username;
	private String shareMsg;
	private String phoneNumber;
	private String time;
	private String birthday;
	private int gender;
	private String userImg;
	
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public FoucsInfo() {
		// TODO Auto-generated constructor stub
	}
	public FoucsInfo(Bitmap bitmap, String username,
			String shareMsg) {
		this.bitmap = bitmap;
		this.username = username;
		this.shareMsg = shareMsg;
	}
	public FoucsInfo(String figureUrl, Bitmap bitmap, String username,
			String shareMsg, String phoneNumber) {
		this.figureUrl = figureUrl;
		this.bitmap = bitmap;
		this.username = username;
		this.shareMsg = shareMsg;
		this.phoneNumber = phoneNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getFigureUrl() {
		return figureUrl;
	}
	public void setFigureUrl(String figureUrl) {
		this.figureUrl = figureUrl;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getShareMsg() {
		return shareMsg;
	}
	public void setShareMsg(String shareMsg) {
		this.shareMsg = shareMsg;
	}
	
}
