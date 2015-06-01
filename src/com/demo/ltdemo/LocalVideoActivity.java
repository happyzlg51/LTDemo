package com.demo.ltdemo;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.ltdemo.adapter.VideoPathAdapter;
import com.demo.util.Videos;

public class LocalVideoActivity extends Activity {

	private ListView lv_videolist;
	private List<Videos> lt;
	private TextView tv_path;
	private Videos video_click;
	// É¨ÃèÂ·¾¶
	private final String local_path = "/sdcard/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mediaMetadataRetriever = new MediaMetadataRetriever();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.local_video_list);
		lv_videolist = (ListView) findViewById(R.id.videolist);
		tv_path = (TextView) findViewById(R.id.pathtext);
		lt = new ArrayList<Videos>();
		getVideoFile(lt, new File(local_path));
		tv_path.setText(local_path);
		lv_videolist.setAdapter(new VideoPathAdapter(LocalVideoActivity.this,
				lt));
		lv_videolist
				.setOnItemClickListener(new MyVideoListOnItemClickListener());

		((MyApplication) getApplicationContext()).setVideos(lt);
		MainActivity.dissDialog();
	}

	class MyVideoListOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			video_click = lt.get(arg2);
			Intent intent = new Intent("com.test.videoplayer");
			String path = video_click.getvPath();
			intent.putExtra(BasePlayerActivity.KEY_DATA, path);
			LocalVideoActivity.this.startActivity(intent);
			finish();
		}
	}

	MediaMetadataRetriever mediaMetadataRetriever;

	private void getVideoFile(final List<Videos> list, File file) {
		file.listFiles(new FileFilter() {
			// @Override
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				String name = file.getName();
				int i = name.indexOf('.');
				if (i != -1) {
					name = name.substring(i);
					if (name.equalsIgnoreCase(".mp4")
							|| name.equalsIgnoreCase(".3gp")
							|| name.equalsIgnoreCase(".mkv")
					// || name.equalsIgnoreCase(".3dv")
					) {
						Videos video = new Videos();
						video.vName = file.getName();
						video.vPath = file.getAbsolutePath();
						mediaMetadataRetriever.setDataSource(video.vPath);
						video.duration = mediaMetadataRetriever
								.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
						list.add(video);
						return true;
					}
				} else if (file.isDirectory()) {
					getVideoFile(list, file);
				}
				return false;
			}
		});
	}

}
