package com.demo.util;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.util.EncodingUtils;

public class Utils {
	/*
	 * get string from file
	 */
	public static String getTextFromFile(String inputFile) {
		byte[] buffer = null;
		String retString = "";
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			int length = fstream.available();
			buffer = new byte[length];
			fstream.read(buffer);
			fstream.close();
			retString = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retString;
	}

	/*
	 * get byte[] data from file
	 */
	public static byte[] getBytesFromRawFile(String inputFile) {
		byte[] buffer = null;
		try {
			FileInputStream fstream = new FileInputStream(inputFile);
			int length = fstream.available();
			buffer = new byte[length];
			fstream.read(buffer);
			fstream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * ͨ���������õ�ʱ���ַ���
	 * 
	 * @param duration
	 * @return
	 */
	public static String getTimeByDuration(long duration) {
		int time = (int) (duration / 1000);// ͨ������õ���
		int hh;
		int mm;
		int ss;
		String hhStr;
		String mmStr;
		String ssStr;
		hh = time / 3600;
		mm = (time - hh * 3600) / 60;
		ss = (time - hh * 3600) - (mm * 60);
		hhStr = String.valueOf(hh);
		mmStr = String.valueOf(mm);
		ssStr = String.valueOf(ss);
		if (hh < 10) {
			hhStr = "0" + hh;
		}
		if (mm < 10) {
			mmStr = "0" + mm;
		}
		if (ss < 10) {
			ssStr = "0" + ss;
		}
		String timeResult = hhStr + ":" + mmStr + ":" + ssStr;
		return timeResult;
	}

	/**
	 * ͨ��������õ�ʱ���ַ���
	 * 
	 * @param duration
	 * @return
	 */
	public static String getTimeBySecond(long second) {
		return getTimeByDuration(second * 1000);
	}

	private static SimpleDateFormat dateTimeFormat;

	public static String getDateTimeByDuration(long duration) {
		if (duration < 0)
			duration = 0;
		if (dateTimeFormat == null) {
			dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		return dateTimeFormat.format(new Date(duration));
	}

	/**
	 * ͨ���ַ����õ�long������
	 * 
	 * @param timeStr
	 * @return
	 */
	public static long getDurationByTimeStr(String timeStr) {
		long result = 0;
		if (timeStr.contains(".")) {// �����ϰ汾���Ե�ָ�ʱ��
			try {
				int hh = Integer.parseInt(timeStr.substring(0,
						timeStr.indexOf(".")));
				int mm = Integer.parseInt(timeStr.substring(
						timeStr.indexOf(".") + 1, timeStr.lastIndexOf(".")));
				int ss = Integer.parseInt(timeStr.substring(timeStr
						.lastIndexOf(".") + 1));
				result = (hh * 3600 + mm * 60 + ss) * 1000;
			} catch (Exception e) {
			}
		} else if (timeStr.contains(":")) {
			try {
				int hh = Integer.parseInt(timeStr.substring(0,
						timeStr.indexOf(":")));
				int mm = Integer.parseInt(timeStr.substring(
						timeStr.indexOf(":") + 1, timeStr.lastIndexOf(":")));
				int ss = Integer.parseInt(timeStr.substring(timeStr
						.lastIndexOf(":") + 1));

				result = (hh * 3600 + mm * 60 + ss) * 1000;
			} catch (Exception e) {
			}
		}

		return result;
	}

	/**
	 * ����ת�ɷ���
	 * 
	 * @param seconds
	 * @return
	 */
	public static String getMinute(String seconds) {
		try {
			int second = Integer.parseInt(seconds);
			if (second >= 60) {
				return String.valueOf(second / 60);
			}
		} catch (Exception e) {
		}
		return "1";
	}

}