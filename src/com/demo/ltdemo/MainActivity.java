package com.demo.ltdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText ecitText1;

	public static boolean is_first_start = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ecitText1 = (EditText) findViewById(R.id.editText1);
		ecitText1.setText("");
		Button btn_local = (Button) findViewById(R.id.btn_local);
		btn_local.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog();
				MainActivity.this.startActivity(new Intent(MainActivity.this,
						LocalVideoActivity.class));
			}
		});

		is_first_start = checkFirstOpen();
		Log.e("info", "app——isfirst_start:" + is_first_start);
	}

	public void startPlay(View view) {
		String strURL = ecitText1.getText().toString().trim();
		if (null == strURL || "".equals(strURL)) {
			Toast.makeText(getApplicationContext(), "请输入网络视频播放地址！",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(this, NetPlayerActivity.class);
		intent.putExtra(BasePlayerActivity.KEY_DATA, strURL);
		startActivity(intent);
	}

	static ProgressDialog dialog;

	public void showDialog() {

		dialog = ProgressDialog.show(this, "", "loading localVideos...", true);
	}

	public static void dissDialog() {

		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean checkFirstOpen() {
		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
			int currentVersion = info.versionCode;
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			int lastVersion = prefs.getInt("versioncode", 0);
			if (currentVersion > lastVersion) {
				// 如果当前版本大于上次版本，该版本属于第一次启动
				// Toast.makeText(this, "是第一次启动！", Toast.LENGTH_LONG).show();
				// 将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
				prefs.edit().putInt("versioncode", currentVersion).commit();
				return true;
			} else {
				// Toast.makeText(this, "Bu是第一次启动！", Toast.LENGTH_LONG).show();
				return false;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return true;
		}
	}
}
