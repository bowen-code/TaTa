package com.tata.activity;

import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class RuleActivity extends BaseActivity {

	private String refund,booking;
	private TextView refundTextView,bookingTextView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterView(R.layout.activity_rule);
		setTopText("Ô¤¶¨ÐèÖª");
		Intent intent=getIntent();
		refund=intent.getStringExtra("refund");
		booking=intent.getStringExtra("booking");
		refundTextView=(TextView) findViewById(R.id.refund);
		bookingTextView=(TextView) findViewById(R.id.booking);
		refundTextView.setText(refund);
		bookingTextView.setText(booking);
	}


}
