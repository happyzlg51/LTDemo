package com.demo.util;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class PlayerGestureListener extends SimpleOnGestureListener {

	private int brightNessRight;// ���ȵ��ұ߽߱�
	private int voiceTouchLeft;// ��������߽߱�
	private boolean isSetting;// ����or�����Ƿ��ڵ���
	private boolean isForward;// �Ƿ��ڿ����

	private float brightnessNow;
	private int voiceNow;
	private int maxVoice;
	private int moveDisY;

	private int screenHeight;
	private int screenWidth;

	private Activity context;
	private PlayerGestureCallback callback;

	public PlayerGestureListener(Activity context, int screenWidth,
			int screenHeight, PlayerGestureCallback callback) {
		this.context = context;
		this.callback = callback;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		brightNessRight = screenWidth / 4;
		voiceTouchLeft = screenWidth - brightNessRight;
		brightnessNow = PlayerGestureHelper.getBrightness(context);
		voiceNow = PlayerGestureHelper.getVolume(context);
		maxVoice = PlayerGestureHelper.getMaxVolume(context);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		int x1 = (int) e1.getX();
		int x2 = (int) e2.getX();
		int y1 = (int) e1.getY();
		int y2 = (int) e2.getY();
		Math.ceil(x1);

		if (Math.abs(y2 - y1) > Math.abs(x2 - x1) && Math.abs(y2 - y1) > 50) {
			// ����or��������
			moveDisY += distanceY;
			if (x1 < brightNessRight && x2 < brightNessRight) {
				// ����
				moveDisY = 0;
				brightnessNow += distanceY / screenHeight;
				brightnessNow = Math.max(0.1f, Math.min(brightnessNow, 1.0f));// ��������ת�����ȵ��ڵ�λ�߽�ֵ�ж�
				callback.onBrightNessChange(brightnessNow);
			} else if (x1 > voiceTouchLeft && x2 > voiceTouchLeft) {
				// ����
				int change = (int) (moveDisY * maxVoice / screenHeight);
				if (Math.abs(change) >= 1) {
					voiceNow += change;
					voiceNow = callback.onVolumeChange(voiceNow);
					moveDisY = 0;
				}
			}
		} else {
			moveDisY = 0;
			// �������
			// if (isForward) {
			//
			if (Math.abs(x2 - x1) > 100) {
				float sliddingX = e2.getX() - e1.getX();
				callback.onforwarding(sliddingX);
			}
		}
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	public void forwardOver() {
		isForward = false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		isSetting = false;
		isForward = false;
		Log.e("info", "onDown()");
		return true;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		callback.onSingleTapUp();
		Log.e("info", "onSingleTapUp()");
		return false;
	}

	public interface PlayerGestureCallback {
		void onBrightNessChange(float brightness);

		int onVolumeChange(int voice);

		void onSingleTapUp();

		void onforwarding(float sliddingX);

	}
}
