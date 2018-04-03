package org.bitman.ay27.module;

import org.bitman.ay27.common.ContentType;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-19.
 */
public class Follow extends BaseModule  {
    private long id;
    private long followerId;
    private long sourceId;

    public Follow() {
    }

    public long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(long followerId) {
        this.followerId = followerId;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
