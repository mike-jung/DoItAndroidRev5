package org.techtown.bitmapwidget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 비트맵 버튼을 정의합니다.
 */
public class BitmapButton extends AppCompatButton {
	/**
	 * 아이콘 리소스 : NORMAL
	 */
	int iconNormal = R.drawable.bitmap_button_normal;

	/**
	 * 아이콘 리소스 : CLICKED
	 */
	int iconClicked = R.drawable.bitmap_button_clicked;

	/**
	 * 아이콘 상태 : STATUS_NORMAL, STATUS_CLICKED
	 */
	int iconStatus = STATUS_NORMAL;

	public static int STATUS_NORMAL = 0;

	public static int STATUS_CLICKED = 1;

	/**
	 * 생성자
	 */
	public BitmapButton(Context context) {
		super(context);

		init();
	}

	/**
	 * 생성자
	 */
	public BitmapButton(Context context, AttributeSet atts) {
		super(context, atts);

		init();
	}

	/**
	 * 초기화
	 */
	public void init() {
		setBackgroundResource(iconNormal);

		int defaultTextColor = Color.WHITE;
		float defaultTextSize = getResources().getDimension(R.dimen.text_size);
		Typeface defaultTypeface = Typeface.DEFAULT_BOLD;

		setTextColor(defaultTextColor);
		setTextSize(defaultTextSize);
		setTypeface(defaultTypeface);
	}

	/**
	 * 아이콘 리소스 설정
	 */
	public void setIcon(int iconNormal, int iconClicked) {
		this.iconNormal = iconNormal;
		this.iconClicked = iconClicked;
	}

	/**
	 * Handles touch event, move to main screen
	 */
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		int action = event.getAction();

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				setBackgroundResource(R.drawable.bitmap_button_clicked);

				iconStatus = STATUS_CLICKED;

				break;

			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				setBackgroundResource(R.drawable.bitmap_button_normal);

				iconStatus = STATUS_NORMAL;

				break;
		}

		// 다시 그리기
		invalidate();

		return true;
	}

}
