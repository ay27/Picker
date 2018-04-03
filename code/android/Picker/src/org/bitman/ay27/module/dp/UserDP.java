package org.bitman.ay27.module.dp;

import org.bitman.ay27.module.User;
import org.bitman.ay27.module.interfaces.IsFollow;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-9-1.
 */
public class UserDP extends User implements IsFollow {

    private boolean isFollow;
    private String strDate;

    public UserDP(String name) {
        super(name);
    }

    public UserDP(int id) {
        super(id);
    }

    public UserDP(UserDP userDP) {
        setId((int)userDP.getId());
        setName(userDP.getName());
        setPassword(userDP.getPassword());
        setEmail(userDP.getEmail());
        setLastVisit(userDP.getLastVisit());
        setRegisterTime(userDP.getRegisterTime());
        setFavoriteNum(userDP.getFavoriteNum());
        setFollow(userDP.isFollow());
        setFollowNum(userDP.getFollowNum());
        setBeFollowNum(userDP.getBeFollowNum());
        setQuestionNum(userDP.getQuestionNum());
        setNoteNum(userDP.getNoteNum());
        setAnswerNum(userDP.getAnswerNum());
        setCircleNum(userDP.getCircleNum());
        setBookNum(userDP.getBookNum());
        setAvatarUrl(userDP.getAvatarUrl());
        setSignature(userDP.getSignature());
    }

    public boolean isFollow() {
        return isFollow;
    }

    @Override
    public void setFollow(boolean isFollow) {
        this.isFollow = isFollow;
    }
}
