package com.tata.adapterAndListener;

import com.tata.R;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;

public class GuidePageChangeListener implements ViewPager.OnPageChangeListener{
   private ImageView[] imageViews;
	
	
	public GuidePageChangeListener(ImageView[] imageViews) {
	super();
	this.imageViews = imageViews;
}

	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		 for (int i = 0; i < imageViews.length; i++) {
             imageViews[arg0]
                     .setImageResource(R.drawable.ic_page_selected);
             if (arg0 != i) {
                 imageViews[i]
                         .setImageResource(R.drawable.ic_page_normal);
             }

         }
	}

}
