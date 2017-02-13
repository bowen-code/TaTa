package com.tata.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tata.activity.NewPwdActivity;

import android.graphics.Bitmap;

public class NearTravel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productID;
	private int typeID;
	private String cityName;
	private String remarkp;
	private String startPlace;
	private String rotue;
//	private String description;
	private String include;
	private String noContain;
	private String refund;
	private String booking;
	private String serviceTel1;
	private String serviceTel2;
	
	
	public String getServiceTel1() {
		return serviceTel1;
	}
	public void setServiceTel1(String serviceTel1) {
		this.serviceTel1 = serviceTel1;
	}
	public String getServiceTel2() {
		return serviceTel2;
	}
	public void setServiceTel2(String serviceTel2) {
		this.serviceTel2 = serviceTel2;
	}
	public String getInclude() {
		return include;
	}
	public void setInclude(String include) {
		this.include = include;
	}
	public String getNoContain() {
		return noContain;
	}
	public void setNoContain(String noContain) {
		this.noContain = noContain;
	}
	public String getRefund() {
		return refund;
	}
	public void setRefund(String refund) {
		this.refund = refund;
	}
	public String getbooking() {
		return booking;
	}
	public void setbooking(String booking) {
		this.booking = booking;
	}
	private int price;
	private int priceTwo;
	private String title;
	private String images[]=new String[3];
	private List<Route> routeList=new ArrayList<Route>();
	private List<String> days=new ArrayList<String>();
	
	public List<String> getDays() {
		return days;
	}
	public void setDays(List<String> days) {
		this.days = days;
	}
	public List<Route> getRouteList() {
		return routeList;
	}
	public void setRouteList(List<Route> routeList) {
		this.routeList = routeList;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	
	public String getRemarkp() {
		return remarkp;
	}
	public void setRemarkp(String remarkp) {
		this.remarkp = remarkp;
	}
	public NearTravel(String serviceTel1,String serviceTel2,String productID, String remarkp,int typeID,
//			String cityName,
			 String rotue, int price,
			int priceTwo, String title,String images[],List<Route> routeList,String include,String noContain,String refund,String booking,List<String> days) {
		this.productID = productID;
		this.typeID = typeID;
//		this.cityName = cityName;
		this.rotue = rotue;
//		this.description = description;
		this.price = price;
		this.priceTwo = priceTwo;
		this.title = title;
		this.images = images;
		this.routeList=routeList;
		this.remarkp=remarkp;
		this.include=include;
		this.noContain=noContain;
		this.refund=refund;
		this.booking=booking;
		this.days=days;
		this.serviceTel1=serviceTel1;
		this.serviceTel2=serviceTel2;
	}
	
	
	
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public int getTypeID() {
		return typeID;
	}
	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getStartPlace() {
		return startPlace;
	}
	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}
	public String getRotue() {
		return rotue;
	}
	public void setRotue(String rotue) {
		this.rotue = rotue;
	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getPriceTwo() {
		return priceTwo;
	}
	public void setPriceTwo(int priceTwo) {
		this.priceTwo = priceTwo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public NearTravel() {
	}
	
	
}
