package com.tata.bean;

import java.io.Serializable;

public class Dianzan implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int praiseID;
	private String userName;
	private String praisePhoneNumber;
	public int getPraiseID() {
		return praiseID;
	}
	public void setPraiseID(int praiseID) {
		this.praiseID = praiseID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPraisePhoneNumber() {
		return praisePhoneNumber;
	}
	public void setPraisePhoneNumber(String praisePhoneNumber) {
		this.praisePhoneNumber = praisePhoneNumber;
	}
	public Dianzan(int praiseID, String userName, String praisePhoneNumber) {
		super();
		this.praiseID = praiseID;
		this.userName = userName;
		this.praisePhoneNumber = praisePhoneNumber;
	}
	public Dianzan() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
