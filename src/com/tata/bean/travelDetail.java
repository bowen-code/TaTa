package com.tata.bean;

public class travelDetail {

	private String travelName;
	private String travelImg;
	private String travelAddr;
	private String travelDetail;
	private double trvaelPrice;
	private String time;
	private String travelLati;
	private String travelLongti;
	private int travelLike;
	/*
	 * 返回0和1 0代表有住宿，1代表无
	 */
	private int hotel;

	public String getTravelName() {
		return travelName;
	}

	public void setTravelName(String travelName) {
		this.travelName = travelName;
	}

	public String getTravelImg() {
		return travelImg;
	}

	public void setTravelImg(String travelImg) {
		this.travelImg = travelImg;
	}

	public String getTravelAddr() {
		return travelAddr;
	}

	public void setTravelAddr(String travelAddr) {
		this.travelAddr = travelAddr;
	}

	public String getTravelDetail() {
		return travelDetail;
	}

	public void setTravelDetail(String travelDetail) {
		this.travelDetail = travelDetail;
	}

	public double getTrvaelPrice() {
		return trvaelPrice;
	}

	public void setTrvaelPrice(double trvaelPrice) {
		this.trvaelPrice = trvaelPrice;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTravelLati() {
		return travelLati;
	}

	public void setTravelLati(String travelLati) {
		this.travelLati = travelLati;
	}

	public String getTravelLongti() {
		return travelLongti;
	}

	public void setTravelLongti(String travelLongti) {
		this.travelLongti = travelLongti;
	}

	public int getTravelLike() {
		return travelLike;
	}

	public void setTravelLike(int travelLike) {
		this.travelLike = travelLike;
	}

	public int getHotel() {
		return hotel;
	}

	public void setHotel(int hotel) {
		this.hotel = hotel;
	}

	public travelDetail(String travelName, String travelImg, String travelAddr,
			String travelDetail, double trvaelPrice, String time,
			String travelLati, String travelLongti, int travelLike, int hotel) {
		this.travelName = travelName;
		this.travelImg = travelImg;
		this.travelAddr = travelAddr;
		this.travelDetail = travelDetail;
		this.trvaelPrice = trvaelPrice;
		this.time = time;
		this.travelLati = travelLati;
		this.travelLongti = travelLongti;
		this.travelLike = travelLike;
		this.hotel = hotel;
	}

	public travelDetail() {
	}

}
