package com.melonsail.app.melonfriends.sns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class DummySnsUtil extends SnsUtil {

	public DummySnsUtil(String snsName) {
		super(snsName);
	}

	@Override
	public SnsUtil fGetSnsInstance() {
		return null;
	}

	@Override
	public boolean isSessionValid() {
		return false;
	}

	@Override
	public void fAuth(Activity activity) {
		
	}

	@Override
	public void fGetNewsFeeds(Context context) {
		
	}

	@Override
	public void fPublishFeeds(Bundle params) {
		
	}

	@Override
	public void fUploadPic(String message, String selectedImagePath) {
		
	}

	@Override
	public void fPostComment(Bundle params) {
		
	}

	@Override
	public void fPostReply(Bundle params) {
		
	}

	@Override
	public void fLikeFeeds(Bundle params) {
		
	}

	@Override
	public void fUnLikeFeeds(Bundle params) {
		
	}

	@Override
	public void fShareFeeds(Bundle params) {
		
	}

	@Override
	public void fLogout(Bundle params) {
		
	}

	@Override
	public void onComplete(int requestCode, int resultCode, Intent data) {
		
	}

	@Override
	public void fNotifyComplete(int status) {
		
	}

	@Override
	public void fNotifyError() {
		
	}

	@Override
	public void fDeleteAccessKeySP(Context context) {
		
	}

	@Override
	public void fSaveAccessKeySP(Context context, String key) {
		
	}

	@Override
	public void fSetAccessToken(String token) {
		
	}

}
