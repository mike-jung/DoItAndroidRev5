package org.techtown.graphics.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 뷰를 상속하여 직접 만든 뷰
 * 
 * @author Mike
 *
 */
public class CustomView extends View {
	
	/**
	 * 그리기할 때 사용하는 속성을 담고 있는 페인트 객체
	 */
	private Paint paint;

	/**
	 * 생성자
	 * 
	 * @param context
	 */
	public CustomView(Context context) {
		super(context);

		paint = new Paint();
		paint.setColor(Color.RED);
	}

	/**
	 * 화면에 그리기
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawRect(100, 100, 200, 200, paint);
	}
	
	/**
	 * 터치 이벤트 처리
	 * 터치할 때마다 터치한 위치를 토스트 메시지로 표시
	 */
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Toast.makeText(super.getContext(), "MotionEvent.ACTION_DOWN : " + event.getX() + ", " + event.getY(), Toast.LENGTH_LONG).show();
		}
		
		return super.onTouchEvent(event);
	}

}
