package com.tata.activity;

import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

public class HongbaoActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=getLayoutInflater().inflate(R.layout.activity_hongbao, null);
		init(view);
	}

	private void init(View view) {
		setTopText("ÎÒµÄºì°ü");
		setCenterView(view);
	}

}
