package org.bitman.ay27.module;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-19.
 */
public class Favorite extends BaseModule {
    private long userId;
    private long objectId;

    public Favorite() {
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    @Override
    public long getId() {
        return 0;
    }
}
