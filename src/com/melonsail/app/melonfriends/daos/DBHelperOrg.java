package com.melonsail.app.melonfriends.daos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeedEntry;
import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeedEntryComments.FBFeedEntryComment;
import com.melonsail.app.melonfriends.sns.facebook.FBHomeFeedEntryFrom;
import com.melonsail.app.melonfriends.utils.Const;

public class DBHelperOrg {

	static final String DATABASE_NAME = "melonfriend.db";
    static final int DATABASE_VERSION = 1;
    
    //query condition
    static final String ORDER_DESC = " DESC";
    static final String LIMIT = " LIMIT 10";
    
    // database tables
    static final String T_USER = "User";
    static final String T_FRIENDSLIST = "FriendsList";
    
    static final String T_FEED = "Feed";
    static final String T_LIKE = "Like";
    static final String T_COMMENT = "Comment";
    static final String T_TAG = "Tag";
    
    static final String T_PHOTO = "Photo";
    static final String T_ALBUM = "Album";
    
    static final String T_ERRORS = "Error";
    
    // {{ General keys for tables
    static final String C_KEY_ID = "id";
    static final String C_KEY_SNS = "SNS";
    static final String C_KEY_MELON = "melonkey";
    // }}
    
    // {{ region User 
    static final String C_USER_NAME = "name";
    static final String C_USER_SCREENAME = "screenname";
    static final String C_USER_PROFILEIMG = "profileimg";
    static final String C_USER_PROFILEIMG_L = "profileimg_large";
    
    static final String C_USER_LOCATION = "location";
    static final String C_USER_WEBSITE = "website";
    static final String C_USER_GENDER = "gender";
    static final String C_USER_DESCRIPTION = "description"; // personal quotes
    
    static final String C_USER_CNT_FOLLOWERS = "cnt_followers";
    static final String C_USER_CNT_BIFOLLOWERS = "cnt_bifollowers";
    static final String C_USER_CNT_FRIENDS = "cnt_friends";
    static final String C_USER_CNT_FAVORITES = "cnt_favorites";
    
    static final String C_USER_RELATIONSHIP = "relationship";
    // }}
    
    // {{ region FriendLists
    static final String C_FRIENDLIST_FRIENDID = "friendsid";
    static final String C_FRIENDLIST_RELATIONSHIP = "relationship";
    // }}

    // {{ region Feed
    static final String C_FEED_FROM_NAME = "feed_from_name";
    static final String C_FEED_FROM_ID = "feed_from_id";
    static final String C_FEED_FROM_IMG = "feed_from_img";
    static final String C_FEED_MSG = "msg"; //text
    static final String C_FEED_STORY = "story";
    
    static final String C_FEED_PIC = "pic";
    static final String C_FEED_PICL = "pic_large";
    static final String C_FEED_LINK = "link";
    static final String C_FEED_NAME = "name"; //name of the link
    static final String C_FEED_CAPTION = "caption"; //caption of the link
    static final String C_FEED_DESCRIPTION = "description"; //description of the link
    
    static final String C_FEED_SOURCE = "source"; //url to movie file
    static final String C_FEED_ICON = "icon"; // icon of the post type
    static final String C_FEED_ANNOTATION = "annotation"; // application info
    
    static final String C_FEED_TYPE = "type";
    
    static final String C_FEED_CREATED_TIME = "created_time";
    static final String C_FEED_UPDATED_TIME = "updated_time";
    
    static final String C_FEED_ISREAD = "isread";
    static final String C_FEED_ISLIKED = "isliked";
    static final String C_FEED_CNT_LIKE = "cnt_likes";
    static final String C_FEED_CNT_COMMENT = "cnt_comments";
    static final String C_FEED_CNT_SHARE = "cnt_share"; //repost
    
    // special to Sina and Twitter
    static final String C_FEED_REPLYTO_STATUS_ID = "feed_replyto_status_id";
    static final String C_FEED_REPLYTO_USER_ID = "feed_replyto_from_id";
    static final String C_FEED_REPLYTO_SCREENNAME = "feed_replyto_from_name";
    // }}
    
    // {{ region Comment
    static final String C_COMMENT_FEEDID = "feedid";
    static final String C_COMMENT_FROM_ID = "comment_from_id";
    static final String C_COMMENT_FROM_NAME = "comment_from_name";
    static final String C_COMMENT_FROM_IMG = "comment_from_img";
    
    static final String C_COMMENT_MSG = "msg";
    static final String C_COMMENT_CREATED_TIME = "created_time";
    
    static final String C_COMMENT_ISLIKED = "isliked";
    static final String C_COMMENT_CNT_LIKES = "cnt_likes";
    // }}
    
