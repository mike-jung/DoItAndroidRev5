package org.techtown.mission26;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

public class NFCScanForegroundActivity extends AppCompatActivity {
    public static final String TAG = "NFCScanForeground";

    public static final int REQ_CODE_PUSH = 1001;

    public static final String TAG_TEXT = "P1-B2-23";

    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText;
    private TextView broadcastBtn;

    private LinearLayout parkingLinearLayout;
    private TextView floorTextView;
    private LinearLayout numberLinearLayout;

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_URI = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = NfcAdapter.getDefaultAdapter(this);

        setContentView(R.layout.scan);

        Log.d(TAG, "onCreate() called.");


        mText = (TextView) findViewById(R.id.text);
        if (mAdapter == null) {
            mText.setText("사용하기 전에 NFC를 활성화하세요.");
        } else {
            mText.setText("NFC 태그를 스캔하세요.");
        }

        broadcastBtn = (TextView) findViewById(R.id.broadcastBtn);
        broadcastBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int type = TYPE_TEXT;

                NdefMessage mMessage = createTagMessage(TAG_TEXT, type);
                NdefMessage[] msgs = new NdefMessage[1];
                msgs[0] = mMessage;

                Intent intent = new Intent(NfcAdapter.ACTION_TAG_DISCOVERED);
                intent.putExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, msgs);
                startActivity(intent);
            }
        });

        parkingLinearLayout = (LinearLayout) findViewById(R.id.parkingLinearLayout);
        floorTextView = (TextView) findViewById(R.id.floorTextView);
        numberLinearLayout = (LinearLayout) findViewById(R.id.numberLinearLayout);
        setParkingTextView(0);
        setNumberTextView(0);


        Intent targetIntent = new Intent(this, NFCScanForegroundActivity.class);
        targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, targetIntent, 0);


        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        mFilters = new IntentFilter[] {
                ndef,
        };

        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };


        Intent passedIntent = getIntent();
        if (passedIntent != null) {
            String action = passedIntent.getAction();
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
                processTag(passedIntent);
            }
        }

    }

    public void onResume() {
        super.onResume();

        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
    }

    public void onPause() {
        super.onPause();

        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }

    private void processTag(Intent passedIntent) {
        Log.d(TAG, "processTag() called.");

        Parcelable[] rawMsgs = passedIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs == null) {
            Log.d(TAG, "NDEF is null.");
            return;
        }

        mText.setText(rawMsgs.length + "개 태그 스캔됨");

        NdefMessage[] msgs;
        if (rawMsgs != null) {
            msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                msgs[i] = (NdefMessage) rawMsgs[i];
                showTag(msgs[i]);
                showView(msgs[i]);
            }
        }
    }


    private int showTag(NdefMessage mMessage) {
        List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
        final int size = records.size();
        mText.append("\n");
        for (int i = 0; i < size; i++) {
            ParsedRecord record = records.get(i);

            int recordType = record.getType();
            String recordStr = "";
            if (recordType == ParsedRecord.TYPE_TEXT) {
                recordStr = "TEXT : " + ((TextRecord) record).getText() + "\n";
            } else if (recordType == ParsedRecord.TYPE_URI) {
                recordStr = "URI : " + ((UriRecord) record).getUri().toString() + "\n";
            }

            Log.d(TAG, "record string : " + recordStr);

            mText.append(recordStr);
            mText.invalidate();
        }

        return size;
    }


    private int showView(NdefMessage mMessage) {
        List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
        final int size = records.size();
        floorTextView.setText(" : ");
        for (int i = 0; i < size; i++) {
            ParsedRecord record = records.get(i);

            int recordType = record.getType();
            String recordStr = "";
            if (recordType == ParsedRecord.TYPE_TEXT) {
                recordStr = ((TextRecord) record).getText();
            }

            if(recordStr.length() >= 8) {
                String parking = recordStr.substring(0, recordStr.indexOf("-"));
                String floor = recordStr.substring(recordStr.indexOf("-") + 1, recordStr.lastIndexOf("-"));
                String number = recordStr.substring(recordStr.lastIndexOf("-") + 1, recordStr.length());

                Log.d(TAG, "parking : " + parking + "\nfloor: " + floor + "\nnumber : " + number);

                setParkingTextView(Integer.parseInt(parking.substring(1, parking.length())));
                floorTextView.append(floor);
                floorTextView.invalidate();
                setNumberTextView(Integer.parseInt(number));
            }
        }

        return size;
    }


    private void setParkingTextView(int parking) {
        parkingLinearLayout.removeAllViewsInLayout();

        for(int i=1 ; i<=3 ; i++) {
            LayoutParams textViewParams = new LayoutParams(LayoutParams.FILL_PARENT, 100, 1);
            textViewParams.setMargins(10, 10, 10, 10);

            TextView parkingTextView = new TextView(this);
            parkingTextView.setLayoutParams(textViewParams);
            parkingTextView.setGravity(Gravity.CENTER);
            parkingTextView.setPadding(3, 3, 3, 3);

            parkingTextView.setText("P" + String.valueOf(i));

            if(i == parking) {
                parkingTextView.setBackgroundColor(Color.RED);
                parkingTextView.setTextColor(Color.WHITE);
            } else {
                parkingTextView.setBackgroundColor(0xFFAAB1FF);
                parkingTextView.setTextColor(Color.BLACK);
            }

            parkingLinearLayout.addView(parkingTextView);
        }
    }


    private void setNumberTextView(int number) {
        numberLinearLayout.removeAllViewsInLayout();

        int mNumber = 20;

        for(int X = 1; X <= 2; X++) {
            LayoutParams LinearLayoutParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
            if(X % 2 == 0) {
                LinearLayoutParams.setMargins(0, 30, 0, 0);
            } else {
                LinearLayoutParams.setMargins(0, 0, 0, 0);
            }

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(LinearLayoutParams);

            for(int y = 1; y <= 4; y++) {
                LayoutParams textViewParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1);
                textViewParams.setMargins(3, 3, 3, 3);

                TextView numberTextView = new TextView(this);
                numberTextView.setLayoutParams(textViewParams);
                numberTextView.setGravity(Gravity.CENTER);
                numberTextView.setPadding(3, 3, 3, 3);

                mNumber = mNumber + 1;

                numberTextView.setText(String.valueOf(mNumber));

                Log.d(TAG, mNumber + " = " + number);
                if(mNumber == number) {
                    numberTextView.setBackgroundColor(Color.RED);
                    numberTextView.setTextColor(Color.WHITE);
                } else {
                    numberTextView.setBackgroundColor(Color.WHITE);
                    numberTextView.setTextColor(Color.BLACK);
                }

                linearLayout.addView(numberTextView);
            }

            numberLinearLayout.addView(linearLayout);
        }
    }


    public void onNewIntent(Intent passedIntent) {
        Log.d(TAG, "onNewIntent() called.");

        if (passedIntent != null) {
            processTag(passedIntent);
        }
    }


    private NdefMessage createTagMessage(String msg, int type) {
        NdefRecord[] records = new NdefRecord[1];

        if (type == TYPE_TEXT) {
            records[0] = createTextRecord(msg, Locale.KOREAN, true);
        } else if (type == TYPE_URI){
            records[0] = createUriRecord(msg.getBytes());
        }

        NdefMessage mMessage = new NdefMessage(records);

        return mMessage;
    }


    private NdefRecord createTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        final byte[] langBytes = locale.getLanguage().getBytes(Charsets.US_ASCII);
        final Charset utfEncoding = encodeInUtf8 ? Charsets.UTF_8 : Charset.forName("UTF-16");
        final byte[] textBytes = text.getBytes(utfEncoding);
        final int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        final char status = (char) (utfBit + langBytes.length);
        final byte[] data = Bytes.concat(new byte[] {(byte) status}, langBytes, textBytes);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    private NdefRecord createUriRecord(byte[] data) {
        return new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI, new byte[0], data);
    }

}