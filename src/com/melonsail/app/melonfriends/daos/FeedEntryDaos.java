package com.melonsail.app.melonfriends.daos;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeed;
import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeedEntry;
import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeedEntryComments;
import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeedEntryComments.FBHomeFeedEntryComment;
import com.melonsail.app.melonfriends.sns.melon.FeedEntry;
import com.melonsail.app.melonfriends.sns.melon.FeedEntryComment;
import com.melonsail.app.melonfriends.utils.Const;
import com.melonsail.app.melonfriends.utils.Pref;

public class FeedEntryDaos {
	private static final String TAG = "FeedEntryDaos";
	
	private Activity mActivity;
	private Context mContext;
	
	private DBHelper mDBHelper;
	
	private String[] sSQLInputParams = {"\'Facebook\'", "123456"};
	private String sSQLScript;
	
	public FeedEntryDaos(Context context) {
		this.mContext = context;
		
		mDBHelper = new DBHelper(context);
		
		//for testing purpose only
		sSQLScript = XMLSQLParser.fGetSQLScript(context, "sSelectUserBySNS");
		Log.i(TAG, "Script: " + sSQLScript);
	}

	//{{ deprecated DB methods
	/**
	 * Insert feed into db from JSON reply
	 * Insert feed related users and comments as well
	 * @param entries
	 * @deprecated
	 */
	public void fInsertFeed(FBHomeFeed entries) {
		if(entries != null && entries.getData()!=null) {
			Log.i(TAG, "FB get single feed from list#" + entries.getData().size());
			for(int i= 0; i<entries.getData().size();i++) {
				FBHomeFeedEntry entry = (FBHomeFeedEntry) entries.getData().get(i);
				//String fromHeadUrl = "https://graph.facebook.com/" + fromID + "/picture";
				
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
				try {
					FBHomeFeedEntryComments comments = entry.getComments();
					Log.i(TAG, "FB insert comment #:" + comments.getCount());
					if (comments != null && Integer.parseInt(comments.getCount()) > 0) {
						for (FBHomeFeedEntryComment comment : comments.getData()) {
							if (comment != null ) {
								Log.i(TAG, "FB insert comment from:" + comment.getFrom().getName());
								mDBHelper.fInsertComments(comment);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Retrieve all available feed for sns
	 * Not recommended, as it consumes a lot of memory due to number feeds in each sns
	 * @param sns
	 * @return list of feed entries
	 * @deprecated
	 */
	public ArrayList<FeedEntry> fGetAll(String sns) {
		ArrayList<FeedEntry> entries = new ArrayList<FeedEntry>();
		//no where condition, no limit
		String[][] result = mDBHelper.fGetItemsDesc(DBHelper.T_FEED, sns, null, null);
		for (int i= 0; i < result.length; i++) {
			FeedEntry entry = new FeedEntry();
			fTransformValue2Object(result[i], entry);
			entries.add(entry);
		}
		return entries;
	}
	
	/**
	 * Retrieve a specific feed from sns
	 * This is generally used for DetailView
	 * @param sns
	 * @param feedid
	 * @return FeedEntry
	 * @deprecated
	 */
	public FeedEntry fGetFeedByID(String sns, String feedid) {
		FeedEntry entry = new FeedEntry();
		String where = String.format("%s" + "=%s", DBHelper.C_KEY_ID, feedid);
		// only 1 feed required, so no limit required
		String[][] result = mDBHelper.fGetItemsDesc(DBHelper.T_FEED, sns, where, null);
		for (int i= 0; i < result.length; i++) {
			fTransformValue2Object(result[i], entry);
		}
		return entry;
	}
	
	/**
	 * Retrieve most recent 10 feed
	 * This is normally used when user force refresh, or newly login
	 * @param sns
	 * @return list of 10 feed entries in updatetime desc order
	 * @deprecated
	 */
	public ArrayList<FeedEntry> fGetLastest10FeedEntries(String sns) {
		ArrayList<FeedEntry> entries = null;
		// retrieve 10 feeds from now
		String from = Const.simpleDateFormat.format(new Date());
		entries = fGet10FeedEntries(sns, from);
		return entries;
	}
	
	/**
	 * Retrieve 10 feeds from current, or previous reading position
	 * Position is recorded by the feed update time stored in SharedPreference.
	 * @param sns
	 * @return list of 10 feed entries in updatetime desc order
	 * @deprecated
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
	 * @deprecated
	 */
	private ArrayList<FeedEntry> fGet10FeedEntries(String sns, String from) {
		ArrayList<FeedEntry> entries = new ArrayList<FeedEntry>();
		String where = String.format("%s" + "<= %s", DBHelper.C_FEED_UPDATED_TIME, from);
		String[][] result = mDBHelper.fGetItemsDesc(DBHelper.T_FEED, sns, where, DBHelper.LIMIT);
		for (int i= 0; i < result.length; i++) {
			FeedEntry entry = new FeedEntry();
			fTransformValue2Object(result[i], entry);
			entries.add(entry);
		}
		return entries;
	}
	
	/**
	 * Retrieve feed newer than feed last read by users
	 * @param sns
	 * @param from
	 * @return
	 * @deprecated
	 */
	public ArrayList<FeedEntry> fGetToReadFeedEntries(String sns, String from) {
		ArrayList<FeedEntry> entries = new ArrayList<FeedEntry>();
		String where = String.format("%s" + ">= %s", DBHelper.C_FEED_UPDATED_TIME, from);
		String[][] result = mDBHelper.fGetItemsDesc(DBHelper.T_FEED, sns, where, null);
		for (int i= 0; i < result.length; i++) {
			FeedEntry entry = new FeedEntry();
			fTransformValue2Object(result[i], entry);
			entries.add(entry);
		}
		return entries;
	}
	
	/**
	 * Retrieve feed has not been read yet.
	 * For notification purpose
	 * @param sns
	 * @return
	 * @deprecated
	 */
	public ArrayList<FeedEntry> fGetUnreadFeedCount(String sns) {
		ArrayList<FeedEntry> entries = null;
		
		
		return entries;
	}
	// }}
	
	// {{ data vs object conversion
	/**
	 * Map DB values to Feed Objects
	 * @param value
	 * @return
	 */
	private void fTransformValue2Object(String[] value, FeedEntry entry) {
		//FeedEntry entry = new FeedEntry();
		int index = 0;
		
		entry.setsID(value[index++]);
		entry.setsSNS(value[index++]);							//sns
		entry.setsOwnerName(value[index++]);					//feed owner name
		entry.setsOwnerID(value[index++]);						//feed owner id
		entry.setsOwnerImg(value[index++]);						//feed owner img
		entry.setsMsg(value[index++]);							//message
		entry.setsStory(value[index++]);						//story
		
		entry.setsPhotoUrl(value[index++]);						//pic url
		entry.setsPhotoUrl_L(value[index++]);					//raw photo url
		
		entry.setsLink(value[index++]);							//links
		entry.setsPhotoName(value[index++]);					//pic/album name
		entry.setsPhotoCaption(value[index++]);					//pic/album caption
		entry.setsPhotoDescription(value[index++]);				//pic/album description
		
		entry.setsIcon(value[index++]);							//icon
		entry.setsSource(value[index++]);						//source
		entry.setsAnnotation(value[index++]);					//annotation
		entry.setsFeedType(value[index++]);						//feedtype
		
		entry.setsIsRead(value[index++]);						//is_read
		entry.setsIsLiked(value[index++]);						//is_liked
		entry.setsCountLikes(value[index++]);					// #likes
		entry.setsCountComments(value[index++]);				// #comments
		entry.setsCountShares(value[index++]);					// #shares
		
		entry.setsFeed_Replyto_Status_ID(value[index++]);
		entry.setsFeed_Replyto_From_ID(value[index++]);
		entry.setsFeed_Replyto_From_Name(value[index++]);
		
		entry.setsCreatedTime(value[index++]);					//created time
		entry.setsUpdatedTime(value[index++]);					//updated time
		
		//return entry;
	}

	private void fTransformValue2Object(String[] value, FeedEntryComment comment) {
		//FeedEntryComment comment = new FeedEntryComment();
		
		int index = 0;
		comment.setCommentedID(value[index++]);
		comment.setSns(value[index++]);							//sns
		
		comment.setCommentedfeedID(value[index++]);				//feed id
		
		comment.setCommentedUserID(value[index++]);				//comment owner id
		comment.setCommentedName(value[index++]);					//comment owner name
		comment.setCommentedHeadUrl(value[index++]);				//comment owner url
		
		comment.setCommentedMsg(value[index++]);						//message
		comment.setCommentedSource(value[index++]);						//source
		comment.setCommentedTime(value[index++]);						//comment time
		
		comment.setCommentedIsLiked(value[index++]);				//isLiked
		comment.setCommentedCntLikes(value[index++]);				//cntLikes
		//return comment;
	}
	
	private String[] fTransformObject2Value(FBHomeFeedEntry entry, String table) {
		String[] colNames = mDBHelper.fGetColNames(table);
		String[] params = new String[colNames.length - 3];
		String sParamPattern = "%s";
		
		int i = 0;
		params[i++] = String.format(sParamPattern, entry.getId() );
		params[i++] = String.format(sParamPattern, Const.SNS_FACEBOOK);
		params[i++] = String.format(sParamPattern, entry.getFrom().getId());
		params[i++] = String.format(sParamPattern, entry.getFrom().getName() );
		params[i++] = String.format(sParamPattern, entry.getFrom().getHeadurl() );
		params[i++] = String.format(sParamPattern, entry.getMessage() );
		params[i++] = String.format(sParamPattern, entry.getStory() );
		params[i++] = String.format(sParamPattern, entry.getPicture() );
		params[i++] = String.format(sParamPattern, entry.getRawPhoto() );
		params[i++] = String.format(sParamPattern, entry.getLink() );
		params[i++] = String.format(sParamPattern, entry.getName() );
		params[i++] = String.format(sParamPattern, entry.getCaption() );
		params[i++] = String.format(sParamPattern, entry.getDescription() );
		params[i++] = String.format(sParamPattern, entry.getSource() );
		params[i++] = String.format(sParamPattern, entry.getIcon() );
		params[i++] = String.format(sParamPattern, entry.getApplication().getName()); //entry.getAnnotation();
		params[i++] = String.format(sParamPattern, entry.getType() );
		Log.i(TAG, "type i = " + i); // i = 17
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS");
		try {
			Date dCreatedTime = sdf.parse(entry.getCreated_time());
			Date dUpdatedTime = sdf.parse(entry.getUpdated_time());
			params[i++] = String.format(sParamPattern, Const.simpleDateFormat.format(dCreatedTime) );
			params[i++] = String.format(sParamPattern, Const.simpleDateFormat.format(dUpdatedTime) );
		} catch (Exception e) {
			Log.w(TAG, "Unable to parse date string \"" + entry.getCreated_time() + "\"");
		}
		Log.i(TAG, "updatetime i = " + i); // i= 19
		params[i++] = String.format(sParamPattern, "0" /*isread ??*/ );
		params[i++] = String.format(sParamPattern, "0" /*islike ??*/ );
		try {
			params[i++] = String.format(sParamPattern, entry.getComments().getCount() );
		} catch (NullPointerException e) {
			params[i - 1] = String.format(sParamPattern, "0");
			Log.w(TAG, "No comment, i: " + i); //i = 22
		}
		try {
			params[i++] = String.format(sParamPattern, entry.getLikes().getCount() );
		} catch (NullPointerException e) {
			params[i - 1] = String.format(sParamPattern, "0");
			Log.w(TAG, "No like, i: " + i); // i = 23
		}
		params[i++] = String.format(sParamPattern, "0" /*Facebook do not have share count*/);

		return params;
	}
	
	private String[] fTransformObject2Value(FBHomeFeedEntryComment comment, String table, String commentedFeedID) {
		String[] colNames = mDBHelper.fGetColNames(table);
		String[] params = new String[colNames.length - 3];
		String sParamPattern = "%s";
		
		int i = 0;
		params[i++] = String.format(sParamPattern, comment.getId() );
		params[i++] = String.format(sParamPattern, Const.SNS_FACEBOOK);
		
		params[i++] = String.format(sParamPattern, commentedFeedID);
		
		params[i++] = String.format(sParamPattern, comment.getFrom().getId());
		params[i++] = String.format(sParamPattern, comment.getFrom().getName() );
		params[i++] = String.format(sParamPattern, comment.getFrom().getHeadurl() );
		
		params[i++] = String.format(sParamPattern, comment.getMessage() );
		params[i++] = String.format(sParamPattern, "" ); //comment_annotation /application/source
		params[i++] = String.format(sParamPattern, comment.getCreated_time() );
		params[i++] = String.format(sParamPattern, comment.getUser_likes() ); //comment_is_like
		params[i++] = String.format(sParamPattern, comment.getLikes() ); // comment_cnt_likes
		return params;
	}
	// }}

	// {{ retrieve additional feed info
	private String fGetFacebookRawPic(String picUrl) {
		String picRawUrl = null;
		if ( picUrl != null ) {
			String response = "";
			Bundle mBundle = new Bundle();
			String sToken = Pref.getMyStringPref(mContext, Const.SNS_FACEBOOK + Const.SNS_TOKEN);
			mBundle.putString("access_token", sToken);
			String url = "https://graph.facebook.com/" + picUrl.split("_")[1];
			try {
				response = com.facebook.android.Util.openUrl(url, "GET", mBundle);
				JSONObject picSrcResponse = new JSONObject(response);
				picRawUrl = picSrcResponse.getString("source");
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return picRawUrl;
	}
	
	private void fRetrieveAdditionalFeedInfo(FBHomeFeedEntry entry) {
		
		//retrieve large photo if feed type is photo
		if (entry.getType().equals("photo")) {
			entry.setRawPhoto( fGetFacebookRawPic(entry.getPicture()) );					
		}
	}
	// }}
	
	/**
	 * Insert feed into db from JSON reply
	 * Insert feed related comments as well
	 * @param entries
	 */
	public void fInsertFeed(FBHomeFeed entries, String temp) {

		int i = 0;
		int j = 0;
		try {
			if(entries != null && entries.getData()!=null) {
				//Log.i(TAG, "FB get single feed from list#" + entries.getData().size());
				
				for( i= 0; i<entries.getData().size();i++) {
					FBHomeFeedEntry entry = (FBHomeFeedEntry) entries.getData().get(i);
					fRetrieveAdditionalFeedInfo(entry);
					
					//insert feed
					//Log.i(TAG, "FB insert feed");
					sSQLInputParams = fTransformObject2Value(entry, DBHelper.T_FEED);
					sSQLScript = XMLSQLParser.fGetSQLScript(mContext, "sqlInsertFeed");
					mDBHelper.fExec(sSQLScript, sSQLInputParams);
					
					//insert comments
					FBHomeFeedEntryComments comments = entry.getComments();
					try {
						if ( comments != null && comments.getData() != null) {
							Log.i(TAG, "FB insert comment #:" + comments.getCount());
							for(j= 0; j<comments.getData().size();j++) {
								FBHomeFeedEntryComment comment = comments.getData().get(j);
								sSQLInputParams = fTransformObject2Value(comment, DBHelper.T_COMMENT, entry.getId());
								sSQLScript = XMLSQLParser.fGetSQLScript(mContext, "sqlInsertComment");
								mDBHelper.fExec(sSQLScript, sSQLInputParams);
							}
						}
					} catch (Exception e) {
						Log.e(TAG, "Error inserting comment " + j);
					}
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "Error inserting feed " + i);
		}
	}
	
	/**
	 * Retrieve the specific feed
	 * @param sns
	 * @param feedid
	 * @return
	 */
	public FeedEntry fGetFeedByID(String sns, String feedid, String temp) {
		FeedEntry entry = new FeedEntry();
		// only 1 feed required, so no limit required
		Log.i(TAG, "Get feed from " + sns + "feedid " + feedid);
		String sParamPattern = "%s";
		ArrayList<String> params = new ArrayList<String>();
		params.add(String.format(sParamPattern, sns));  //sns = sns
		params.add(String.format(sParamPattern, feedid)); //limit 10
		sSQLInputParams = (String[]) params.toArray();
		sSQLScript = XMLSQLParser.fGetSQLScript(mContext, "sqlSelectFeedByIDSNS");
		
		String[][] result = mDBHelper.fExec(sSQLScript, sSQLInputParams);
		
		for (int i= 0; i < result.length; i++) {
			fTransformValue2Object(result[i], entry);
		}
		return entry;
	}

	/**
	 * Retrieve multiple feed entries
	 * Sort by update time desc
	 * @param sns
	 * @param from
	 * @return
	 */
	public ArrayList<FeedEntry> fGetFeedEntries(String queryName, String[] params) {
		ArrayList<FeedEntry> entries = new ArrayList<FeedEntry>();
		
		Log.i(TAG, "Get multiple feeds: " + queryName);
		sSQLScript = XMLSQLParser.fGetSQLScript(mContext, queryName);
		
		// execute scripts line by line
		String[] queries = sSQLScript.split(";");
		String[][] result = null;
		for (String query : queries) {
			if (query.contains("?")) {
				result = mDBHelper.fExec(query, params);
			} else {
				result = mDBHelper.fExec(query, null);
			}
			
		}
		
		for (int i= 0; i < result.length; i++) {
			FeedEntry entry = new FeedEntry();
			fTransformValue2Object(result[i], entry);
			entries.add(entry);
		}
		
		return entries;
	}
}
