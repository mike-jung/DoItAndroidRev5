package org.androidtown.multimemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.multimemo.common.TitleBitmapButton;
import org.androidtown.multimemo.db.MemoDatabase;

import java.io.File;
import java.util.Date;
import java.util.Locale;

/**
 * 멀티메모 메인 액티비티
 *
 * @author Mike
 * @date 2011-07-01
 */
public class MultiMemoActivity extends AppCompatActivity {

	public static final String TAG = "MultiMemoActivity";

	/**
	 * 메모 리스트뷰
	 */
	ListView mMemoListView;

	/**
	 * 메모 리스트 어댑터
	 */
	MemoListAdapter mMemoListAdapter;

	/**
	 * 메모 갯수
	 */
	int mMemoCount = 0;


	TextView itemCount;

	/**
	 * 데이터베이스 인스턴스
	 */
	public static MemoDatabase mDatabase = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multimemo);


        // Set current locale
        Locale curLocale = getResources().getConfiguration().locale;
        BasicInfo.language = curLocale.getLanguage();
        Log.d(TAG, "current language : " + BasicInfo.language);


        // SD Card checking
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    		Toast.makeText(this, R.string.no_sdcard_message, Toast.LENGTH_LONG).show();
    		return;
    	} else {
    		String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    		if (!BasicInfo.ExternalChecked && externalPath != null) {
    			BasicInfo.ExternalPath = externalPath + File.separator;
    			Log.d(TAG, "ExternalPath : " + BasicInfo.ExternalPath);

    			BasicInfo.FOLDER_PHOTO = BasicInfo.ExternalPath + BasicInfo.FOLDER_PHOTO;
    			BasicInfo.FOLDER_VIDEO = BasicInfo.ExternalPath + BasicInfo.FOLDER_VIDEO;
    			BasicInfo.FOLDER_VOICE = BasicInfo.ExternalPath + BasicInfo.FOLDER_VOICE;
    			BasicInfo.FOLDER_HANDWRITING = BasicInfo.ExternalPath + BasicInfo.FOLDER_HANDWRITING;
    			BasicInfo.DATABASE_NAME = BasicInfo.ExternalPath + BasicInfo.DATABASE_NAME;

    			BasicInfo.ExternalChecked = true;
    		}
    	}


        // 메모 리스트
        mMemoListView = (ListView)findViewById(R.id.memoList);
    	mMemoListAdapter = new MemoListAdapter(this);
    	mMemoListView.setAdapter(mMemoListAdapter);
    	mMemoListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				viewMemo(position);
			}
		});


        // 새 메모 버튼 설정
        TitleBitmapButton newMemoBtn = (TitleBitmapButton)findViewById(R.id.newMemoBtn);
    	newMemoBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "newMemoBtn clicked.");

				Intent intent = new Intent(getApplicationContext(), MemoInsertActivity.class);
				intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_INSERT);
				startActivityForResult(intent, BasicInfo.REQ_INSERT_ACTIVITY);
			}
		});

    	// 닫기 버튼 설정
        TitleBitmapButton closeBtn = (TitleBitmapButton)findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

        itemCount = (TextView)findViewById(R.id.itemCount);


		checkDangerousPermissions();
	}

	private void checkDangerousPermissions() {
		String[] permissions = {
				android.Manifest.permission.READ_EXTERNAL_STORAGE,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
				android.Manifest.permission.CAMERA,
				android.Manifest.permission.RECORD_AUDIO
		};

		int permissionCheck = PackageManager.PERMISSION_GRANTED;
		for (int i = 0; i < permissions.length; i++) {
			permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
			if (permissionCheck == PackageManager.PERMISSION_DENIED) {
				break;
			}
		}

		if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

			if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
				Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
			} else {
				ActivityCompat.requestPermissions(this, permissions, 1);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 1) {
			for (int i = 0; i < permissions.length; i++) {
				if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
				}
			}
		}
	}



	protected void onStart() {

        // 데이터베이스 열기
        openDatabase();

        // 메모 데이터 로딩
        loadMemoListData();


		super.onStart();
	}





	/**
     * 데이터베이스 열기 (데이터베이스가 없을 때는 만들기)
     */
    public void openDatabase() {
		// open database
    	if (mDatabase != null) {
    		mDatabase.close();
    		mDatabase = null;
    	}

    	mDatabase = MemoDatabase.getInstance(this);
    	boolean isOpen = mDatabase.open();
    	if (isOpen) {
    		Log.d(TAG, "Memo database is open.");
    	} else {
    		Log.d(TAG, "Memo database is not open.");
    	}
    }



    /**
     * 메모 리스트 데이터 로딩
     */
    public int loadMemoListData() {
     	String SQL = "select _id, INPUT_DATE, CONTENT_TEXT, ID_PHOTO, ID_VIDEO, ID_VOICE, ID_HANDWRITING from MEMO order by INPUT_DATE desc";

     	int recordCount = -1;
     	if (MultiMemoActivity.mDatabase != null) {
	   		Cursor outCursor = MultiMemoActivity.mDatabase.rawQuery(SQL);

	   		recordCount = outCursor.getCount();
			Log.d(TAG, "cursor count : " + recordCount + "\n");

			mMemoListAdapter.clear();

			for (int i = 0; i < recordCount; i++) {
				outCursor.moveToNext();

				String memoId = outCursor.getString(0);

				String dateStr = outCursor.getString(1);
				if (dateStr != null && dateStr.length() > 10) {
					//dateStr = dateStr.substring(0, 10);
					try {
						Date inDate = BasicInfo.dateFormat.parse(dateStr);

						if (BasicInfo.language.equals("ko")) {
							dateStr = BasicInfo.dateNameFormat2.format(inDate);
						} else {
							dateStr = BasicInfo.dateNameFormat3.format(inDate);
						}
					} catch(Exception ex) {
						ex.printStackTrace();
					}
				} else {
					dateStr = "";
				}

				String memoStr = outCursor.getString(2);
				String photoId = outCursor.getString(3);
				String photoUriStr = getPhotoUriStr(photoId);

				String videoId = outCursor.getString(4);
				String videoUriStr = null;

				String voiceId = outCursor.getString(5);
				String voiceUriStr = null;

				String handwritingId = outCursor.getString(6);
				String handwritingUriStr = null;

				// Stage3 added
				handwritingUriStr = getHandwritingUriStr(handwritingId);

				// Stage4 added
				videoUriStr = getVideoUriStr(videoId);
				voiceUriStr = getVoiceUriStr(voiceId);


				mMemoListAdapter.addItem(new MemoListItem(memoId, dateStr, memoStr, handwritingId, handwritingUriStr, photoId, photoUriStr, videoId, videoUriStr, voiceId, voiceUriStr));
			}

			outCursor.close();

			mMemoListAdapter.notifyDataSetChanged();

			itemCount.setText(recordCount + " " + getResources().getString(R.string.item_count));
			itemCount.invalidate();
     	}

	   return recordCount;
    }

	/**
	 * 사진 데이터 URI 가져오기
	 */
	public String getPhotoUriStr(String id_photo) {
		String photoUriStr = null;
		if (id_photo != null && !id_photo.equals("-1")) {
			String SQL = "select URI from " + MemoDatabase.TABLE_PHOTO + " where _ID = " + id_photo + "";
			Cursor photoCursor = MultiMemoActivity.mDatabase.rawQuery(SQL);
	    	if (photoCursor.moveToNext()) {
	    		photoUriStr = photoCursor.getString(0);
	    	}
	    	photoCursor.close();
		} else if(id_photo == null || id_photo.equals("-1")) {
			photoUriStr = "";
		}

		return photoUriStr;
	}

	/**
	 * 손글씨 데이터 URI 가져오기
	 */
	public String getHandwritingUriStr(String id_handwriting) {
		Log.d(TAG, "Handwriting ID : " + id_handwriting);

		String handwritingUriStr = null;
		if (id_handwriting != null && id_handwriting.trim().length() > 0 && !id_handwriting.equals("-1")) {
			String SQL = "select URI from " + MemoDatabase.TABLE_HANDWRITING + " where _ID = " + id_handwriting + "";
			Cursor handwritingCursor = MultiMemoActivity.mDatabase.rawQuery(SQL);
	    	if (handwritingCursor.moveToNext()) {
	    		handwritingUriStr = handwritingCursor.getString(0);
	    	}
	    	handwritingCursor.close();
		} else {
			handwritingUriStr = "";
		}

		return handwritingUriStr;
	}



	/**
	 * 동영상 데이터 URI 가져오기
	 */
	public String getVideoUriStr(String id_video) {
		Log.d(TAG, "Video ID : " + id_video);

		String videoUriStr = null;
		if (id_video != null && id_video.trim().length() > 0 && !id_video.equals("-1")) {
			String SQL = "select URI from " + MemoDatabase.TABLE_VIDEO + " where _ID = " + id_video + "";
			Cursor videoCursor = MultiMemoActivity.mDatabase.rawQuery(SQL);
	    	if (videoCursor.moveToNext()) {
	    		videoUriStr = videoCursor.getString(0);
	    	}
	    	videoCursor.close();
		} else {
			videoUriStr = "";
		}

		return videoUriStr;
	}

	/**
	 * 녹음 데이터 URI 가져오기
	 */
	public String getVoiceUriStr(String id_voice) {
		Log.d(TAG, "Voice ID : " + id_voice);

		String voiceUriStr = null;
		if (id_voice != null && id_voice.trim().length() > 0 && !id_voice.equals("-1")) {
			String SQL = "select URI from " + MemoDatabase.TABLE_VOICE + " where _ID = " + id_voice + "";
			Cursor voiceCursor = MultiMemoActivity.mDatabase.rawQuery(SQL);
	    	if (voiceCursor.moveToNext()) {
	    		voiceUriStr = voiceCursor.getString(0);
	    	}
	    	voiceCursor.close();
		} else {
			voiceUriStr = "";
		}

		return voiceUriStr;
	}


    private void viewMemo(int position) {
    	MemoListItem item = (MemoListItem)mMemoListAdapter.getItem(position);

    	// 메모 보기 액티비티 띄우기
		Intent intent = new Intent(getApplicationContext(), MemoInsertActivity.class);
		intent.putExtra(BasicInfo.KEY_MEMO_MODE, BasicInfo.MODE_VIEW);
		intent.putExtra(BasicInfo.KEY_MEMO_ID, item.getId());
		intent.putExtra(BasicInfo.KEY_MEMO_DATE, item.getData(0));
		intent.putExtra(BasicInfo.KEY_MEMO_TEXT, item.getData(1));

		intent.putExtra(BasicInfo.KEY_ID_HANDWRITING, item.getData(2));
		intent.putExtra(BasicInfo.KEY_URI_HANDWRITING, item.getData(3));

		intent.putExtra(BasicInfo.KEY_ID_PHOTO, item.getData(4));
		intent.putExtra(BasicInfo.KEY_URI_PHOTO, item.getData(5));

		intent.putExtra(BasicInfo.KEY_ID_VIDEO, item.getData(6));
		intent.putExtra(BasicInfo.KEY_URI_VIDEO, item.getData(7));

		intent.putExtra(BasicInfo.KEY_ID_VOICE, item.getData(8));
		intent.putExtra(BasicInfo.KEY_URI_VOICE, item.getData(9));

		startActivityForResult(intent, BasicInfo.REQ_VIEW_ACTIVITY);
    }



    /**
     * 다른 액티비티의 응답 처리
     */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode) {
			case BasicInfo.REQ_INSERT_ACTIVITY:
				if(resultCode == RESULT_OK) {
					loadMemoListData();
				}

				break;

			case BasicInfo.REQ_VIEW_ACTIVITY:
				loadMemoListData();

				break;

		}
	}


}