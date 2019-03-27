package org.androidtown.multimemo;

import java.text.SimpleDateFormat;

public class BasicInfo {

	public static String language = "";


	/**
	 * 외장 메모리 패스
	 */
	public static String ExternalPath = "/mnt/sdcard/";

	/**
	 * 외장 메모리 패스 체크 여부
	 */
	public static boolean ExternalChecked = false;

	/**
	 * 사진 저장 위치
	 */
	public static String FOLDER_PHOTO 		= "memo/photo/";

	/**
	 * 동영상 저장 위치
	 */
	public static String FOLDER_VIDEO 		= "memo/video/";

	/**
	 * 녹음 저장 위치
	 */
	public static String FOLDER_VOICE 		= "memo/voice/";

	/**
	 * 손글씨 저장 위치
	 */
	public static String FOLDER_HANDWRITING 	= "memo/handwriting/";

	/**
	 * 미디어 포맷
	 */
	public static final String URI_MEDIA_FORMAT		= "content://media";

	/**
	 * 데이터베이스 이름
	 */
	public static String DATABASE_NAME = "memo/memo.db";


	//========== 인텐트 부가정보 전달을 위한 키값 ==========//
	public static final String KEY_MEMO_MODE = "MEMO_MODE";
	public static final String KEY_MEMO_TEXT = "MEMO_TEXT";
	public static final String KEY_MEMO_ID = "MEMO_ID";
	public static final String KEY_MEMO_DATE = "MEMO_DATE";
	public static final String KEY_ID_PHOTO = "ID_PHOTO";
	public static final String KEY_URI_PHOTO = "URI_PHOTO";
	public static final String KEY_ID_VIDEO = "ID_VIDEO";
	public static final String KEY_URI_VIDEO = "URI_VIDEO";
	public static final String KEY_ID_VOICE = "ID_VOICE";
	public static final String KEY_URI_VOICE = "URI_VOICE";
	public static final String KEY_ID_HANDWRITING = "ID_HANDWRITING";
	public static final String KEY_URI_HANDWRITING = "URI_HANDWRITING";


	//========== 메모 모드 상수 ==========//
	public static final String MODE_INSERT = "MODE_INSERT";
	public static final String MODE_MODIFY = "MODE_MODIFY";
	public static final String MODE_VIEW = "MODE_VIEW";


	//========== 액티비티 요청 코드  ==========//
	public static final int REQ_VIEW_ACTIVITY = 1001;
	public static final int REQ_INSERT_ACTIVITY = 1002;
	public static final int REQ_PHOTO_CAPTURE_ACTIVITY = 1501;
	public static final int REQ_PHOTO_SELECTION_ACTIVITY = 1502;
	public static final int REQ_VIDEO_RECORDING_ACTIVITY = 1503;
	public static final int REQ_VIDEO_LOADING_ACTIVITY = 1504;
	public static final int REQ_VOICE_RECORDING_ACTIVITY = 1505;
	public static final int REQ_HANDWRITING_MAKING_ACTIVITY = 1506;



	//========== 날짜 포맷  ==========//
	public static SimpleDateFormat dateDayNameFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
	public static SimpleDateFormat dateDayFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateNameFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
	public static SimpleDateFormat dateNameFormat2 = new SimpleDateFormat("yyyy-MM-dd HH시 mm분");
	public static SimpleDateFormat dateNameFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat dateTimeNameFormat = new SimpleDateFormat("HH시 mm분");
	public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("HH:mm");

	//========== 대화상자 키값  ==========//
	public static final int WARNING_INSERT_SDCARD = 1001;
	public static final int IMAGE_CANNOT_BE_STORED = 1002;

	public static final int CONTENT_PHOTO = 2001;
	public static final int CONTENT_VIDEO = 2002;
	public static final int CONTENT_VOICE = 2003;
	public static final int CONTENT_HANDWRITING = 2004;
	public static final int CONTENT_PHOTO_EX = 2005;
	public static final int CONTENT_VIDEO_EX = 2006;
	public static final int CONTENT_VOICE_EX = 2007;
	public static final int CONTENT_HANDWRITING_EX = 2008;

	public static final int CONFIRM_DELETE = 3001;

	public static final int CONFIRM_TEXT_INPUT = 3002;



	public static boolean isAbsoluteVideoPath(String videoUri) {
    	if(videoUri.startsWith(URI_MEDIA_FORMAT)) {
    		return false;
    	} else {
    		return true;
    	}
    }

}
