package org.techtown.mission19;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    ListView listView;
    PictureAdapter adapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    int pictureCount = 0;

    ArrayList<ImageInfo> pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new PictureAdapter();

        ArrayList<ImageInfo> result = queryAllPictures();
        adapter.setItems(result);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ImageInfo item = (ImageInfo) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : " + item.getDisplayName(), Toast.LENGTH_LONG).show();
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ImageInfo> result = queryAllPictures();
                adapter.setItems(result);
                adapter.notifyDataSetChanged();
            }
        });

    }


    private ArrayList<ImageInfo> queryAllPictures() {
        ArrayList<ImageInfo> result = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_ADDED };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        int columnDataIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnNameIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
        int columnDateIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED);

        while (cursor.moveToNext()) {
            String path = cursor.getString(columnDataIndex);
            String displayName = cursor.getString(columnNameIndex);
            String outDate = cursor.getString(columnDateIndex);
            String addedDate = dateFormat.format(new Date(new Long(outDate).longValue() * 1000L));

            if (!TextUtils.isEmpty(path)) {
                ImageInfo info = new ImageInfo(path, displayName, addedDate);
                result.add(info);
            }

            pictureCount++;
        }

        textView.setText(pictureCount + " 개");
        Log.d("MainActivity", "Picture count : " + pictureCount);

        for (ImageInfo info : result) {
            Log.d("MainActivity", info.toString());
        }

        return result;
    }

    class PictureAdapter extends BaseAdapter {
        ArrayList<ImageInfo> items = new ArrayList<ImageInfo>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(ImageInfo item) {
            items.add(item);
        }

        public void setItems(ArrayList<ImageInfo> items) {
            this.items = items;
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            PictureItemView view = null;
            if (convertView == null) {
                view = new PictureItemView(getApplicationContext());
            } else {
                view = (PictureItemView) convertView;
            }

            ImageInfo item = items.get(position);
            view.setName(item.getDisplayName());
            view.setDate(item.getDateAdded());
            view.setImage(item.getPath());

            return view;
        }
    }

}
