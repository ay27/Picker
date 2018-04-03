package org.bitman.ay27.module;

import java.util.Date;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-24.
 */
public class User extends BaseModule  {
    private long id;
    private String username;
    private String email;
    private String password;
    private Date lastVisit;
    private Date registerTime;
    private int favoriteNum;
    // 被关注
    private int followNum;
    // 关注别人
    private int followOthersNum;
    private int questionNum;
    private int noteNum;
    private int answerNum;
    private int circleNum;
    private int bookNum;
    private String avatarUrl;

    // 暂时不用，后备，但是Gson解析需要这个域的存在
    private String signature;

    public User() {
    }

    public User(long _id) {
        this.id = _id;
    }

    public User(long _id, String name) {
        this.id = _id;
        this.username = name;
    }

    public User(String name, String password) {
        this.username = name;
        this.password = password;
    }

    public User(String name, String password, String email) {
        this.username = name;
        this.password = password;
    }

    public User(String name) {
        this.username = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public int getFavoriteNum() {
        return favoriteNum;
    }

    public void setFavoriteNum(int favoriteNum) {
        this.favoriteNum = favoriteNum;
    }

    public int getBeFollowNum() {
        return followNum;
    }

    public void setBeFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public int getFollowNum() {
        return followOthersNum;
    }

    public void setFollowNum(int followOthersNum) {
        this.followOthersNum = followOthersNum;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public int getNoteNum() {
        return noteNum;
    }

    public void setNoteNum(int noteNum) {
        this.noteNum = noteNum;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public int getCircleNum() {
        return circleNum;
    }

    public void setCircleNum(int circleNum) {
        this.circleNum = circleNum;
    }

    public int getBookNum() {
        return bookNum;
    }

    public void setBookNum(int bookNum) {
        this.bookNum = bookNum;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
