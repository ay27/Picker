package org.bitman.ay27.module;

import java.util.Date;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-24.
 */
public class Answer extends BaseModule {
    private long id;
    private long questionId;
    private long replierId;
    private String content;
    private int favoriteNum;
    private int commentNum;
    private Date date;

    public Answer() {
    }

    public Answer(int userID, int questionID, String content) {
        this.replierId = userID;
        this.questionId = questionID;
        this.content = content;
    }

    public long getQuestionID() {
        return questionId;
    }

    public void setQuestionID(int questionID) {
        this.questionId = questionID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserID() {
        return replierId;
    }

    public void setUserID(long userID) {
        this.replierId = userID;
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

    public Date getCreateTime() {
        return date;
    }

    public void setCreateTime(Date createTime) {
        this.date = createTime;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

}
