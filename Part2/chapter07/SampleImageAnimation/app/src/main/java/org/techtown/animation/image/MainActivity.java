package org.techtown.animation.image;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

/**
 * ImageSwitcher 를 이용한 이미지 애니메이션 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    ImageSwitcher switcher;
    Handler mHandler = new Handler();
    ImageThread thread;
    boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 시작 버튼 이벤트 처리
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startAnimation();
            }
        });

        // 중지 버튼 이벤트 처리
        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopAnimation();
            }
        });


        switcher = (ImageSwitcher) findViewById(R.id.switcher);
        switcher.setVisibility(View.INVISIBLE);

        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setBackgroundColor(0xFF000000);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                return imageView;
            }
        });

    }

    /**
     * 애니메이션 시작
     */
    private void startAnimation() {
        switcher.setVisibility(View.VISIBLE);

        thread = new ImageThread();
        thread.start();
    }

    /**
     * 애니메이션 중지
     */
    private void stopAnimation() {
        running = false;
        try {
            thread.join();
        } catch(InterruptedException ex) { }

        switcher.setVisibility(View.INVISIBLE);
    }

    /**
     * 이미지 처리 스레드
     */
    class ImageThread extends Thread {
        int duration = 250;
        final int imageId[] = { R.drawable.emo_im_crying,
                R.drawable.emo_im_happy,
                R.drawable.emo_im_laughing,
                R.drawable.emo_im_surprised };
        int currentIndex = 0;

        public void run() {
            running = true;
            while (running) {
                synchronized (this) {
                    mHandler.post(new Runnable() {
                        public void run() {
                            switcher.setImageResource(imageId[currentIndex]);
                        }
                    });

                    currentIndex++;
                    if (currentIndex == imageId.length) {
                        currentIndex = 0;
                    }

                    try {
                        Thread.sleep(duration);
                    } catch (InterruptedException ex) { }
                }
            }
        }
    }

}
