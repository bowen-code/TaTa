package com.tata.bean;

import java.io.Serializable;

import android.R.integer;


public class Reserve implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String time;
	private String title;
	private String ticketName;
	private int ticketsum;
	private String validTime;
	private int price;
	private int type;
	private int indentID;
	private String indentTime;
	private String startTime;
	private int degree;
	private String productID;
	private int productState;
	private String contactsName;
	private String contactsPho;
	private String id;
	
	 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getProductState() {
		return productState;
	}
	public void setProductState(int productState) {
		this.productState = productState;
	}
	public String getContactsName() {
		return contactsName;
	}
	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	public String getContactsPho() {
		return contactsPho;
	}
	public void setContactsPho(String contactsPho) {
		this.contactsPho = contactsPho;
	}
	private int payIndentID;
	private int indentState;
	 
	private String[][] traveller;
	
	
	public int getIndentState() {
		return indentState;
	}
	public void setIndentState(int indentState) {
		this.indentState = indentState;
	}
	public int getPayIndentID() {
		return payIndentID;
	}
	public void setPayIndentID(int payIndentID) {
		this.payIndentID = payIndentID;
	}
	public String[][] getTraveller() {
		return traveller;
	}
	public void setTraveller(String[][] traveller) {
		this.traveller = traveller;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getIndentTime() {
		return indentTime;
	}
	public void setIndentTime(String indentTime) {
		this.indentTime = indentTime;
	}
	public int getIndentID() {
		return indentID;
	}
	public void setIndentID(int indentID) {
		this.indentID = indentID;
	}
	public Reserve(String time, String title, String ticketName,
			int ticketsum, String validTime, int price, int type) {
		super();
		this.time = time;
		this.title = title;
		this.ticketName = ticketName;
		this.ticketsum = ticketsum;
		this.validTime = validTime;
		this.price = price;
		this.type = type;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String gettitle() {
		return title;
	}
	public void settitle(String title) {
		this.title = title;
	}
	public String getTicketName() {
		return ticketName;
	}
	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}
	public int getTicketsum() {
		return ticketsum;
	}
	public void setTicketsum(int ticketsum) {
		this.ticketsum = ticketsum;
	}
	public String getValidTime() {
		return validTime;
	}
	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Reserve(String time, String title, String ticketName,
			int ticketsum, String validTime, int price) {
		this.time = time;
		this.title = title;
		this.ticketName = ticketName;
		this.ticketsum = ticketsum;
		this.validTime = validTime;
		this.price = price;
	}
	public Reserve() {
	}
	
}
