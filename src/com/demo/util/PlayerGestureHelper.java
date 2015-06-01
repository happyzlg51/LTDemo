package com.demo.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.Window;
import android.view.WindowManager;

public class PlayerGestureHelper {

	/**
	 * ��������
	 * 
	 * @param activity
	 * @param brightness
	 */
	public static void setBrightness(Activity activity, float brightness) {

		if (brightness < 0.1) {// =0����
			brightness = 0.1f;
		}
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		Window window = activity.getWindow();
		lp.screenBrightness = brightness;
		window.setAttributes(lp);
	}

	public static float getBrightness(Activity activity) {
		return activity.getWindow().getAttributes().screenBrightness;
	}

	/**
	 * ��������
	 * 
	 * @param activity
	 * @param volume
	 */
	public static int setVolume(Activity activity, int volume) {
		AudioManager am = (AudioManager) activity
				.getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		if (volume > maxVolume) {
			volume = maxVolume;
		} else if (volume < 0) {
			volume = 0;
		}
		am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
		return volume;
	}

	/**
	 * ��ȡ����
	 * 
	 * @param activity
	 * @return
	 */
	public static int getVolume(Activity activity) {
		AudioManager am = (AudioManager) activity
				.getSystemService(Context.AUDIO_SERVICE);
		return am.getStreamVolume(AudioManager.STREAM_MUSIC);
	}

	/**
	 * ��ȡ�������ֵ
	 * 
	 * @param activity
	 * @return
	 */
	public static int getMaxVolume(Activity activity) {
		AudioManager am = (AudioManager) activity
				.getSystemService(Context.AUDIO_SERVICE);
		return am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}
}
