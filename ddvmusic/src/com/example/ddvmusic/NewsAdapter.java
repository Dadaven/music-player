package com.example.ddvmusic;

import java.util.List;

import com.example.entity.News;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	
	private List<News> list;
	private Context context;
    public NewsAdapter(List<News> list , Context context) {
		// TODO Auto-generated constructor stub
    	this.list = list;
    	this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView == null){
			convertView = View.inflate(context, R.layout.newsitem, null);
			holder = new ViewHolder();
			holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
			holder.tv_describle = (TextView)convertView.findViewById(R.id.tv_description);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_title.setText(list.get(position).getTitle());
		holder.tv_describle.setText(list.get(position).getDescription());
		return convertView;
	}
    static class ViewHolder{
    	TextView tv_title;
    	TextView tv_describle;
    }
}
