package com.tata.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyViewPager extends ViewPager{

	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
    
	PointF downPoint = new PointF();  
    OnTouchListener onTouchListener;  
  
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
    	// TODO Auto-generated method stub
    	return true;
    }
    
    @Override  
    public boolean onTouchEvent(MotionEvent evt) {  
        switch (evt.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            // ��¼����ʱ�������  
            downPoint.x = evt.getX();  
            downPoint.y = evt.getY();  
            if (this.getChildCount() > 1) { //�����ݣ�����1��ʱ  
                // ֪ͨ�丸�ؼ������ڽ��е��Ǳ��ؼ��Ĳ���������������  
                getParent().requestDisallowInterceptTouchEvent(true);  
            }  
            break;  
        case MotionEvent.ACTION_MOVE:  
            if (this.getChildCount() > 1) { //�����ݣ�����1��ʱ  
                // ֪ͨ�丸�ؼ������ڽ��е��Ǳ��ؼ��Ĳ���������������  
                getParent().requestDisallowInterceptTouchEvent(true);  
                onSwitchTouch(this);
            }  
            break;  
        case MotionEvent.ACTION_UP:  
        	if(downPoint.x==evt.getX()&&
            downPoint.y == evt.getY())
            	onUpTouch(this);  
            break;  
        }  
        return super.onTouchEvent(evt);  
    }  
  
    public void onUpTouch(View v) {  
        if (onTouchListener != null) {  
            onTouchListener.onUpTouch();  
        }  
    }  
    public void onSwitchTouch(View v) {  
    	if (onTouchListener != null) {  
    		onTouchListener.onSwitchTouch();  
    	}  
    }  
  
    public interface OnTouchListener {  
        public void onUpTouch();  
        public void onSwitchTouch();  
    }  
  
    public void setOnTouchListener(  
            OnTouchListener onTouchListener) {  
        this.onTouchListener = onTouchListener;  
    }  
}
