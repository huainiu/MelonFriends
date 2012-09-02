package com.melonsail.app.melonfriends.daos;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeedEntry;
import com.melonsail.app.melonfriends.sns.melon.FeedEntry;

public class FeedEntryDaos {
	private Activity mActivity;
	private Context mContext;
	
	private DBHelper mDBHelper;
	
	public FeedEntryDaos(Context context) {
		this.mContext = context;
		
		mDBHelper = new DBHelper(context);
	}
	
	public ArrayList<FeedEntry> fGetAll() {
		ArrayList<FeedEntry> entries = null;
		
		return entries;
	}
	
	public FeedEntry fGetFeedByID(String sns, String feedid) {
		FeedEntry entry = null;
		String where = String.format("%s" + "=%s", DBHelper.C_FEED_ID, feedid);
		String[][] result = mDBHelper.fGetItemsCondition(DBHelper.T_FEED, sns, where);
		
		return entry;
	}
	
	public ArrayList<FBHomeFeedEntry> fGet10FeedEntries(String sns, String from) {
		ArrayList<FBHomeFeedEntry> entries = null;
		
		
		return entries;
	}
	
	public ArrayList<FeedEntry> fGetUnreadFeed(String sns) {
		ArrayList<FeedEntry> entries = null;
		
		
		return entries;
	}
	
	private FeedEntry fTransformValueToObject(String[] value) {
		FeedEntry entry = new FeedEntry();
		int index = 0;
		
		entry.setsID(value[index++]);
		entry.setsSNS(value[index++]);							//name
		entry.setsName(value[index++]);							//name
		entry.setsOwnerID(value[index++]);						//feed owner id
		entry.setsMsgBody(value[index++]);						//message
		entry.setsStory(value[index++]);						//story
		
		entry.setsPhotoPreviewLink(value[index++]);				//pic url
		entry.setsPhotoLargeLink(value[index++]);				//raw photo url
		entry.setsSource(value[index++]);						//source
		entry.setsLink(value[index++]);							//links
		
		entry.setsPhotoPreviewName(value[index++]);				//pic/album name
		entry.setsPhotoPreviewCaption(value[index++]);			//pic/album caption
		entry.setsPhotoPreviewDescription(value[index++]);		//pic/album description
		
		entry.setsIcon(value[index++]);
		entry.setsType(value[index++]);
		entry.setsIsRead(value[index++]);
		entry.setsCntLikes(value[index++]);
		
		entry.setsCreatedTime(value[index++]);					//created time
		entry.setsUpdatedTime(value[index++]);					//created time
		entry.setsFeedType(value[index++]);
		
		return entry;
	}

}
