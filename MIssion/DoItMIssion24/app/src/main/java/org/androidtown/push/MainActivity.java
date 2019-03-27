package org.androidtown.push;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText messageInput;
    //TextView messageOutput;
    //TextView log;

    RequestQueue queue;

    TextView textView;
    ListView listView;
    FriendAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageInput = (EditText) findViewById(R.id.messageInput);
        //messageOutput = (TextView) findViewById(R.id.messageOutput);
        //log = (TextView) findViewById(R.id.log);

        textView = (TextView) findViewById(R.id.textView);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new FriendAdapter();

        adapter.addItem(new FriendItem("소녀시대", "010-1000-1000"));
        adapter.addItem(new FriendItem("걸스데이", "010-2000-2000"));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                FriendItem item = (FriendItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : " + item.getName(), Toast.LENGTH_LONG).show();
            }
        });



        Button sendButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = messageInput.getText().toString();
                send(input);
            }
        });

        queue = Volley.newRequestQueue(getApplicationContext());

        getRegistrationId();

        Intent intent = getIntent();
        if (intent != null) {
            processIntent(intent);
        }
    }


    class FriendAdapter extends BaseAdapter {
        ArrayList<FriendItem> items = new ArrayList<FriendItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(FriendItem item) {
            items.add(item);
        }

        public void setItem(int index, FriendItem item) {
            items.set(index, item);
        }

        public void setItems(ArrayList<FriendItem> items) {
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
            FriendItemView view = null;
            if (convertView == null) {
                view = new FriendItemView(getApplicationContext());
            } else {
                view = (FriendItemView) convertView;
            }

            FriendItem item = items.get(position);
            view.setName(item.getName());
            view.setMobile(item.getMobile());

            return view;
        }
    }


    public void getRegistrationId() {
        println("getRegistrationId() 호출됨.");

        String regId = FirebaseInstanceId.getInstance().getToken();
        println("regId : " + regId);

        FriendItem item = (FriendItem) adapter.getItem(0);
        if (item != null) {
            item.setRegId(regId);
            adapter.setItem(0, item);
        }
    }

    public void send(String input) {

        JSONObject requestData = new JSONObject();

        try {
            requestData.put("priority", "high");

            JSONObject dataObj = new JSONObject();
            dataObj.put("contents", input);
            requestData.put("data", dataObj);

            JSONArray idArray = new JSONArray();
            for (int i = 0; i < adapter.getCount(); i++) {
                FriendItem item = (FriendItem) adapter.getItem(i);
                String regId = item.getRegId();
                println("regId #" + i + " : " + regId);

                idArray.put(i, regId);
            }

            requestData.put("registration_ids", idArray);

        } catch(Exception e) {
            e.printStackTrace();
        }

        sendData(requestData, new SendResponseListener() {
            @Override
            public void onRequestCompleted() {
                println("onRequestCompleted() 호출됨.");

                Toast.makeText(getApplicationContext(), "공지사항 전송함", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestStarted() {
                println("onRequestStarted() 호출됨.");
            }

            @Override
            public void onRequestWithError(VolleyError error) {
                println("onRequestWithError() 호출됨.");
            }
        });

    }

    public interface SendResponseListener {
        public void onRequestStarted();
        public void onRequestCompleted();
        public void onRequestWithError(VolleyError error);
    }

    public void sendData(JSONObject requestData, final SendResponseListener listener) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onRequestCompleted();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestWithError(error);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Authorization","key=AAAA_b17X-4:APA91bEOAH0oYbwFx2x4aeNJpz-X-i6BBsEaKz4Khyet7Q9xTGW6SIgJec66_TBPqi4ZxUbCtJ87ZcFNbLweg0IzubLbsfKRtcvmaFxV-6kqcZVFJVhzvDHExfeK4rltvfhWA83MoxOPVhTUFnEOUnt9xiC6nt_Ypg");

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setShouldCache(false);
        listener.onRequestStarted();
        queue.add(request);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        println("onNewIntent() called.");

        if (intent != null) {
            processIntent(intent);
        }

        super.onNewIntent(intent);
    }


    private void processIntent(Intent intent) {
        String from = intent.getStringExtra("from");
        if (from == null) {
            println("from is null.");
            return;
        }

        String contents = intent.getStringExtra("contents");

        println("DATA : " + from + ", " + contents);
        Toast.makeText(getApplicationContext(), "공지사항 수신함 : " + contents, Toast.LENGTH_LONG).show();

        //messageOutput.setText("[" + from + "]로부터 수신한 데이터 : " + contents);
    }

    public void println(String data) {
        //log.append(data + "\n");
        Log.d("MainActivity", data + "\n");
    }

}
