package com.tata.bean;

import java.io.Serializable;

public class ShareComment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int commentID;
	private String userNameC;
	private String parName;
	private String commentContent;
	private String phoneNumberC;
	private String parPhoneNumber;
	
	public String getPhoneNumberC() {
		return phoneNumberC;
	}
	public void setPhoneNumberC(String phoneNumberC) {
		this.phoneNumberC = phoneNumberC;
	}
	public String getParPhoneNumber() {
		return parPhoneNumber;
	}
	public void setParPhoneNumber(String parPhoneNumber) {
		this.parPhoneNumber = parPhoneNumber;
	}
	public int getCommentID() {
		return commentID;
	}
	public void setCommentID(int commentID) {
		this.commentID = commentID;
	}
	public String getUserNameC() {
		return userNameC;
	}
	public void setUserNameC(String userNameC) {
		this.userNameC = userNameC;
	}
	public String getParName() {
		return parName;
	}
	public void setParName(String parName) {
		this.parName = parName;
	}
	public String getCommentContent() {
		return commentContent;
	}
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}
	public ShareComment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
