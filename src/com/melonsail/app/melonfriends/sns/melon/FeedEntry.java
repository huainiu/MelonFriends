package com.melonsail.app.melonfriends.sns.melon;

import java.util.ArrayList;


public class FeedEntry /*implements Parcelable*/{

	private String sHeadImg;
	
	private String sID;
	private String sSNS;
	private String sName;
	private String sOwnerID;
	private String sCreatedTime;
	private String sUpdatedTime;
	private String sSource;
	private String sFeedType;
	private String sMsgBody;
	private String sStory;
	private String sLink;
	//private String sStory_Tags;
	private String sPhotoPreviewLink;
	private String sPhotoLargeLink;
	private String sPhotoPreviewName;
	private String sPhotoPreviewCaption;
	private String sPhotoPreviewDescription;
	
	//private String sType;
	private String sIcon;
	private String sCntLikes;
	private String sIsRead;
	private String sCntCmt;
	
	
	private UserFriend zFriend;
	private ArrayList<FeedEntryComment> zComments;
	
	public FeedEntry() {
		setzFriend(new UserFriend());
		zComments = new ArrayList<FeedEntryComment>();
	}
	
	public void setsHeadImg(String sHeadImg) {
		this.sHeadImg = sHeadImg;
	}
	public String getsHeadImg() {
		return sHeadImg;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getsName() {
		return sName;
	}
	public void setsCreatedTime(String sCreatedTime) {
		this.sCreatedTime = sCreatedTime;
	}
	public String getsCreatedTime() {
		return sCreatedTime;
	}
	public void setsUpdatedTime(String sUpdatedTime) {
		this.sUpdatedTime = sUpdatedTime;
	}

	public String getsUpdatedTime() {
		return sUpdatedTime;
	}

	public void setsPhotoPreviewLink(String sPhotoPreviewLink) {
		this.sPhotoPreviewLink = sPhotoPreviewLink;
	}
	public String getsPhotoPreviewLink() {
		return sPhotoPreviewLink;
	}
	public void setsMsgBody(String sMsgBody) {
		this.sMsgBody = sMsgBody;
	}
	public String getsMsgBody() {
		return sMsgBody;
	}
	public void setsPhotoPreviewDescription(String sPhotoPreviewDescription) {
		this.sPhotoPreviewDescription = sPhotoPreviewDescription;
	}
	public String getsPhotoPreviewDescription() {
		return sPhotoPreviewDescription;
	}
	public void setsPhotoPreviewName(String sPhotoPreviewName) {
		this.sPhotoPreviewName = sPhotoPreviewName;
	}
	public String getsPhotoPreviewName() {
		return sPhotoPreviewName;
	}
	public void setsPhotoPreviewCaption(String sPhotoPreviewCaption) {
		this.sPhotoPreviewCaption = sPhotoPreviewCaption;
	}
	public String getsPhotoPreviewCaption() {
		return sPhotoPreviewCaption;
	}
	public void setsStory(String sStory) {
		this.sStory = sStory;
	}
	public String getsStory() {
		return sStory;
	}
//	public void setsStory_Tags(String sStory_Tags) {
//		this.sStory_Tags = sStory_Tags;
//	}
//	public String getsStory_Tags() {
//		return sStory_Tags;
//	}
	public void setsOwnerID(String sOwnerID) {
		this.sOwnerID = sOwnerID;
	}
	public String getsOwnerID() {
		return sOwnerID;
	}

	public void setzFriend(UserFriend zFriend) {
		this.zFriend = zFriend;
	}

	public UserFriend getzFriend() {
		return zFriend;
	}

	public void setsFeedType(String sFeedType) {
		this.sFeedType = sFeedType;
	}

	public String getsFeedType() {
		return sFeedType;
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public String getsID() {
		return sID;
	}

	public void setzComments(ArrayList<FeedEntryComment> zComments) {
		this.zComments = zComments;
	}

	public ArrayList<FeedEntryComment> getzComments() {
		return zComments;
	}

	public void setsLink(String sLink) {
		this.sLink = sLink;
	}

	public String getsLink() {
		return sLink;
	}

	public void setsPhotoLargeLink(String sPhotoLargeLink) {
		this.sPhotoLargeLink = sPhotoLargeLink;
	}

	public String getsPhotoLargeLink() {
		return sPhotoLargeLink;
	}

	public void setsCntLikes(String sCntLikes) {
		this.sCntLikes = sCntLikes;
	}

	public String getsCntLikes() {
		return sCntLikes;
	}
	
	public String getsCntCmt() {
		return sCntCmt;
	}

	public void setsCntCmt(String sCntCmt) {
		this.sCntCmt = sCntCmt;
	}

	public void setsSNS(String sSNS) {
		this.sSNS = sSNS;
	}

	public String getsSNS() {
		return sSNS;
	}

	public void setsSource(String sSource) {
		this.sSource = sSource;
	}

	public String getsSource() {
		return sSource;
	}

//	public void setsType(String sType) {
//		this.sType = sType;
//	}
//
//	public String getsType() {
//		return sType;
//	}

	public void setsIcon(String sIcon) {
		this.sIcon = sIcon;
	}

	public String getsIcon() {
		return sIcon;
	}

	public void setsIsRead(String sIsRead) {
		this.sIsRead = sIsRead;
	}

	public String getsIsRead() {
		return sIsRead;
	}

}
