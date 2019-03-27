package org.androidtown.multimemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;


public class VideoPlayActivity extends AppCompatActivity {

	VideoView mVideoView;
	String mVideoUri;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_playing_activity);

		setVideoView();

		setMediaController();

	}

	public void setVideoView()
	{
		mVideoView = (VideoView)findViewById(R.id.video_playing_videoView);

		Intent intent = getIntent();
		mVideoUri = intent.getStringExtra(BasicInfo.KEY_URI_VIDEO);
		mVideoView.setVideoPath(mVideoUri);
	}

	public void setMediaController()
	{
		MediaController mediaController = new MediaController(VideoPlayActivity.this);
        mVideoView.setMediaController(mediaController);
        mVideoView.start();
	}
}