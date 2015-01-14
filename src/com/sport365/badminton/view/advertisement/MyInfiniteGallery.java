package com.sport365.badminton.view.advertisement;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class MyInfiniteGallery extends Gallery {
	private int galleryCount;

	public MyInfiniteGallery(Context context) {
		super(context);
	}
	
	public MyInfiniteGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyInfiniteGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {		
		int kEvent;
		if (isScrollingLeft(e1, e2)) {
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);

		if (this.getSelectedItemPosition() == 0 && galleryCount > 1) {
			try {
				this.setSelection(galleryCount);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;

	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	public int getGalleryCount() {
		return galleryCount;
	}

	public void setGalleryCount(int galleryCount) {
		this.galleryCount = galleryCount;
	}

}