package org.techtown.sampleparcelable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    TextView textView;

    public static final String KEY_SIMPLE_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        textView = (TextView) findViewById(R.id.textView);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 인텐트 객체를 만듭니다.
                Intent intent = new Intent();
                intent.putExtra("name", "mike");

                // 응답을 전달하고 이 액티비티를 종료합니다.
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // 메인 액티비티로부터 전달받은 인텐트를 확인합니다.
        Intent intent = getIntent();
        processIntent(intent);
    }

    /**
     * 전달된 인텐트 처리
     */
    private void processIntent(Intent intent) {
        if (intent != null) {
            // 인텐트 안의 번들 객체를 참조합니다.
            Bundle bundle = intent.getExtras();

            // 번들 객체 안의 SimpleData 객체를 참조합니다.
            SimpleData data = (SimpleData) bundle.getParcelable(KEY_SIMPLE_DATA);

            // 텍스트뷰에 값을 보여줍니다.
            textView.setText("전달받은 데이터\nNumber : " + data.getNumber() + "\nMessage : " + data.getMessage());
        }
    }

}
