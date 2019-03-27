package org.techtown.mission15;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();

    TextView textView;
    EditText editText;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlStr = editText.getText().toString().trim();

                RequestThread thread = new RequestThread(urlStr);
                thread.start();
            }
        });
    }

    class RequestThread extends Thread {
        String urlStr;

        public RequestThread(String str) {
            urlStr = str;
        }

        public void run() {
            try {
                final String output = request(urlStr);
                handler.post(new Runnable() {
                    public void run() {
                        textView.setText(output);
                        webView.loadData(output, "text/html", "UTF8");
                    }
                });

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        private String request(String urlStr) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    int resCode = conn.getResponseCode();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }

                    reader.close();
                    conn.disconnect();
                }
            } catch(Exception ex) {
                Log.e("MainActivity", "Exception in processing response.", ex);
                ex.printStackTrace();
            }

            return output.toString();
        }

    }


}
