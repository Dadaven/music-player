package com.example.ddvmusic;

import java.io.IOException;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.util.MusicUtil;

public class MyService extends Service {

   static public  MediaPlayer  mp;
   static  public  int  index=0;
    MyBinder myBinder;
   static public int playtype=0;
   Notification notification;
   public static final String STOP = "stop";
   public static final String NEXT = "next";
   public static final String INTENT = "intent";
   TextView tv_songname;
   TextView tv_singer;
   Handler handler=new Handler();
    class MyBinder extends Binder{
    	public MyService getServiceInstance(){
    		return MyService.this;
    	}
    }
    private BroadcastReceiver receiver_onclick = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if (intent.getAction().equals(STOP)) {
                puase();
            }
            else if(intent.getAction().equals(NEXT)){
            	next(); 	
            }
            
        }
    };
public void puase(){
	handler.post(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			    notification.contentView.setTextViewText(R.id.textView1,MusicUtil.MusicFiles.get(index).getName());
		    	notification.contentView.setTextViewText(R.id.textView2,MusicUtil.MusicFiles.get(index).getSingerName());
		    	notification.contentView.setImageViewResource(R.id.button2,R.drawable.nplay1);
		    	startForeground(1, notification);
		}
	});
	if(mp.isPlaying())
    {
    	mp.pause();
        handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				    notification.contentView.setTextViewText(R.id.textView1,MusicUtil.MusicFiles.get(index).getName());
			    	notification.contentView.setTextViewText(R.id.textView2,MusicUtil.MusicFiles.get(index).getSingerName());
			    	notification.contentView.setImageViewResource(R.id.button2,R.drawable.nplay1);
			    	startForeground(1, notification);
			}
		});
    }
    else
    {
    	mp.start();
    	handler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				notification.contentView.setImageViewResource(R.id.button2,R.drawable.nstart1);
		    	startForeground(1, notification);
			}
		});
    }
	
}
    @Override
    public void onCreate() {
    	// TODO Auto-generated method stub
    	super.onCreate();
    	MusicUtil.getMusicFile(this);
    	Log.d("*********", "oncreate");
    	myBinder=new MyBinder();
    	mp=new MediaPlayer();
    	paly();
    	notification= new Notification(R.drawable.toubiao,
                "ddv", System.currentTimeMillis());
        RemoteViews view = new RemoteViews(getPackageName(),R.layout.musicnotificatiom);
        notification.contentView = view;
        addstopnitify();
        addnextnotify();
        addintentnoyify();
        notification.contentView.setTextViewText(R.id.textView1,MusicUtil.MusicFiles.get(index).getName());
    	notification.contentView.setTextViewText(R.id.textView2,MusicUtil.MusicFiles.get(index).getSingerName());
        startForeground(1, notification);
	 	
    }
	private void addnextnotify() {
		// TODO Auto-generated method stub
		IntentFilter filter_click = new IntentFilter();
        filter_click.addAction(NEXT);
        //注册广播
        registerReceiver(receiver_onclick, filter_click);
        Intent Intent_pre = new Intent(NEXT);
        //得到PendingIntent
        PendingIntent pendIntent_click = PendingIntent.getBroadcast(this, 0, Intent_pre, 0);
        //设置监听
        notification.contentView.setOnClickPendingIntent(R.id.button1,pendIntent_click);
	}
	private void addintentnoyify() {
		// TODO Auto-generated method stub
		IntentFilter filter_click = new IntentFilter();
        filter_click.addAction(INTENT);
        //注册广播
        registerReceiver(receiver_onclick, filter_click);
        Intent Intent_pre = new Intent(INTENT);
        //得到PendingIntent
        PendingIntent pendIntent_click = PendingIntent.getBroadcast(this, 0, Intent_pre, 0);
        //设置监听
        notification.contentView.setOnClickPendingIntent(R.id.imageView1,pendIntent_click);
	}
	private void addstopnitify() {
		// TODO Auto-generated method stub
		IntentFilter filter_click = new IntentFilter();
        filter_click.addAction(STOP);
        //注册广播
        registerReceiver(receiver_onclick, filter_click);
        Intent Intent_pre = new Intent(STOP);
        //得到PendingIntent
        PendingIntent pendIntent_click = PendingIntent.getBroadcast(this, 0, Intent_pre, 0);
        //设置监听
        notification.contentView.setOnClickPendingIntent(R.id.button2,pendIntent_click);
        //前台运行
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return myBinder;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mp.stop();
		mp.release();
		mp=null;
	}
	
	
	public void pre(){
		 if(mp.isPlaying()){
				if(index>0){
					index--;
				}
				else{
					index=MusicUtil.MusicFiles.size()-1;
				}
				mp.stop();
				mp.release();
				mp=null;
				mp=new MediaPlayer();
				try {
					mp.setDataSource(MusicUtil.MusicFiles.get(index).getUrl());
					mp.prepare();
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
				mp.start();
			}
			else{
				if(index>0){
					index--;
				}
				else{
					index=MusicUtil.MusicFiles.size()-1;
				}
				mp.stop();
				mp.release();

				mp=null;
				mp=new MediaPlayer();
				try {
					mp.setDataSource(MusicUtil.MusicFiles.get(index).getUrl());
					mp.prepare();
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
			}
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					notification.contentView.setImageViewResource(R.id.button2,R.drawable.nstart1);
			        notification.contentView.setTextViewText(R.id.textView1,MusicUtil.MusicFiles.get(index).getName());
			    	notification.contentView.setTextViewText(R.id.textView2,MusicUtil.MusicFiles.get(index).getSingerName());
			        startForeground(1, notification);
				}
			});
	 }
	 
	 ///下一曲
	 public void next(){
	if(mp.isPlaying()){
		if(index<MusicUtil.MusicFiles.size()-1){
			index++;
		}
		else{
			index=0;
		}
		mp.stop();
		mp.release();
		mp=null;
		mp=new MediaPlayer();
		try {
			mp.setDataSource(MusicUtil.MusicFiles.get(index).getUrl());
			mp.prepare();
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
		mp.start();
		}else
		{
			if(index<MusicUtil.MusicFiles.size()-1){
				index++;
			}
			else{
				index=0;
			}
			mp.stop();
			mp.release();
			mp=null;
			mp=new MediaPlayer();
			try {
				mp.setDataSource(MusicUtil.MusicFiles.get(index).getUrl());
				mp.prepare();
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
		}

	
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						notification.contentView.setImageViewResource(R.id.button2,R.drawable.nstart1);
				        notification.contentView.setTextViewText(R.id.textView1,MusicUtil.MusicFiles.get(index).getName());
				    	notification.contentView.setTextViewText(R.id.textView2,MusicUtil.MusicFiles.get(index).getSingerName());
				        startForeground(1, notification);
					}
				});
				
	
	
}
	 
	 public void playone(){
		    mp.stop();
			mp.release();
			mp=null;
			mp=new MediaPlayer();
			try {
				mp.setDataSource(MusicUtil.MusicFiles.get(index).getUrl());
				mp.prepare();
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
			mp.start();
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					notification.contentView.setImageViewResource(R.id.button2,R.drawable.nstart1);
			        notification.contentView.setTextViewText(R.id.textView1,MusicUtil.MusicFiles.get(index).getName());
			    	notification.contentView.setTextViewText(R.id.textView2,MusicUtil.MusicFiles.get(index).getSingerName());
			        startForeground(1, notification);
				}
			});
	 }
	 //顺序播放
	 public void sort(){
		 if(index<MusicUtil.MusicFiles.size()-1){
				index++;
			}
			else{
				index=0;
			}
			mp.stop();
			mp.release();
			mp=null;
			mp=new MediaPlayer();
			try {
				mp.setDataSource(MusicUtil.MusicFiles.get(index).getUrl());
				mp.prepare();
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
			mp.start();
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					notification.contentView.setImageViewResource(R.id.button2,R.drawable.nstart1);
			        notification.contentView.setTextViewText(R.id.textView1,MusicUtil.MusicFiles.get(index).getName());
			    	notification.contentView.setTextViewText(R.id.textView2,MusicUtil.MusicFiles.get(index).getSingerName());
			        startForeground(1, notification);
				}
			});
	 }
	 
	   	public void paly()  {
	   		try {
				mp.setDataSource(MusicUtil.MusicFiles.get(index).getUrl());
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
			try {
				mp.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		 	handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					notification.contentView.setImageViewResource(R.id.button2,R.drawable.nplay1);
			        notification.contentView.setTextViewText(R.id.textView1,MusicUtil.MusicFiles.get(index).getName());
			    	notification.contentView.setTextViewText(R.id.textView2,MusicUtil.MusicFiles.get(index).getSingerName());
			        startForeground(1, notification);
				}
			});
	   	}
	  
	   	

	   		
	   	
	   		
	   	}


