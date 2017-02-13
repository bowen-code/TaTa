package com.tata.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.tata.R;
import com.tata.R.layout;
import com.tata.R.menu;
import com.tata.utils.Constants;
import com.tata.utils.ToastUtil;
import com.tata.view.Day;
import com.tata.view.ExtendedCalendarView;
import com.tata.view.ExtendedCalendarView.OnDayClickListener;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.AdapterView;
import android.widget.Toast;

public class CalendarActivity extends BaseActivity implements
		OnDayClickListener {

	private ExtendedCalendarView extendedCalendarView;
	private boolean flag = false;
	private String startTime, endTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLayoutInflater().inflate(R.layout.activity_calendar,
				null);
		init(view);
	}

	private void init(View view) {
		setTopText("日期选择");
		setCenterView(view);
		extendedCalendarView = (ExtendedCalendarView) view
				.findViewById(R.id.calendar);
		extendedCalendarView.setOnDayClickListener(this);
	}

	Day temp;
	private Date date;

	public void onDayClicked(AdapterView<?> adapter, View view, int position,
			long id, com.tata.view.Day day) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sf.parse(day.toString() + " 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date now = new Date();
		if (date.getTime() >= now.getTime()) {
			Constants.startTime = day.toString();
			Toast.makeText(this, "选择日期成功", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, "请选择以后的日期", Toast.LENGTH_SHORT).show();
		}
	}

}
