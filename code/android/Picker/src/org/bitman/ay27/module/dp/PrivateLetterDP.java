package org.bitman.ay27.module.dp;

import org.bitman.ay27.module.PrivateLetter;

import java.util.Date;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-30.
 */
public class PrivateLetterDP extends PrivateLetter {

    private String senderName;
    private String senderAvatarUrl;
    private String receiverName;
    private String receiverAvatarUrl;
    private String strDate;

    public PrivateLetterDP(long myUserID, long targetUserID, String fromUserAvatarUrl, String content, Date date) {
        super(myUserID, targetUserID, content, date);
        id = System.currentTimeMillis();     // random to generate an id.
    }

    public PrivateLetterDP(long _id) {
        super(_id);
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAvatarUrl() {
        return receiverAvatarUrl;
    }

    public void setReceiverAvatarUrl(String receiverAvatarUrl) {
        this.receiverAvatarUrl = receiverAvatarUrl;
    }
}
