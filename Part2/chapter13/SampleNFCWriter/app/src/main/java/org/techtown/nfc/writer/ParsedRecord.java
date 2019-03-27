package org.techtown.nfc.writer;

public interface ParsedRecord {

	public static final int TYPE_TEXT = 1;
	public static final int TYPE_URI = 2;

	public int getType();

}
