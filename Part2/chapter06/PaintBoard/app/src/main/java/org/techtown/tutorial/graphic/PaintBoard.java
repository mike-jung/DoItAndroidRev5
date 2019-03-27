package org.techtown.tutorial.graphic;

import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 뷰를 상속한 기본 페인트보드 클래스 정의
 * 
 * @author Mike
 *
 */
public class PaintBoard extends View {

	/**
	 * Canvas instance
	 */
	Canvas mCanvas;
	
	/**
	 * Bitmap for double buffering
	 */
	Bitmap mBitmap;
	
	/**
	 * Paint instance
	 */
	final Paint mPaint;
	
	/**
	 * X coordinate
	 */
	int lastX;
	
	/**
	 * Y coordinate
	 */
	int lastY;

	/**
	 * Initialize paint object and coordinates
	 * 
	 * @param c
	 */
	public PaintBoard(Context context) {
		super(context);
		
		// create a new paint object
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.BLACK);
		
		this.lastX = -1;
		this.lastY = -1;
		
		Log.i("PaintBoard", "initialized.");
	}

	/**
	 * onSizeChanged
	 */
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();
		canvas.setBitmap(img);
		canvas.drawColor(Color.WHITE);
		
		mBitmap = img;
		mCanvas = canvas;
		
	}

	/**
	 * Draw the bitmap
	 */
	protected void onDraw(Canvas canvas) {
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
	}

	/**
	 * Handles touch event, UP, DOWN and MOVE
	 */
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();

		int X = (int) event.getX();
		int Y = (int) event.getY();

		switch (action) {
			case MotionEvent.ACTION_UP:
				// reset coordinates
				lastX = -1;
				lastY = -1;
				
				break;
	
			case MotionEvent.ACTION_DOWN:
				// draw line with the coordinates
				if (lastX != -1) {
					if (X != lastX || Y != lastY) {
						mCanvas.drawLine(lastX, lastY, X, Y, mPaint);
					}
				}
				
				// set the last coordinates
				lastX = X;
				lastY = Y;
				
				break;
	
			case MotionEvent.ACTION_MOVE:
				// draw line with the coordinates
				if (lastX != -1) {
					mCanvas.drawLine(lastX, lastY, X, Y, mPaint);
				}
	
				lastX = X;
				lastY = Y;
				
				break;
		}

		// repaint the screen
		invalidate();

		return true;
	}

	/**
	 * Save this contents into a Jpeg image
	 * 
	 * @param outstream
	 * @return
	 */
	public boolean Save(OutputStream outstream) {
		try {
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
			invalidate();
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
}
