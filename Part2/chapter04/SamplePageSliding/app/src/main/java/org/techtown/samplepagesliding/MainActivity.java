package org.techtown.samplepagesliding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    /**
     * 페이지가 열려 있는지 알기 위한 플래그
     */
    boolean isPageOpen = false;

    /**
     * 애니메이션 객체
     */
    Animation translateLeftAnim;
    Animation translateRightAnim;

    /**
     * 슬라이딩으로 보여지는 페이지 레이아웃
     */
    LinearLayout page;

    /**
     * 버튼
     */
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

        // 슬라이딩으로 보여질 레이아웃 객체 참조
        page = (LinearLayout) findViewById(R.id.page);

        // 애니메이션 객체 로딩
        translateLeftAnim = AnimationUtils.loadAnimation(this, R.anim.translate_left);
        translateRightAnim = AnimationUtils.loadAnimation(this, R.anim.translate_right);

        // 애니메이션 객체에 리스너 설정
        SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
        translateLeftAnim.setAnimationListener(animListener);
        translateRightAnim.setAnimationListener(animListener);

    }

    public void onButton1Clicked(View v) {
        // 애니메이션 적용
        if (isPageOpen) {
            page.startAnimation(translateRightAnim);
        } else {
            page.setVisibility(View.VISIBLE);
            page.startAnimation(translateLeftAnim);
        }
    }

    /**
     * 애니메이션 리스너 정의
     */
    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        /**
         * 애니메이션이 끝날 때 호출되는 메소드
         */
        public void onAnimationEnd(Animation animation) {
            if (isPageOpen) {
                page.setVisibility(View.INVISIBLE);

                button.setText("Open");
                isPageOpen = false;
            } else {
                button.setText("Close");
                isPageOpen = true;
            }
        }

        public void onAnimationRepeat(Animation animation) {

        }

        public void onAnimationStart(Animation animation) {

        }

    }

}
