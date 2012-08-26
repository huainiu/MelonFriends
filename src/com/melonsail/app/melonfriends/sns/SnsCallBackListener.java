package com.melonsail.app.melonfriends.sns;



/**
 * @author Li Ji
 * 
 * Listener for events that Sns is Selected/Unselected
 * It is the interface between Presenter(MainUIViews, etc) and Model(SnsOrg)
 * 
 */

public interface SnsCallBackListener {
	
	public void onAsyncCallBack(String snsName, int message);
	
}
