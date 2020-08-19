package com.example.ddvmusic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.entity.Music1;

public class MyMusicListAdapter extends BaseAdapter {

	private ArrayList<Music1> musiclist;
	private Context con;
	
	
	public MyMusicListAdapter(ArrayList<Music1> musiclist, Context con) {
		this.musiclist =musiclist;
		this.con = con;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return musiclist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Music1 music=musiclist.get(arg0);
		String name=music.getName();
		String singer=music.getSingerName();
		LayoutInflater inflater=LayoutInflater.from(con);
		View v=inflater.inflate(R.layout.musicitem, null);
		TextView tv_name=(TextView) v.findViewById(R.id.textView1);
		TextView tv_singer=(TextView) v.findViewById(R.id.textView2);
		
	
		tv_name.setText(name);
		tv_singer.setText(singer);
		return v;
	}

}
