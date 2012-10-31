package com.melonsail.app.melonfriends.sns.facebook;

import java.util.List;


public class FBHomeFeedEntryComments {

	private String count;
	private List<FBHomeFeedEntryComment> data;
	
	public class FBHomeFeedEntryComment {
		private FBHomeFeedEntryFrom from;
		
		private String id;
		private String message;
		private String created_time;
		
		private String likes;
		private String user_likes;
		
		public void setFrom(FBHomeFeedEntryFrom from) {
			this.from = from;
		}
		public FBHomeFeedEntryFrom getFrom() {
			return from;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getMessage() {
			return message;
		}
		public void setCreated_time(String created_time) {
			this.created_time = created_time;
		}
		public String getCreated_time() {
			return created_time;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getId() {
			return id;
		}
		public void setLikes(String likes) {
			this.likes = likes;
		}
		public String getLikes() {
			return likes;
		}
		public void setUser_likes(String user_likes) {
			this.user_likes = user_likes;
		}
		public String getUser_likes() {
			return user_likes;
		}
		
	}
	
	public void setCount(String count) {
		this.count = count;
	}
	public String getCount() {
		return count;
	}

	public void setData(List<FBHomeFeedEntryComment> data) {
		this.data = data;
	}

	public List<FBHomeFeedEntryComment> getData() {
		return data;
	}
	
}
