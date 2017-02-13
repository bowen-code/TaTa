package com.tata.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;

public class Route implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int dayID;
	private String generalize;
	private String describe;
	private String remarkr;
	private List<String> images = new ArrayList<String>();

	public int getDayID() {
		return dayID;
	}

	public void setDayID(int dayID) {
		this.dayID = dayID;
	}

	public String getGeneralize() {
		return generalize;
	}

	public void setGeneralize(String generalize) {
		this.generalize = generalize;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getRemarkr() {
		return remarkr;
	}

	public void setRemarkr(String remarkr) {
		this.remarkr = remarkr;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Route(int dayID, String generalize, String describe, String remarkr,
			List<String> images) {
		this.dayID = dayID;
		this.generalize = generalize;
		this.describe = describe;
		this.remarkr = remarkr;
		this.images = images;
	}

	public Route(int dayID, String generalize, String describe, String remarkr) {
		super();
		this.dayID = dayID;
		this.generalize = generalize;
		this.describe = describe;
		this.remarkr = remarkr;
	}
	
	

}
