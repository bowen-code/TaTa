package com.tata.activity;

import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class DetailPriceActivity extends BaseActivity {

	private String include,noContain;
	private TextView includeTextView,noContainTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_detail_price);
		setTopText("·ÑÓÃ°üº¬");
		Intent intent=getIntent();
		include=intent.getStringExtra("include");
		noContain=intent.getStringExtra("noContain");
		includeTextView=(TextView) findViewById(R.id.include);
		noContainTextView=(TextView) findViewById(R.id.noContain);
		includeTextView.setText(include);
		noContainTextView.setText(noContain);
	}


}
