package org.techtown.nfc;

import java.nio.charset.Charset;
import java.util.Locale;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;

public class NFCPushActivity extends Activity {

    private NfcAdapter mAdapter;
    private NdefMessage mMessage;

    private TextView mText;

    public static String sourceMsg = "Hello Push!";

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        mAdapter = NfcAdapter.getDefaultAdapter(this);

        setContentView(R.layout.push);

        mText = (TextView) findViewById(R.id.text);
        if (mAdapter != null) {
            mText.setText("푸쉬할 메시지 : " + sourceMsg + "\n\n다른 안드로이드폰의 NFC 앱을 터치하세요.");
        } else {
            mText.setText("먼저 NFC를 활성화하세요.");
        }

        mMessage = new NdefMessage(
                new NdefRecord[] { createTextRecord(sourceMsg, Locale.ENGLISH, true)});
    }

    public void onResume() {
        super.onResume();
        if (mAdapter != null) mAdapter.enableForegroundNdefPush(this, mMessage);
    }

    public void onPause() {
        super.onPause();
        if (mAdapter != null) mAdapter.disableForegroundNdefPush(this);
    }

    public static NdefRecord createTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

}