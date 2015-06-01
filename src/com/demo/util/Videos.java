package com.demo.util;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Videos implements Serializable, Parcelable {
	public String vName;
	public String vPath;

	public String duration;

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getvName() {
		return vName;
	}

	public void setvName(String vName) {
		this.vName = vName;
	}

	public String getvPath() {
		return vPath;
	}

	public void setvPath(String vPath) {
		this.vPath = vPath;
	}

	public Videos(String vName, String vPath) {
		super();
		this.vName = vName;
		this.vPath = vPath;
	}

	public Videos() {
		super();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(vName);
		dest.writeString(vPath);
	}
}
