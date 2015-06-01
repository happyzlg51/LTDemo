package com.demo.ltdemo;

import java.util.ArrayList;
import java.util.List;

import com.demo.util.Videos;

import android.app.Application;

public class MyApplication extends Application {
	private List<Videos> lVideos=new ArrayList<Videos>();
	
	public void setVideos(List<Videos> lVideos){
		this.lVideos=lVideos;
	}
	public List<Videos> getVideos(){
		return lVideos;
	}
}
