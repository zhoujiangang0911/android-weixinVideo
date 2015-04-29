package com.zhougj77.weixinvideo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class AudioRecorderButton extends Button {
	private static final int STATE_NORMAL = 1;
	private static final int STATE_RECORDING = 2;
	private static final int STATE_WANT_TO_CANCEL = 3;
	
	private static final int DISTANCE_Y_CANCEL = 50;
	
	private int mCurState = STATE_NORMAL;
	//已经开始录音
	private boolean isRecoding = false;
	
	
	

	public AudioRecorderButton(Context context) {
		this(context, null);
	}

	public AudioRecorderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			//TODO
			isRecoding = true;
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
			if (mCurState == STATE_RECORDING) {
				//释放录音资源 通知Activity
			}else if (mCurState == STATE_WANT_TO_CANCEL){
				//取消
				
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
		changeState(STATE_NORMAL);
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
				break;
			case STATE_RECORDING:
				setBackgroundResource(R.drawable.btn_recoding);
				setText(R.string.str_recoder_recoding);
				if (isRecoding) {
					//更新duihuakuang
				}
				break;
			case STATE_WANT_TO_CANCEL:
				setBackgroundResource(R.drawable.btn_recoding);
				setText(R.string.str_recoder_want_cancel);
				break;
			default:
				break;
			}
		}
		
		
	}

}
