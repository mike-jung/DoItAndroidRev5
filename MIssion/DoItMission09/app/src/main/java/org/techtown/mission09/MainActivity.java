package org.techtown.mission09;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    EditText editText2;
    EditText editText3;

    TextView textView;

    ListView listView;
    CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        adapter = new CustomerAdapter();

        adapter.addItem(new CustomerItem("김준수", "1995-10-20", "010-1000-1000", R.drawable.customer));
        adapter.addItem(new CustomerItem("이희연", "1994-02-13", "010-2000-2000", R.drawable.customer));

        listView.setAdapter(adapter);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);

        textView = (TextView) findViewById(R.id.textView);

        textView.setText("2 명");

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                String birth = editText2.getText().toString();
                String mobile = editText3.getText().toString();

                adapter.addItem(new CustomerItem(name, birth, mobile, R.drawable.customer));
                adapter.notifyDataSetChanged();

                textView.setText(adapter.getCount() + " 명");
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CustomerItem item = (CustomerItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : " + item.getName(), Toast.LENGTH_LONG).show();
            }
        });

    }

    class CustomerAdapter extends BaseAdapter {
        ArrayList<CustomerItem> items = new ArrayList<CustomerItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(CustomerItem item) {
            items.add(item);
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
            CustomerItemView view = new CustomerItemView(getApplicationContext());

            CustomerItem item = items.get(position);
            view.setName(item.getName());
            view.setBirth(item.getBirth());
            view.setMobile(item.getMobile());
            view.setImage(item.getResId());

            return view;
        }
    }

}
