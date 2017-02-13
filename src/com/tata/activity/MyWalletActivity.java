package com.tata.activity;

import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

public class MyWalletActivity extends BaseActivity {
    private RelativeLayout balance,hongbao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=getLayoutInflater().inflate(R.layout.activity_my_wallet, null);
		init(view);
	}

	private void init(View view) {
		setTopText("ÎÒµÄÇ®°ü");
		setCenterView(view);
		balance=(RelativeLayout) findViewById(R.id.balance);
		balance.setOnClickListener(this);
		hongbao=(RelativeLayout) findViewById(R.id.hongbao);
		hongbao.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.balance:
			intent=new Intent(this,BalanceActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.hongbao:
			intent=new Intent(this,HongbaoActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;

		default:
			break;
		}
	}
}
