package org.androidtown.multimemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidtown.multimemo.common.TitleBitmapButton;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 손글씨 입력 액티비티
 *
 * @author Mike
 * @date 2011-07-01
 */
public class HandwritingMakingActivity extends Activity {
	private static final String TAG = "HandwritingActivity";

	HandwritingView mWritingBoard;
	TitleBitmapButton mColorBtn;
	TitleBitmapButton mPenBtn;
	TitleBitmapButton mEraserBtn;
	TitleBitmapButton mUndoBtn;

	LinearLayout mAddedLayout;
	TitleBitmapButton mColorLegendBtn;
	TextView mSizeLegendTxt;

	int mColor = 0xff000000;
	int mSize = 8;
	int mOldColor;
	int mOldSize;
	boolean mEraserSelected = false;

	TitleBitmapButton mHandwritingMakingSaveBtn;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.handwriting_making_activity);

		setTopLayout();

		setBottomLayout();

		setWritingBorad();

    }

	public void setWritingBorad() {
		LinearLayout boardLayout = (LinearLayout) findViewById(R.id.boardLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.FILL_PARENT,
        		LinearLayout.LayoutParams.FILL_PARENT);

        mWritingBoard = new HandwritingView(this);
        mWritingBoard.setLayoutParams(params);
        mWritingBoard.setPadding(2, 2, 2, 2);

        boardLayout.addView(mWritingBoard);
	}

	public void setTopLayout() {
		LinearLayout toolsLayout = (LinearLayout) findViewById(R.id.toolsLayout);

		mColorBtn = (TitleBitmapButton) findViewById(R.id.colorBtn);
		mPenBtn = (TitleBitmapButton) findViewById(R.id.penBtn);
		mEraserBtn = (TitleBitmapButton) findViewById(R.id.eraserBtn);
		mUndoBtn = (TitleBitmapButton) findViewById(R.id.undoBtn);

        mColorBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {

        		ColorPaletteDialog.mSelectedListener = new OnColorSelectedListener() {
        			public void onColorSelected(int color) {
        				mColor = color;
        				mWritingBoard.updatePaintProperty(mColor, mSize);
        				displayPaintProperty();
        			}
        		};


        		// show color palette dialog
        		Intent intent = new Intent(getApplicationContext(), ColorPaletteDialog.class);
        		startActivity(intent);

        	}
        });

        mPenBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {

        		PenPaletteDialog.mSelectedListener = new OnPenSelectedListener() {
        			public void onPenSelected(int size) {
        				mSize = size;
        				mWritingBoard.updatePaintProperty(mColor, mSize);
        				displayPaintProperty();
        			}
        		};


        		// show pen palette dialog
        		Intent intent = new Intent(getApplicationContext(), PenPaletteDialog.class);
        		startActivity(intent);

        	}
        });

        mEraserBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {

        		mEraserSelected = !mEraserSelected;

        		if (mEraserSelected) {
        			mColorBtn.setEnabled(false);
        			mPenBtn.setEnabled(false);
        			mUndoBtn.setEnabled(false);

                    mColorBtn.invalidate();
                    mPenBtn.invalidate();
                    mUndoBtn.invalidate();

                    mOldColor = mColor;
                    mOldSize = mSize;

                    mColor = Color.WHITE;
                    mSize = 15;

                    mWritingBoard.updatePaintProperty(mColor, mSize);
                    displayPaintProperty();

                } else {
                	mColorBtn.setEnabled(true);
                	mPenBtn.setEnabled(true);
                	mUndoBtn.setEnabled(true);

                    mColorBtn.invalidate();
                    mPenBtn.invalidate();
                    mUndoBtn.invalidate();

                    mColor = mOldColor;
                    mSize = mOldSize;

                    mWritingBoard.updatePaintProperty(mColor, mSize);
                    displayPaintProperty();

                }

        	}
        });

        mUndoBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		Log.d(TAG, "undo button clicked.");

        		mWritingBoard.undo();
        	}
        });

        // add legend buttons
        LinearLayout.LayoutParams addedParams = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.FILL_PARENT,
        		LinearLayout.LayoutParams.FILL_PARENT);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.FILL_PARENT,
        		LinearLayout.LayoutParams.WRAP_CONTENT);
        mAddedLayout = new LinearLayout(this);
        mAddedLayout.setLayoutParams(addedParams);
        mAddedLayout.setOrientation(LinearLayout.VERTICAL);
        mAddedLayout.setPadding(8,8,8,8);

        LinearLayout outlineLayout = new LinearLayout(this);
        outlineLayout.setLayoutParams(buttonParams);
        outlineLayout.setOrientation(LinearLayout.VERTICAL);
        outlineLayout.setBackgroundColor(Color.LTGRAY);
        outlineLayout.setPadding(1,1,1,1);

        mColorLegendBtn = new TitleBitmapButton(this);
        mColorLegendBtn.setClickable(false);
        mColorLegendBtn.setLayoutParams(buttonParams);
        mColorLegendBtn.setText(" ");
        mColorLegendBtn.setBackgroundColor(mColor);
        mColorLegendBtn.setHeight(20);
        outlineLayout.addView(mColorLegendBtn);
        mAddedLayout.addView(outlineLayout);

        mSizeLegendTxt = new TextView(this);
        mSizeLegendTxt.setLayoutParams(buttonParams);
        mSizeLegendTxt.setText("Size : " + mSize);
        mSizeLegendTxt.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        mSizeLegendTxt.setTextSize(16);
        mSizeLegendTxt.setTextColor(Color.BLACK);
        mAddedLayout.addView(mSizeLegendTxt);

        toolsLayout.addView(mAddedLayout);
	}

	public void setBottomLayout()
	{
		mHandwritingMakingSaveBtn = (TitleBitmapButton)findViewById(R.id.handwriting_making_saveBtn);

		mHandwritingMakingSaveBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveHandwritingMaking();
			}
		});
	}

    public int getChosenColor() {
    	return mColor;
    }

    public int getPenThickness() {
    	return mSize;
    }

    private void displayPaintProperty() {
    	mColorLegendBtn.setBackgroundColor(mColor);
    	mSizeLegendTxt.setText("Size : " + mSize);

    	mAddedLayout.invalidate();
    }

    public void saveHandwritingMaking() {

    	try {
    		checkHandwritingFolder();

        	String handwritingName = "made";

        	File file = new File(BasicInfo.FOLDER_HANDWRITING + handwritingName);
        	if(file.exists()) {
        		file.delete();
        	}

			FileOutputStream outstream = new FileOutputStream(BasicInfo.FOLDER_HANDWRITING + handwritingName);

			Bitmap mBitmap = mWritingBoard.getImage();
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream);
			outstream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		setResult(RESULT_OK);
		finish();
	}

    public void checkHandwritingFolder() {
    	File handwritingFolder = new File(BasicInfo.FOLDER_HANDWRITING);
		if(!handwritingFolder.isDirectory()){
			Log.d(TAG, "creating handwriting folder : " + handwritingFolder);

			handwritingFolder.mkdirs();
		}
    }
}
