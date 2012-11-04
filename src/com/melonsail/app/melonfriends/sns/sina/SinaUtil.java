package com.melonsail.app.melonfriends.sns.sina;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.melonsail.app.melonfriends.daos.FeedEntryDaos;
import com.melonsail.app.melonfriends.sns.SnsCallBackListener;
import com.melonsail.app.melonfriends.sns.SnsUtil;
import com.melonsail.app.melonfriends.sns.melon.FeedEntry;
import com.melonsail.app.melonfriends.utils.Const;
import com.melonsail.app.melonfriends.utils.Pref;
import com.weibo.net.AccessToken;
import com.weibo.net.AsyncWeiboRunner;
import com.weibo.net.DialogError;
import com.weibo.net.Oauth2AccessTokenHeader;
import com.weibo.net.Utility;
import com.weibo.net.WBStatus;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;


public class SinaUtil extends SnsUtil{

	private static final String TAG = "SinaUtil";

	
    //melonfriends
    private static final String CONSUMER_KEY = "194048236";
	private static final String CONSUMER_SECRET= "f224d6f3ee63132ab16459b59dfc9bdf";
	
	private Weibo zSina;
	private String access_token;
	
	private Activity zActivity;
	private Context zContext;
	
	//private NotificationTask notificationTask;
	
	public SinaUtil()
	{
		super(Const.SNS_SINA);
		access_token = Const.SNS_SINA + Const.SNS_TOKEN;
		zSina = Weibo.getInstance();
	}
	
	
	public SinaUtil(Activity zActivity) {
		this();
		this.zActivity = zActivity;
		this.feedDao = new FeedEntryDaos(zActivity);
	}
	
	public SinaUtil(Context zContext)
	{
		this();
		this.zContext = zContext;
		this.feedDao = new FeedEntryDaos(zContext);
	}
	
	@Override
	public boolean isSessionValid() {
		if ( zSina != null ) {
			return zSina.isSessionValid();
		}
		else
		{
			return false;
		}
	}
	
	//Variable Passing to Uri Call Back Listener
	
	//private SnsCallBackListener snsEventListener = null;
	//private boolean uptPref = false;
	
	@Override
	public void fAuth(final Activity zActivity) {
		//this.snsEventListener = snsEventListener;
		//this.uptPref = uptPref;
		
		this.setSinaToken(zActivity.getApplicationContext());
        this.setUpSinaClient();
        
        WeiboDialogListener listener = new WeiboDialogListener(){

			@Override
			public void onComplete(Bundle values) {

				String token = values.getString("access_token");
				String expires_in = values.getString("expires_in");
				saveToken2Pref(zActivity,token,CONSUMER_SECRET , expires_in);
				setSinaToken(zActivity.getApplication());
				fNotifyComplete(Const.MSG_SERVICE_LOGIN);
				
			}

			@Override
			public void onWeiboException(WeiboException e) {
				// TODO Auto-generated method stub
				Toast.makeText(zActivity,
						"Sina Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onError(DialogError e) {
				// TODO Auto-generated method stub
				Toast.makeText(zActivity,
						"Sina Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				Toast.makeText(zActivity, "Sina Auth cancel", Toast.LENGTH_LONG).show();
			}};

		zSina.authorize(zActivity, listener);
	}
	
	private void setUpSinaClient()
	{
		if(zSina == null) zSina = Weibo.getInstance();
		
		Utility.setAuthorization(new Oauth2AccessTokenHeader());
		zSina.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);
		zSina.setRedirectUrl("http://www.melonsail.com");
	}
	
	private void sendWeiboRequest(final Context context, AsyncWeiboRunner.RequestListener listener, String partUrl, String httpMethod, WeiboParameters params, String fileType)
	{
		this.setSinaToken(context);
		this.setUpSinaClient();
		if (isSessionValid()) {
			AsyncWeiboRunner asyncWeibo = new AsyncWeiboRunner(zSina);
			String url = Weibo.SERVER + partUrl;
			
			//if(fileType != null) this.startNotification(7, fileType);
			
			asyncWeibo.request(context, url, params, httpMethod, listener);
		}
	}
	
	@Override
	public void fGetNewsFeeds(final Context context) {


			AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

				@Override
				public void onComplete(String response) {
					Log.i(TAG, response);
					
					 //Save Feeds
					  WBHomeFeed bean = new Gson().fromJson(response, WBHomeFeed.class);
					  feedDao.fInsertFeed(bean);
					  //zPubSub.fGetFeedOrganisor().fSaveNewFeeds(bean.getStatuses(), context);
					  
					  //Bulk Fetch Comments
					  fGetBulkComments(bean.getStatuses(),context);
					  fNotifyComplete(Const.MSG_UI_RECEIVE_NEWFEED);
				}

				@Override
				public void onIOException(IOException e) {
					e.printStackTrace();
				}

				@Override
				public void onError(WeiboException e) {
					e.printStackTrace();
				}};
				
				
		        WeiboParameters bundle = new WeiboParameters();
		        bundle.add("source", Weibo.getAppKey());
				this.sendWeiboRequest(context, listener, "statuses/friends_timeline.json", "GET", bundle, null);
		
	}
	
