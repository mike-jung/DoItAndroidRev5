package org.techtown.thread.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 루퍼를 사용하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    TextView textView, textView2;
    EditText editText, editText2;

    /**
     * 메인 스레드의 핸들러
     */
    MainHandler mainHandler;

    /**
     * 새로 만든 스레드
     */
    ProcessThread thread1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainHandler = new MainHandler();
        thread1 = new ProcessThread();

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);

        // 버튼 이벤트 처리
        Button processButton = (Button) findViewById(R.id.processButton);
        processButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String inStr = editText.getText().toString();
                Message msgToSend = Message.obtain();
                msgToSend.obj = inStr;

                thread1.handler.sendMessage(msgToSend);
            }
        });

        thread1.start();

    }

    /**
     * 새로 정의한 스레드
     */
    class ProcessThread extends Thread {
        // 새로운 스레드를 위한 핸들러
        ProcessHandler handler;

        public ProcessThread() {
            handler = new ProcessHandler();
        }

        public void run() {
            // 루퍼 사용
            Looper.prepare();
            Looper.loop();
        }

    }

    class ProcessHandler extends Handler {
        public void handleMessage(Message msg) {
            Message resultMsg = Message.obtain();
            resultMsg.obj = msg.obj + " Mike!!!";

            mainHandler.sendMessage(resultMsg);
        }
    }

    class MainHandler extends Handler {
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            editText2.setText(str);
        }
    }

}
