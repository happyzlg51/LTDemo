package com.demo.ltdemo;

import com.demo.view.LoadingDialog;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NetPlayerActivity extends BasePlayerActivity {
	private static String TAG = "PlayerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRenderer.setRendererCallBack(this);
		mPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
		mPlayer.setOnErrorListener(errorListener);
		mPlayer.setOnInfoListener(infoListener);
		dialog = new LoadingDialog(NetPlayerActivity.this);
		dialog.show();
	}

	boolean isBuffering = false;
	OnInfoListener infoListener = new OnInfoListener() {
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
				if (!dialog.isShowing()) {
					dialog.show();
				}
			} else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
				if (dialog.isShowing())
					dialog.dismiss();
			}

			return false;
		}
	};
	LoadingDialog dialog;

	@Override
	protected void onPrepare() {
		super.onPrepare();
		Log.e("info", "子类onprepare");
		if (dialog.isShowing())
			dialog.dismiss();
	}

	private OnErrorListener errorListener = new OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Log.e("info", "onErrorListener:" + what + ",extra:" + extra);
			Toast.makeText(NetPlayerActivity.this, "播放出错!", Toast.LENGTH_SHORT)
					.show();
			if (dialog.isShowing())
				dialog.dismiss();
			onBack();
			return false;
		}
	};
	private OnBufferingUpdateListener bufferingUpdateListener = new OnBufferingUpdateListener() {

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			seekBar.setSecondaryProgress((int) ((percent / 100f) * seekBar
					.getMax()));
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		// if (mPlayer != null && !mPlayer.isPlaying()) {
		// mPlayer.stop();
		// }

	};

	@Override
	protected void onPause() {
		super.onPause();
		if (mPlayer != null && mPlayer.isPlaying()) {
			mPlayer.pause();
			btnPlay.setBackgroundResource(R.drawable.player_icon_play);
			isPlaying = false;
		}
	}
}
