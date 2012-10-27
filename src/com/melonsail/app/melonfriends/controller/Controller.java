package com.melonsail.app.melonfriends.controller;


public interface Controller {
	static final String TAG = Controller.class.getSimpleName();
	
	/**
	 * Refresh section that is static
	 * Like: settings, panels
	 */
	public void fRefreshPanelView();

	/**
	 * Refresh section that is dynamic
	 * Like: feeds, comments, etc
	 * @param snsName
	 */
	public void fRefreshContentView(String snsName);
}
