package org.techtown.mission10;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductItemView extends LinearLayout {
    private static final String TAG = "ProductItemView";

	TextView textView1;
	TextView textView2;
	TextView textView3;
	TextView textView4;
	
	ImageView imageView1;

	public ProductItemView(Context context) {
		super(context);

		init(context);
	}
	
	public ProductItemView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.product_item, this, true);
	
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView4 = (TextView) findViewById(R.id.textView4);
		
		imageView1 = (ImageView) findViewById(R.id.imageView1);

	}
	
	public void setName(String name) {
		textView2.setText(name);
	}
	
	public void setCountAgent(String countAgent) {
		textView1.setText(countAgent);
	}
	
	public void setPrice(String price) {
		textView4.setText(price);
	}
	
	public void setImage(int imageId) {
		imageView1.setImageResource(imageId);
	}

}








