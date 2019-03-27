package org.techtown.nfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

/**
 * NFC 태그의 값을 읽어들이는 방법에 대해 알 수 있습니다.
 * 여기에서는 실제 태그를 읽어들일 수도 있지만 버튼을 눌러 가상 태그 데이터를 읽은 것처럼 만들 수도 있습니다.
 *
 * @author Mike
 */ 
public class MainActivity extends Activity {
	public static final String TAG = "ScanActivity";

    public static final int REQ_CODE_PUSH = 1001;
    public static final int SHOW_PUSH_CONFIRM = 2001;

	private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private TextView mText;

    private TextView broadcastBtn;

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_URI = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // NFC 어댑터 객체 참조
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate() called.");


        mText = (TextView) findViewById(R.id.text);
        if (mAdapter == null) {
        	mText.setText("사용하기 전에 NFC를 활성화하세요.");
        } else {
        	mText.setText("NFC 태그를 스캔하세요.");
        }

        // 버튼 이벤트 처리
        broadcastBtn = (TextView) findViewById(R.id.broadcastBtn);
        broadcastBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		int type = TYPE_TEXT;
        		String msg = "Hello Android!";

        		// 메모리에 태그 정보 생성
        		NdefMessage mMessage = createTagMessage(msg, type);
        		NdefMessage[] msgs = new NdefMessage[1];
        		msgs[0] = mMessage;

        		// 가상으로 인텐트 전달
        		Intent intent = new Intent(NfcAdapter.ACTION_TAG_DISCOVERED);
                intent.putExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, msgs);
                startActivity(intent);
        	}
        });

        Intent targetIntent = new Intent(this, MainActivity.class);
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
            }
        }

        showDialog(SHOW_PUSH_CONFIRM);
    }


    /**
     * 태그 정보 보여주기
     * 
     * @param mMessage
     * @return
     */
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

    public void onNewIntent(Intent passedIntent) {
    	Log.d(TAG, "onNewIntent() called.");

    	if (passedIntent != null) {
        	processTag(passedIntent);
        }
    }


    protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder;

    	switch (id) {
			case SHOW_PUSH_CONFIRM:
				builder = new AlertDialog.Builder(this);
				builder.setTitle("푸쉬 액티비티");
				builder.setMessage("푸쉬 액티비티를 띄우시겠습니까?");
				builder.setPositiveButton("예",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
						        Intent newIntent = new Intent(getApplicationContext(), NFCPushActivity.class);
						    	startActivityForResult(newIntent, REQ_CODE_PUSH);
							}
						});
				builder.setNegativeButton("아니오",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

							}
						});

				return builder.create();

    	}
    	return null;
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