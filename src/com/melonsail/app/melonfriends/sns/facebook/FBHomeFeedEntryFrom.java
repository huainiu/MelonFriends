package com.melonsail.app.melonfriends.sns.facebook;

public class FBHomeFeedEntryFrom {
	
	static final String USER_IMG_URL_FB = "https://graph.facebook.com/%s/picture";
	
	private String id;
	private String name;
	private String headurl;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setHeadurl(String headurl) {
		this.headurl = headurl;
	}

	public String getHeadurl() {
		headurl = String.format(USER_IMG_URL_FB, id);
		return headurl;
	}
	   
}
