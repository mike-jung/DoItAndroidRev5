package org.techtown.listview;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


/**
 * ListActivity를 상속하여 간단한 리스트뷰를 만드는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends ListActivity {
    // 사용할 데이터들
    String[] items = { "mike", "angel", "crow", "john",
            "ginnie", "sally", "cohen", "rice" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ArrayAdapter를 사용하여 어댑터 설정
        setListAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                items));

    }

    protected void onListItemClick(ListView list, View v, int position, long id) {
        super.onListItemClick(list, v, position, id);

        String text = "position : " + position + " " + items[position];
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}

