package com.tata.bean;

public class HomePageImage {

	private String message;
	private String code;
	private String url;
	private String imgUrl;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public HomePageImage(String message, String code, String url, String imgUrl) {
		this.message = message;
		this.code = code;
		this.url = url;
		this.imgUrl = imgUrl;
	}
	public HomePageImage() {
	}
	
}
