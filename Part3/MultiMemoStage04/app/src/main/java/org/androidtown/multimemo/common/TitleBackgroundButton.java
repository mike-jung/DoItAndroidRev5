package org.androidtown.multimemo.common;

import org.androidtown.multimemo.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

public class TitleBackgroundButton extends Button {

	/**
	 * Base Context
	 */
	Context context;

	/**
	 * Paint instance
	 */
	Paint paint;

	/**
	 * Default Color
	 */
	int defaultColor = 0xff333333;

	/**
	 * Default Size
	 */
	float defaultSize = 20F;

	/**
	 * Default ScaleX
	 */
	float defaultScaleX = 1.0F;

	/**
	 * Default Typeface
	 */
	Typeface defaultTypeface = Typeface.DEFAULT_BOLD;

	/**
	 * Title Text
	 */
	String titleText = "";

	/**
	 * Flag for paint changed
	 */
	boolean paintChanged = false;

	public TitleBackgroundButton(Context context) {
		super(context);

		this.context = context;
		init();
	}

	public TitleBackgroundButton(Context context, AttributeSet atts) {
		super(context, atts);

		this.context = context;
		init();
	}

	/**
	 * Initialize
	 */
	public void init() {
		setBackgroundResource(R.drawable.title_background);

		paint = new Paint();
		paint.setColor(defaultColor);
		paint.setAntiAlias(true);
		paint.setTextScaleX(defaultScaleX);
		paint.setTextSize(defaultSize);
		paint.setTypeface(defaultTypeface);

	}


	/**
	 * Handles touch event, move to main screen
	 */
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		int action = event.getAction();

		switch (action) {
			case MotionEvent.ACTION_UP:

				break;

			case MotionEvent.ACTION_DOWN:
				Toast.makeText(context, titleText, 500).show();
				break;

		}

		// repaint the screen
		invalidate();

		return true;
	}

	/**
	 * Draw the text
	 */
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int curWidth = getWidth();
		int curHeight = getHeight();

		// apply paint attributes
		if (paintChanged) {
			paint.setColor(defaultColor);
			paint.setTextScaleX(defaultScaleX);
			paint.setTextSize(defaultSize);
			paint.setTypeface(defaultTypeface);
		}

		// calculate bounds
		Rect bounds = new Rect();
		paint.getTextBounds(titleText, 0, titleText.length(), bounds);
		float textWidth = ((float)curWidth - bounds.width())/2.0F;
		float textHeight = ((float)(curHeight) - bounds.height())/2.0F + bounds.height();

		// draw title text
		canvas.drawText(titleText, textWidth, textHeight, paint);

	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	public int getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(int defaultColor) {
		this.defaultColor = defaultColor;
		paintChanged = true;
	}

	public float getDefaultSize() {
		return defaultSize;
	}

	public void setDefaultSize(float defaultSize) {
		this.defaultSize = defaultSize;
		paintChanged = true;
	}

	public float getDefaultScaleX() {
		return defaultScaleX;
	}

	public void setDefaultScaleX(float defaultScaleX) {
		this.defaultScaleX = defaultScaleX;
		paintChanged = true;
	}

	public Typeface getDefaultTypeface() {
		return defaultTypeface;
	}

	public void setDefaultTypeface(Typeface defaultTypeface) {
		this.defaultTypeface = defaultTypeface;
		paintChanged = true;
	}


}
