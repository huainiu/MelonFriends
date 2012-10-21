package com.melonsail.app.melonfriends.sns.facebook;


public class FBHomeFeedEntryTags {
	
	private FBFeedEndtryTag[] data;
	
	public class FBFeedEndtryTag {
		private String name;
		private String id;
		private String offset;
		private String length;
		private String type;
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getId() {
			return id;
		}
		public void setOffset(String offset) {
			this.offset = offset;
		}
		public String getOffset() {
			return offset;
		}
		public void setLength(String length) {
			this.length = length;
		}
		public String getLength() {
			return length;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getType() {
			return type;
		}
	}

	public void setData(FBFeedEndtryTag[] data) {
		this.data = data;
	}

	public FBFeedEndtryTag[] getData() {
		return data;
	}
}
