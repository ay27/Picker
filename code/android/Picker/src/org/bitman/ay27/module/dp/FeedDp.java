package org.bitman.ay27.module.dp;

import org.bitman.ay27.module.Feed;
import org.bitman.ay27.module.interfaces.IsFavorite;
import org.bitman.ay27.module.interfaces.IsFollow;

public class FeedDp extends Feed implements IsFavorite, IsFollow {
	private String userName;

	private String userAvatarUrl;

	private boolean isFollow;

	private boolean isFavorite;
    private String strDate;

	@Override
	public boolean isFollow() {
		return isFollow;
	}

	@Override
	public void setFollow(boolean value) {
		this.isFollow = value;
	}

	@Override
	public boolean isFavorite() {
		return isFavorite;
	}

	@Override
	public void setFavorite(boolean value) {
		this.isFavorite = value;
	}

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

    public FeedDp() {
	}

	public FeedDp(Feed feed, String userName, String userAvatarUrl,
			boolean isFollow, boolean isFavorite) {
		super((int)feed.getId(), feed.getBookId(), feed.getUserId(),
				feed.getTitle(), feed.getContent(), feed.getDate(), feed
						.getPage(), feed.getType(), feed.isPublic(), feed
						.getFavoriteNum(), feed.getAnswerNum(), feed
						.getCommentNum(), feed.getFollowNum());

		this.userName = userName;
		this.userAvatarUrl = userAvatarUrl;
		this.isFollow = isFollow;
		this.isFavorite = isFavorite;
	}

}
