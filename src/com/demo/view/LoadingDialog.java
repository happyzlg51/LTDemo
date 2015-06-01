package com.demo.view;

import com.demo.ltdemo.R;
import com.demo.ltdemo.R.layout;
import com.demo.ltdemo.R.style;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class LoadingDialog extends Dialog {
	View view;
	LayoutInflater inflater;
	Activity context;

	public LoadingDialog(Activity context) {
		super(context, R.style.Translucent_NoTitle);
		// if (inflater == null)
		// inflater = LayoutInflater.from(context);
		// view = inflater.inflate(R.layout.dialog, null);
		// setContentView(view);
		setContentView(R.layout.loading_dialog);
		setCanceledOnTouchOutside(false);
		this.context = context;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		context.finish();
	}
}
