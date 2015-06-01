package com.demo.ltdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

import com.demo.ltdemo.R;
import com.demo.util.Utils;
import com.demo.util.Videos;

import android.R.color;
import android.R.dimen;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VideoListAdapter extends BaseAdapter {

	private List<Videos> vList;

	private Context context;

	public VideoListAdapter(Context context, List<Videos> vList) {
		if (null == vList)
			vList = new ArrayList<Videos>();
		this.vList = vList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return vList.size();
	}

	@Override
	public Object getItem(int position) {
		return vList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.localplayerlist_item, null);
			viewHolder = new ViewHolder();
			viewHolder.videoName = (TextView) convertView
					.findViewById(R.id.locallist_videoname);
			viewHolder.videoTime = (TextView) convertView
					.findViewById(R.id.locallist_videotime);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Videos video = vList.get(position);
		viewHolder.videoName.setText(video.getvName());
		viewHolder.videoTime.setText("Ê±³¤:"
				+ Utils.getTimeByDuration(Long.parseLong(video.duration)));
		if (video.getvPath().equals(curPath)) {
			viewHolder.videoName.setTextColor(Color.WHITE);
			viewHolder.videoTime.setTextColor(Color.WHITE);
		} else {
			viewHolder.videoName.setTextColor(Color.GRAY);
			viewHolder.videoTime.setTextColor(Color.GRAY);

		}
		return convertView;
	}

	String curPath;

	public void setCurPath(String path) {
		this.curPath = path;
		// Log.e("info", "adapter_setCurPath():" + curPath);
		notifyDataSetChanged();
	}

	class ViewHolder {

		TextView videoName;
		TextView videoTime;
	}
}