	//public void fDisplaySinaFeed() {
//	@Override
//	public void fDisplayFeed(){
//		zPubSub.fGetActivity().runOnUiThread(new Runnable() {
//			public void run() {
//				
//				LstViewFeedAdapter feedAdapter = zPubSub.fGetAdapterFeedPreview();
//				feedAdapter.clear();
//				ArrayList<FeedEntry> feeds = zPubSub.fGetFeedOrganisor().fGetUnReadNewsFeed(Const.SNS_SINA);
//				for (FeedEntry item : feeds ) {
//					feedAdapter.addItem(item);
//				}
//				feedAdapter.notifyDataSetChanged();
//			}
//		});
//	}
	
	public void fGetBulkComments(List<WBStatus> statuses, final Context context)
	{
		
			AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

				@Override
				public void onComplete(String response) {
					Log.i(TAG, response);
					
					 //Save Feeds
					  WBHomeComment bean = new Gson().fromJson(response, WBHomeComment.class);
					  feedDao.fInsertComment(bean);
					
				}

				@Override
				public void onError(WeiboException e) {
					Log.i(TAG, e.getMessage());
					e.printStackTrace();
				}

				@Override
				public void onIOException(IOException e) {
					Log.i(TAG, e.getMessage());
					e.printStackTrace();
				}};
		
			
			for(WBStatus status : statuses)
			{
	          WeiboParameters bundle = new WeiboParameters();
	          bundle.add("source", Weibo.getAppKey());
	          bundle.add("id", status.getId().toString());
			  this.sendWeiboRequest(context, listener, "comments/show.json", "GET", bundle, null);
			}
			
	}
	
	
	@Override
	public void fPostComment(Bundle params, final Context context) {

		AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

					@Override
					public void onComplete(String response) {
						// TODO Auto-generated method stub
						Log.i(TAG, response);
						//stopNotification();
					}

					@Override
					public void onError(WeiboException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, e.getMessage(), 0);
					}

					@Override
					public void onIOException(IOException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, "Comments Update Failed. Please reauthenticate Weibo", 0);
					}};
					
			    WeiboParameters bundle = new WeiboParameters();
			    bundle.add("source", Weibo.getAppKey());
			    bundle.add("comment", params.getString(Const.COMMENTED_MSG));
			    bundle.add("id", params.getString(Const.SFEEDID));
			    
				this.sendWeiboRequest(context, listener, "comments/create.json", "POST", bundle, "Comment");
		
	}
	
	@Override
	public void fPublishFeeds(Bundle params, final Context context) {
		
		AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

					@Override
					public void onComplete(String response) {
						// TODO Auto-generated method stub
						Log.i(TAG, response);
						//stopNotification();
					}

					@Override
					public void onError(WeiboException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, e.getMessage(), 0);
					}

					@Override
					public void onIOException(IOException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, "Status Publish Failed. Please reauthenticate Weibo", 0);
					}};
					
			    WeiboParameters bundle = new WeiboParameters();
			    bundle.add("source", Weibo.getAppKey());
			    bundle.add("status", params.getString(Const.SMSGBODY));
				
				this.sendWeiboRequest(context, listener, "statuses/update.json", "POST", bundle, "Feed");
	}
	
	
	@Override
	public void fUploadPic(String message, String selectedImagePath, final Context context) {
		
		AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

					@Override
					public void onComplete(String response) {
						// TODO Auto-generated method stub
						Log.i(TAG, response);
						//stopNotification();
					}

					@Override
					public void onError(WeiboException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, "Picture Upload Failed. Please reauthenticate Weibo", 0);
					}

					@Override
					public void onIOException(IOException e) {
						e.printStackTrace();
						Log.i(TAG, e.getMessage());
						Toast.makeText(context, "Picture Upload Failed. Please reauthenticate Weibo", 0);
					}};
					
				
			    WeiboParameters bundle = new WeiboParameters();
			    bundle.add("source", Weibo.getAppKey());
			    bundle.add("status", message);
			    bundle.add("pic", selectedImagePath);
			    
				this.sendWeiboRequest(context, listener, "statuses/upload.json", "POST", bundle, "Picture");
	}
	
	
    
    @Override
    public void fLikeFeeds(Bundle params, final Context context) {
    	
    	AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				Log.i(TAG, response);
				//stopNotification();
			}

			@Override
			public void onError(WeiboException e) {
				e.printStackTrace();
				Log.i(TAG, e.getMessage());
				Toast.makeText(context, "Feed favoriate error. Please reauthenticate Weibo", 0);
			}

			@Override
			public void onIOException(IOException e) {
				e.printStackTrace();
				Log.i(TAG, e.getMessage());
				Toast.makeText(context, "Feed favoriate error. Please reauthenticate Weibo", 0);
			}};
			
			  WeiboParameters bundle = new WeiboParameters();
			  bundle.add("source", Weibo.getAppKey());
			  bundle.add("id", params.getString(Const.SFEEDID));
			    
			  this.sendWeiboRequest(context, listener, "favorites/create.json", "POST", bundle, null);
    
	}
    
	
	public void fUnLikeFeeds(Bundle params, final Context context) {
		
		AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				Log.i(TAG, response);
				//stopNotification();
			}

			@Override
			public void onError(WeiboException e) {
				e.printStackTrace();
				Log.i(TAG, e.getMessage());
				Toast.makeText(context, "Feed favoriate destroy error. Please reauthenticate Weibo", 0);
			}

			@Override
			public void onIOException(IOException e) {
				e.printStackTrace();
				Log.i(TAG, e.getMessage());
				Toast.makeText(context, "Feed favoriate destroy error. Please reauthenticate Weibo", 0);
			}};
		
		     WeiboParameters bundle = new WeiboParameters();
		     bundle.add("source", Weibo.getAppKey());
		     bundle.add("id", params.getString(Const.SFEEDID));
			    
			  this.sendWeiboRequest(context, listener, "favorites/destroy.json", "POST", bundle, null);
