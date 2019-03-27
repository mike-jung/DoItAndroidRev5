package org.androidtown.multimemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import org.androidtown.multimemo.common.TitleBitmapButton;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 사진 캡쳐  액티비티
 *
 * @author Mike
 * @date 2011-07-01
 */
public class PhotoCaptureActivity extends AppCompatActivity {
	private static final String TAG = "PhotoCaptureActivity";

	CameraSurfaceView mCameraView;

	FrameLayout mFrameLayout;

	/**
	 * 버튼을 두 번 이상 누를 때 문제 해결
	 */
	boolean processing = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 상태바와 타이틀 설정
        final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.photo_capture_activity);

        mCameraView = new CameraSurfaceView(getApplicationContext());
        mFrameLayout = (FrameLayout) findViewById(R.id.frame);
        mFrameLayout.addView(mCameraView);

        setCaptureBtn();

    }

    public void setCaptureBtn() {
    	TitleBitmapButton takeBtn = (TitleBitmapButton) findViewById(R.id.capture_takeBtn);
    	takeBtn.setBackgroundBitmap(R.drawable.btn_camera_capture_normal, R.drawable.btn_camera_capture_click);
        takeBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		if (!processing) {
        			processing = true;
	        		mCameraView.capture(new CameraPictureCallback());
        		}
        	}
        });
    }

    /**
     * 키 이벤트 처리 (카메라 찍기 버튼)
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_CAMERA) {
			mCameraView.capture(new CameraPictureCallback());

			return true;
        } else if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();

			return true;
		}

        return false;
    }


    class CameraPictureCallback implements Camera.PictureCallback {

		public void onPictureTaken(byte[] data, Camera camera) {
            Log.v(TAG, "onPictureTaken() called.");

            int bitmapWidth = 480;
            int bitmapHeight = 360;

            Bitmap capturedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(capturedBitmap, bitmapWidth, bitmapHeight, false);

            Bitmap resultBitmap = null;

            Matrix matrix = new Matrix();
        	matrix.postRotate(0);

        	resultBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);

            try {
            	File photoFolder = new File(BasicInfo.FOLDER_PHOTO);

            	//폴더가 없다면 폴더를 생성한다.
    			if(!photoFolder.isDirectory()){
    				Log.d(TAG, "creating photo folder : " + photoFolder);
    				photoFolder.mkdirs();
    			}

    			String photoName = "captured";

    			// 기존 이미지가 있으면 삭제
    			File file = new File(BasicInfo.FOLDER_PHOTO + photoName);
		    	if(file.exists()) {
		    		file.delete();
		    	}

    			FileOutputStream outstream = new FileOutputStream(BasicInfo.FOLDER_PHOTO + photoName);
    			resultBitmap.compress(CompressFormat.PNG, 100, outstream);
    			outstream.close();

            } catch (Exception ex) {
                Log.e(TAG, "Error in writing captured image.", ex);
                showDialog(BasicInfo.IMAGE_CANNOT_BE_STORED);
            }

            showParentActivity();
        }
	}



    /**
     * 부모 액티비티로 돌아가기
     */
    private void showParentActivity() {
    	Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);

        finish();
    }


    protected Dialog onCreateDialog(int id) {
		Log.d(TAG, "onCreateDialog() called");

		switch (id) {
			case BasicInfo.IMAGE_CANNOT_BE_STORED:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.no_sdcard_message);
				builder.setPositiveButton(R.string.confirm_btn,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {

							}
						});
				return builder.create();
		}

		return null;
	}

}