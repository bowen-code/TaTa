package com.tata.bean;

public class PersonInfo {
	private String userName;
	private String school;
	private String userImgUrl;
	private String userArrearage;
	private String userNoTravel;
	private String NoEvaluated;
	private double Balance;
	private double Bonus;
	private int Integration;
	private String birthday;
	private String phoneNumber;
	private int gender=3;
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public PersonInfo(String userName, String userImgUrl, double balance,
			double bonus, int integration, String birthday, String phoneNumber,
			int gender) {
		this.userName = userName;
		this.userImgUrl = userImgUrl;
		Balance = balance;
		Bonus = bonus;
		Integration = integration;
		this.birthday = birthday;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getUserImgUrl() {
		return userImgUrl;
	}
	public void setUserImgUrl(String userImgUrl) {
		this.userImgUrl = userImgUrl;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getuserImgUrl() {
		return userImgUrl;
	}
	public void setuserImgUrl(String userImgUrl) {
		this.userImgUrl = userImgUrl;
	}
	public String getUserArrearage() {
		return userArrearage;
	}
	public void setUserArrearage(String userArrearage) {
		this.userArrearage = userArrearage;
	}
	public String getUserNoTravel() {
		return userNoTravel;
	}
	public void setUserNoTravel(String userNoTravel) {
		this.userNoTravel = userNoTravel;
	}
	public String getNoEvaluated() {
		return NoEvaluated;
	}
	public void setNoEvaluated(String noEvaluated) {
		NoEvaluated = noEvaluated;
	}
	public double getBalance() {
		return Balance;
	}
	public void setBalance(double balance) {
		Balance = balance;
	}
	public double getBonus() {
		return Bonus;
	}
	public void setBonus(double bonus) {
		Bonus = bonus;
	}
	public int getIntegration() {
		return Integration;
	}
	public void setIntegration(int integration) {
		Integration = integration;
	}
	public PersonInfo(String userName, String userImgUrl, String userArrearage,
			String userNoTravel, String noEvaluated, double balance,
			double bonus, int integration) {
		this.userName = userName;
		this.userImgUrl = userImgUrl;
		this.userArrearage = userArrearage;
		this.userNoTravel = userNoTravel;
		NoEvaluated = noEvaluated;
		Balance = balance;
		Bonus = bonus;
		Integration = integration;
	}
	public PersonInfo() {
	}
	@Override
	public String toString() {
		return "PersonInfo [userName=" + userName + ", school=" + school
				+ ", userImgUrl=" + userImgUrl + ", userArrearage="
				+ userArrearage + ", userNoTravel=" + userNoTravel
				+ ", NoEvaluated=" + NoEvaluated + ", Balance=" + Balance
				+ ", Bonus=" + Bonus + ", Integration=" + Integration
				+ ", birthday=" + birthday + ", phoneNumber=" + phoneNumber
				+ ", gender=" + gender + "]";
	}
    
	
}
