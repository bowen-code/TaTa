package com.tata.bean;

import android.graphics.Bitmap;

public class RentCar {

	private Bitmap scene;
	private String title;
	private int saleNum;
	private int commentNum;
	private double price;
	public Bitmap getScene() {
		return scene;
	}
	public void setScene(Bitmap scene) {
		this.scene = scene;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(int saleNum) {
		this.saleNum = saleNum;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public RentCar(Bitmap scene, String title, int saleNum, int commentNum,
			double price) {
		this.scene = scene;
		this.title = title;
		this.saleNum = saleNum;
		this.commentNum = commentNum;
		this.price = price;
	}
	public RentCar() {
	}
	
}
