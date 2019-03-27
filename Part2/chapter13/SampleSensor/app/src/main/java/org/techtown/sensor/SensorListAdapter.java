package org.techtown.sensor;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 센서 리스트뷰를 위한 어댑터 정의
 */
public class SensorListAdapter extends ArrayAdapter<Sensor> {
	private Context context = null;
	private List<Sensor> objects = null;

	public SensorListAdapter(Context context, int viewid, List<Sensor> objects) {
		super(context, viewid, objects);

		this.context = context;
		this.objects = objects;
	}

	public int getCount() {
		return ((null != objects) ? objects.size() : 0);
	}

	public long getItemId(int position) {
		return position;
	}

	public Sensor getItem(int position) {
		return ((null != objects) ? objects.get(position) : null);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = null;

		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = inflater.inflate(R.layout.listitem, null);
		} else {
			itemView = (View) convertView;
		}

		Sensor sensor = objects.get(position);
		if(sensor != null) {
			TextView txtName = (TextView) itemView.findViewById(R.id.txtName);
			TextView txtVendor = (TextView) itemView.findViewById(R.id.txtVendor);
			TextView txtVersion = (TextView) itemView.findViewById(R.id.txtVersion);

			String sensorName = sensor.getName();
			String sensorVendor = sensor.getVendor();
			int sensorVersion = sensor.getVersion();

			txtName.setText("센서 : " + sensorName);
			txtVendor.setText("제조사 : " + sensorVendor);
			txtVersion.setText("버전 : " + sensorVersion);
		}

		return itemView;
	}
}