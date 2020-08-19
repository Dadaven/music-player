package com.example.ddvmusic;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView tv;
	private Button but;
	 Timer timer;
	 Handler hd;
	 int count=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if((getIntent().getFlags()&Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)!=0){
        	finish();
        	return;
        }
        ActionBar actionbar=getActionBar();
        actionbar.hide();
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //Í¸Ã÷×´Ì¬À¸
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //Í¸Ã÷µ¼º½À¸
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        tv=(TextView)findViewById(R.id.textView2);
        but=(Button)findViewById(R.id.button1);
        hd=new Handler();
        timer=new Timer();
timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				count--;
				
				if(count>0){
					
					
					hd.post(new Runnable() {
						
						@Override
						public void run() {
							
							tv.setText(count+"s");		
						}
					});
					
				}else{
					
					timer.cancel();
					Intent it=new Intent(MainActivity.this,MusicActivity.class);		
					
					startActivity(it);
					finish();
				}		
			}
		}, 1000, 1000);
        but.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				timer.cancel();
				Intent it=new Intent(MainActivity.this,MusicActivity.class);			
				startActivity(it);
				finish();
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            return rootView;
        }
    }

}
