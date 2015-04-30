package com.zhougj77.weixinvideo;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogManager {
	
	private Dialog mDialog;
	
	private ImageView mIcon;
	private ImageView mVideo;
	private TextView mLable;
	private Context mContext;

	public DialogManager(Context context) {

		mContext = context;
	}
	
	public void showRecordingDialog(){
		mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_recorder,null);
		mDialog.setContentView(view);
		mIcon = (ImageView) mDialog.findViewById(R.id.image_a);
		mVideo = (ImageView) mDialog.findViewById(R.id.image_b);
		mLable = (TextView) mDialog.findViewById(R.id.tv_dialogcontent);
		mDialog.show();
	}
	
	
	public void recoding(){
		if (mDialog != null&& mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVideo.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);
			
			mIcon.setImageResource(R.drawable.recorder);
			mLable.setText("手指上滑，取消发送");
			
		}
		
	}
	
	
	public void wantToCancel(){
		if (mDialog != null&& mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVideo.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
			mIcon.setImageResource(R.drawable.cancel);
			mLable.setText("松开手指，取消发送");
			
			
		}
	}
	
	public void tooShort(){
		if (mDialog != null&& mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVideo.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
			
			mIcon.setImageResource(R.drawable.voice_to_short);
			mLable.setText("录音时间过短");
		}
		
	}
	
	public void dismissDialog(){
		if (mDialog != null&& mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog =null;
		}
		
	}
	
	/**
	 * 录音的等级
	 * @param level
	 */
	public void updateVoiceLevel(int level){
		if (mDialog != null&& mDialog.isShowing()) {
//			mIcon.setVisibility(View.VISIBLE);
//			mVideo.setVisibility(View.VISIBLE);
//			mLable.setVisibility(View.VISIBLE);
			
			//根据资源ID动态加载图片
			int resid = mContext.getResources().getIdentifier("v"+level, "drawable", mContext.getPackageName());
			mVideo.setImageResource(resid);
			//mVideo.setImageResource(R.drawable.call_interface_hands_free);
		
		}
		
	}
	
}
