package org.techtown.thread.delayed;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 핸들러를 이용해 실행 시간을 지연시키는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        // 버튼 이벤트 처리
        Button requestButton = (Button) findViewById(R.id.requestButton);
        requestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                request();
            }
        });

    }

    /**
     * 요청 메소드
     */
    private void request() {
        String title = "원격 요청";
        String message = "데이터를 요청하시겠습니까?";
        String titleButtonYes = "예";
        String titleButtonNo = "아니오";

        AlertDialog dialog = makeRequestDialog(title, message, titleButtonYes, titleButtonNo);
        dialog.show();

        textView.setText("원격 데이터 요청 중 ...");
    }

    /**
     * 요청 대화상자 만들기
     *
     * @param title
     * @param message
     * @param titleButtonYes
     * @param titleButtonNo
     * @return
     */
    private AlertDialog makeRequestDialog(CharSequence title, CharSequence message,
                                          CharSequence titleButtonYes, CharSequence titleButtonNo) {

        AlertDialog.Builder requestDialog = new AlertDialog.Builder(this);
        requestDialog.setTitle(title);
        requestDialog.setMessage(message);
        requestDialog.setPositiveButton(titleButtonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                RequestHandler handler = new RequestHandler();
                handler.sendEmptyMessageDelayed(0, 20);
            }
        });

        requestDialog.setNegativeButton(titleButtonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return requestDialog.show();
    }

    /**
     * 요청 스레드
     * @author michael
     *
     */
    class RequestHandler extends Handler {
        public void handleMessage(Message msg) {
            for (int k = 0; k < 10; k++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
            }
            textView.setText("원격 데이터 요청 완료.");
        }
    }

}
