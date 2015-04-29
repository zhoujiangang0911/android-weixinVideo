package com.zhougj77.weixinvideo;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

public class DialogManager {
	
	private Dialog mDialog;
	
	private ImageView mIcon;
	private ImageView mVideo;
	
	private Context mContext;

	public DialogManager(Context context) {

		mContext = context;
	}
	
	public void showRecordingDialog(){
		mDialog = new Dialog(mContext,R.style.Theme_AudioDialog);
	}
	
	public void wantToCancel(){
		
	}
	
	
	public void tooShort(){
		
		
	}
	
	public void dismissDialog(){
		
		
	}
	
	public void updateVoiceLevel(int level){
		
	}
	
}
