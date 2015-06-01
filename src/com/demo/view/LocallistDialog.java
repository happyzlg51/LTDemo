package com.demo.view;

import com.demo.ltdemo.R;
import com.demo.ltdemo.R.layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class LocallistDialog extends DialogFragment {

	View root;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (root == null)
			root = inflater.inflate(R.layout.videolist_dialog, null);
		initView();
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	private ListView listView;

	private void initView() {

	}

}
