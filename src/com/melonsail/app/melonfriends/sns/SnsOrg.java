package com.melonsail.app.melonfriends.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.util.Log;

import com.melonsail.app.melonfriends.R;
import com.melonsail.app.melonfriends.activities.MainActivity;
import com.melonsail.app.melonfriends.controller.LstViewFeedAdapter;
import com.melonsail.app.melonfriends.services.MelonFriendsService;
import com.melonsail.app.melonfriends.sns.facebook.FacebookUtil;
import com.melonsail.app.melonfriends.utils.Const;
import com.melonsail.app.melonfriends.utils.Pref;

public class SnsOrg implements SnsCallBackListener {
	private static final String TAG = "SnsOrg";

	private static ArrayList<SnsUtil> mActiveSnsList;
	private ArrayList<SnsUtil> mSnsList;
	
	private Map<String, Integer> mLogos;
	
	private Activity mActivity;
	private Service mService;
	private Context mContext;
	
	private ArrayList<LstViewFeedAdapter> mMainViewFeedAdapterList;
	
	//SnsUtil
	private FacebookUtil zFacebookUtil;

	public SnsOrg() {
		mActiveSnsList = new ArrayList<SnsUtil>();
		mSnsList = new ArrayList<SnsUtil>();
		mMainViewFeedAdapterList = new ArrayList<LstViewFeedAdapter>();
		mLogos = new HashMap<String, Integer>();
		
		mLogos.put(Const.SNS_FACEBOOK, R.drawable.fh_facebook_logo);
		mLogos.put(Const.SNS_RENREN, R.drawable.fh_renren_logo);
		mLogos.put(Const.SNS_TWITTER, R.drawable.fh_twitter_logo);
		mLogos.put(Const.SNS_SINA, R.drawable.fh_sina_logo);
		mLogos.put(Const.SNS_DUMMY, R.drawable.fh_sina_logo);
		
		
	}
	
	/**
	 * Initialize by backend service process
	 * @param service
	 */
	public SnsOrg(Service service) {
		this();
		this.mService = service;
		//this.mContext = service;
		fInitActiveSns(service);
	}
	
	/**
	 * Initialize by frontend activity process
	 * @param activity
	 */
	public SnsOrg(Activity activity) {
		this();
		this.mActivity = activity;
		fInitSnsList();
		fInitActiveSns(activity);
	}
	
	public void fInitSnsList() {
		for(int i=0; i< Const.SNSGROUPS.length; i++) {
			SnsUtil snsUtil = this.fGetSnsInstance(Const.SNSGROUPS[i]);
			mMainViewFeedAdapterList.add(new LstViewFeedAdapter(mActivity, R.layout.feed_item_preview, snsUtil.fGetSNSName()));
			mSnsList.add(snsUtil);
		}
	}
	
	public SnsUtil fGetSnsInstance(String snsName) {
		SnsUtil snsUtil = null;
		snsUtil = fGetActiveSNSByName(snsName);
		if (snsUtil != null) {
			return snsUtil;
		}
		
		// sns not in active list, create new instance
		if(snsName.equals(Const.SNS_FACEBOOK)) {
			if(this.zFacebookUtil == null) {
				if (mActivity != null) {
					this.zFacebookUtil = new FacebookUtil(mActivity);
				} else {
					this.zFacebookUtil = new FacebookUtil(mService);
				}
				
			}
			snsUtil = this.zFacebookUtil;
		} else {
			snsUtil = new DummySnsUtil(Const.SNS_DUMMY);
		}
		
		return snsUtil;
	}
	
	public void fInitActiveSns(Context context) {
		String activeSnsName = Pref.getMyStringPref(context, Const.SNS_ACTIVE);
		String[] snsNames = (activeSnsName.length() > 1) ? activeSnsName.split(Const.DELIMETER): null;
		if (snsNames != null) {
			for (int i = 0; i < snsNames.length; i++) {
				String snsName = snsNames[i];
				SnsUtil snsUtil = fGetSnsInstance(snsName);
				String token = Pref.getMyStringPref(context, snsName + Const.SNS_TOKEN);
				snsUtil.fSetAccessToken(token);
				if (snsUtil.isSessionValid() ) {
					Log.i(TAG, snsName + " is still active.");
					snsUtil.addListeners(this);
					fAddActiveSns(snsName);
				} else {
					Log.i(TAG, snsName + " previous sessions expired");
					fRemoveActiveSns(snsName);
					//notifiy users about the expiration and relogin
				}
				
			}
		}
	}
	
	/**
	 * Add Active Sns into list.
	 * It will be called when service or activity is initialized
	 * It will be called when users add new sns from leftpanel
	 * @param snsName
	 * @return
	 */
	public boolean fAddActiveSns(String snsName) {
		Log.i(TAG, "SnsOrg.fAddActiveSns: SNS = " + snsName);
		SnsUtil snsUtil = fGetSnsInstance(snsName);
		snsUtil.fSetActive();
		mActiveSnsList.add(snsUtil);
		fUpdateActiveSnsInPref();
		//fRefreshView();
		return true;
	}
	
	/**
	 * New Sns is activated or existing ones has been removed
	 * Notify main_controller and leftpanel_controller to refresh their view
	 * Used by activity
	 */
	private void fRefreshView() {
		((MainActivity)mActivity).fGetController().fRefreshView();
	}
	
