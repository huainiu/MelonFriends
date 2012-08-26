package com.melonsail.app.melonfriends.sns.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.google.gson.Gson;
import com.melonsail.app.melonfriends.sns.SnsCallBackListener;
import com.melonsail.app.melonfriends.sns.SnsUtil;
import com.melonsail.app.melonfriends.utils.Const;
import com.melonsail.app.melonfriends.utils.Pref;

public class FacebookUtil extends SnsUtil {
	private static final String TAG = "FacebookUtil";
	
	//private static final String APP_ID = "158943034230205";
	
	private static final String[] PERMISSIONS = new String[] {"publish_stream", "read_stream", "user_photos", "friends_photos" };
	//private static final String FBTOKEN = "fbToken";
	//private static final String FBTOKENEXPIRES = "fbAccessExpires";

	private Facebook zFacebook;
	private static AsyncFacebookRunner asyncFB;
	
	protected Activity mActivity;
	protected Context mContext;
	
	public FacebookUtil(Activity activity) {
		this();
		this.mActivity = activity;
	}
	
	public FacebookUtil(Context context) {
		this();
		this.mContext = context;
	}
	
	public FacebookUtil() {
		super(Const.SNS_FACEBOOK);
		APP_ID = "158943034230205";
		accessToken = Const.SNS_FACEBOOK + Const.SNS_TOKEN;
		if (zFacebook == null) {
			zFacebook = new Facebook(APP_ID);
		}
	}
	
	@Override
	public SnsUtil fGetSnsInstance() {
		return null;
	}
	
	@Override
	public boolean isSessionValid() {
		if (zFacebook != null) {
			return zFacebook.isSessionValid();
		} else {
			return false;
		}
	}
	
	@Override
	public void fAuth(final Activity activity) {
		zFacebook.authorize(activity, PERMISSIONS,
			new DialogListener() {
				@Override
				public void onComplete(Bundle values) {
					Log.d(TAG, "Facebook.authorize Complete: ");
					fSaveAccessKeySP(activity.getApplicationContext(), zFacebook.getAccessToken());
					fNotifyComplete(Const.MSG_SERVICE_LOGIN);
				}
				@Override
				public void onFacebookError(FacebookError error) {
					Log.d(TAG, "Facebook.authorize Error: " + error.toString());
				}
				@Override
				public void onError(DialogError e) {
					Log.d(TAG, "Facebook.authorize DialogError: " + e.toString());
				}
				@Override
				public void onCancel() {
					Log.d(TAG, "Facebook authorization canceled");
				}
			}
		);
	}
	
	@Override
	public void fSetAccessToken(String token) {
		zFacebook.setAccessToken(token);
	}
	
	@Override
	public void fSaveAccessKeySP(Context context, String key) {
		Pref.setMyStringPref(context, accessToken, key);
	}
	
	@Override
	public void fDeleteAccessKeySP(Context context) {
		Pref.setMyStringPref(context, accessToken, "");
	}
	
	public void onComplete(int requestCode, int resultCode, Intent data) {
		if (zFacebook != null) {
			zFacebook.authorizeCallback(requestCode, resultCode, data);
		}
	}
	
	public AsyncFacebookRunner fGetAsyncFacebook() {
		if (asyncFB == null) {
			asyncFB = new AsyncFacebookRunner(zFacebook);
		}
		return asyncFB;
	}
	
	@Override
	public void fGetNewsFeeds(final Context context) {
		if (isSessionValid()) {
			String sToken = Pref.getMyStringPref(context, accessToken);
			Log.i(TAG, "FacebookUtil.fGetNewsFeeds: accessToke = " + sToken);
			Bundle data = new Bundle();
			data.putString(Facebook.TOKEN, sToken);
			
			RequestListener listener = new RequestListener() {
				@Override
				public void onComplete(final String response, Object state) {
					Log.i(TAG, "FacebookUtil.fGetNewsFeeds: reponse = " + response);
					if (response.contains("error")) {
						fDeleteAccessKeySP(context);
						Log.i(TAG, "FB response contains error");
						fNotifyComplete(Const.MSG_UI_RECEIVE_NEWFEED_ERROR);
					} else {
						FBHomeFeed bean = new Gson().fromJson(response, FBHomeFeed.class);
						// store in db
						fNotifyComplete(Const.MSG_UI_RECEIVE_NEWFEED);
					}
				}

				@Override
				public void onIOException(IOException e, Object state) {
					System.out.println("onIOException: " + e.getMessage());
				}

				@Override
				public void onFileNotFoundException(FileNotFoundException e,Object state) {
					System.out.println("onFileNotFoundException: " + e.getMessage());
				}

				@Override
				public void onMalformedURLException(MalformedURLException e,Object state) {
					System.out.println("onMalformedURLException: " + e.getMessage());
				}

				@Override
				public void onFacebookError(FacebookError e, Object state) {
					System.out.println("onFacebooError: " + e.getMessage());
				}

			};
			fGetAsyncFacebook().request("me/home", data, listener);
		} else {
			fNotifyError();
		}
	}

	@Override
	public void fPublishFeeds(Bundle params) {
		
	}

	@Override
	public void fUploadPic(String message, String selectedImagePath) {
		
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
	public void fPostComment(Bundle params) {
		
	}

	@Override
	public void fPostReply(Bundle params) {
		
	}

	@Override
	public void fNotifyComplete(int status) {
		for (SnsCallBackListener listener: listeners) {
			listener.onAsyncCallBack(snsName, status);
		}
	}

	@Override
	public void fNotifyError() {
		
	}

}
