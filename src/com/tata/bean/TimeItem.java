package com.tata.bean;

import java.util.List;

import android.graphics.Bitmap;

public class TimeItem {

	private int type;
	private String year;
	private String m_d;
	private String word;
	private Bitmap bitmap;
	private List<String> images;
	
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getM_d() {
		return m_d;
	}
	public void setM_d(String m_d) {
		this.m_d = m_d;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public TimeItem(int type, String year, String m_d, String word,
			List<String> images) {
		super();
		this.type = type;
		this.year = year;
		this.m_d = m_d;
		this.word = word;
		this.images = images;
	}
	public TimeItem() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TimeItem(int type, String year, String m_d, String word) {
		super();
		this.type = type;
		this.year = year;
		this.m_d = m_d;
		this.word = word;
	}
}
