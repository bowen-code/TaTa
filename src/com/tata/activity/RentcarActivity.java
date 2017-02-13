package com.tata.activity;

import java.util.ArrayList;
import java.util.List;

import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.adapterAndListener.NearAdapter;
import com.tata.adapterAndListener.RentCarAdapter;
import com.tata.bean.NearTravel;
import com.tata.bean.RentCar;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class RentcarActivity extends BaseActivity {

	private ListView rentcarListView;
	private List<RentCar> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTopText("租车");
		View view = getLayoutInflater()
				.inflate(R.layout.activity_rentcar, null);
		setCenterView(view);
		init(view);
		rentcarListView.setAdapter(new RentCarAdapter(list,this));
	}

	private void init(View view) {
		rentcarListView = (ListView) view.findViewById(R.id.rentCarList);
		list = new ArrayList<RentCar>();
		list.add(new RentCar(BitmapFactory.decodeResource(getResources(),
				R.drawable.scene1), "敦煌西线拼车包车", 120, 100,200));
		list.add(new RentCar(BitmapFactory.decodeResource(getResources(),
				R.drawable.scene2), "敦煌西线拼车包车", 120, 100,200));
		list.add(new RentCar(BitmapFactory.decodeResource(getResources(),
				R.drawable.scene1), "敦煌西线拼车包车", 120, 100,200));

	}

}
