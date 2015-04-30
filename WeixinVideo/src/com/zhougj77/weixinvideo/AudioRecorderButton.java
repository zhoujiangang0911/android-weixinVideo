package com.zhougj77.weixinvideo;

import com.zhougj77.weixinvideo.AudioManager.AudioStateListene;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AudioRecorderButton extends Button implements AudioStateListene {
	private static final int STATE_NORMAL = 1;
	private static final int STATE_RECORDING = 2;
	private static final int STATE_WANT_TO_CANCEL = 3;
	
	private static final int DISTANCE_Y_CANCEL = 50;
	
	private int mCurState = STATE_NORMAL;
	//已经开始录音
	private boolean isRecoding = false;
	
	private DialogManager mDialogManager ;
	
	private AudioManager mAudioManager;
	
	
	private float mTime;
	//是否触发longClick
	private boolean mReady;
	
	
	public AudioRecorderButton(Context context) {
		this(context, null);
		
	}

	public AudioRecorderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDialogManager = new DialogManager(getContext());
		
		String dir = Environment.getExternalStorageDirectory()+"/weixinvideoasd";
		
		mAudioManager = AudioManager.getInstance(dir);
		mAudioManager.setOnAudioStateListene(this);
		
		
		setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				mReady = true;
				mAudioManager.prepareAudio();
				return false;
			}
		});
		
		
		
	}
	
	/**
	 * @author zhoujg77
	 *	录音完成后的回调
	 */
	public interface AudioFinshRecoderListener{
		void onFinish(float seconds,String path);
	}
	
	private AudioFinshRecoderListener mListener;
	
	public void setAudioFinshRecoderListener(AudioFinshRecoderListener lister){
		mListener = lister;
	}
	
	
	
	
	
	
	
	
	/*
	 * 獲取音量大小的線程
	 */
	private Runnable mGetVoiceLevelRunnable = new Runnable() {
		
		@Override
		public void run() {
			while (isRecoding) {
				try {
					
					Thread.sleep(100);
					mTime += 0.1f;
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	};
	private static final int MSG_AUDIO_PREPARED = 0x110;
	private static final int MSG_VOICE_CHANGED = 0x111;
	private static final int MSG_DIALOG_DIMISS = 0x112;
	private Handler  mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_AUDIO_PREPARED:
				//显示對話框在开始录音以后
				mDialogManager.showRecordingDialog();
				isRecoding = true;
				new Thread(mGetVoiceLevelRunnable).start();
				break;
			case MSG_VOICE_CHANGED:
				mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));	
				
				break;
			case MSG_DIALOG_DIMISS:
				mDialogManager.dismissDialog();
				break;

			default:
				break;
			}
			
			
			super.handleMessage(msg);
		}
	};
	
	//huidiao
	@Override
	public void wellPrepared() {
		mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Toast.makeText(getContext(), "11",Toast.LENGTH_LONG).show();
			changeState(STATE_RECORDING);
			break;
		case MotionEvent.ACTION_MOVE:
			if (isRecoding) {
				//如果想要取消录音，根据X，y的坐标判断
				if (wantToCancel(x,y)) {
					changeState(STATE_WANT_TO_CANCEL);
				}else{
					changeState(STATE_RECORDING);
				}

			}
			
			break;
		case MotionEvent.ACTION_UP:
			if (!mReady) {
				reset();
				return super.onTouchEvent(event);
			}
			if (!isRecoding||mTime < 0.6f){
				mDialogManager.tooShort();
				mAudioManager.cancel();
				mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS,1300);//延迟显示对话框
			}
			else if (mCurState == STATE_RECORDING) {//正常录制结束
				//释放录音资源 通知Activity
				mDialogManager.dismissDialog();
			
				mAudioManager.release();
				if (mListener != null) {
					mListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
				}
			}else if (mCurState == STATE_WANT_TO_CANCEL){
				//取消
				mDialogManager.dismissDialog();
				mAudioManager.cancel();
			}
			reset();
			break;
		default:
			break;
		}

		return super.onTouchEvent(event);

	}
	//恢复状态 标志位
	private void reset() {
		isRecoding = false;
		mReady = false;
		changeState(STATE_NORMAL);
		mTime = 0;
	}

	private boolean wantToCancel(int x, int y) {
		if (x<0||x>getWidth()) {
			return true;
		}
		
		
		if (y<-DISTANCE_Y_CANCEL||y>(getHeight()+DISTANCE_Y_CANCEL)) {
			return true;
		}
		return false;
	}

	private void changeState(int state) {
		if (mCurState != state) {
			mCurState = state;
			
			switch (state) {
			case STATE_NORMAL:
				setBackgroundResource(R.drawable.btn_recoder_normal);
				setText(R.string.str_recoder_normal);
				mDialogManager.showRecordingDialog();
				break;
			case STATE_RECORDING:
				setBackgroundResource(R.drawable.btn_recoding);
				setText(R.string.str_recoder_recoding);
				if (isRecoding) {
					//更新duihuakuang
					mDialogManager.recoding();
				}
				break;
			case STATE_WANT_TO_CANCEL:
				setBackgroundResource(R.drawable.btn_recoding);
				setText(R.string.str_recoder_want_cancel);
				mDialogManager.wantToCancel();
				break;
			default:
				break;
			}
		}
		
		
	}

	

}
