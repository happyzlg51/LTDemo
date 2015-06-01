package com.demo.player;

import java.io.IOException;

import com.demo.ltdemo.LocalPlayerActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

public class Player {
	private static String TAG = "Player";
	private Context mContext = null;
	private Renderer mRenderer = null;

	// private OnPreparedListener mOnPreparedListener;

	public Player(Context context) {
		mContext = context;
		try {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		} catch (Exception e) {
			Log.e(TAG, "MediaPlayer throws Exception new");
		}
	}

	public void setOnPreparedListener(OnPreparedListener listener) {
		mMediaPlayer.setOnPreparedListener(listener);
	}

	public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
		mMediaPlayer.setOnBufferingUpdateListener(listener);
	}

	public void setOnErrorListener(OnErrorListener listener) {
		mMediaPlayer.setOnErrorListener(listener);
	}

	public void setOnCompletionListener(OnCompletionListener listener) {
		mMediaPlayer.setOnCompletionListener(listener);
	}

	public void setOnInfoListener(OnInfoListener listener) {
		mMediaPlayer.setOnInfoListener(listener);
	}

	public void play(String uri) {
		try {
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(uri);
			if (mContext instanceof LocalPlayerActivity) {
				mMediaPlayer.prepare();
				Log.e(TAG, "LocalPlayerActivity");
			} else {
				mMediaPlayer.prepareAsync();
				Log.e(TAG, "NetPlayerActivity");
			}

		} catch (IllegalStateException e) {
			Log.e(TAG, "MediaPlayer throws IllegalStateException, uri=" + uri);
		} catch (IOException e) {
			Log.e(TAG, "MediaPlayer throws IOException, uri=" + uri);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "MediaPlayer throws IllegalArgumentException, uri="
					+ uri);
		} catch (SecurityException e) {
			Log.e(TAG, "MediaPlayer throws SecurityException, uri=" + uri);
		}
	}

	public void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	public void stop() {
		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
		}
	}

	public void start() {
		if (mMediaPlayer != null) {
			mMediaPlayer.start();
		}
	}

	public void pause() {
		if (mMediaPlayer != null) {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
			}
		}
	}

	public void reset() {
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
		}
	}

	public int getVideoWidth() {
		return mMediaPlayer.getVideoWidth();
	}

	public int getVideoHeight() {
		return mMediaPlayer.getVideoHeight();
	}

	public int getDuration() {
		return mMediaPlayer.getDuration();
	}

	public int getCurrentPosition() {
		return mMediaPlayer.getCurrentPosition();
	}

	public void setRenderer(Renderer renderer) {
		mRenderer = renderer;
	}

	public void setSurface(Surface surface) {
		mMediaPlayer.setSurface(surface);
	}

	public void setDisplay(SurfaceHolder holder) {
		mMediaPlayer.setDisplay(holder);
	}

	public boolean isPlaying() {
		return mMediaPlayer.isPlaying();
	}

	public void seekTo(int length) {
		mMediaPlayer.seekTo(length);
	}

	private MediaPlayer mMediaPlayer = null;

}
