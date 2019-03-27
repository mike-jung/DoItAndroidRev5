package org.techtown.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 스레드를 이용해 프로그레스바를 보여주는 방법에 대해 알 수 있습니다.
 * 별도로 만든 스레드에서 메인 스레드를 접근할 때 핸들러를 사용해야 한다는 것을 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 프로그레스바
     */
    ProgressBar bar;
    TextView textView;
    boolean isRunning = false;

    /**
     * 메인 스레드의 UI에 접근하기 위한 핸들러
     */
    ProgressHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = (ProgressBar) findViewById(R.id.progress);
        textView = (TextView) findViewById(R.id.textView);

        handler = new ProgressHandler();

    }

    public void onStart() {
        super.onStart();

        bar.setProgress(0);
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                try {
                    for (int i = 0; i < 20 && isRunning; i++) {
                        Thread.sleep(1000);

                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }
                } catch (Exception ex) {
                    Log.e("MainActivity", "Exception in processing message.", ex);
                }
            }
        });

        isRunning = true;
        thread1.start();
    }

    public void onStop() {
        super.onStop();

        isRunning = false;
    }


    public class ProgressHandler extends Handler {

        public void handleMessage(Message msg) {

            bar.incrementProgressBy(5);

            if (bar.getProgress() == bar.getMax()) {
                textView.setText("Done");
            } else {
                textView.setText("Working ..." + bar.getProgress());
            }

        }

    }

}
