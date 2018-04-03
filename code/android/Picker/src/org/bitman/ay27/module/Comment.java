package org.bitman.ay27.module;


import java.util.Date;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-19.
 */
public class Comment extends BaseModule {
    private long id;
    private long producerId;
    private long commentedId;
    private String content;
    private int favoriteNum;
    private Date date;

    public Comment(long _id) {
        this.id = _id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public long getCommentedId() {
        return commentedId;
    }

    public void setCommentedId(long commentedId) {
        this.commentedId = commentedId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(int favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
