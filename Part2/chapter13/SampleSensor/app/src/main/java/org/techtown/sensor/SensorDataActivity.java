package org.techtown.sensor;


import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

/**
 * 선택한 센서의 값을 확인하는 액티비티
 */
public class SensorDataActivity extends AppCompatActivity implements SensorEventListener {
	public static final String SENSOR_INDEX = "SensorIndex";

	SensorManager manager = null;
	List<Sensor> sensors = null;

	int sensorIndex = 0;
	String sensorName = null;

	TextView txtSensorName = null;
	TextView txtSensorAccuracy = null;
	TextView txtSensorValues = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data);

		txtSensorName = (TextView) findViewById(R.id.txtSensorName);
		txtSensorAccuracy = (TextView) findViewById(R.id.txtSensorAccuracy);
		txtSensorValues = (TextView) findViewById(R.id.txtSensorValues);

		manager = (SensorManager)getSystemService(SENSOR_SERVICE);
		sensors = manager.getSensorList(Sensor.TYPE_ALL);

		Intent passedIntent = getIntent();
		if (passedIntent != null) {
			sensorIndex = getIntent().getIntExtra(SENSOR_INDEX, 0);
			sensorName = sensors.get(sensorIndex).getName();
			txtSensorName.setText(sensorName);
		}
	}

	protected void onResume() {
		super.onResume();

		manager.registerListener(this, sensors.get(sensorIndex), SensorManager.SENSOR_DELAY_UI);
	}

	protected void onStop() {
		manager.unregisterListener(this);

		super.onStop();
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		txtSensorAccuracy.setText("Sensor Accuracy : " + getSensorAccuracyAsString(accuracy));
	}

	/**
	 * 센서값을 받았을 때 호출되는 메소드
	 */
	public void onSensorChanged(SensorEvent event) {
		String data = "Sensor Timestamp: " + event.timestamp + "\n\n";

		for(int index=0; index<event.values.length; ++index){
			data += ("Sensor Value #" + (index + 1) + ": " + event.values[index] + "\n");
		}

		txtSensorValues.setText(data);
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
