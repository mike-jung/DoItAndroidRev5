package org.techtown.graphics.custom.image;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 직접 만든 뷰 위에 이미지를 그리는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 직접 만든 뷰를 화면에 설정
        CustomViewImage myView = new CustomViewImage(this);
        setContentView(myView);
    }

}
