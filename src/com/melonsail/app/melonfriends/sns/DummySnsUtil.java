package com.melonsail.app.melonfriends.sns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DummySnsUtil extends SnsUtil {

	public DummySnsUtil(String snsName) {
		super(snsName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SnsUtil fGetSnsInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSessionValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void fAuth(Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fGetNewsFeeds(Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fPublishFeeds(Bundle params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fUploadPic(String message, String selectedImagePath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fPostComment(Bundle params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fPostReply(Bundle params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fLikeFeeds(Bundle params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fUnLikeFeeds(Bundle params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fShareFeeds(Bundle params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fLogout(Bundle params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fNotifyComplete(int status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fNotifyError() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fDeleteAccessKeySP(Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fSaveAccessKeySP(Context context, String key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fSetAccessToken(String token) {
		// TODO Auto-generated method stub
		
	}

}
