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
import com.melonsail.app.melonfriends.utils.Const;

public class DBHelper {

	static final String DATABASE_NAME = "friendhost_feed.db";
    static final String NEWSFEED_TABLE_NAME = "newsfeed";
    static final int DATABASE_VERSION = 1;
    
    //query condition
    static final String ORDER_DESC = " DESC";
    static final String LIMIT = " LIMIT 10";
    
    //sns network type
    static final String SNS_FACEBOOK = "Facebook";
    static final String SNS_RENREN = "Renren";
    static final String SNS_SINA = "Sina";
    static final String SNS_TWITTER = "Twitter";

    // database tables
    static final String T_USER = "User";
    static final String T_FEED = "Feed";
    static final String T_COMMENTS = "Comments";
    static final String T_ACTIONS = "Actions";
    static final String T_TAGS = "Tags";
    static final String T_ERRORS = "Errors";
    
    // User Columns
    static final String C_USER_ID = "id";
    static final String C_USER_SNS = "SNS";
    static final String C_USER_NAME = "name";
    static final String C_USER_HEADURL = "headurl";

    // Feed Columns
    static final String C_FEED_ID = "id";
    static final String C_FEED_FROM = "feedFrom";
    static final String C_FEED_OWNER_ID = "feed_owner_id";
    static final String C_FEED_SNS = "SNS";
    static final String C_FEED_MSG = "msg";
    static final String C_FEED_STORY = "story";
    static final String C_FEED_PIC = "pic";
    static final String C_FEED_RAW_PIC = "raw_pic";
    static final String C_FEED_SOURCE = "source";
    static final String C_FEED_LINK = "link";
    static final String C_FEED_NAME = "name";
    static final String C_FEED_CAPTION = "caption";
    static final String C_FEED_DESCRIPTION = "description";
    static final String C_FEED_ICON = "icon";
    static final String C_FEED_TYPE = "type";
    static final String C_FEED_CNT_LIKE = "cntlike";
    static final String C_FEED_ISREAD = "isread";
    static final String C_FEED_CREATED_TIME = "created_time";
    static final String C_FEED_UPDATED_TIME = "updated_time";
    static final String C_FEED_ISLIKED = "0";
    
    // Comments Columns
    static final String C_COMMENTS_ID = "id";
    static final String C_COMMENTS_SNS = "SNS";
    static final String C_COMMENTS_FEEDID = "feedid";
    static final String C_COMMENTS_USERID = "comment_userid";
    static final String C_COMMENTS_USERNAME = "comment_username";
    static final String C_COMMENTS_USERHEADURL = "comment_userheadurl";
    static final String C_COMMENTS_MSG = "msg";
    static final String C_COMMENTS_CREATED_TIME = "created_time";
    
    // Action Columns
    static final String C_ACTIONS_FEEDID = "feedid";
    static final String C_ACTIONS_NAME = "name";
    static final String C_ACTIONS_LINK = "link";
    
    // Error DB
    static final String C_ERROR_MSG = "message";
    static final String C_ERROR_TIME = "created_time";
    static final String C_ERROR_SRC = "source";
    
    // Create table SQL statement
    static final String CREATE_USER_TABLE = "CREATE TABLE " + T_USER + " ("
										    + C_USER_ID + " TEXT PRIMARY KEY,"
										    + C_USER_SNS + " TEXT,"
										    + C_USER_NAME + " TEXT,"
										    + C_USER_HEADURL + " TEXT"
										    + ");";
    
    static final String CREATE_FEED_TABLE = "CREATE TABLE " + T_FEED + " ("
										    + C_FEED_ID + " TEXT PRIMARY KEY,"
										    + C_FEED_SNS + " TEXT,"
										    + C_FEED_FROM + " TEXT,"
										    + C_FEED_OWNER_ID + " TEXT,"
										    + C_FEED_MSG + " TEXT,"
										    + C_FEED_STORY + " TEXT,"
										    + C_FEED_PIC + " TEXT,"
										    + C_FEED_RAW_PIC + " TEXT,"
										    + C_FEED_SOURCE + " TEXT,"
										    + C_FEED_LINK + " TEXT,"
										    + C_FEED_NAME + " TEXT,"
										    + C_FEED_CAPTION + " TEXT,"
										    + C_FEED_DESCRIPTION + " TEXT,"
										    + C_FEED_ICON + " TEXT,"
										    + C_FEED_TYPE + " TEXT,"
										    + C_FEED_ISREAD + " TEXT,"
										    + C_FEED_CNT_LIKE + " TEXT,"
										    + C_FEED_CREATED_TIME + " TEXT,"
										    + C_FEED_UPDATED_TIME + " TEXT"
										    + C_FEED_ISLIKED + " TEXT"
										    + ");";
    
