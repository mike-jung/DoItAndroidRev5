package org.techtown.graphics.custom.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

/**
 * 뷰를 상속하여 새로 만든 뷰 클래스
 * 
 * @author Mike
 *
 */
public class CustomViewImage extends View {

	// cache bitmap
	private Bitmap cacheBitmap;

	// cache canvas
	private Canvas cacheCanvas;

	private Paint mPaint;

	public CustomViewImage(Context context) {
		super(context);

		// create a new paint object
		mPaint = new Paint();

	}

	/**
	 * To be called when the size is changed.
	 */
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		createCacheBitmap(w, h);
		testDrawing();
	}

	/**
	 * Create the cache bitmap
	 *
	 * @param w width
	 * @param h height
	 */
	private void createCacheBitmap(int w, int h) {
		cacheBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		cacheCanvas = new Canvas();
		cacheCanvas.setBitmap(cacheBitmap);

	}

	private void testDrawing() {

		cacheCanvas.drawColor(Color.WHITE);

		mPaint.setColor(Color.RED);
		cacheCanvas.drawRect(100, 100, 200, 200, mPaint);

		// create and draw images
		Bitmap srcImg = BitmapFactory.decodeResource(getResources(), R.drawable.waterdrop);
		cacheCanvas.drawBitmap(srcImg, 30, 30, mPaint);

		Matrix horInverseMatrix = new Matrix();
		horInverseMatrix.setScale(-1, 1);
		Bitmap horInverseImg = Bitmap.createBitmap(srcImg, 0, 0,
				srcImg.getWidth(), srcImg.getHeight(), horInverseMatrix, false);
		cacheCanvas.drawBitmap(horInverseImg, 30, 130, mPaint);

		Matrix verInverseMatrix = new Matrix();
		verInverseMatrix.setScale(1, -1);
		Bitmap verInverseImg = Bitmap.createBitmap(srcImg, 0, 0,
				srcImg.getWidth(), srcImg.getHeight(), verInverseMatrix, false);
		cacheCanvas.drawBitmap(verInverseImg, 30, 230, mPaint);

		// mask
		Bitmap srcImg2 = BitmapFactory.decodeResource(getResources(), R.drawable.face);
		mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
		Bitmap scaledImg = Bitmap.createScaledBitmap(srcImg2,
				srcImg2.getWidth()*2, srcImg2.getHeight()*2, false);
		cacheCanvas.drawBitmap(scaledImg, 100, 230, mPaint);
		
	}

	/**
	 * Draw the bitmap
	 */
	protected void onDraw(Canvas canvas) {
		if (cacheBitmap != null) {
			canvas.drawBitmap(cacheBitmap, 0, 0, null);
		}
	}

}
