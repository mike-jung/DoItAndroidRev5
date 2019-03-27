package org.techtown.multitouch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

/**
 * 두 손가락을 이용해 멀티터치할 때 발생하는 이벤트를 처리하는 방법을 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 이미지를 보여줄 뷰를 담고있을 레이아웃 객체
     */
    LinearLayout viewerContainer;

    /**
     * 이미지를 보여줄 뷰
     */
    ImageDisplayView displayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    /**
     * 초기화
     */
    private void init() {
        viewerContainer = (LinearLayout) findViewById(R.id.viewerContainer);
        Bitmap sourceBitmap = loadImage();
        if (sourceBitmap != null) {
            displayView = new ImageDisplayView(this);

            displayView.setImageData(sourceBitmap);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            viewerContainer.addView(displayView, params);
        }
    }

    /**
     * 리소스의 이미지를 비트맵 객체로 로딩
     *
     * @return
     */
    private Bitmap loadImage() {
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.beach);

        return bitmap;
    }

}
