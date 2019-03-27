package org.techtown.actionbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 선택된 메뉴를 표시할 텍스트뷰
        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        View v = menu.findItem(R.id.menu_search).getActionView();
        if (v != null) {
            editText = (EditText) v.findViewById(R.id.editText);

            if (editText != null) {
                editText.setOnEditorActionListener(onSearchListener);
            }
        } else {
            Toast.makeText(getApplicationContext(), "ActionView is null.", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_refresh:  // 새로고침 메뉴 선택
                textView.setText("새로고침 메뉴를 선택했습니다.");
                return true;

            case R.id.menu_search:  // 검색 메뉴 선택
                textView.setText("검색 메뉴를 선택했습니다.");
                return true;

            case R.id.menu_settings:  // 설정 메뉴 선택
                textView.setText("설정 메뉴를 선택했습니다.");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 키 입력이 끝났을 때 검색합니다.
     */
    private TextView.OnEditorActionListener onSearchListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
                // 검색 메소드 호출
                search();

                // 키패드 닫기
                InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }

            return (true);
        }
    };

    /**
     * 검색 메소드 : 여기에서는 단순히 메시지로 검색어만 보여줍니다.
     */
    private void search() {
        String searchString = editText.getEditableText().toString();
        Toast.makeText(this, "검색어 : " + searchString, Toast.LENGTH_SHORT).show();
    }

}
