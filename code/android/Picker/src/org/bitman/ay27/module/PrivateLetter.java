package org.bitman.ay27.module;

import java.util.Date;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-19.
 */
public class PrivateLetter extends BaseModule  {
    protected long id = -1;
    protected long senderId;
    protected long receiverId;
    protected long dialogId;
    protected String content;
    protected Date time;



    public PrivateLetter(long _id) {
        this.id = _id;
    }

    public PrivateLetter(long myUserID, long targetUserID, String content, Date date) {
        this.senderId = myUserID; this.receiverId = targetUserID;
        this.content = content; this.time = date;
    }

    public PrivateLetter(PrivateLetter privateMessage) {
        this.id = privateMessage.getId();
        this.senderId = privateMessage.getSenderId();
        this.receiverId = privateMessage.getReceiverId();
        this.dialogId = privateMessage.getDialogID();
        this.content = privateMessage.getContent();
        this.time = privateMessage.getDate();
    }

    public long getId() {
        return (id==-1)? id=System.currentTimeMillis() : id ;
    }

    public void set_id(long _id) {
        this.id = _id;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getDialogID() {
        return dialogId;
    }

    public void setDialogID(long dialogID) {
        this.dialogId = dialogID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return time;
    }

    public void setDate(Date date) {
        this.time = date;
    }
}
