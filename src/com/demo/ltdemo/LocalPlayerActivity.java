package com.demo.ltdemo;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.ltdemo.adapter.VideoListAdapter;
import com.demo.util.Videos;

public class LocalPlayerActivity extends BasePlayerActivity {
	private static String TAG = "LocalPlayerActivity";
	List<Videos> videos;
	PopupWindow popupWindow;
	List<String> paths;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRenderer.setRendererCallBack(this);
		mPlayer.setOnErrorListener(errorListener);
		video_list.setVisibility(View.VISIBLE);
		videos = ((MyApplication) getApplicationContext()).getVideos();
		paths = new ArrayList<String>();
		curPath = strURL;
		initPopupWindow();
		for (Videos video : videos) {
			paths.add(video.getvPath());
		}
	}

	private void initPopupWindow() {
		if (null == popupWindow) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.videolist_dialog, null);
			popupWindow = new PopupWindow(view, screenWidth / 2 - screenWidth
					/ 5, LayoutParams.WRAP_CONTENT);
			initListview(view);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					isPopupWindowShow = false;
				}
			});
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
		}

	}

	public void popupWindowShow() {
		popupWindow.showAsDropDown(player_top, screenWidth, 0);
	}

	private ListView videoListview;
	public VideoListAdapter videoListAdapter;

	private void initListview(View view) {

		videoListview = (ListView) view
				.findViewById(R.id.videolist_dialog_list);
		videoListAdapter = new VideoListAdapter(this, videos);
		videoListview.setAdapter(videoListAdapter);
		videoListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.e("info", "onItemClick()"); //
				// 如果点击的是当前正在播放的视频则不做事
				String clickPath = paths.get(position);
				if (curPath.equals(clickPath)) {
					return;
				} else {
					curPath = clickPath;
					if (mPlayer != null) {
						mPlayer.stop();
						mPlayer.reset();
						mPlayer.play(curPath);
						mPlayer.start();
						videoListAdapter.setCurPath(curPath);
						video_name.setText(videos.get(position).getvName());
					}

				}
			}
		});
		videoListview.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						video_list.setTextColor(Color.WHITE);
						isPopupWindowShow = false;
						return true;
					}
				}
				return false;
			}
		});

	}

	private OnErrorListener errorListener = new OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Toast.makeText(LocalPlayerActivity.this, "播放出错!",
					Toast.LENGTH_SHORT).show();
			onBack();
			return false;
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
		Log.e("info", "子类onPause()");
		if (mPlayer != null && mPlayer.isPlaying()) {
			isPlaying = false;
			mPlayer.pause();
			btnPlay.setBackgroundResource(R.drawable.player_icon_play);
		}
	}

	String curPath;

	@Override
	protected void onCompleted(MediaPlayer mp) {
		// super.onCompleted(mp);
		Log.e("info", "子类onCompleted");

		int curIndex = paths.indexOf(curPath);// 0,1,2,3...

		if (videos.size() <= 0 || videos.size() == 1
				|| curIndex >= paths.size() - 1) {
			super.onCompleted(mp);
		} else {
			curPath = paths.get(curIndex + 1);
			Log.e("info", "onCompleted_nextPath:" + curPath);
			mp.reset();
			mPlayer.play(curPath);
			mPlayer.start();
			videoListAdapter.setCurPath(curPath);
			video_name.setText(videos.get(curIndex + 1).getvName());
		}

	}

	@Override
	protected void onSettingclick() {
		if (popupWindow != null) {
			if (popupWindow.isShowing()) {
				video_list.setTextColor(Color.WHITE);
				popupWindow.dismiss();
			} else {
				popupWindowShow();
				isPopupWindowShow = true;
				Log.e("info", "onSetting_Click_curentPaht:" + curPath);
				videoListAdapter.setCurPath(curPath);
			}
			sendHideViewMsg();
		}

	}

}
