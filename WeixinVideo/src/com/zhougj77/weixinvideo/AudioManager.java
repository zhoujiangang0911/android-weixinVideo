package com.zhougj77.weixinvideo;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;
import android.util.Log;

public class AudioManager {
	
	private MediaRecorder mMediaRecorder;
	private String mDir;
	private String mCurrentFilePath;
	
	private static AudioManager mInstance;
	
	private boolean isPrepare;
	
	private AudioManager(String dir){
		mDir = dir;
	}
	
	
	public interface AudioStateListene{
		void wellPrepared();
	}
	
	
	public AudioStateListene mListener;
	
	
	/**回调准备完毕
	 * @param listener
	 */
	public void setOnAudioStateListene(AudioStateListene listener){
		mListener = listener;
	}



	public static AudioManager getInstance(String dir){
		if (mInstance == null) {
			synchronized (AudioManager.class) {
				if (mInstance == null) {
					mInstance = new AudioManager(dir);
				}
			}
		}
		return mInstance;
	}
	
	
	/**
	 * 准备录音
	 */
	public void prepareAudio(){
		
		
		try {
			isPrepare = false;
			File dir = new File(mDir);
			if (dir.exists()) {
				dir.mkdirs();
			}
			String filename = generateFileName();
			File file = new File(dir,filename);
			mCurrentFilePath =file.getAbsolutePath();
			mMediaRecorder = new MediaRecorder();
			//设置输出文件
			mMediaRecorder.setOutputFile(file.getAbsolutePath());
			//设置MediaRecoder的音频远为麦克风
			mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//音频格式
			mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			//音频编码
			mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			//准备录音
			mMediaRecorder.prepare();
			Log.i("----zhoujg77","prepare" );
			//开始
			mMediaRecorder.start();
			Log.i("----zhoujg77","start" );
			//准备结束
			isPrepare = true;
			if (mListener!= null) {
				mListener.wellPrepared();
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**随机生成文件名
	 * @return
	 */
	private String generateFileName() {
		return UUID.randomUUID().toString()+".amr";
	}



	/**
	 * 获得音量等级
	 */
	public int  getVoiceLevel(int maxlevel){
		if (isPrepare) {
			try {
				//mMediaRecorder.getMaxAmplitude() 1~32767
				return maxlevel*mMediaRecorder.getMaxAmplitude()/32768+1;
			} catch (Exception e) {
			}
		}
		return 1;
	}
	
	
	/**
	 *释放资源 
	 */
	public void release(){
		//mMediaRecorder.stop();
		mMediaRecorder.reset();
		mMediaRecorder = null;
	}
	
	
	/**
	 * 取消录音
	 */
	public void cancel(){
		release();
		if (mCurrentFilePath!= null) {
			File file = new File(mCurrentFilePath);
			file.delete();
			mCurrentFilePath = null;
		}
		
	}



	public String getCurrentFilePath() {
		
		return mCurrentFilePath;
	}
}
