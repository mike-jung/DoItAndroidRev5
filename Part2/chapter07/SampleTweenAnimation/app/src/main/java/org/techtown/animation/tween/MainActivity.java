package org.techtown.animation.tween;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

/**
 * 트윈 애니메이션을 적용하는 간단한 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 첫번째 버튼 이벤트 처리
        Button scaleButton = (Button) findViewById(R.id.scaleButton);
        scaleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
                v.startAnimation(anim);
            }
        });

        // 두번째 버튼 이벤트 처리
        Button scale2Button = (Button) findViewById(R.id.scale2Button);
        scale2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale2);
                v.startAnimation(anim);
            }
        });

    }

}
