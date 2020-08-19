package com.example.ddvmusic;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.entity.Music1;
import com.example.util.MusicUtil;

public class MusicListActivity extends Activity {

	TextView tvback;
	ListView lv;
	ArrayList<Music1> musiclist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_list);
		 musiclist=new ArrayList<Music1>();
	        lv=(ListView)findViewById(R.id.listView1);
	        musiclist=MusicUtil.MusicFiles;
	        MyMusicListAdapter madpter=new MyMusicListAdapter(musiclist, this);
	        lv.setAdapter(madpter);
	        Animation ani=AnimationUtils.loadAnimation(this, R.anim.translate);
	        LayoutAnimationController lac=new LayoutAnimationController(ani);
	        lv.setLayoutAnimation(lac);
	        lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					Intent it=new Intent(MusicListActivity.this,MusicActivity.class);	
					
						MyService.index=arg2;
						MyService.mp.stop();
						MyService.mp.release();
						MyService.mp=null;
						MyService.mp=new MediaPlayer();
						try {
							MyService.mp.setDataSource(MusicUtil.MusicFiles.get(MyService.index).getUrl());
							MyService.mp.prepare();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						it.putExtra("isplaying",true);
						setResult(RESULT_OK,it);
						finish();
				}
			});
	        tvback=(TextView)findViewById(R.id.textView1);
	        tvback.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent it=new Intent(MusicListActivity.this,MusicActivity.class);
					setResult(RESULT_OK,it);
					finish();
				}
			});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent it=new Intent(MusicListActivity.this,MusicActivity.class);
			setResult(RESULT_OK,it);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
}
