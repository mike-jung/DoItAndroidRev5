package org.androidtown.multimemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidtown.multimemo.VoiceRecordingActivity.RemainingTimeCalculator;
import org.androidtown.multimemo.common.TitleBitmapButton;

import java.io.IOException;

/**
 * 음성 재생  액티비티
 *
 * @author Mike
 * @date 2011-07-01
 */
public class VoicePlayActivity extends AppCompatActivity {

	public static final String TAG = "VoicePlayActivity";

	TitleBitmapButton mStartStopBtn;
	ProgressBar mProgressBar;
	TextView mPlayingTimeText;
	TextView mTotalTimeText;

	MediaPlayer mPlayer = null;
	RemainingTimeCalculator mRemainingTimeCalculator;
	int mTime;
	boolean isPlaying;
	boolean isHolding;
	int mCurTime;

	String mVoicePath;
	Handler mHandler = new Handler();
	Runnable mUpdateTimer = new Runnable()
	{
        public void run()
        {
        	if(isPlaying && mTime <= (mPlayer.getDuration() / 1000))
        	{
        		if(mTime > 0)
                {
                	mProgressBar.incrementProgressBy(1);
                }
        		updateTimerView();
        	}
        	else
        	{
        		mProgressBar.setProgress(mProgressBar.getMax());
        		if(mProgressBar.getProgress() == mProgressBar.getMax())
        		{
        			mStartStopBtn.setBackgroundBitmap(R.drawable.btn_voice_play, R.drawable.btn_voice_play);
        			isPlaying = false;
        			isHolding = false;

        			stop();
        		}
        	}
        }
    };


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_playing_activity);

		setTitle(R.string.audio_play_title);

		Intent intent = getIntent();
		mVoicePath = intent.getStringExtra(BasicInfo.KEY_URI_VOICE);

		isPlaying = true;
		isHolding = false;
		mCurTime = 0;

		mStartStopBtn = (TitleBitmapButton)findViewById(R.id.playing_stopBtn);
		mStartStopBtn.setBackgroundBitmap(R.drawable.btn_voice_pause, R.drawable.btn_voice_pause);
		mStartStopBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(isPlaying && !isHolding)
				{
					isPlaying = false;

					mPlayer.pause();
					mStartStopBtn.setBackgroundBitmap(R.drawable.btn_voice_play, R.drawable.btn_voice_play);
					isHolding = true;
					mCurTime = mProgressBar.getProgress();
					mHandler.removeCallbacks(mUpdateTimer);
				}
				else if(isHolding)
				{
					isPlaying = true;
					mPlayer.start();
					mStartStopBtn.setBackgroundBitmap(R.drawable.btn_voice_pause, R.drawable.btn_voice_pause);
					isHolding = false;
					mProgressBar.setProgress(mCurTime);
					mHandler.post(mUpdateTimer);
				}
				else
				{
					isPlaying = true;
					mProgressBar.setProgress(0);
					startPlayback(mVoicePath);
				}
			}
		});

		mProgressBar = (ProgressBar)findViewById(R.id.playing_progressBar);
		mPlayingTimeText = (TextView)findViewById(R.id.playing_playingTimeText);
		mTotalTimeText = (TextView)findViewById(R.id.playing_totalTimeText);
		startPlayback(mVoicePath);

		TitleBitmapButton closeBtn = (TitleBitmapButton)findViewById(R.id.playing_closeBtn);
		closeBtn.setText(R.string.close_btn);
		closeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				stop();
				finish();
			}
		});


		mProgressBar.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent ev) {
				int action = ev.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					Log.d(TAG, "Action down on progress : " + ev.getX() + ", " + ev.getY());

					// calc %
					int progressWidth = mProgressBar.getWidth();
					float currentX = ev.getX();
					float currentOffset = currentX / (float)progressWidth;

					if (currentOffset > 0.0F && currentOffset < 1.0F) {
						if (mPlayer != null) {
							int offsetProgressInt = new Float(currentOffset * 100).intValue();
							float offsetFloat = ((float)mPlayer.getDuration()) * currentOffset;
							//Toast.makeText(getApplicationContext(), "current offset progress : " + offsetProgressInt + ", offset : " + offsetFloat, 1000).show();

							if (isPlaying) {
								stop();
						        mTime = 0;
						        mPlayer = new MediaPlayer();
						        try {
						            mPlayer.setDataSource(mVoicePath);
						            mPlayer.prepare();
						            mProgressBar.setMax(mPlayer.getDuration() / 1000);
						            mProgressBar.setProgress(new Float(offsetFloat/1000.0F).intValue());
						            mPlayer.seekTo(new Float(offsetFloat).intValue());
						            mTime = new Float(offsetFloat/1000.0F).intValue();
						            mPlayer.start();
						            mStartStopBtn.setBackgroundBitmap(R.drawable.btn_voice_pause, R.drawable.btn_voice_pause);

							        mHandler.post(mUpdateTimer);

						        } catch (IllegalArgumentException e) {
						            mPlayer = null;
						        } catch (IOException e) {
						            mPlayer = null;
						        }

							}

						}
					}

				}

				return true;
			}
		});

	}
	public void onBackPressed() {
		super.onBackPressed();
		stop();
		finish();
	}




	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(mUpdateTimer);
	}



	public void startPlayback(String path) {
        stop();
        mTime = 0;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(path);
            mPlayer.prepare();
            mProgressBar.setMax(mPlayer.getDuration() / 1000);
            String mTimerFormat = "%02d:%02d";
            String timeStr = String.format(mTimerFormat, (mPlayer.getDuration() / 1000) / 60, (mPlayer.getDuration() / 1000) % 60);
            mTotalTimeText.setText(timeStr);
            mPlayer.start();
            mStartStopBtn.setBackgroundBitmap(R.drawable.btn_voice_pause, R.drawable.btn_voice_pause);
        } catch (IllegalArgumentException e) {
            mPlayer = null;
            return;
        } catch (IOException e) {
            mPlayer = null;
            return;
        }
        mHandler.post(mUpdateTimer);
    }

	public void stopPlayback() {
        if (mPlayer == null) // we were not in playback
            return;

        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
        mStartStopBtn.setBackgroundBitmap(R.drawable.btn_voice_play, R.drawable.btn_voice_play);
    }

	public void stop() {
        stopPlayback();
        mProgressBar.setProgress(0);
        mHandler.removeCallbacks(mUpdateTimer);
        mPlayingTimeText.setText("00:00");
    }


    /**
     * Update the big MM:SS timer. If we are in playback, also update the
     * progress bar.
     */
    private void updateTimerView() {

        String mTimerFormat = "%02d:%02d";

        String timeStr = String.format(mTimerFormat, mTime / 60, mTime % 60);

        mPlayingTimeText.setText(timeStr);

        mTime++;

        mHandler.postDelayed(mUpdateTimer, 1000);
    }

}
