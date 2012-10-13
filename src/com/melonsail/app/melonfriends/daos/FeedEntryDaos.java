package com.melonsail.app.melonfriends.daos;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeed;
import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeedEntry;
import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeedEntryComments;
import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeedEntryComments.FBFeedEntryComment;
import com.melonsail.app.melonfriends.sns.melon.FeedEntry;
import com.melonsail.app.melonfriends.utils.Const;
import com.melonsail.app.melonfriends.utils.Pref;

public class FeedEntryDaos {
	private static final String TAG = "FeedEntryDaos";
	
	private Activity mActivity;
	private Context mContext;
	
	private DBHelper mDBHelper;
	
	public FeedEntryDaos(Context context) {
		this.mContext = context;
		
		mDBHelper = new DBHelper(context);
	}
	/**
	 * Insert feed into db from JSON reply
	 * Insert feed related users and comments as well
	 * @param entries
	 */
	public void fInsertFeed(FBHomeFeed entries) {
		if(entries != null && entries.getData()!=null) {
			Log.i(TAG, "FB get single feed from list#" + entries.getData().size());
			for(int i= 0; i<entries.getData().size();i++) {
				FBHomeFeedEntry entry = (FBHomeFeedEntry) entries.getData().get(i);
				//String fromHeadUrl = "https://graph.facebook.com/" + fromID + "/picture";
				String fromHeadUrl = String.format(Const.USER_IMG_URL_FB, entry.getFrom().getId());
				entry.getFrom().setHeadurl(fromHeadUrl);
				
				if (entry.getType().equals("photo")) {
					//retrieve original photo source					
				}
				
				//insert feed
				Log.i(TAG, "FB insert feed");
				mDBHelper.fInsertFeed(entry);
				//insert friends
				// to be enhanced with more complete friends' info with seperate graph request
				Log.i(TAG, "FB insert friend");
				mDBHelper.fInsertFriend(entry.getFrom());
				
				//insert comments
				FBHomeFeedEntryComments comments = entry.getComments();
				Log.i(TAG, "FB insert comment #:" + comments.getCount());
				if (comments != null && Integer.parseInt(comments.getCount()) > 0) {
					for (FBFeedEntryComment comment : comments.getData()) {
						if (comment != null ) {
							Log.i(TAG, "FB insert comment from:" + comment.getFrom().getName());
							mDBHelper.fInsertComments(comment);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Retrieve all available feed for sns
	 * Not recommended, as it consumes a lot of memory due to number feeds in each sns
	 * @param sns
	 * @return list of feed entries
	 */
	public ArrayList<FeedEntry> fGetAll(String sns) {
		ArrayList<FeedEntry> entries = new ArrayList<FeedEntry>();
		//no where condition, no limit
		String[][] result = mDBHelper.fGetItemsDesc(DBHelper.T_FEED, sns, null, null);
		for (int i= 0; i < result.length; i++) {
			entries.add(fTransformValueToObject(result[i]));
		}
		return entries;
	}
	
	/**
	 * Retrieve a specific feed from sns
	 * This is generally used for DetailView
	 * @param sns
	 * @param feedid
	 * @return FeedEntry
	 */
	public FeedEntry fGetFeedByID(String sns, String feedid) {
		FeedEntry entry = null;
		String where = String.format("%s" + "=%s", DBHelper.C_FEED_ID, feedid);
		// only 1 feed required, so no limit required
		String[][] result = mDBHelper.fGetItemsDesc(DBHelper.T_FEED, sns, where, null);
		for (int i= 0; i < result.length; i++) {
			entry = fTransformValueToObject(result[i]);
		}
		return entry;
	}
	
	/**
	 * Retrieve most recent 10 feed
	 * This is normally used when user force refresh, or newly login
	 * @param sns
	 * @return list of 10 feed entries in updatetime desc order
	 */
	public ArrayList<FeedEntry> fGetLastest10FeedEntries(String sns) {
		ArrayList<FeedEntry> entries = null;
		// retrieve 10 feeds from now
		String from = mDBHelper.fGetDateFormat().format(new Date());
		entries = fGet10FeedEntries(sns, from);
		return entries;
	}
	
	/**
	 * Retrieve 10 feeds from current, or previous reading position
	 * Position is recorded by the feed update time stored in SharedPreference.
	 * @param sns
	 * @return list of 10 feed entries in updatetime desc order
	 */
	public ArrayList<FeedEntry> fGetNext10FeedEntries(String sns) {
		ArrayList<FeedEntry> entries = null;
		//update time for different sns stored in SP
		String from = Pref.getMyStringPref(mContext, sns + Const.SNS_READITEM_UPDATETIME);
		entries = fGet10FeedEntries(sns, from);
		return entries;
	}
	
	/**
	 * TODO: transform value to objects
	 * @param sns
	 * @param from
	 * @return
	 */
	private ArrayList<FeedEntry> fGet10FeedEntries(String sns, String from) {
		ArrayList<FeedEntry> entries = new ArrayList<FeedEntry>();
		String where = String.format("%s" + "<= %s", DBHelper.C_FEED_UPDATED_TIME, from);
		String[][] result = mDBHelper.fGetItemsDesc(DBHelper.T_FEED, sns, where, DBHelper.LIMIT);
		for (int i= 0; i < result.length; i++) {
			entries.add(fTransformValueToObject(result[i]));
		}
		return entries;
	}
	
	/**
	 * Retrieve feed newer than feed last read by users
	 * @param sns
	 * @param from
	 * @return
	 */
	public ArrayList<FeedEntry> fGetToReadFeedEntries(String sns, String from) {
		ArrayList<FeedEntry> entries = new ArrayList<FeedEntry>();
		String where = String.format("%s" + ">= %s", DBHelper.C_FEED_UPDATED_TIME, from);
		String[][] result = mDBHelper.fGetItemsDesc(DBHelper.T_FEED, sns, where, null);
		for (int i= 0; i < result.length; i++) {
			entries.add(fTransformValueToObject(result[i]));
		}
		return entries;
	}
	
	/**
	 * Retrieve feed has not been read yet.
	 * For notification purpose
	 * @param sns
	 * @return
	 */
	public ArrayList<FeedEntry> fGetUnreadFeedCount(String sns) {
		ArrayList<FeedEntry> entries = null;
		
		
		return entries;
	}
	
	/**
	 * Map DB values to Feed Objects
	 * @param value
	 * @return
	 */
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
		
		entry.setsIcon(value[index++]);							//icon
		entry.setsFeedType(value[index++]);						//feedtype
		entry.setsIsRead(value[index++]);						//is_read status
		entry.setsCntLikes(value[index++]);						// how many likes
		
		entry.setsCreatedTime(value[index++]);					//created time
		entry.setsUpdatedTime(value[index++]);					//created time
		
		return entry;
	}

}
