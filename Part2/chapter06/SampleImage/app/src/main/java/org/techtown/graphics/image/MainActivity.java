package org.techtown.graphics.image;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 이미지뷰를 직접 추가하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView imgView = new ImageView(this);
        imgView.setImageResource(R.drawable.waterdrop);
        imgView.setAdjustViewBounds(true);

        mainLayout.addView(imgView, params);

    }

}
