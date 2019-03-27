package org.androidtown.multimemo;

import org.androidtown.multimemo.R;
import org.androidtown.multimemo.common.TitleBitmapButton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

/**
 * 굵기 선택 대화상자
 *
 * @author Mike
 * @date 2011-07-01
 */
public class PenPaletteDialog extends Activity {

	GridView mGridView;
	TitleBitmapButton  mCloseBtn;
	PenDataAdapter mAdapter;

	public static OnPenSelectedListener mSelectedListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handwriting_dialog);

        this.setTitle(R.string.pen_selection_title);

        mGridView = (GridView) findViewById(R.id.colorGrid);
        mCloseBtn = (TitleBitmapButton ) findViewById(R.id.closeBtn);

        mGridView.setColumnWidth(12);
        mGridView.setBackgroundColor(Color.GRAY);
        mGridView.setVerticalSpacing(4);
        mGridView.setHorizontalSpacing(4);

        mAdapter = new PenDataAdapter(this);
        mGridView.setAdapter(mAdapter);
        mGridView.setNumColumns(mAdapter.getNumColumns());

        mCloseBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		finish();
        	}
        });

	}

}

/**
 * Adapter for Pen Data
 */
class PenDataAdapter extends BaseAdapter {

	/**
	 * Application Context
	 */
	Context mContext;

	/**
	 * Pens defined
	 */
    public static final int [] pens = new int[] {
        1,2,3,4,5,
        6,7,8,9,10,
        11,13,15,17,20
    };

	int rowCount;
	int columnCount;



	public PenDataAdapter(Context context) {
		super();

		mContext = context;

		rowCount = 3;
		columnCount = 5;

	}

	public int getNumColumns() {
		return columnCount;
	}

	public int getCount() {
		return rowCount * columnCount;
	}

	public Object getItem(int position) {
		return pens[position];
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View view, ViewGroup group) {
		Log.d("PenDataAdapter", "getView(" + position + ") called.");

		// calculate position
		int rowIndex = position / rowCount;
		int columnIndex = position % rowCount;
		Log.d("PenDataAdapter", "Index : " + rowIndex + ", " + columnIndex);

		GridView.LayoutParams params = new GridView.LayoutParams(
				GridView.LayoutParams.FILL_PARENT,
				GridView.LayoutParams.FILL_PARENT);

		// create a Pen Image
		int areaWidth = 10;
		int areaHeight = 20;

		Bitmap penBitmap = Bitmap.createBitmap(areaWidth, areaHeight, Bitmap.Config.ARGB_8888);
		Canvas penCanvas = new Canvas();
		penCanvas.setBitmap(penBitmap);

		Paint mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		penCanvas.drawRect(0, 0, areaWidth, areaHeight, mPaint);

		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeWidth((float)pens[position]);
		penCanvas.drawLine(0, areaHeight/2, areaWidth-1, areaHeight/2, mPaint);
		BitmapDrawable penDrawable = new BitmapDrawable(mContext.getResources(), penBitmap);

		// create a Button with the color
		TitleBitmapButton  aItem = new TitleBitmapButton (mContext);
		aItem.setText(" ");
		aItem.setLayoutParams(params);
		aItem.setPadding(4, 4, 4, 4);
		aItem.setBackgroundDrawable(penDrawable);
		aItem.setHeight(48);
		aItem.setTag(pens[position]);

		// set listener
		aItem.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (PenPaletteDialog.mSelectedListener != null) {
					PenPaletteDialog.mSelectedListener.onPenSelected(((Integer)v.getTag()).intValue());
				}

				((PenPaletteDialog)mContext).finish();
			}
		});

		return aItem;
	}
}