	/**
	 * New request is completed
	 * Allow service to reply message to requesting activity
	 * Used by service
	 * @param serviceMsg 
	 * @param snsName 
	 */
	private void fServiceMessage(String snsName, int serviceMsg) {
		((MelonFriendsService)mService).fReplyMesseage(snsName, serviceMsg);
	}

	/**
	 * Remove Sns from Active list
	 * It will be called when service detect the session is expired.
	 * It will be called when users remove sns from leftpanel.
	 * @param snsName
	 * @return
	 */
	public boolean fRemoveActiveSns(String snsName) {
		SnsUtil snsUtil = fGetActiveSNSByName(snsName);
		if (snsUtil != null ) {
			Log.i(TAG, "SnsOrg.fRemoveActiveSns: SNS = " + snsName);
			if (mActivity != null) {
				snsUtil.fDeleteAccessKeySP(mActivity);
			} else {
				snsUtil.fDeleteAccessKeySP(mService);
			}
			snsUtil.fSetInactive();
			mActiveSnsList.remove(snsUtil);
			snsUtil.fLogout(null);
			fUpdateActiveSnsInPref();
			fRefreshView();
			return true;
		}
		return false;
	}
	
	public SnsUtil fGetActiveSNSByName(String snsName) {
		for (SnsUtil util: mActiveSnsList) {
			if (util.snsName.equals(snsName)) {
				return util;
			}
		}
		return null;
	}
	
	/**
	 * It is to toggle active status when users click on leftpanel
	 * If remove fail, means the sns not in active list, hence add
	 * @param snsName
	 */
	public void fToggleSnsSelection(String snsName) {
		if (!fRemoveActiveSns(snsName) ) {
			SnsUtil snsUtil = fGetSnsInstance(snsName);
			snsUtil.addListeners(this);
			snsUtil.fAuth(mActivity);
		}
	}
	
	public void fActiveSnsGetNewFeed(Context context) {
		for (SnsUtil util: mActiveSnsList) {
			util.fGetNewsFeeds(context);
		}
	}
	
//	public void fActiveSnsDisplayNewFeed(Context context, int pos) {
//		mActiveSnsList.get(pos).fDisplayNewsFeeds(context);
//		for (SnsUtil util: mActiveSnsList) {
//			util.fGetNewsFeeds(context);
//		}
//	}

	public void fSetActiveSnsList(ArrayList<SnsUtil> mSnsList) {
		this.mActiveSnsList = mSnsList;
	}

	public ArrayList<SnsUtil> fGetActiveSnsList() {
		return mActiveSnsList;
	}

	public void fSetSnsList(ArrayList<SnsUtil> mSnsList) {
		this.mSnsList = mSnsList;
	}

	public ArrayList<SnsUtil> fGetSnsList() {
		return mSnsList;
	}
	
	/**
	 * Used by LeftPanel to display logo
	 * @return
	 */
	public Map<String, Integer> fGetLogoMap() {
		return mLogos;
	}

	public void fAddMainViewFeedAdapterList(LstViewFeedAdapter mMaiViewFeedAdapter) {
		this.mMainViewFeedAdapterList.add(mMaiViewFeedAdapter);
	}
	
	public void fRemoveMainViewFeedAdapterList(LstViewFeedAdapter mMaiViewFeedAdapter) {
		this.mMainViewFeedAdapterList.remove(mMaiViewFeedAdapter);
	}

	public ArrayList<LstViewFeedAdapter> fGetMainViewFeedAdapterList() {
		return mMainViewFeedAdapterList;
	}

	@Override
	public void onAsyncCallBack(String snsName, int message) {
		switch (message) {
		case Const.MSG_SERVICE_LOGIN:
			fSnsLoginCallBack(snsName);
			break;
		case Const.MSG_UI_RECEIVE_NEWFEED:
			//fReplyMesseage();
			Log.i(TAG, "SnsOrg.CallBack: message = New Feed Received");
			fServiceMessage(snsName, Const.MSG_UI_RECEIVE_NEWFEED);
			break;
		case Const.MSG_UI_RECEIVE_NEWFEED_ERROR:
			Log.i(TAG, "SnsOrg.CallBack: message = New Feed Retrieval Error");
			fRemoveActiveSns(snsName);
			fServiceMessage(snsName, Const.MSG_UI_RECEIVE_NEWFEED_ERROR);
			break;
		default:
			break;
		}
	}
	
	private void fSnsLoginCallBack(String snsName) {
		fAddActiveSns(snsName);
		fRefreshView();
	}
	
	/**
	 * Update Active snsNames in SharePreference in order to restore when user start app next time 
	 */
	private void fUpdateActiveSnsInPref() {
		String activeSnsName = "";
		for (SnsUtil snsUtil : mActiveSnsList) {
			activeSnsName += snsUtil.fGetSNSName() + Const.DELIMETER;
		}
		if (mActivity != null) {//init by front end activity
			Pref.setMyStringPref(mActivity, Const.SNS_ACTIVE, activeSnsName);
		} else {//initialized by backend service
			Pref.setMyStringPref(mService, Const.SNS_ACTIVE, activeSnsName);
		}
		
	}


}
