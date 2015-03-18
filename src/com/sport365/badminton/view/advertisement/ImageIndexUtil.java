package com.sport365.badminton.view.advertisement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import com.sport365.badminton.R;

public class ImageIndexUtil extends View{

	private Bitmap selectorBitmap, bitmap ;
	
	private int total, selectIndex = 0 ;
	private Canvas canvas ;
	private int width, height ;
	private int bitmapWidth, bitmapHeight ;
	private int space = 8 ;
	private Context context ;
	private LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
	public ImageIndexUtil(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context ;
		init(context) ;
	}

	public ImageIndexUtil(Context context) {
		super(context);
		this.context = context ;
		init(context) ;
	}
	
	/**
	 * 设置选择索引的图片标示
	 * @param img1
	 * @param img2
	 *2012-9-13 上午9:46:19
	 */
	public void setImageBitmap(int img1, int img2){
		selectorBitmap = BitmapFactory.decodeResource(context.getResources(), img2) ;
		bitmap= BitmapFactory.decodeResource(context.getResources(), img1) ;
		bitmapWidth = bitmap.getWidth() ;
		bitmapHeight = bitmap.getHeight() ;
		setLayoutParams(params) ;	
	}
	
	private void init(Context context) {
		selectorBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.icon_pointlight) ;
		bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.icon_point) ;
		bitmapWidth = bitmap.getWidth() ;
		bitmapHeight = bitmap.getHeight() ;
		setLayoutParams(params) ;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas = canvas ;
		width = getWidth() ;
		height = getHeight() ;
		drawSelector(selectIndex) ;
	}
	Paint paint = new Paint() ;
	private void drawSelector(int index) {
		for (int i = 0; i < total; i++) {
			if (i == index) {
				canvas.drawBitmap(selectorBitmap, (bitmapWidth + space) * i  , (height - bitmapHeight) / 2, paint) ;
				continue ;
			}
			canvas.drawBitmap(bitmap, (bitmapWidth + space) * i  , (height - bitmapHeight) / 2, paint) ;
		}
	}
	
	
	public void setTotal(int total) {
		this.total = total;
		width = bitmapWidth * total + space * (total -1) ;
		height = bitmapHeight * 2 ;
		params.width = width ;
		params.height = height ;
		this.setLayoutParams(params) ;
	}

	public int getTotal() {
		return total;
	}

	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
		invalidate() ;
	}

	public int getSelectIndex() {
		return selectIndex;
	}
	
	public void setSpace(int space) {
		this.space = space;
	}
}