//		try {
//			zSina.destroyFavorite(Long.valueOf(params.getString(Const.SFEEDID)));
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (WeiboException e) {
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void fShareFeeds(Bundle params, final Context context) {
		
		AsyncWeiboRunner.RequestListener listener = new AsyncWeiboRunner.RequestListener(){

			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				Log.i(TAG, response);
				//stopNotification();
			}

			@Override
			public void onError(WeiboException e) {
				e.printStackTrace();
				Log.i(TAG, e.getMessage());
				Toast.makeText(context, "Feed Share error. Please reauthenticate Weibo", 0);
			}

			@Override
			public void onIOException(IOException e) {
				e.printStackTrace();
				Log.i(TAG, e.getMessage());
				Toast.makeText(context, "Feed Share error. Please reauthenticate Weibo", 0);
			}};
		
		 WeiboParameters bundle = new WeiboParameters();
	     bundle.add("source", Weibo.getAppKey());
	     bundle.add("id", params.getString(Const.SFEEDID));
	     this.sendWeiboRequest(context, listener, "statuses/repost.json", "POST", bundle, "Repost");
//		try {
//			zSina.retweetStatus(Long.valueOf(params.getString(Const.SFEEDID)));
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		} catch (WeiboException e) {
//			e.printStackTrace();
//		}
	}
	
	private void setSinaToken(Context context)
	{
		String sTokenKey = Pref.getMyStringPref(context, Const.SP_SINA_TOKENKEY);
		String sTokenSecret = Pref.getMyStringPref(context, Const.SP_SINA_TOKENSECRET);
		String sTokenExpire = Pref.getMyStringPref(context, Const.SP_SINA_TOKENEXPIRE);
		
		AccessToken accessToken = new AccessToken(sTokenKey, sTokenSecret);
		
		if(sTokenExpire.length() == 0) sTokenExpire = "0";
		accessToken.setExpiresIn(sTokenExpire);
		
		if(zSina == null) zSina = Weibo.getInstance();
		zSina.setAccessToken(accessToken);
		
	}
	
	private void saveToken2Pref(Context context,String tokenKey, String tokenSecret, String expiresIn)
	{
		Pref.setMyStringPref(context, Const.SP_SINA_TOKENKEY, tokenKey);
		Pref.setMyStringPref(context, Const.SP_SINA_TOKENSECRET, tokenSecret);
		Pref.setMyStringPref(context, Const.SP_SINA_TOKENEXPIRE, expiresIn);
	}
	
	
	@Override
	public void fNotifyComplete(int status) {
		for (SnsCallBackListener listener: listeners) {
			listener.onAsyncCallBack(snsName, status);
		}
	}


	@Override
	public SnsUtil fGetSnsInstance() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void fSetAccessToken(String token) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void fPostReply(Bundle params) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void fUnLikeFeeds(Bundle params) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void fLogout(Bundle params) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void fSaveAccessKeySP(Context context, String key) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void fDeleteAccessKeySP(Context context) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onComplete(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void fNotifyError() {
		// TODO Auto-generated method stub
		
	}
	
}
