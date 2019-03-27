package org.androidtown.multimemo.common;



import org.androidtown.multimemo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;


public class TitleBitmapButton extends Button {

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
	int defaultColor = 0xffffffff;

	/**
	 * Default Size
	 */
	float defaultSize = 18F;

	/**
	 * Default ScaleX
	 */
	float defaultScaleX = 1.0F;

	/**
	 * Default Typeface
	 */
	Typeface defaultTypeface = Typeface.DEFAULT;

	/**
	 * Title Text
	 */
	String titleText = "";

	/**
	 * Icon Status : 0 - Normal, 1 - Clicked
	 */
	int iconStatus = 0;

	/**
	 * Icon Clicked Bitmap
	 */
	Bitmap iconNormalBitmap;

	/**
	 * Icon Clicked Bitmap
	 */
	Bitmap iconClickedBitmap;

	public static final int BITMAP_ALIGN_CENTER = 0;

	public static final int BITMAP_ALIGN_LEFT = 1;

	public static final int BITMAP_ALIGN_RIGHT = 2;


	int backgroundBitmapNormal = R.drawable.title_button;
	int backgroundBitmapClicked = R.drawable.title_button_clicked;


	/**
	 * Alignment
	 */
	int bitmapAlign = BITMAP_ALIGN_CENTER;

	/**
	 * Padding for Left or Right
	 */
	int bitmapPadding = 10;



	/**
	 * Flag for paint changed
	 */
	boolean paintChanged = false;


	private boolean selected;


	private int tabId;


	public TitleBitmapButton(Context context) {
		super(context);

		this.context = context;
		init();
	}

	public TitleBitmapButton(Context context, AttributeSet atts) {
		super(context, atts);

		this.context = context;
		init();
	}


	public void setTabId(int id) {
		tabId = id;
	}


	public void setSelected(boolean flag) {
		selected = flag;
		if (selected) {
			setBackgroundResource(backgroundBitmapClicked);
			paintChanged = true;
			defaultColor = Color.BLACK;
		} else {
			setBackgroundResource(backgroundBitmapNormal);
			paintChanged = true;
			defaultColor = Color.WHITE;
		}
	}

	public boolean isSelected() {
		return selected;
	}


	/**
	 * Initialize
	 */
	public void init() {
		setBackgroundResource(backgroundBitmapNormal);

		paint = new Paint();
		paint.setColor(defaultColor);
		paint.setAntiAlias(true);
		paint.setTextScaleX(defaultScaleX);
		paint.setTextSize(defaultSize);
		paint.setTypeface(defaultTypeface);

		selected = false;

	}

	/**
	 * Set icon bitmap
	 *
	 * @param icon
	 */
	public void setIconBitmap(Bitmap iconNormal, Bitmap iconClicked) {
		iconNormalBitmap = iconNormal;
		iconClickedBitmap = iconClicked;

	}

	public void setBackgroundBitmap(int resNormal, int resClicked) {
		backgroundBitmapNormal = resNormal;
		backgroundBitmapClicked = resClicked;

		setBackgroundResource(backgroundBitmapNormal);
	}



	/**
	 * Handles touch event, move to main screen
	 */
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		int action = event.getAction();

		switch (action) {
			case MotionEvent.ACTION_UP:

				if (selected) {

				} else {

					setBackgroundResource(backgroundBitmapNormal);

					iconStatus = 0;

					paintChanged = true;
					defaultColor = Color.WHITE;

					// fire event
					//khc 100913 - 아래코드 제거시 탭 이동현상 없어짐.
					//MainScreenActivity.getInstance().tabSelected(tabId);
				}

				break;

			case MotionEvent.ACTION_DOWN:

				if (selected) {

				} else {
					setBackgroundResource(backgroundBitmapClicked);

					iconStatus = 1;

					paintChanged = true;
					defaultColor = Color.BLACK;
				}

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

			paintChanged = false;
		}

		// bitmap
		Bitmap iconBitmap = iconNormalBitmap;
		if (iconStatus == 1) {
			iconBitmap = iconClickedBitmap;
		}

		if (iconBitmap != null) {
			int iconWidth = iconBitmap.getWidth();
			int iconHeight = iconBitmap.getHeight();
			int bitmapX = 0;
			if (bitmapAlign == BITMAP_ALIGN_CENTER) {
				bitmapX = (curWidth-iconWidth)/2;
			} else if(bitmapAlign == BITMAP_ALIGN_LEFT) {
				bitmapX = bitmapPadding;
			} else if(bitmapAlign == BITMAP_ALIGN_RIGHT) {
				bitmapX = curWidth-bitmapPadding;
			}

			canvas.drawBitmap(iconBitmap, bitmapX, (curHeight-iconHeight)/2, paint);
		}

		// text
		Rect bounds = new Rect();
		paint.getTextBounds(titleText, 0, titleText.length(), bounds);
		float textWidth = ((float)curWidth - bounds.width())/2.0F;
		float textHeight = ((float)curHeight + bounds.height())/2.0F+4.0F;

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

	public int getBitmapAlign() {
		return bitmapAlign;
	}

	public void setBitmapAlign(int bitmapAlign) {
		this.bitmapAlign = bitmapAlign;
	}

	public int getBitmapPadding() {
		return bitmapPadding;
	}

	public void setBitmapPadding(int bitmapPadding) {
		this.bitmapPadding = bitmapPadding;
	}



}
