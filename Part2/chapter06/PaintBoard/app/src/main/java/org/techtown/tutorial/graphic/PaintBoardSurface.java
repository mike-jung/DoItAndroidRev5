package org.techtown.tutorial.graphic;

import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 서피스뷰를 이용하는 페인트보드 클래스 정의
 * 
 * @author Mike
 *
 */
public class PaintBoardSurface extends SurfaceView implements SurfaceHolder.Callback {

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

	SurfaceHolder mHolder;
	
	/**
	 * Initialize paint object and coordinates
	 * 
	 * @param c
	 */
	public PaintBoardSurface(Context context) {
		super(context);
		
		mHolder = getHolder();   
		mHolder.addCallback(this); 
		
		
		// create a new paint object
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.RED);
		
		this.lastX = -1;
		this.lastY = -1;
		
		Log.i("PaintBoard", "initialized.");
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

	 
		Log.d("PaintBoard-Surface", "repaintCanvas()");
		
		// repaint the screen
		draw();
		
		return true;
	}

	
	public void surfaceCreated(SurfaceHolder holder) { 
		int w = getWidth();
		int h = getHeight();
		
		Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();
		canvas.setBitmap(img);
		canvas.drawColor(Color.WHITE);
		
		mBitmap = img;
		mCanvas = canvas;
		
		draw();
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) { 
	
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) { 
	
	}	
	
	
	/**
	 * draw this SurfaceView
	 */
	private void draw() {
		Canvas _canvas = null;
		try {
			_canvas = mHolder.lockCanvas(null);
			
			super.draw(_canvas);
			
			_canvas.drawBitmap(mBitmap, 0, 0, null);
		
		} finally {
			if (_canvas != null) {
				mHolder.unlockCanvasAndPost(_canvas);
			}
		}
		
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