    // {{ region Tags
    static final String C_TAGS_ID = "id";
    static final String C_TAGS_FEEDID = "feedid";
    static final String C_TAGS_SNS = "sns";
    static final String C_TAGS_NAME = "name";
    static final String C_TAGS_OFFSET = "offset";
    static final String C_TAGS_LENTH = "length";
    static final String C_TAGS_TYPE = "type";
    // }}
    
    // {{ region Error
    static final String C_ERROR_MSG = "message";
    static final String C_ERROR_TIME = "created_time";
    static final String C_ERROR_SRC = "source";
    // }}
    
    // {{ region Create user table 
    static final String CREATE_USER_TABLE = "CREATE TABLE " + T_USER + " ("
										    + C_KEY_ID + " TEXT PRIMARY KEY,"
										    + C_KEY_SNS + " TEXT,"
										    + C_USER_NAME + " TEXT,"
										    + C_USER_PROFILEIMG + " TEXT,"
										    + C_USER_PROFILEIMG_L + " TEXT,"
										    + C_USER_LOCATION + " TEXT,"
										    + C_USER_WEBSITE + " TEXT,"
										    + C_USER_GENDER + " TEXT,"
										    + C_USER_DESCRIPTION + " TEXT,"
										    + C_USER_CNT_FOLLOWERS + " TEXT,"
										    + C_USER_CNT_BIFOLLOWERS + " TEXT,"
										    + C_USER_CNT_FRIENDS + " TEXT,"
										    + C_USER_CNT_FAVORITES + " TEXT,"
										    + C_USER_RELATIONSHIP + " TEXT"
										    + ");";
    
    static final String CREATE_FRIENDLIST_TABLE = "CREATE TABLE " + T_FRIENDSLIST + " ("
										    + C_KEY_ID + " TEXT PRIMARY KEY,"
										    + C_KEY_SNS + " TEXT,"
										    + C_FRIENDLIST_FRIENDID + " TEXT,"
										    + C_FRIENDLIST_RELATIONSHIP + " TEXT"
    										+ ");";
    // }}
    // {{ region Create feed & comment table 
    static final String CREATE_FEED_TABLE = "CREATE TABLE " + T_FEED + " ("
										    + C_KEY_ID + " TEXT PRIMARY KEY,"
										    + C_KEY_SNS + " TEXT,"
										    + C_FEED_FROM_NAME + " TEXT,"
										    + C_FEED_FROM_ID + " TEXT,"
										    + C_FEED_FROM_IMG + " TEXT,"
										    + C_FEED_MSG + " TEXT,"
										    + C_FEED_STORY + " TEXT,"
										    + C_FEED_PIC + " TEXT,"
										    + C_FEED_PICL + " TEXT,"
										    + C_FEED_LINK + " TEXT,"
										    + C_FEED_NAME + " TEXT,"
										    + C_FEED_CAPTION + " TEXT,"
										    + C_FEED_DESCRIPTION + " TEXT,"
										    + C_FEED_SOURCE + " TEXT,"
										    + C_FEED_ICON + " TEXT,"
										    + C_FEED_ANNOTATION + " TEXT,"
										    + C_FEED_TYPE + " TEXT,"
										    + C_FEED_ISREAD + " TEXT,"
										    + C_FEED_ISLIKED + " TEXT,"
										    + C_FEED_CNT_LIKE + " TEXT,"
										    + C_FEED_CNT_COMMENT + " TEXT,"
										    + C_FEED_CNT_SHARE + " TEXT,"
										    + C_FEED_REPLYTO_STATUS_ID + " TEXT,"
										    + C_FEED_REPLYTO_USER_ID + " TEXT,"
										    + C_FEED_REPLYTO_SCREENNAME + " TEXT,"
										    + C_FEED_CREATED_TIME + " TEXT,"
										    + C_FEED_UPDATED_TIME + " TEXT"
										    + ");";
    
    static final String CREATE_COMMENTS_TABLE = "CREATE TABLE " + T_COMMENT + " ("
										    + C_KEY_ID + " TEXT PRIMARY KEY,"
										    + C_KEY_SNS + " TEXT,"
										    + C_COMMENT_FEEDID + " TEXT,"
										    + C_COMMENT_FROM_ID + " TEXT,"
										    + C_COMMENT_FROM_NAME + " TEXT,"
										    + C_COMMENT_FROM_IMG + " TEXT,"
										    + C_COMMENT_MSG + " TEXT,"
										    + C_COMMENT_CREATED_TIME + " TEXT,"
										    + C_COMMENT_ISLIKED + " TEXT,"
										    + C_COMMENT_CNT_LIKES + " TEXT"
										    + ");";
    
