package com.example.ddvmusic;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Newsmore_Activity extends Activity {

	String title;
	String url;
	WebView wv;
	TextView tv;
	ImageView img;
	Animation a;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsmore_);

		ActionBar actionbar=getActionBar();
        actionbar.hide();
        tv=(TextView)findViewById(R.id.textView1);
        wv=(WebView)findViewById(R.id.webView1);
        img=(ImageView)findViewById(R.id.imageView1);
        wv.getSettings().setJavaScriptEnabled(true);
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        url=intent.getStringExtra("url");
        tv.setText(title);
        wv.loadUrl(url);
        a=AnimationUtils.loadAnimation(this,R.anim.loadrotate);
        wv.setWebViewClient(new WebViewClient(){
        	@Override
        	public void onPageStarted(WebView view, String url, Bitmap favicon) {
        		// TODO Auto-generated method stub
        		super.onPageStarted(view, url, favicon);
        		img.setVisibility(view.VISIBLE);		
		    	  img.setAnimation(a);
        	}
        	@Override
        	public void onPageFinished(WebView view, String url) {
        		// TODO Auto-generated method stub
        		super.onPageFinished(view, url);
        		img.setAnimation(null);
        		img.setVisibility(view.GONE);
        	}
        	@Override
        	public void onReceivedError(WebView view, int errorCode,
        			String description, String failingUrl) {
        		// TODO Auto-generated method stub
        		super.onReceivedError(view, errorCode, description, failingUrl);
        		Toast.makeText(Newsmore_Activity.this,"º”‘ÿ ß∞‹",Toast.LENGTH_SHORT).show();
        	}
        });

	}


}
