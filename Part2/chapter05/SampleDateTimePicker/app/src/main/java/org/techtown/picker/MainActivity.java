package org.techtown.picker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 날짜와 시간을 한꺼번에 선택할 수 있는 복합위젯을 만드는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
    TextView textView;
    DateTimePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        picker = (DateTimePicker)findViewById(R.id.picker);

        // 이벤트 처리
        picker.setOnDateTimeChangedListener(new DateTimePicker.OnDateTimeChangedListener() {
            public void onDateTimeChanged(DateTimePicker view, int year,
                                          int monthOfYear, int dayOfYear, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfYear, hourOfDay, minute);

                // 바뀐 시간 텍스트뷰에 표시
                textView.setText(dateFormat.format(calendar.getTime()));
            }
        });

        // 현재 시간 텍스트뷰에 표시
        Calendar calendar = Calendar.getInstance();
        calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth(), picker.getCurrentHour(), picker.getCurrentMinute());
        textView.setText(dateFormat.format(calendar.getTime()));

    }

}
