package com.tata.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GuaGuaKa extends View {
	/** 
     * 绘制线条的Paint,即用户手指绘制Path 
     */  
    private Paint mOutterPaint = new Paint();  
    /** 
     * 记录用户绘制的Path 
     */  
    private Path mPath = new Path();  
    /** 
     * 内存中创建的Canvas 
     */  
    private Canvas mCanvas;  
    /** 
     * mCanvas绘制内容在其上 
     */  
    private Bitmap mBitmap;  
  
    private int mLastX;  
    private int mLastY;
    private Paint mBackPint = new Paint();  
    private Rect mTextBound = new Rect();  
    private String mText = "50000000";  
    /** 
     * 初始化canvas的绘制用的画笔 
     */  
    private void setUpBackPaint()  
    {  
        mBackPint.setStyle(Style.FILL);  
        mBackPint.setTextScaleX(2f);  
        mBackPint.setColor(Color.DKGRAY);  
        mBackPint.setTextSize(15);  
        mBackPint.getTextBounds(mText, 0, mText.length(), mTextBound);  
    }  
	public GuaGuaKa(Context context) {
		super(context);
	}

	public GuaGuaKa(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GuaGuaKa(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(); 
	}
	private void init()  
    {  
        mPath = new Path();  
  
    }  
	
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		// 初始化bitmap
		mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		setUpBackPaint();
		//绘制这改成
		mCanvas.drawColor(Color.parseColor("#C8C8C8"));
	}
	
	
	/** 
     * 绘制线条 
     */  
    private void drawPath()  
    {  
    	mOutterPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));  
        mCanvas.drawPath(mPath, mOutterPaint);  
  
    }  
    
	@Override
	protected void onDraw(Canvas canvas)
	{
		 //绘制奖项  
        canvas.drawText(mText, getWidth() / 2 - mTextBound.width() / 2,  
                getHeight() / 2 + mTextBound.height() / 2, mBackPint);  
          
        drawPath();  
        canvas.drawBitmap(mBitmap, 0, 0, null);  
	}
  
    @Override  
    public boolean onTouchEvent(MotionEvent event)  
    {  
        int action = event.getAction();  
        int x = (int) event.getX();  
        int y = (int) event.getY();  
        switch (action)  
        {  
        case MotionEvent.ACTION_DOWN:  
            mLastX = x;  
            mLastY = y;  
            mPath.moveTo(mLastX, mLastY);  
            break;  
        case MotionEvent.ACTION_MOVE:  
  
            int dx = Math.abs(x - mLastX);  
            int dy = Math.abs(y - mLastY);  
  
            if (dx > 3 || dy > 3)  
                mPath.lineTo(x, y);  
  
            mLastX = x;  
            mLastY = y;  
            break;  
        }  
  
        invalidate();  
        return true;  
    }  
}
