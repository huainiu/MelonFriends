package com.melonsail.app.melonfriends.sns.sina;

import java.util.List;

import com.weibo.net.WBComment;

public class WBHomeComment {

	private String total_number;
	private List<WBComment> comments;
	
	public WBHomeComment(){}

	public String getTotal_number() {
		return total_number;
	}

	public void setTotal_number(String totalNumber) {
		total_number = totalNumber;
	}

	public List<WBComment> getComments() {
		return comments;
	}

	public void setComments(List<WBComment> comments) {
		this.comments = comments;
	}
	
}
