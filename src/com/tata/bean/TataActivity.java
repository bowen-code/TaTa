package com.tata.bean;

import android.graphics.Bitmap;

public class TataActivity {
       private Bitmap image;
       private String type;
       private String describe;
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public TataActivity(Bitmap image, String type, String describe) {
		this.image = image;
		this.type = type;
		this.describe = describe;
	}
	public TataActivity() {
	}
       
	
}
