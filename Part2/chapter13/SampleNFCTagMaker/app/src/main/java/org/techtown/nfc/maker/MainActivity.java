package org.techtown.nfc.maker;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;


/**
 * NFC 태그 데이터를 메모리에 만들어 볼 수 있습니다.
 * 실제 태그에 쓰는 것과는 다른 예제입니다.
 * 실제 태그에 데이터를 직접 쓰고 싶다면 SampleNFCWriter 예제 프로젝트를 참조하기 바랍니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {
    EditText messageInput;
    TextView messageOutput;
    TextView tagOutput;

    RadioGroup rgroup01;
    RadioButton rbutton01;
    RadioButton rbutton02;

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_URI = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 쓰기 버튼 이벤트 처리
        Button writeBtn = (Button) findViewById(R.id.writeBtn);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String msg = messageInput.getText().toString();
                int type = TYPE_TEXT;
                if (rbutton02.isChecked()) {
                    type = TYPE_URI;
                }

                NdefMessage mMessage = createTagMessage(msg, type);
                byte[] messageBytes = mMessage.toByteArray();

                String hexStr = bytesToHex0x(messageBytes);
                messageOutput.setText(hexStr);

                showTag(mMessage);

            }
        });

        messageInput = (EditText) findViewById(R.id.messageInput);
        messageOutput = (TextView) findViewById(R.id.messageOutput);
        tagOutput = (TextView) findViewById(R.id.tagOutput);

        rgroup01 = (RadioGroup) findViewById(R.id.rgroup01);
        rbutton01 = (RadioButton) findViewById(R.id.rbutton01);
        rbutton02 = (RadioButton) findViewById(R.id.rbutton02);

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
        final byte[] data = Bytes.concat(new byte[]{(byte) status}, langBytes, textBytes);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    private NdefRecord createUriRecord(byte[] data) {
        return new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI, new byte[0], data);
    }


    private void showTag(NdefMessage mMessage) {
        List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
        final int size = records.size();
        tagOutput.setText("");
        for (int i = 0; i < size; i++) {
            ParsedRecord record = records.get(i);

            int recordType = record.getType();
            String recordStr = "";
            if (recordType == ParsedRecord.TYPE_TEXT) {
                recordStr = "TEXT : " + ((TextRecord) record).getText() + "\n";
            } else if (recordType == ParsedRecord.TYPE_URI) {
                recordStr = "URI : " + ((UriRecord) record).getUri().toString() + "\n";
            }

            tagOutput.append(recordStr);
            tagOutput.invalidate();
        }
    }


    public static String stringToHex(String s) {
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            result += String.format("%02X ", (int) s.charAt(i));
        }

        return result;
    }

    public static String stringToHex0x(String s) {
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            result += String.format("0x%02X ", (int) s.charAt(i));
        }

        return result;
    }

    public static String bytesToHex0x(byte[] s) {
        String result = "";

        for (int i = 0; i < s.length; i++) {
            result += String.format("0x%02X ", (int) s[i]);
        }

        return result;
    }

}
