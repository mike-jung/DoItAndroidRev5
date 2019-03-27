package org.techtown.ui.coverflow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.FileInputStream;

/**
 * 갤러리를 변형하여 커버플로우를 만드는 방법에 대해 알 수 있습니다.
 *
 * @author Mike
 *
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 간격
     */
    public static int spacing = -45;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 커버플로우에 어댑터 설정
        CoverFlow coverFlow = (CoverFlow) findViewById(R.id.coverflow);
        ImageAdapter coverImageAdapter = new ImageAdapter(this);
        coverFlow.setAdapter(coverImageAdapter);

        // 커버플로우에 속성 설정
        coverFlow.setSpacing(spacing);
        coverFlow.setSelection(2, true);
        coverFlow.setAnimationDuration(3000);

    }

    /**
     * 어댑터 클래스 정의
     */
    public class ImageAdapter extends BaseAdapter {

        int itemBackground;
        private Context mContext;
        private FileInputStream outstream;

        private Integer[] mImageIds = { R.drawable.item01, R.drawable.item02,
                R.drawable.item03, R.drawable.item04, R.drawable.item05 };

        private ImageView[] mImages;

        public ImageAdapter(Context c) {
            mContext = c;
            mImages = new ImageView[mImageIds.length];
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView img = new ImageView(mContext);
            img.setImageResource(mImageIds[position]);
            img.setLayoutParams(new CoverFlow.LayoutParams(500, 280));
            img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
            drawable.setAntiAlias(true);

            return img;
        }

        public float getScale(boolean focused, int offset) {
            return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
        }

    }

}
