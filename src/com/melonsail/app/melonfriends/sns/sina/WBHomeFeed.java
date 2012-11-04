package com.melonsail.app.melonfriends.sns.sina;

import java.util.List;

import com.weibo.net.WBStatus;

public class WBHomeFeed {
	
	private List<WBStatus> statuses;
	
	public WBHomeFeed() {}

	public List<WBStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<WBStatus> statuses) {
		this.statuses = statuses;
	}
	
}
