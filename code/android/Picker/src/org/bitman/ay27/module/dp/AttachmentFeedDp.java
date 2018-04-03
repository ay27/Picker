package org.bitman.ay27.module.dp;


import org.bitman.ay27.module.AttachmentFile;
import org.bitman.ay27.module.Feed;

import java.util.List;

public class AttachmentFeedDp extends Feed {

	private String userName;

	private String userAvatarUrl;

	private String strDate;

	protected List<AttachmentFile> attachments;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAvatarUrl() {
		return userAvatarUrl;
	}

	public void setUserAvatarUrl(String userAvatarUrl) {
		this.userAvatarUrl = userAvatarUrl;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public List<AttachmentFile> getAttachments() {
		return attachments;
	}

//	public AttachmentFeedDp(Feed attachmentFeed, String userName,
//							String userAvatarUrl) {
//		super(attachmentFeed);
//		this.userAvatarUrl = userAvatarUrl;
//		this.userName = userName;
//	}



}
