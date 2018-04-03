package org.bitman.ay27.module.dp;

import org.bitman.ay27.module.Comment;
import org.bitman.ay27.module.interfaces.IsFavorite;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-30.
 */
public class CommentDP extends Comment implements IsFavorite {

    // TODO : ....
    private String userAvatarUrl;
    private boolean isFavorite;
    private String strDate;
    private String producerName;

    public CommentDP(int _id) {
        super(_id);
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    @Override
    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public void setFavorite(boolean value) {
        isFavorite = value;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }
}
