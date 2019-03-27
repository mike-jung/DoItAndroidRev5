package org.androidtown.mission25;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 단말의 센서 정보를 알아내고 센서 값을 확인하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static final String TAG = "MainActivity";

    SensorManager manager = null;
    Sensor sensor = null;

    TextView textView;
    ImageView imageView;

    boolean nearState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);

        // 센서 매니저 객체 참조
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = manager.getDefaultSensor( Sensor.TYPE_PROXIMITY );
    }

    protected void onResume() {
        super.onResume();

        manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onStop() {
        manager.unregisterListener(this);

        super.onStop();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 센서값을 받았을 때 호출되는 메소드
     */
    public void onSensorChanged(SensorEvent event) {
        String data = "Sensor Timestamp: " + event.timestamp + "\n\n";

        for(int index = 0; index < event.values.length; index++){
            data += ("Sensor Value #" + (index + 1) + " : " + event.values[index] + "\n");
        }

        if (event.values[0] > 0.0) {
            Log.d(TAG, "Normal State : " + event.values[0]);

            if (nearState) {
                textView.setText("일반 상태");
                imageView.setImageResource(R.drawable.smile_01);

                nearState = false;
            }

        } else {
            Log.d(TAG, "Proximity State : " + event.values[0]);

            if (!nearState) {
                textView.setText("근접 상태");
                imageView.setImageResource(R.drawable.smile_02);

                nearState = true;
            }

        }
    }

    private String getSensorAccuracyAsString(int accuracy) {
        String accuracyString = "";

        switch(accuracy) {
            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH: accuracyString = "High"; break;
            case SensorManager.SENSOR_STATUS_ACCURACY_LOW: accuracyString = "Low"; break;
            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM: accuracyString = "Medium"; break;
            case SensorManager.SENSOR_STATUS_UNRELIABLE: accuracyString = "Unreliable"; break;
            default: accuracyString = "Unknown";

                break;
        }

        return accuracyString;
    }

}
