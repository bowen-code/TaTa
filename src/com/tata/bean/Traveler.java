package com.tata.bean;

public class Traveler {

	private String name;
	private String idcard;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public Traveler(String name, String idcard) {
		super();
		this.name = name;
		this.idcard = idcard;
	}
	
}
