package org.techtown.sensor;

import android.app.ListActivity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;


/**
 * 단말의 센서 정보를 알아내고 센서 값을 확인하는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends ListActivity {
    public static final String TAG = "MainActivity";

    SensorManager manager = null;
    List<Sensor> sensors = null;

    SensorListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 센서 매니저 객체 참조
        manager = (SensorManager)getSystemService(SENSOR_SERVICE);

        // 모든 센서 객체 가져오기
        sensors = manager.getSensorList(Sensor.TYPE_ALL);

        // 리스트에 어댑터 설정
        adapter = new SensorListAdapter(this, R.layout.listitem, sensors);
        setListAdapter(adapter);

    }

    /**
     * 리스트의 한 아이템이 클릭되었을 때 호출되는 메소드
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Sensor sensor = sensors.get(position);
        String sensorName = sensor.getName();
        Log.d(TAG, "선택된 센서 : " + sensorName);

        Intent intent = new Intent(this, SensorDataActivity.class);
        intent.putExtra(SensorDataActivity.SENSOR_INDEX, position);
        startActivity(intent);
    }

}