    static final String CREATE_TAGS_TABLE = "CREATE TABLE " + T_TAG + " ("
										    + C_TAGS_ID + " TEXT PRIMARY KEY,"
										    + C_TAGS_SNS + " TEXT,"
										    + C_TAGS_FEEDID + " TEXT,"
										    + C_TAGS_NAME + " TEXT,"
										    + C_TAGS_TYPE + " TEXT,"
										    + C_TAGS_OFFSET + " TEXT,"
										    + C_TAGS_LENTH + " TEXT"
										    + ");";
    
    static final String CREATE_ERRORS_TABLE = "CREATE TABLE " + T_ERRORS + " ("
										    + C_ERROR_SRC + " TEXT PRIMARY KEY,"
										    + C_ERROR_MSG + " TEXT,"
										    + C_ERROR_TIME + " TEXT"
										    + ");";
    
    // }}
	
    private static final String TAG = "DBHelper";

    private static SimpleDateFormat simpleDateFormat;
    private static class DatabaseHelper extends SQLiteOpenHelper {
    	
		DatabaseHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
	    }

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_USER_TABLE);
			db.execSQL(CREATE_FEED_TABLE);
			db.execSQL(CREATE_COMMENTS_TABLE);
			//db.execSQL(CREATE_ACTIONS_TABLE);
			db.execSQL(CREATE_ERRORS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			
			// delete all tables, no backup here
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_USER_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_FEED_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_COMMENTS_TABLE);
			//db.execSQL("DROP TABLE IF EXISTS " + CREATE_ACTIONS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_ERRORS_TABLE);
			
			onCreate(db);
		}

	}

	private static DatabaseHelper mDatabaseHelper;
	private static SQLiteDatabase mSQLiteDB;

	public DBHelperOrg(Context context) {
		mDatabaseHelper = new DatabaseHelper(context);
		if (mSQLiteDB == null) {
			mSQLiteDB = mDatabaseHelper.getWritableDatabase();
		}
	}

	public void fCleanup() {
		if (mSQLiteDB != null) {
			mSQLiteDB.close();
			mSQLiteDB = null;
		}
	}
	
	public SQLiteDatabase fGetDB() {
		return mSQLiteDB;
	}
	public SimpleDateFormat fGetDateFormat() {
		return simpleDateFormat;
	}
	
	public String[] fGetColNammes(String tableName) {
		String[] result = null;
		Cursor cursor = mSQLiteDB.rawQuery(String.format("PRAGMA table_info(%s)", tableName), null);
		int rows = cursor.getCount();
		result = new String[rows];
		for (int i = 0; i < rows; ++i) {
			result[i] = cursor.getString(1);
		}
	    return result;
	}
	
	/**
	 * Insert facebook friends information
	 * TODO: Development more complete information by using graph API for friends information
	 * @param friend
	 * @return
	 */
	public long fInsertFriend(FBHomeFeedEntryFrom friend) {
		long ret = 0;
		
		ContentValues values  = new ContentValues();
		
		values.put(C_KEY_ID, friend.getId());
		values.put(C_USER_NAME, friend.getName());
		values.put(C_USER_PROFILEIMG, friend.getHeadurl());
		values.put(C_KEY_SNS, Const.SNS_FACEBOOK);
		
		String whereClause = C_KEY_ID + "=? and " + C_KEY_SNS + "=?";
		String[] selectArgs = new String[] {friend.getId(), Const.SNS_FACEBOOK};
		String[][] item = fGetItems(T_USER, whereClause, selectArgs, null);
		
		if (item != null ) {
			ret = mSQLiteDB.update(T_USER, values, whereClause, selectArgs);
		} else {
			ret = mSQLiteDB.insert(T_USER, null, values);
		}
		return ret;
	}

	/**
	 * Insert facebook feed
	 * TODO: combine with other social networks
	 * @param entry
	 * @return
	 */
	public long fInsertFeed(FBHomeFeedEntry entry) {
		// check if exist
		long ret = 0;
		ContentValues values  = new ContentValues();

		values.put(C_KEY_SNS, Const.SNS_FACEBOOK);
		values.put(C_FEED_ISREAD, "0");
		values.put(C_KEY_ID, entry.getId());
		values.put(C_FEED_MSG, entry.getMessage());
		values.put(C_FEED_STORY, entry.getStory());
		values.put(C_FEED_FROM_NAME, entry.getFrom().getName());
		values.put(C_FEED_FROM_ID, entry.getFrom().getId());
		values.put(C_FEED_PIC, entry.getPicture());
		values.put(C_FEED_PICL, entry.getRawPhoto());
		values.put(C_FEED_SOURCE, entry.getSource());
		values.put(C_FEED_LINK, entry.getLink());
		values.put(C_FEED_NAME, entry.getName());
		values.put(C_FEED_CAPTION, entry.getCaption());
		values.put(C_FEED_DESCRIPTION, entry.getDescription());
		values.put(C_FEED_ICON, entry.getIcon());
		values.put(C_FEED_TYPE, entry.getType());
		if (entry.getLikes() != null) {
			values.put(C_FEED_CNT_LIKE, entry.getLikes().getCount());
		} else {
			values.put(C_FEED_CNT_LIKE, 0);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+SSSS");
		try {
			Date dCreatedTime = sdf.parse(entry.getCreated_time());
			Date dUpdatedTime = sdf.parse(entry.getUpdated_time());
			values.put(C_FEED_CREATED_TIME, simpleDateFormat.format(dCreatedTime));
			values.put(C_FEED_UPDATED_TIME, simpleDateFormat.format(dUpdatedTime));
		} catch (ParseException e) {
			Log.w(TAG, "Unable to parse date string \"" + entry.getCreated_time() + "\"");
		}
		
		String whereClause = C_KEY_ID + "=? and " + C_KEY_SNS + "=?";
		String[] selectArgs = new String[] {entry.getId(), Const.SNS_FACEBOOK};
		String[][] item = fGetItems(T_FEED, whereClause, selectArgs, null);
		if (item != null && item.length > 0 ) {
			// keep the update time of the particular feed constant so it won't affect sorting
			values.put(C_FEED_UPDATED_TIME, item[0][18]);
			ret = mSQLiteDB.update(T_FEED, values, whereClause, selectArgs);
		} else {
			ret = mSQLiteDB.insert(T_FEED, null, values);
		}
		return ret;
	}
	
	public long fInsertComments(FBFeedEntryComment comment) {
		long ret = 0;
		
		ContentValues values  = new ContentValues();
		values.put(C_KEY_SNS, Const.SNS_FACEBOOK);
		values.put(C_KEY_ID, comment.getId());
		//comment_id = 564125882_10150574078615883_7136761 -> user id, feed id, comment id
		String comment_feedid = comment.getId().split("_")[1];
		values.put(C_COMMENT_FEEDID, comment_feedid);
		values.put(C_COMMENT_FROM_ID, comment.getFrom().getId());
		values.put(C_COMMENT_FROM_NAME, comment.getFrom().getName());
		values.put(C_COMMENT_FROM_IMG, comment.getFrom().getHeadurl());
		values.put(C_COMMENT_MSG, comment.getMessage());
		values.put(C_COMMENT_CREATED_TIME, comment.getCreated_time());	
		
		String whereClause = C_KEY_ID + "=? and " + C_KEY_SNS + "=?";
		String[] selectArgs = new String[] {comment.getId(), Const.SNS_FACEBOOK};
		String[][] item = fGetItems(T_COMMENT, whereClause, selectArgs, null);
		if (item  != null ) {
			ret = mSQLiteDB.update(T_COMMENT, values, whereClause, selectArgs);
		} else {
			ret = mSQLiteDB.insert(T_COMMENT, null, values);
		}
		
		return ret;
	}
	
	private String[][] fGetItems (String table, String where, String[] selectArgs, String orderby) {
		//String where = C_FEED_SNS + " = ? and " + C_FEED_ID + " = ?";
		//String[] selectionArgs = new String[] {sns, id};
		Cursor cursor = null;
		String[][] result = null;
		
		try {
			cursor = mSQLiteDB.query(table, null, where, selectArgs, null, null, orderby);
			cursor.moveToFirst();
			int rows = cursor.getCount();
			int cols = cursor.getColumnCount();
			result = new String[rows][cols];
			//cursor.moveToFirst();
			for (int i = 0; i < rows; ++i) {
				for (int j = 0; j < cols; ++j) {
					result[i][j] = cursor.getString(j);
				}
				cursor.moveToNext();
			}
		} catch (SQLException e) {
			Log.v(TAG, "Fail to get item by where = " + where, e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}
	
	public String[][] fGetAllItems( String table, String sns ) {
		String where = C_KEY_SNS + " = ? and " ;
		String[] selectArgs = new String[] {sns};
		return fGetItems(table, where, selectArgs, ORDER_DESC);
	}
	
	public String[][] fGetItemsDesc (String table, String sns, String where, String limit) {
		String where2 = C_KEY_SNS + " = ?";
		//protection on where and limit clause input
		where = (where == null) ? "": " and " + where;
		limit = (limit == null) ? "": limit;
		String[] selectArgs = new String[] {sns};
		return fGetItems(table, where2 + where, selectArgs, ORDER_DESC + limit);
	}

}
