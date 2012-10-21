package com.melonsail.app.melonfriends.sns.melon;

import java.util.ArrayList;


public class FeedEntry /*implements Parcelable*/{

	private String sID;
	private String sSNS;
	private String sName;
	private String sOwnerID;
	private String sOwnerName;
	private String sOwnerImg;
	
	private String sMsg;
	private String sStory;
	
	private String sPhotoUrl;
	private String sPhotoUrl_L;
	private String sLink;
	private String sPhotoName;
	private String sPhotoCaption;
	private String sPhotoDescription;
	
	private String sSource;
	private String sIcon;
	private String sAnnotation;
	private String sFeedType;
	
	private String sIsRead;
	private String sIsLiked;
	private String sCountLikes;
	private String sCountComment;
	private String sCountShare;
	
	private String sFeed_Replyto_Status_ID;
	private String sFeed_Replyto_From_ID;
	private String sFeed_Replyto_From_Name;
	
	private String sCreatedTime;
	private String sUpdatedTime;
	
	private ArrayList<FeedTags> zStory_Tags;
	private ArrayList<FeedEntryComment> zComments;
	
	public FeedEntry() {
		setzStory_Tags(new ArrayList<FeedTags>());
		setzComments(new ArrayList<FeedEntryComment>());
	}

	public void setsID(String sID) {
		this.sID = sID;
	}

	public String getsID() {
		return sID;
	}

	public void setsSNS(String sSNS) {
		this.sSNS = sSNS;
	}

	public String getsSNS() {
		return sSNS;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getsName() {
		return sName;
	}

	public void setsOwnerID(String sOwnerID) {
		this.sOwnerID = sOwnerID;
	}

	public String getsOwnerID() {
		return sOwnerID;
	}

	public void setsOwnerName(String sOwnerName) {
		this.sOwnerName = sOwnerName;
	}

	public String getsOwnerName() {
		return sOwnerName;
	}

	public void setsOwnerImg(String sOwnerImg) {
		this.sOwnerImg = sOwnerImg;
	}

	public String getsOwnerImg() {
		return sOwnerImg;
	}

	public void setsMsg(String sMsg) {
		this.sMsg = sMsg;
	}

	public String getsMsg() {
		return sMsg;
	}

	public void setsStory(String sStory) {
		this.sStory = sStory;
	}

	public String getsStory() {
		return sStory;
	}

	public void setsPhotoUrl(String sPhotoUrl) {
		this.sPhotoUrl = sPhotoUrl;
	}

	public String getsPhotoUrl() {
		return sPhotoUrl;
	}

	public void setsPhotoUrl_L(String sPhotoUrl_L) {
		this.sPhotoUrl_L = sPhotoUrl_L;
	}

	public String getsPhotoUrl_L() {
		return sPhotoUrl_L;
	}

	public void setsLink(String sLink) {
		this.sLink = sLink;
	}

	public String getsLink() {
		return sLink;
	}

	public void setsPhotoName(String sPhotoName) {
		this.sPhotoName = sPhotoName;
	}

	public String getsPhotoName() {
		return sPhotoName;
	}

	public void setsPhotoCaption(String sPhotoCaption) {
		this.sPhotoCaption = sPhotoCaption;
	}

	public String getsPhotoCaption() {
		return sPhotoCaption;
	}

	public void setsPhotoDescription(String sPhotoDescription) {
		this.sPhotoDescription = sPhotoDescription;
	}

	public String getsPhotoDescription() {
		return sPhotoDescription;
	}

	public void setsSource(String sSource) {
		this.sSource = sSource;
	}

	public String getsSource() {
		return sSource;
	}

	public void setsIcon(String sIcon) {
		this.sIcon = sIcon;
	}

	public String getsIcon() {
		return sIcon;
	}

	public void setsAnnotation(String sAnnotation) {
		this.sAnnotation = sAnnotation;
	}

	public String getsAnnotation() {
		return sAnnotation;
	}

	public void setsFeedType(String sFeedType) {
		this.sFeedType = sFeedType;
	}

	public String getsFeedType() {
		return sFeedType;
	}

	public void setsIsRead(String sIsRead) {
		this.sIsRead = sIsRead;
	}

	public String getsIsRead() {
		return sIsRead;
	}

	public void setsIsLiked(String sIsLiked) {
		this.sIsLiked = sIsLiked;
	}

	public String getsIsLiked() {
		return sIsLiked;
	}

	public void setsCountLikes(String sCountLikes) {
		this.sCountLikes = sCountLikes;
	}

	public String getsCountLikes() {
		return sCountLikes;
	}

	public void setsCountComments(String sCountComment) {
		this.sCountComment = sCountComment;
	}

	public String getsCountComment() {
		return sCountComment;
	}

	public void setsCountShares(String sCountShare) {
		this.sCountShare = sCountShare;
	}

	public String getsCountShare() {
		return sCountShare;
	}

	public void setsFeed_Replyto_Status_ID(String sFeed_Replyto_Status_ID) {
		this.sFeed_Replyto_Status_ID = sFeed_Replyto_Status_ID;
	}

	public String getsFeed_Replyto_Status_ID() {
		return sFeed_Replyto_Status_ID;
	}

	public void setsFeed_Replyto_From_ID(String sFeed_Replyto_From_ID) {
		this.sFeed_Replyto_From_ID = sFeed_Replyto_From_ID;
	}

	public String getsFeed_Replyto_From_ID() {
		return sFeed_Replyto_From_ID;
	}

	public void setsFeed_Replyto_From_Name(String sFeed_Replyto_From_Name) {
		this.sFeed_Replyto_From_Name = sFeed_Replyto_From_Name;
	}

	public String getsFeed_Replyto_From_Name() {
		return sFeed_Replyto_From_Name;
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

	public void setzStory_Tags(ArrayList<FeedTags> zStory_Tags) {
		this.zStory_Tags = zStory_Tags;
	}

	public ArrayList<FeedTags> getzStory_Tags() {
		return zStory_Tags;
	}

	public void setzComments(ArrayList<FeedEntryComment> zComments) {
		this.zComments = zComments;
	}

	public ArrayList<FeedEntryComment> getzComments() {
		return zComments;
	}
	

}
