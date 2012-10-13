package com.melonsail.app.melonfriends.sns;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.melonsail.app.melonfriends.daos.FeedEntryDaos;
import com.melonsail.app.melonfriends.sns.melon.FeedEntry;
import com.melonsail.app.melonfriends.utils.Const;
import com.melonsail.app.melonfriends.utils.Pref;

public abstract class SnsUtil {
	static final String TAG = "SnsUtil";
	protected String snsName;
	protected boolean isSelected;
	
	protected String APP_ID;
	protected String accessToken;
	
	protected ArrayList<SnsCallBackListener> listeners;
	protected FeedEntryDaos feedDao;
	
	public abstract SnsUtil fGetSnsInstance();
	public abstract boolean isSessionValid();
	public abstract void fAuth (Activity activity);
	public abstract void fSetAccessToken(String token);
	
	public abstract void fGetNewsFeeds(Context context);
	//public abstract void fDisplayNewsFeeds(Activity activiy);
	
	public abstract void fPublishFeeds(Bundle params);
	public abstract void fUploadPic(String message, String selectedImagePath);
	
	public abstract void fPostComment(Bundle params);
	public abstract void fPostReply(Bundle params);
	public abstract void fLikeFeeds(Bundle params);
	public abstract void fUnLikeFeeds(Bundle params);
	
	public abstract void fShareFeeds(Bundle params);
	
	public abstract void fLogout(Bundle params);
	public abstract void fSaveAccessKeySP(Context context, String key);
	public abstract void fDeleteAccessKeySP(Context context);
	
	public abstract void onComplete(int requestCode, int resultCode, Intent data);
	public abstract void fNotifyComplete(int status);
	public abstract void fNotifyError();
	
	public SnsUtil(String snsName) {
		this.snsName = snsName;
		this.isSelected = false;
		listeners = new ArrayList<SnsCallBackListener>();
	}
	
	public String fGetSNSName() {
		return this.snsName;
	}
	
	public boolean fIsActive() {
		return isSelected;
	}
	public void fSetActive() {
		isSelected = true;
	}
	public void fSetInactive() {
		isSelected = false;
	}

	public void addListeners(SnsCallBackListener listener) {
		this.listeners.add(listener);
	}
	public void removeListeners(SnsCallBackListener listener) {
		this.listeners.remove(listener);
	}
	
	public ArrayList<FeedEntry> fDisplayFeeds(Context context, String snsName) {
		ArrayList<FeedEntry> feedList = feedDao.fGetLastest10FeedEntries(snsName);
		FeedEntry lastItem = null;
		
		fSaveLastLoadedFeed(lastItem, context, snsName);
		return feedList;
	}
	
	private void fSaveLastLoadedFeed(FeedEntry lastItem, Context context, String snsName) {
		// TODO Auto-generated method stub
		Pref.setMyStringPref(context, snsName, lastItem.getsUpdatedTime());
	}
	
}
