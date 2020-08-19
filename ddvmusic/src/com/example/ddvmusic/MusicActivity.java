package com.example.ddvmusic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R.mipmap;
import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.DataSetObserver;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ddvmusic.MyService.MyBinder;
import com.example.entity.News;
import com.example.util.MusicUtil;
import com.example.util.MyTimeUtil;

public class MusicActivity extends Activity {

	private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2;// 页卡头标
    private int offset = 0;// 动画图片偏移量
    private int bmpW;// 动画图片宽
    
    //新闻
    ListView lv_news;
    private List<News> list;
	private InputStream in = null;
	private News news;
	Button but_music;
	Button but_movie;
	Button but_pic;
	Button but_famous;
	private SlideShowView slideShowView;
	Handler newshandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
            list=(List<News>) msg.obj;
            lv_news.setAdapter(new NewsAdapter(list,MusicActivity.this));
            ListAdapter listAdapter = lv_news.getAdapter(); 
    	    if (listAdapter == null) { 
    	        return; 
    	    } 
    	    int totalHeight = 0; 
    	    for (int i = 0; i < listAdapter.getCount(); i++) { 
    	        View listItem = listAdapter.getView(i, null, lv_news); 
    	        listItem.measure(0, 0); 
    	        totalHeight += listItem.getMeasuredHeight(); 
    	    } 
    	    ViewGroup.LayoutParams params = lv_news.getLayoutParams(); 
    	    params.height = totalHeight + (lv_news.getDividerHeight() * (listAdapter.getCount() - 1)); 
    	    lv_news.setLayoutParams(params);
		}
	};
    
    //音乐
    MyService myService;
    MyServiceConnection msc=new MyServiceConnection();
    TextView tv_mname;
    TextView tv_singer;
    SeekBar seekbar;
    Button but_next;
    Button but_pre;
    ImageButton s_s;
    int durction;//时长
    int nowtime;//当前位置
    TextView tv_passtime;
    TextView tv_duraction;
    Handler handler=new Handler();
    RadioButton rb_d;
    RadioButton rb_l;
    RadioButton rb_s;
    RadioGroup rg;
    ImageButton imgb;
    TextView tv_geci;
    ImageView imgc;
    Handler hd;
    private NotificationManager notificationManager;	//定义通知管理器对象
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		ActionBar actionbar=getActionBar();
        actionbar.hide();

		Intent intent=new Intent(this, MyService.class);
		startService(intent);
		bindService(intent, msc, Service.BIND_AUTO_CREATE);
	    new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				serchnews("http://ent.qq.com/m_news/rss_yinyue.xml");
			}
		}).start();
		InitTextView();
        InitViewPager();
        InitImageView();
        InitMusicView();
        hd=new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		super.handleMessage(msg);
        		 new Thread(new Runnable() {
        				
        				@Override
        				public void run() {
        					
        					Log.d("----------","界面线程");
        					// TODO Auto-generated method stub
        					while(true){
        						if(myService.mp!=null)
        						{
        						nowtime=myService.mp.getCurrentPosition();
        						handler.post(new Runnable() {
        							
        							@Override
        							public void run() {
        								// TODO Auto-generated method stub
        								
        								seekbar.setProgress(nowtime);
        								tv_passtime.setText(MyTimeUtil.formatSecond(nowtime));
        								tv_mname.setText(MusicUtil.MusicFiles.get(myService.index).getName());
        								 tv_singer.setText(MusicUtil.MusicFiles.get(myService.index).getSingerName());
        							     durction=myService.mp.getDuration();
        							     tv_duraction.setText(MyTimeUtil.formatSecond(durction));
        							     seekbar.setMax(durction);
        							     playbutton();
        							     
        							     myService.mp.setOnCompletionListener(new OnCompletionListener() {
        										
        										@Override
        										public void onCompletion(MediaPlayer arg0) {
        											// TODO Auto-generated method stub
        										   Log.d("***********","播放模式是"+myService.playtype);
        											switch (myService.playtype) {
        											case 0:
        											{
        												myService.playone();
        											}
        												break;
        											case 1:
        											{
        												myService.sort();
        											}
        												break;
        											case 2:
        											{
        												java.util.Random random=new java.util.Random();// 定义随机类
        												myService.index=random.nextInt(MusicUtil.MusicFiles.size()-1);
        												Log.d("%%%%%%%%%%%%555",myService.index+")))");
        												myService.playone();
        											}
        												break;
        											default:
        												break;
        											}
        										}
        									});
        							     
        							}
        						});
        					}
        						try {
        							Thread.sleep(1000);
        						} catch (InterruptedException e) {
        							// TODO Auto-generated catch block
        							e.printStackTrace();
        						}
        					}
        				}
        			}).start();
        	}
        };
       
       
       
	}
	class MyServiceConnection implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			// TODO Auto-generated method stub
			MyBinder mybinder=(MyBinder)arg1;
			myService=mybinder.getServiceInstance();
			Message m=hd.obtainMessage();
			m.obj=1;
			hd.sendMessage(m);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			myService=null;
		}
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(msc);
	}
	 private void InitMusicView() {
		// TODO Auto-generated method stub
		// 
		 but_pre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				myService.pre();
			}
		});
		 but_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				myService.next();
			}
		});
	}

	 
	private void InitTextView() {    

	     t1 = (TextView) findViewById(R.id.tv_musicname);    
	     t2 = (TextView) findViewById(R.id.tv_singer);    
	 
	     t1.setOnClickListener(new MyOnClickListener(0));    
	     t2.setOnClickListener(new MyOnClickListener(1));       
	     }  

	    /**   
	    * 头标点击监听   
	    */   
	        public class MyOnClickListener implements View.OnClickListener {    
	          private int index = 0;    
	   
	          public MyOnClickListener(int i) {    
	           index = i;    
	           }    
	   
	         @Override   
	          public void onClick(View v) {    
	          mPager.setCurrentItem(index);    
	         }    
	         };  

	       private void InitViewPager() {  
	    	   LayoutInflater mInflater = getLayoutInflater(); 
	    	   //音乐部分
	    	  View music=mInflater.inflate(R.layout.music, null);
	    	  imgb=(ImageButton)music.findViewById(R.id.imgb_musiclist);
	    	  imgb.setBackgroundResource(R.drawable.bottom);
	    	  but_pre=(Button)music.findViewById(R.id.button1);
	    	  but_next=(Button)music.findViewById(R.id.button2);
	    	  seekbar=(SeekBar)music.findViewById(R.id.seekBar1);
	    	  tv_passtime=(TextView)music.findViewById(R.id.textView1);
	    	  tv_duraction=(TextView)music.findViewById(R.id.textView2);
	    	  tv_geci=(TextView)music.findViewById(R.id.textView3);
	    	  tv_mname=(TextView)music.findViewById(R.id.tv_musicname);
	    	  tv_singer=(TextView)music.findViewById(R.id.tv_singer);
	    	  imgc=(ImageView)music.findViewById(R.id.imageView2);
	    	  
	    	  rg=(RadioGroup)music.findViewById(R.id.radioGroup1);
	    	  rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					// TODO Auto-generated method stub
					switch (arg1) {
					case R.id.radio0:{
						myService.playtype=0;}
						break;
					case R.id.radio1:{
						myService.playtype=1;}
						break;
					case R.id.radio2:{
						myService.playtype=2;}
						break;
					default:
						break;
					}
					//Toast.makeText(MusicActivity.this, playtype+"gaigaigai",Toast.LENGTH_LONG).show();
				}
			});
	    	  rb_d=(RadioButton)music.findViewById(R.id.radio0);
	    	  rb_l=(RadioButton)music.findViewById(R.id.radio1);
	    	  rb_s=(RadioButton)music.findViewById(R.id.radio2);
	    	  seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
					// TODO Auto-generated method stub
					if(arg2){
						myService.mp.seekTo(arg1);
					}
				}
			});
	    	  
	    	  imgb.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub	
					imgb.setBackgroundResource(R.drawable.baiheng);
					seekbar.setVisibility(View.GONE);
					s_s.setVisibility(View.GONE);
					rg.setVisibility(View.GONE);
					tv_duraction.setVisibility(View.GONE);
					tv_passtime.setVisibility(View.GONE);
					tv_geci.setVisibility(View.GONE);
					imgc.setVisibility(View.GONE);
					Intent it=new Intent(MusicActivity.this,MusicListActivity.class);		
					startActivityForResult(it, 1);
				}
			});
	    	  
	    	  s_s=(ImageButton)music.findViewById(R.id.imageButton1);
	    	  s_s.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub				
				pause();
				}
			});
	    	  
	    	  
	    	  //新闻部分
	    View news=mInflater.inflate(R.layout.news, null); 
	    lv_news=(ListView)news.findViewById(R.id.listView1); 
	    List<Integer> listg=new ArrayList<Integer>();
        listg.add(0,R.drawable.cui);
        listg.add(1,R.drawable.yeshi);
        listg.add(2,R.drawable.erweima);
        slideShowView=(SlideShowView)news.findViewById(R.id.guanggao);
        slideShowView.setImageUris(listg);
	    lv_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(MusicActivity.this, Newsmore_Activity.class);
				intent.putExtra("url",list.get(arg2).getLink());
				intent.putExtra("title",list.get(arg2).getTitle());
				Log.d("网址",list.get(arg2).getLink());
				startActivity(intent);
			}
		});
	    but_music=(Button)news.findViewById(R.id.button1);
        but_music.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
					serchnews("http://ent.qq.com/m_news/rss_yinyue.xml");
					}
				}).start();
			}
		});
        but_movie=(Button)news.findViewById(R.id.button2);
        but_movie.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						serchnews("http://ent.qq.com/movie/rss_movie.xml");
					}
				}).start();
			}
		});
        but_pic=(Button)news.findViewById(R.id.button3);
        but_pic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
                 new Thread(new Runnable() {
					
					@Override
					public void run() {
						serchnews("http://ent.qq.com/entpic/rss_entpic.xml");
					}
				}).start();
			}
		});
        but_famous=(Button)news.findViewById(R.id.button4);
        but_famous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
                new Thread(new Runnable() {
					
					@Override
					public void run() {
						serchnews("http://ent.qq.com/tv/rss_tv.xml");
					}
				}).start();
			}
		});
	    
	    
	    
	    
	    
	    
	    
	       mPager = (ViewPager) findViewById(R.id.vPager);    
	       listViews = new ArrayList<View>();    	          
	      listViews.add(music);    
	      listViews.add(news);        
	      mPager.setAdapter(new MyPagerAdapter(listViews));    
	      mPager.setCurrentItem(0);    
	      mPager.setOnPageChangeListener(new MyOnPageChangeListener());    
	       }



	

	   	private void pause() {
	   		
	   		//歌曲的暂停
	   		myService.puase();
	   		if(myService.mp.isPlaying()){// 代表歌曲是否正在播放
	   			s_s.setBackgroundResource(R.drawable.stop);
	   			Animation a=AnimationUtils.loadAnimation(this,R.anim.rotate);
		    	  imgc.setAnimation(a);
	   		}else{
	   			
		    	  s_s.setBackgroundResource(R.drawable.began);
		   			imgc.setAnimation(null);
	   		}
	   		
	   	
	   		
	   	}
	   	public void playbutton(){
	   		if(myService.mp.isPlaying()){// 代表歌曲是否正在播放
	   			s_s.setBackgroundResource(R.drawable.stop);
	   		}else{	
		    	  s_s.setBackgroundResource(R.drawable.began);
		   			imgc.setAnimation(null);
	   		}
	   	}
	   	

	       
	       @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    	// TODO Auto-generated method stub
	    	   super.onActivityResult(requestCode, resultCode, intent);
	   			if(intent.getExtras()!=null)
	   			{
	   				myService.mp=new MediaPlayer();
			 try {
				 myService.mp.setDataSource(MusicUtil.MusicFiles.get(myService.index).getUrl());
				 myService.mp.prepare();
             	pause();	

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
						imgb.setBackgroundResource(R.drawable.bottom);
						seekbar.setVisibility(View.VISIBLE);
						s_s.setVisibility(View.VISIBLE);
						rg.setVisibility(View.VISIBLE);
						tv_duraction.setVisibility(View.VISIBLE);
						tv_passtime.setVisibility(View.VISIBLE);
						tv_geci.setVisibility(View.VISIBLE);
						imgc.setVisibility(View.VISIBLE);
					}
				});
	       }
	       
	  ///新闻相关方法
	       public void serchnews(String url1){
	       	try {
	   			URL  url=new URL(url1);
	   			
	   			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	   			

	   			if(conn.getResponseCode()==200){
	   				
	   				InputStream in = conn.getInputStream();
	   				
	   					List<News> list2 = getList(in);
	   			
	   					
	   					Message msg=new Message();
	   				
	   					msg.obj=list2;
	   					
	   					
	   					newshandler.sendMessage(msg);
	   				
	   			}
	   			else{
	   				
	   			}
	   		} catch (MalformedURLException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		} catch (IOException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		} catch (XmlPullParserException e) {
	   			// TODO Auto-generated catch block
	   			e.printStackTrace();
	   		}
	       }
	       public List<News> getList(InputStream in) throws XmlPullParserException, IOException{
	    	   
	    	   XmlPullParserFactory factory  = XmlPullParserFactory.newInstance();
	    	   XmlPullParser parser = factory.newPullParser();
	    	   parser.setInput(in, "utf-8");
	    	   int eventType = parser.getEventType();
	    	   while(eventType!=XmlPullParser.END_DOCUMENT){
	    		   switch (eventType) {
	    		case XmlPullParser.START_DOCUMENT:
	    			list = new ArrayList<News>();
	    			break;
	    		case XmlPullParser.START_TAG:
	    			if("title".equals(parser.getName())){
	    				
	    				news=new News();
	    				String title = parser.nextText();
	    				
//	    				System.out.println(title+"是否执行了");
	    				
	    				news.setTitle(title);
	    				
	    			}else if("link".equals(parser.getName())){
	    				
	    				String link = parser.nextText();
	    				
	    				news.setLink(link);
	    				
	    			}else if("author".equals(parser.getName())){
	    				String author = parser.nextText();
	    				news.setAuthor(author);
	    			}else if("pubDate".equals(parser.getName())){
	    				String pubDate = parser.nextText();
	    				news.setDescription(pubDate);
	    				
	    			}else if("description".equals(parser.getName())){
	    				
	    				String description = parser.nextText();
	    				news.setDescription(description);
	    			}
	    			break;
	    		case XmlPullParser.END_TAG:
	    			if("item".equals(parser.getName())){
	    				list.add(news);
	    				news = null;
	    			}
	    			break;
	    		}
	    		   eventType = parser.next();
	    	   }
	    	  System.out.println(list.size()+"新闻的内容及数据大小");
	    	   return list;
	       }
	       
	       
	 
	       
	       
	       
	       
	       
	       
	       
	       
	       
	       
	       
	       
	/**   
	* ViewPager适配器   
	*/   
	public class MyPagerAdapter extends PagerAdapter {    
	public List<View> mListViews;    
	   
	public MyPagerAdapter(List<View> mListViews) {    
	this.mListViews = mListViews;    
	}    
	   
	@Override   
	public void destroyItem(View arg0, int arg1, Object arg2) {    
	((ViewPager) arg0).removeView(mListViews.get(arg1));    
	}    
	   
	@Override   
	public void finishUpdate(View arg0) {    
	}    
	   
	@Override   
	public int getCount() {    
	return mListViews.size();    
	}    
	   
	@Override   
	public Object instantiateItem(View arg0, int arg1) {    
	((ViewPager) arg0).addView(mListViews.get(arg1), 0);    
	return mListViews.get(arg1);    
	}    
	   
	@Override   
	public boolean isViewFromObject(View arg0, Object arg1) {    
	return arg0 == (arg1);    
	}    
	   
	@Override   
	public void restoreState(Parcelable arg0, ClassLoader arg1) {    
	}    
	   
	@Override   
	public Parcelable saveState() {    
	return null;    
	}    
	   
	@Override   
	public void startUpdate(View arg0) {    
	}    
	}  



	/**   
	* 初始化动画   
	*/   
	private void InitImageView() {    
	cursor = (ImageView) findViewById(R.id.imageView1);    
	bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)    
	.getWidth();// 获取图片宽度    
	DisplayMetrics dm = new DisplayMetrics();    
	getWindowManager().getDefaultDisplay().getMetrics(dm);    
	int screenW = dm.widthPixels;// 获取分辨率宽度    
	offset = (screenW / 2- bmpW) / 2;// 计算偏移量    
	Matrix matrix = new Matrix();    
	matrix.postTranslate(offset, 0);    
	cursor.setImageMatrix(matrix);// 设置动画初始位置    
	}  


	/**   
	* 页卡切换监听   
	*/   
	public class MyOnPageChangeListener implements OnPageChangeListener {    
	   
	// 页卡1 -> 页卡2 偏移量    
	@Override   
	public void onPageSelected(int arg0) { 
	int one = offset*2 + bmpW;
	Log.d("长度", one+"____");
	Animation animation = null;  
	Log.d("滑动界面", arg0+"____");
	switch (arg0) {  
	case 0:      
	animation = new TranslateAnimation(one+10,0+10, 0, 0);     
	break;       
	case 1:    
	animation = new TranslateAnimation(0, one+10, 0, 0);    
	break;    
	}       
	animation.setFillAfter(true);// True:图片停在动画结束位置    
	animation.setDuration(300);    
	cursor.startAnimation(animation);    
	}    
	   
	@Override   
	public void onPageScrolled(int arg0, float arg1, int arg2) {    
	}    
	   
	@Override   
	public void onPageScrollStateChanged(int arg0) {    
	}    
	}  
	
	
	
}
