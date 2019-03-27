package org.techtown.animation.graph;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 막대그래프 모양처럼 만들어 애니메이션을 적용하는 가장 간단한 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    LinearLayout mainLayout;
    Resources res;
    Animation growAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();
        growAnim = AnimationUtils.loadAnimation(this, R.anim.grow);
        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);

        // 아이템 추가
        addItem("Apple", 80);
        addItem("Orange", 100);
        addItem("Kiwi", 40);

    }

    /**
     * 아이템 추가 메소드
     *
     * @param name
     * @param value
     */
    private void addItem(String name, int value) {

        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 텍스트뷰 추가
        TextView textView = new TextView(this);
        textView.setText(name);
        params.width = 240;
        params.setMargins(0, 4, 0, 4);
        itemLayout.addView(textView, params);

        // 프로그레스바 추가
        ProgressBar proBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        proBar.setIndeterminate(false);
        proBar.setMax(100);
        proBar.setProgress(100);
        proBar.setAnimation(growAnim);
        params2.height = 80;
        params2.width = value * 5;
        params2.gravity = Gravity.LEFT;
        itemLayout.addView(proBar, params2);

        mainLayout.addView(itemLayout, params3);

    }

    /**
     * 화면에 보여지기 전에 호출되는 메소드
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Toast.makeText(this, "onWindowFocusChanged : " + hasFocus, Toast.LENGTH_SHORT).show();

        if (hasFocus) {
            growAnim.start();
        } else {
            growAnim.reset();
        }
    }

}
