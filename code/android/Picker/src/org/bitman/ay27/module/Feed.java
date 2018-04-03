package org.bitman.ay27.module;

import java.util.Date;


public class Feed extends BaseModule implements Comparable<Feed> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_QUESTION = 1;
    public static final int TYPE_NOTE = 2;
    public static final int TYPE_ATTACHMENT_FEED = 3;

    protected long id;
    protected long bookId;
    protected long userId;
    protected String title;
    protected String content;
    protected Date date;
    protected int page;
    protected int type;
    protected boolean isPublic;
    protected int favoriteNum;
    protected int answerNum;
    protected int commentNum;
    protected int followNum;
    protected String sectionStr;
    protected String brief;

    public Feed() {

    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Feed(long id, long bookId, long userId, String title, String content,
                Date date, int page, int type, boolean isPublic, int favoriteNum,
                int answerNum, int commentNum, int followNum) {
        super();
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.page = page;
        this.type = type;
        this.isPublic = isPublic;
        this.favoriteNum = favoriteNum;
        this.answerNum = answerNum;
        this.commentNum = commentNum;
        this.followNum = followNum;
    }

    public Feed(long bookId, long userId, String title, String content,
                Date date, int page, int type, boolean isPublic) {
        this.bookId = bookId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
        this.page = page;
        this.type = type;
        this.isPublic = isPublic;
    }

    public Feed(long bookId, long userId, String title, String content, int page,
                int type, boolean isPublic) {
        this.bookId = bookId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = new Date();
        this.page = page;
        this.type = type;
        this.isPublic = isPublic;
    }

    public Feed(long bookId, long userId, String title, String content, int page,
                int type) {
        this.bookId = bookId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = new Date();
        this.page = page;
        this.type = type;
        this.isPublic = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(int favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public String getSectionStr() {
        return sectionStr;
    }

    public void setSectionStr(String sectionStr) {
        this.sectionStr = sectionStr;
    }

    @Override
    public String toString() {
        return "Feed [id=" + id + ", bookId=" + bookId + ", userId=" + userId
                + ", title=" + title + ", content=" + content + ", date="
                + date + ", page=" + page + ", type=" + type + ", isPublic="
                + isPublic + ", favoriteNum=" + favoriteNum + ", answerNum="
                + answerNum + ", commentNum=" + commentNum + ", followNum="
                + followNum + "]";
    }

    @Override
    public int compareTo(Feed another) {
        return page - another.page;
    }
}
