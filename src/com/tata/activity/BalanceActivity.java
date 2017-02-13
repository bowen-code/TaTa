package com.tata.activity;

import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class BalanceActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=getLayoutInflater().inflate(R.layout.activity_balance, null);
		init(view);
	}

	private void init(View view) {
		setTopText("’Àªß”‡∂Ó");
		setCenterView(view);
	}


}
