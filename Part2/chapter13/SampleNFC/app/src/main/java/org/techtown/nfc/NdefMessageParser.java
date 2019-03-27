package org.techtown.nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import java.util.ArrayList;
import java.util.List;

public class NdefMessageParser {

    // Utility class
    private NdefMessageParser() {

    }


    public static List<ParsedRecord> parse(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedRecord> getRecords(NdefRecord[] records) {
        List<ParsedRecord> elements = new ArrayList<ParsedRecord>();
        for (NdefRecord record : records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            }
        }

        return elements;
    }
}
