package com.tata.bean;

public class ShareDatas {
	private String time;
	private String userName;
	private String shareContext;
	private String shareImgUrl;
	private String userImgUrl;
	private String shareId;
	private String gender;
	private int age;
	private String distance;
	
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getShareContext() {
		return shareContext;
	}
	public void setShareContext(String shareContext) {
		this.shareContext = shareContext;
	}
	public String getShareImgUrl() {
		return shareImgUrl;
	}
	public void setShareImgUrl(String shareImgUrl) {
		this.shareImgUrl = shareImgUrl;
	}
	public String getUserImgUrl() {
		return userImgUrl;
	}
	public void setUserImgUrl(String userImgUrl) {
		this.userImgUrl = userImgUrl;
	}
	public String getShareId() {
		return shareId;
	}
	public void setShareId(String shareId) {
		this.shareId = shareId;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public ShareDatas() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ShareDatas(String time, String userName,
			String shareContext, String shareImgUrl, String userImgUrl,
			String shareId, String gender, int age, String distance) {
		super();
		this.time = time;
		this.userName = userName;
		this.shareContext = shareContext;
		this.shareImgUrl = shareImgUrl;
		this.userImgUrl = userImgUrl;
		this.shareId = shareId;
		this.gender = gender;
		this.age = age;
		this.distance = distance;
	}

	

}
