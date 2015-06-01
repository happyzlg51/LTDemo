package com.demo.ltdemo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.ltdemo.R;
import com.demo.util.Videos;

public class VideoPathAdapter extends BaseAdapter {
	private Context context;
	private List<Videos> lt;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lt.size();
	}

	public VideoPathAdapter(Context context, List<Videos> lt) {
		super();
		this.context = context;
		this.lt = lt;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public List<Videos> getLt() {
		return lt;
	}

	public void setLt(List<Videos> lt) {
		this.lt = lt;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lt.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflate = LayoutInflater.from(context);
		View view = inflate.inflate(R.layout.item, null);
		TextView tv = (TextView) view.findViewById(R.id.textView1);
		tv.setText(lt.get(position).getvName());
		return view;
	}

}
