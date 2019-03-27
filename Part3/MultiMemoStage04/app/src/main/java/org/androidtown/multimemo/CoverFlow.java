package org.androidtown.multimemo;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * CoverFlow widget
 *
 * @author Mike
 * @date 2011-07-01
 */
public class CoverFlow extends Gallery {

	private Camera camera = new Camera();

	/**
	 * Max Rotation Angle
	 */
	public static int maxRotationAngle = 55;

	/**
	 * Max Zoom Level
	 */
	public static int maxZoom = -60;

	private int centerPoint;

	/**
	 * Constructor
	 *
	 * @param context
	 */
	public CoverFlow(Context context) {
		super(context);

		init();
	}

	/**
	 * Constructor
	 *
	 * @param context
	 * @param attrs
	 */
	public CoverFlow(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	/**
	 * Constructor
	 *
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CoverFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	/**
	 * Initialization
	 */
	private void init() {
		this.setStaticTransformationsEnabled(true);
	}


	public int getMaxRotationAngle() {
		return maxRotationAngle;
	}

	public void setMaxRotationAngle(int rotationAngle) {
		maxRotationAngle = rotationAngle;
	}

	public int getMaxZoom() {
		return maxZoom;
	}

	public void setMaxZoom(int zoom) {
		maxZoom = zoom;
	}

	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
	}

	private static int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	protected boolean getChildStaticTransformation(View child, Transformation t) {

		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth() ;
		int rotationAngle = 0;
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);

		if (childCenter == centerPoint) {
			transformImageBitmap((ImageView) child, t, 0);
		} else {
			rotationAngle = (int) (((float) (centerPoint - childCenter)/ childWidth) *  maxRotationAngle);
			if (Math.abs(rotationAngle) > maxRotationAngle) {
				rotationAngle = (rotationAngle < 0) ? -maxRotationAngle : maxRotationAngle;
			}
			transformImageBitmap((ImageView) child, t, rotationAngle);
		}

		return true;

	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		centerPoint = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private void transformImageBitmap(ImageView child, Transformation t, int rotationAngle) {
		camera.save();

		final Matrix imageMatrix = t.getMatrix();;
		final int imageHeight = child.getLayoutParams().height;;
		final int imageWidth = child.getLayoutParams().width;
		final int rotation = Math.abs(rotationAngle);

		camera.translate(0.0f, 0.0f, 100.0f);

		if ( rotation < maxRotationAngle ) {
			float zoomAmount = (float) (maxZoom +  (rotation * 1.5));
			camera.translate(0.0f, 0.0f, zoomAmount);
		}

		camera.rotateY(rotationAngle);
		camera.getMatrix(imageMatrix);

		imageMatrix.preTranslate(-(imageWidth/2), -(imageHeight/2));
		imageMatrix.postTranslate((imageWidth/2), (imageHeight/2));

		camera.restore();

	}

}
