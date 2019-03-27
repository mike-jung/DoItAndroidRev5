package org.techtown.sampleorientation;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showToast("onCreate() 호출됨.");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showToast("방향 : ORIENTATION_LANDSCAPE");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            showToast("방향 : ORIENTATION_PORTRAIT");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        showToast("onStart() 호출됨.");
    }

    @Override
    protected void onStop() {
        super.onStop();

        showToast("onStop() 호출됨.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        showToast("onDestroy() 호출됨.");
    }

    public void showToast(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

}
