package com.demo.ltdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.demo.player.Player;
import com.demo.player.Renderer;
import com.demo.player.Renderer.RendererCallBack;
import com.demo.util.PlayerGestureHelper;
import com.demo.util.PlayerGestureListener;
import com.demo.util.PlayerGestureListener.PlayerGestureCallback;
import com.demo.util.Utils;

public class BasePlayerActivity extends Activity implements OnClickListener,
		RendererCallBack {
	//
	public static final String KEY_DATA = "data";
	public static final String TAG = "BasePlayerActivity";
	LinearLayout player_bottom;
	RelativeLayout player_top;
	protected String strURL;
	protected GLSurfaceView mGLSurfaceView;
	protected Renderer mRenderer;
	protected Player mPlayer;
	protected ImageButton btnPlay;
	protected SeekBar seekBar;
	static final int UPDATE_SEEKBAR = 0x001;
	static final int UPDATE_TOP_BOTTOM_VIEW = 0x002;
	static final int UPDATE_BRIGHTNESS_VOICE_VIEW = 0x003;
	static final int UPDATE_FORWARD_VIEW = 0x004;
	protected boolean isPlaying;
	protected boolean isRun = true;

	protected int screenWidth;
	protected int screenHeight;
	protected GestureDetector detector;

	TextView video_name, tv_videoTime, tv_currentTime, forward_time,
			video_list;
	LinearLayout brightness_voice_layout;
	ProgressBar pb;
	ImageView player_iv_back, iv_type;
	FrameLayout guide_layout;
	RelativeLayout layout_setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		getIntentData();
		initView();
		mPlayer = new Player(this);
		mRenderer = new Renderer(this, mPlayer, mGLSurfaceView);
		// mRenderer.setRendererCallBack(this);
		mGLSurfaceView.setEGLContextClientVersion(2);
		mGLSurfaceView.setRenderer(mRenderer);
		mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		mPlayer.setRenderer(mRenderer);
		mPlayer.setOnPreparedListener(onPreparedListener);
		mPlayer.setOnCompletionListener(completionListener);
		PlayerGestureListener gestureListener = new PlayerGestureListener(this,
				screenWidth, screenHeight, playerGestureCallback);
		detector = new GestureDetector(this, gestureListener);

		if (MainActivity.is_first_start) {
			MainActivity.is_first_start = false;
			guide_layout.setVisibility(View.VISIBLE);
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					guide_layout.setVisibility(View.GONE);
				}
			}, 4200);
		}

	}

	String videoName = "";

	private void getIntentData() {
		Intent intent = getIntent();
		strURL = (String) intent.getSerializableExtra(KEY_DATA);
		if (null != strURL || !"".equals(strURL))
			videoName = strURL.substring(strURL.lastIndexOf("/") + 1,
					strURL.length());
		Log.e("info", "父类：url:" + strURL + ",name:" + videoName);
	}

	private void initView() {
		mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceView);
		btnPlay = (ImageButton) findViewById(R.id.btnPlayer);
		btnPlay.setOnClickListener(this);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (mPlayer != null)
					mPlayer.seekTo(seekBar.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		});
		//
		player_iv_back = (ImageView) findViewById(R.id.player_iv_back);
		player_iv_back.setOnClickListener(this);
		player_top = (RelativeLayout) findViewById(R.id.player_top);
		player_bottom = (LinearLayout) findViewById(R.id.player_bottom);
		player_top.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		player_bottom.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		brightness_voice_layout = (LinearLayout) findViewById(R.id.brightness_voice_layout);
		iv_type = (ImageView) findViewById(R.id.iv_type);
		forward_time = (TextView) findViewById(R.id.forward_time);
		video_name = (TextView) findViewById(R.id.video_name);
		pb = (ProgressBar) findViewById(R.id.pb);
		PlayerGestureHelper.setBrightness(this, 0.5F);
		video_name.setText(videoName);
		tv_videoTime = (TextView) findViewById(R.id.time_video_time);
		tv_currentTime = (TextView) findViewById(R.id.time_current);
		video_list = (TextView) findViewById(R.id.tv_video_list);
		guide_layout = (FrameLayout) findViewById(R.id.guide);
		// video_list.setOnClickListener(this);
		layout_setting = (RelativeLayout) findViewById(R.id.layout_setting);

		layout_setting.setOnClickListener(this);
	}

	private OnPreparedListener onPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			onPrepare();
		}
	};
	private OnCompletionListener completionListener = new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			// Log.e("info", "oncomplete");
			// mp.stop();
			// finish();
			onCompleted(mp);
		}
	};

	protected void onCompleted(MediaPlayer mp) {
		// mp.stop();
		// mp.release();
		isPlaying = false;
		mPlayer.release();
		mPlayer = null;
		finish();
		Log.e("info", "父类onCompleted()");
	}

	protected void onPrepare() {
		if (null != mPlayer) {
			//
			mRenderer.setVideoSize(mPlayer.getVideoWidth(),
					mPlayer.getVideoHeight());
			seekBar.setMax(mPlayer.getDuration());
			tv_videoTime
					.setText(Utils.getTimeByDuration(mPlayer.getDuration()));
			isPlaying = true;
			mPlayer.start();
			new Thread() {
				public void run() {
					while (isRun) {
						if (isPlaying) {
							mHandler.removeMessages(UPDATE_SEEKBAR);
							mHandler.sendEmptyMessage(UPDATE_SEEKBAR);
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				};

			}.start();
			sendHideViewMsg();
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_SEEKBAR:
				if (null != mPlayer && seekBar != null) {
					seekBar.setProgress(mPlayer.getCurrentPosition());
					tv_currentTime.setText(Utils.getTimeByDuration(mPlayer
							.getCurrentPosition()));
				}
				break;
			case UPDATE_TOP_BOTTOM_VIEW:
				if (player_top.getVisibility() == View.VISIBLE) {
					player_top.setVisibility(View.INVISIBLE);
					player_bottom.setVisibility(View.INVISIBLE);
					btnPlay.setVisibility(View.INVISIBLE);
				}
				break;
			case UPDATE_BRIGHTNESS_VOICE_VIEW:
				if (brightness_voice_layout.getVisibility() == View.VISIBLE)
					brightness_voice_layout.setVisibility(View.INVISIBLE);
				break;
			case UPDATE_FORWARD_VIEW:
				if (brightness_voice_layout.getVisibility() == View.VISIBLE)
					brightness_voice_layout.setVisibility(View.INVISIBLE);
				if (mPlayer != null && !mPlayer.isPlaying()) {
					isPlaying = true;
					btnPlay.setBackgroundResource(R.drawable.player_icon_pause);
					mPlayer.seekTo(forwardToDuration);
					mPlayer.start();
				}
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPlayer:
			if (isPlaying) {
				mPlayer.pause();
				btnPlay.setBackgroundResource(R.drawable.player_icon_play);
				isPlaying = false;
			} else {
				btnPlay.setBackgroundResource(R.drawable.player_icon_pause);
				mPlayer.start();
				isPlaying = true;
			}
			break;

		case R.id.player_iv_back:
			onBack();
			break;
		case R.id.layout_setting:
			onSettingclick();
			break;
		}
	}

	protected void onSettingclick() {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		onBack();
	}

	@Override
	public void onSurfaceCreated() {
		mPlayer.play(strURL);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (detector.onTouchEvent(event))
			return true;
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.e(TAG, "action_up");
			mHandler.removeMessages(UPDATE_BRIGHTNESS_VOICE_VIEW);
			mHandler.sendEmptyMessage(UPDATE_BRIGHTNESS_VOICE_VIEW);
			if (isforwardend) {
				mHandler.removeMessages(UPDATE_FORWARD_VIEW);
				mHandler.sendEmptyMessage(UPDATE_FORWARD_VIEW);
				isforwardend = false;
			}

			sendHideViewMsg();
		}
		return super.onTouchEvent(event);
	}

	boolean isBrightnessFirst = true;
	boolean isVolumeFirst = true;
	boolean isforwardend = false;

	private void setBrightness(float brightness) {
		pb.setMax(10);
		if (forward_time.getVisibility() == View.VISIBLE)
			forward_time.setVisibility(View.GONE);
		iv_type.setVisibility(View.VISIBLE);
		if (isBrightnessFirst) {
			pb.setProgress(5);
			isBrightnessFirst = false;
		}
		// 默认亮度范围[0,1]
		if (brightness < 0.1f)
			brightness = 0.1f;
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		Window window = getWindow();
		lp.screenBrightness = brightness;
		window.setAttributes(lp);
		int br = (int) (brightness * 10);
		if (br >= 10)
			br = 10;
		iv_type.setBackgroundResource(R.drawable.brightness);
		pb.setProgress(br);
		brightness_voice_layout.setVisibility(View.VISIBLE);
	}

	public int setVolume(int volume) {
		if (forward_time.getVisibility() == View.VISIBLE)
			forward_time.setVisibility(View.GONE);
		iv_type.setVisibility(View.VISIBLE);
		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		pb.setMax(maxVolume);
		if (isVolumeFirst) {
			pb.setProgress(PlayerGestureHelper.getVolume(this));
			isVolumeFirst = false;
		}
		if (volume > maxVolume) {
			volume = maxVolume;
		} else if (volume < 0) {
			volume = 0;
		}
		am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
		pb.setProgress(volume);
		iv_type.setBackgroundResource(R.drawable.volume);
		brightness_voice_layout.setVisibility(View.VISIBLE);
		Log.e("info", "音量:" + volume);
		return volume;
	}

	private void setTopOrBottomVisiable() {
		if (player_top.getVisibility() == View.VISIBLE) {
			player_top.setVisibility(View.INVISIBLE);
			player_bottom.setVisibility(View.INVISIBLE);
			btnPlay.setVisibility(View.INVISIBLE);
		} else {
			player_top.setVisibility(View.VISIBLE);
			player_bottom.setVisibility(View.VISIBLE);
			btnPlay.setVisibility(View.VISIBLE);
		}
	}

	protected boolean isPopupWindowShow = false;

	protected void sendHideViewMsg() {
		if (player_top.getVisibility() == View.VISIBLE) {
			if (!isPopupWindowShow) {
				mHandler.removeMessages(UPDATE_TOP_BOTTOM_VIEW);
				mHandler.sendEmptyMessageDelayed(UPDATE_TOP_BOTTOM_VIEW, 4000);
			} else {
				mHandler.removeMessages(UPDATE_TOP_BOTTOM_VIEW);
			}
		}
	}

	PlayerGestureCallback playerGestureCallback = new PlayerGestureCallback() {
		@Override
		public int onVolumeChange(int voice) {
			return setVolume(voice);
		}

		@Override
		public void onSingleTapUp() {
			if (!isPopupWindowShow) {
				setTopOrBottomVisiable();
				sendHideViewMsg();
			}
		}

		@Override
		public void onBrightNessChange(float brightness) {
			setBrightness(brightness);
		}

		@Override
		public void onforwarding(float sliddingX) {
			Log.e("info", "快进退中...");
			mHandler.removeMessages(UPDATE_TOP_BOTTOM_VIEW);
			/**
			 * 假如一个视频5分多钟,总长是20000毫秒; 滑动一下屏幕大概走700px像素,类似的换算成是700毫秒.
			 * ---对应到视频相当于快进到了多少? ans:
			 */
			isforwardend = true;
			if (mPlayer.isPlaying()) {
				mPlayer.pause();
				isPlaying = false;
				if (btnPlay.getVisibility() == View.VISIBLE)
					btnPlay.setBackgroundResource(R.drawable.player_icon_play);
			}
			int forward = FORWARD_SPEED * (int) sliddingX;
			forwardToDuration = mPlayer.getCurrentPosition() + forward;
			if (forwardToDuration < 0) {
				forwardToDuration = 0;
			} else if (forwardToDuration > mPlayer.getDuration()) {
				forwardToDuration = mPlayer.getDuration();
			}
			UpdateForwardView();
		}
	};
	private static final int FORWARD_SPEED = 30;//
	private int forwardToDuration = -1;

	private void UpdateForwardView() {
		pb.setMax(mPlayer.getDuration());
		pb.setProgress(forwardToDuration);
		forward_time.setText("" + Utils.getTimeByDuration(forwardToDuration)
				+ "/" + Utils.getTimeByDuration(mPlayer.getDuration()));
		brightness_voice_layout.setVisibility(View.VISIBLE);
		iv_type.setVisibility(View.GONE);
		forward_time.setVisibility(View.VISIBLE);
		Log.e("info", "UpdateForwardView()");
	}

	protected void onBack() {
		isRun = false;
		isPlaying = false;
		// mGLSurfaceView.setVisibility(View.INVISIBLE);
		if (mPlayer != null)
			mPlayer.release();
		finish();
	}

}
