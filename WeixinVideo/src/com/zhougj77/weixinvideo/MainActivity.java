package com.zhougj77.weixinvideo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zhougj77.weixinvideo.AudioRecorderButton.AudioFinshRecoderListener;


public class MainActivity extends Activity {
	private ListView mListView;
	
	private ArrayAdapter<Recorder> mAdapter;
	private List<Recorder> mDatas = new ArrayList<MainActivity.Recorder>();
	
	private AudioRecorderButton mAudioRecorderButton;
	
	private View animView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mListView = (ListView) findViewById(R.id.lv_videolist);
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.mybt_viode);
        
        mAudioRecorderButton.setAudioFinshRecoderListener(new AudioFinshRecoderListener() {
			
			@Override
			public void onFinish(float seconds, String path) {
					Recorder recorder = new Recorder(seconds, path);
					mDatas.add(recorder);
					mAdapter.notifyDataSetChanged();
					mListView.setSelection(mDatas.size()-1);
			}
		});
        
        mAdapter = new RecoderAdapter(this,mDatas);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				//播放动画（帧动画）
				if (animView !=null) {
					animView.setBackgroundResource(R.drawable.adj);
					animView = null;
				}
				 animView = view.findViewById(R.id.id_recoder_anim);
				animView.setBackgroundResource(R.drawable.play_anim);
				AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
				animation.start();
				//播放录音
				MediaManager.playSound(mDatas.get(position).filePath,new MediaPlayer.OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						animView.setBackgroundResource(R.drawable.adj);
					}
				});
				
			}
		});
    }

    
    @Override
    protected void onPause() {
    	super.onPause();
    	MediaManager.pause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	MediaManager.resume();
    }
    
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	MediaManager.release();
    }
    class Recorder{
    	float time;
    	String filePath;
		public Recorder(float time, String filePath) {
			super();
			this.time = time;
			this.filePath = filePath;
		}
		public float getTime() {
			return time;
		}
		public void setTime(float time) {
			this.time = time;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
    	
    	
    }

}