    static final String CREATE_COMMENTS_TABLE = "CREATE TABLE " + T_COMMENTS + " ("
										    + C_COMMENTS_ID + " TEXT PRIMARY KEY,"
										    + C_COMMENTS_SNS + " TEXT,"
										    + C_COMMENTS_FEEDID + " TEXT,"
										    + C_COMMENTS_USERID + " TEXT,"
										    + C_COMMENTS_USERNAME + " TEXT,"
										    + C_COMMENTS_USERHEADURL + " TEXT,"
										    + C_COMMENTS_MSG + " TEXT,"
										    + C_COMMENTS_CREATED_TIME + " TEXT"
										    + ");";
    
    static final String CREATE_ACTIONS_TABLE = "CREATE TABLE " + T_ACTIONS + " ("
										    + C_ACTIONS_FEEDID + " TEXT PRIMARY KEY,"
										    + C_ACTIONS_NAME + " TEXT,"
										    + C_ACTIONS_LINK + " TEXT"
										    + ");";
    
    static final String CREATE_ERRORS_TABLE = "CREATE TABLE " + T_ERRORS + " ("
										    + C_ERROR_SRC + " TEXT PRIMARY KEY,"
										    + C_ERROR_MSG + " TEXT,"
										    + C_ERROR_TIME + " TEXT"
										    + ");";
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
			db.execSQL(CREATE_ACTIONS_TABLE);
			db.execSQL(CREATE_ERRORS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
			
			// delete all tables, no backup here
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_USER_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_FEED_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_COMMENTS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_ACTIONS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CREATE_ERRORS_TABLE);
			
			onCreate(db);
		}

	}

	private static DatabaseHelper mDatabaseHelper;
	private static SQLiteDatabase mSQLiteDB;

	public DBHelper(Context context) {
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

	public long fInsertFeed(FBHomeFeedEntry entry) {
		// check if exist
		long ret = 0;
		
		ContentValues values  = new ContentValues();

		values.put(C_FEED_SNS, SNS_FACEBOOK);
		values.put(C_FEED_ISREAD, "0");
		values.put(C_FEED_ID, entry.getId());
		values.put(C_FEED_MSG, entry.getMessage());
		values.put(C_FEED_STORY, entry.getStory());
		values.put(C_FEED_FROM, entry.getFrom().getName());
		values.put(C_FEED_OWNER_ID, entry.getFrom().getId());
		values.put(C_FEED_PIC, entry.getPicture());
		values.put(C_FEED_RAW_PIC, entry.getRawPhoto());
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
		
		if (fIfItemExist(entry.getId(), Const.SNS_FACEBOOK, T_FEED)) {
			ret = mSQLiteDB.update(T_FEED, values, C_FEED_ID + "?", new String[] {entry.getId()});
		} else {
			ret = mSQLiteDB.insert(T_FEED, null, values);
		}
		return ret;
	}
	
	private boolean fIfItemExist(String id, String sns, String table) {
		String item = fGetItemByID(id, sns, table);
		if (item != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public String fGetItemByID (String id, String sns, String table) {
		String where = C_FEED_SNS + " = ? and " 
						+ C_FEED_ID + " = ?";
		String[] selectionArgs = new String[] {sns, id};
		Cursor cursor = null;
		String result = null;
		
		try {
			cursor = mSQLiteDB.query(table, null, where, selectionArgs, null, null, null);
			cursor.moveToFirst();
			if (cursor.getCount() > 0 ) {
				result = cursor.getString(0) +" : "+ cursor.getString(1);
			}
		} catch (SQLException e) {
			Log.v(TAG, "Get all birthday failed.", e);
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return result;
	}

}