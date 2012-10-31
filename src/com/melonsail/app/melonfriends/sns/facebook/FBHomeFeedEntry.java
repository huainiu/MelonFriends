package com.melonsail.app.melonfriends.sns.facebook;

public class FBHomeFeedEntry {
	
	private String id;
	
	private String message;
	private FBHomeFeedEntryTags message_tags;
	private String story;
	private FBHomeFeedEntryTags story_tags;
	
	private String picture;
	private String raw_picture;
	private String link;
	private String name;
	private String description;
	private String caption;
	
	private String source;
	private String icon;
	private String annotation;
	
	private String updated_time;
	private String created_time;
	
	private String type;
	
	private FBHomeFeedEntryTags with_tags;
	   
	//Nested classes for From entries
	private FBHomeFeedEntryFrom from;
	private FBHomeFeedEntryComments comments;
	private FBHomFeedEntryLikes likes;
	private FBHomeFeedApplication application;
	
	public class FBHomeFeedApplication {
		private String id;
		private String name;
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
	}
	
	//Nested collection for Actions entries
	//private List<FBHomeFeedEntryAction> actions;
	
	//No Arg constructor
	public FBHomeFeedEntry() {}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCaption() {
		return caption;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getPicture() {
		return picture;
	}

	public void setAttribution(String attribution) {
		this.annotation = attribution;
	}

	public String getAttribution() {
		return annotation;
	}

	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}

	public String getUpdated_time() {
		return updated_time;
	}

	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setFrom(FBHomeFeedEntryFrom from) {
		this.from = from;
	}

	public FBHomeFeedEntryFrom getFrom() {
		return from;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public String getStory() {
		return story;
	}

	public void setComments(FBHomeFeedEntryComments comments) {
		this.comments = comments;
	}

	public FBHomeFeedEntryComments getComments() {
		return comments;
	}

	public void setLikes(FBHomFeedEntryLikes likes) {
		this.likes = likes;
	}

	public FBHomFeedEntryLikes getLikes() {
		return likes;
	}

	public void setRawPhoto(String links) {
		this.raw_picture = links;
	}
	public String getRawPhoto() {
		return raw_picture;
	}

	public void setMessage_tags(FBHomeFeedEntryTags message_tags) {
		this.message_tags = message_tags;
	}

	public FBHomeFeedEntryTags getMessage_tags() {
		return message_tags;
	}

	public void setStory_tags(FBHomeFeedEntryTags story_tags) {
		this.story_tags = story_tags;
	}

	public FBHomeFeedEntryTags getStory_tags() {
		return story_tags;
	}

	public void setWith_tags(FBHomeFeedEntryTags with_tags) {
		this.with_tags = with_tags;
	}

	public FBHomeFeedEntryTags getWith_tags() {
		return with_tags;
	}

	public void setApplication(FBHomeFeedApplication application) {
		this.application = application;
	}

	public FBHomeFeedApplication getApplication() {
		return application;
	}
}
