package org.techtown.tutorial.graphic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 색상이나 선굵기를 선택할 수 있도록 기능 추가
 * 
 * @author Mike
 *
 */
public class GoodPaintBoardActivity extends AppCompatActivity {
	
	GoodPaintBoard board;
	Button colorBtn;
	Button penBtn;
	Button eraserBtn;
	Button undoBtn;
	int mColor = 0xff000000;
	int mSize = 2;
	int oldColor;
	int oldSize;
	boolean eraserSelected = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        LinearLayout boardLayout = (LinearLayout) findViewById(R.id.boardLayout);
        colorBtn = (Button) findViewById(R.id.colorBtn);
        penBtn = (Button) findViewById(R.id.penBtn);
        eraserBtn = (Button) findViewById(R.id.eraserBtn);
        undoBtn = (Button) findViewById(R.id.undoBtn);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.FILL_PARENT,
        		LinearLayout.LayoutParams.FILL_PARENT);
        
        board = new GoodPaintBoard(this);
        board.setLayoutParams(params);
        board.setPadding(2, 2, 2, 2);
        
        boardLayout.addView(board);
        
        colorBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        		ColorPaletteDialog.listener = new OnColorSelectedListener() {
        			public void onColorSelected(int color) {
        				mColor = color;
        				board.updatePaintProperty(mColor, mSize);
        			}
        		};
        		
        		
        		// show color palette dialog
        		Intent intent = new Intent(getApplicationContext(), ColorPaletteDialog.class);
        		startActivity(intent);
        		
        	}
        });
        
        penBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        		PenPaletteDialog.listener = new OnPenSelectedListener() {
        			public void onPenSelected(int size) {
        				mSize = size;
        				board.updatePaintProperty(mColor, mSize);
        			}
        		};
        		
        		
        		// show pen palette dialog
        		Intent intent = new Intent(getApplicationContext(), PenPaletteDialog.class);
        		startActivity(intent);
        		
        	}
        });
        
        eraserBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        		eraserSelected = !eraserSelected;
        		
        		if (eraserSelected) {
                    colorBtn.setEnabled(false);
                    penBtn.setEnabled(false);
                    undoBtn.setEnabled(false);
        			
                    colorBtn.invalidate();
                    penBtn.invalidate();
                    undoBtn.invalidate();
                    
                    oldColor = mColor;
                    oldSize = mSize;
                    
                    mColor = Color.WHITE;
                    mSize = 15;
                    
                    board.updatePaintProperty(mColor, mSize);
                    
                } else {
                	colorBtn.setEnabled(true);
                    penBtn.setEnabled(true);
                    undoBtn.setEnabled(true);
        			
                    colorBtn.invalidate();
                    penBtn.invalidate();
                    undoBtn.invalidate();
                    
                    mColor = oldColor;
                    mSize = oldSize;
                    
                    board.updatePaintProperty(mColor, mSize);
                    
                }
        		
        	}
        });
        
        undoBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        		board.undo();
        		
        	}
        });
        
    }
    
        
    public int getChosenColor() {
    	return mColor;
    }
    
    public int getPenThickness() {
    	return mSize;
    }

}