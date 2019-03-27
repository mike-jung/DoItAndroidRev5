package org.androidtown.multimemo;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.multimemo.common.TitleBitmapButton;

/**
 * 앨범에서 사진 선택 액티비티
 *
 * @author Mike
 * @date 2011-07-01
 */
public class PhotoSelectionActivity extends AppCompatActivity {
	private static final String TAG = "PhotoSelectionActivity";

	TextView mSelectPhotoText;

	/**
	 * 앨범에서 선택한 사진의 URI
	 */
	Uri mAlbumPhotoUri;

	Bitmap resultPhotoBitmap = null;

	/**
	 * Gallery instance
	 */
	CoverFlow mPhotoGallery;

	/**
	 * Spacing of this Gallery
	 */
	public static int spacing = -45;

	ImageView mSelectedPhotoImage;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_selection_activity);

        setBottomBtns();

    	setSelectPhotoLayout();

      	Log.d(TAG, "Loading gallery data...");


      	mPhotoGallery = (CoverFlow)findViewById(R.id.loading_gallery);
    	Cursor c = getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, null, null, null, Images.Media.DATE_TAKEN + " DESC"); //Image데이터 쿼리
    	PhotoCursorAdapter adapter = new PhotoCursorAdapter(this, c);
    	mPhotoGallery.setAdapter(adapter);

    	mPhotoGallery.setSpacing(spacing);
    	mPhotoGallery.setSelection(2, true);
    	mPhotoGallery.setAnimationDuration(3000);

    	Log.d(TAG, "Count of gallery images : " + mPhotoGallery.getCount());

    	mPhotoGallery.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView parent, View v, int position, long id) {
    			try {
    				Uri uri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, id); //개별 이미지에 대한 URI 생성

    				mAlbumPhotoUri = uri; //앨범에서 이미지를 선택한 URI

    				// MIKE UPDATE START 2012-07-07
    				BitmapFactory.Options options = new BitmapFactory.Options();
    				options.inSampleSize = 8;

    				// 기존 Bitmap 이미지가 있을 경우 기존 이미지의 메모리 해제
    				if (resultPhotoBitmap != null) {
    					resultPhotoBitmap.recycle();
    				}

    				resultPhotoBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

    				// MIKE UPDATE END

    				Log.d(TAG, "Selected image URI from Album : " + mAlbumPhotoUri);
    				mSelectPhotoText.setVisibility(View.GONE);
    				mSelectedPhotoImage.setImageBitmap(resultPhotoBitmap);
    				mSelectedPhotoImage.setVisibility(View.VISIBLE);

    			} catch(Exception ie) {
    				Log.d(TAG, ie.toString());
    			}

    		}
    	});
	}



	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (hasFocus) {
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath())));
		}

	}

	private void setBottomBtns(){
		TitleBitmapButton loading_okBtn = (TitleBitmapButton) findViewById(R.id.loading_okBtn);
		loading_okBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showParentActivity();
			}
        });

		TitleBitmapButton loading_cancelBtn = (TitleBitmapButton) findViewById(R.id.loading_cancelBtn);
		loading_cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void setSelectPhotoLayout() {
		mSelectPhotoText = (TextView) findViewById(R.id.loading_selectPhotoText);

		mSelectedPhotoImage = (ImageView)findViewById(R.id.loading_selectedPhoto);

		mSelectedPhotoImage.setVisibility(View.VISIBLE);
	}

	public static int getBitmapOfWidth( String fileName ){
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, options);
			return options.outWidth;
		} catch(Exception ex) {
			Log.d(TAG, ex.toString());
			return 0;
		}
	}


	class PhotoCursorAdapter extends CursorAdapter {
		int mGalleryItemBackground;

		public PhotoCursorAdapter(Context context, Cursor c) {
			super(context, c);

			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
			mGalleryItemBackground = a.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 0);
			a.recycle();
		}

		public void bindView(View view, Context context, Cursor cursor) {
			ImageView img = (ImageView)view;

			long id = cursor.getLong(cursor.getColumnIndexOrThrow(Images.Media._ID));
			Uri uri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, id); //개별 이미지에 대한 URI 생성
			Log.d(TAG, " id -> " + id + ", uri -> " + uri);

			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

				img.setImageBitmap(bm);

			} catch(Exception e) {}

		}

		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			ImageView v = new ImageView(context);

			int itemWidth = new Float(getResources().getDimension(R.dimen.coverflow_item_width)).intValue();
			int itemHeight = new Float(getResources().getDimension(R.dimen.coverflow_item_height)).intValue();

			v.setLayoutParams(new Gallery.LayoutParams(itemWidth, itemHeight));
			v.setBackgroundResource(mGalleryItemBackground);

			return v;
		}
	}

	/**
	 * 부모 액티비티로 돌아가기
	 */
	private void showParentActivity() {
		Intent intent = getIntent();

		if(mAlbumPhotoUri != null && resultPhotoBitmap != null){
			intent.putExtra(BasicInfo.KEY_URI_PHOTO, mAlbumPhotoUri);
			setResult(RESULT_OK, intent);
		}

		finish();
	}
}