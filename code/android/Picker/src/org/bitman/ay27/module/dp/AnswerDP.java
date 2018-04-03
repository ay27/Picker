package org.bitman.ay27.module.dp;

import org.bitman.ay27.module.Answer;
import org.bitman.ay27.module.interfaces.IsFavorite;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-30.
 */
public class AnswerDP extends Answer implements IsFavorite {

    private String replierAvatarUrl;
    private String questionName;
    private String replierName;
    private boolean isFavorite;
    // TODO: add this
//    private String userName;
    private String strDate;

    public AnswerDP() {
    }

    public AnswerDP(int userID, int questionID, String content) {
        super(userID, questionID, content);
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getUserAvatarUrl() {
        return replierAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.replierAvatarUrl = userAvatarUrl;
    }

    public String getReplierAvatarUrl() {
        return replierAvatarUrl;
    }

    public void setReplierAvatarUrl(String replierAvatarUrl) {
        this.replierAvatarUrl = replierAvatarUrl;
    }

    public String getReplierName() {
        return replierName;
    }

    public void setReplierName(String replierName) {
        this.replierName = replierName;
    }

    @Override
    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public void setFavorite(boolean value) {
        isFavorite = value;
    }

//    public String getUserName() {
//        return userName;
//    }
}
