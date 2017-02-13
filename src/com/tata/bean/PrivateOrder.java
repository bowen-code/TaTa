package com.tata.bean;

public class PrivateOrder {

	private int privateID;
	private int payPrivateIndentID;
	private String destination;
	private String startPlace;
	private String startDate;
	private String privateIndentTime;
	private String description;
	private int days;
	private int personNum;
	private int privateIndentState;
	private int budget;
	
	public int getPayPrivateIndentID() {
		return payPrivateIndentID;
	}
	public void setPayPrivateIndentID(int payPrivateIndentID) {
		this.payPrivateIndentID = payPrivateIndentID;
	}
	public int getPrivateID() {
		return privateID;
	}
	public void setPrivateID(int privateID) {
		this.privateID = privateID;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getStartPlace() {
		return startPlace;
	}
	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getPrivateIndentTime() {
		return privateIndentTime;
	}
	public void setPrivateIndentTime(String privateIndentTime) {
		this.privateIndentTime = privateIndentTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getPersonNum() {
		return personNum;
	}
	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}
	public int getPrivateIndentState() {
		return privateIndentState;
	}
	public void setPrivateIndentState(int privateIndentState) {
		this.privateIndentState = privateIndentState;
	}
	public int getBudget() {
		return budget;
	}
	public void setBudget(int budget) {
		this.budget = budget;
	}
	
}
