package com.zhougj77.weixinvideo;

import java.util.List;

import com.zhougj77.weixinvideo.MainActivity.Recorder;

import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecoderAdapter extends ArrayAdapter<Recorder> {
	
	private int mMinItemWidth;
	private int mMaxItemWidth;
	private LayoutInflater mInflater;
	public RecoderAdapter(Context context,List<Recorder> datas) {
		super(context, -1,datas);
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mMaxItemWidth = (int) (outMetrics.widthPixels*0.7f);
		mMinItemWidth = (int) (outMetrics.widthPixels*0.15f);
		mInflater = LayoutInflater.from(context); 
	}
	
	private class ViewHolder{
		TextView seconds;
		View lenht;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView== null) {
			convertView = mInflater.inflate(R.layout.item_recoder,parent,false);
			holder = new ViewHolder();
			holder.seconds = (TextView) convertView.findViewById(R.id.id_recoder_time);
			holder.lenht =  convertView.findViewById(R.id.id_recoder_lenght);
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.seconds.setText(Math.round(getItem(position).time)+"\"");
		ViewGroup.LayoutParams lp = holder.lenht.getLayoutParams();
		lp.width = (int) (mMinItemWidth+(mMaxItemWidth/60f)*getItem(position).time);
		return convertView;
	}
	
	
	
	
}
