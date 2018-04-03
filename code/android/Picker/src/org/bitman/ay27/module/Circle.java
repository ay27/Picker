package org.bitman.ay27.module;

import java.util.Date;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-19.
 */
public class Circle extends BaseModule  {

    protected long id;
    protected String name;
    protected long establisherID;
    protected Date establishTime;
    protected String describe;
    protected int memberNum;

    public Circle(long _id, String name) {
        this.id = _id;
        this.name = name;
    }

    public Circle(long _id, String name, String describe) {
        this.id = _id;
        this.name = name;
        this.describe = describe;
    }

    public Circle(int id, String name, Date establishTime, int establisherId,
                  String describe, int memberNum) {
        super();
        this.id = id;
        this.name = name;
        this.establishTime = establishTime;
        this.establisherID = establisherId;
        this.describe = describe;
        this.memberNum = memberNum;
    }

    public Circle() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Circle(Circle circle) {
        this(circle.getId(), circle.getName(), circle.getEstablishTime(),
                circle.getEstablisherID(), circle.getDescribe(), circle
                        .getMemberNum());
    }

    public Circle(String name, Date establishTime, int establisherId,
                  String describe) {
        super();
        this.name = name;
        this.establishTime = establishTime;
        this.establisherID = establisherId;
        this.describe = describe;
    }

    public Circle(String name, int establisherId, String describe) {
        this(name, new Date(), establisherId, describe);
    }

    public Circle(long _id, String name, Date establishTime, long establisherID, String describe, int memberNum) {
        this.id = _id;
        this.name = name;
        this.establishTime = establishTime;
        this.establisherID = establisherID;
        this.describe = describe;
        this.memberNum = memberNum;
    }

    public Circle(long circleID) {
        this.id = circleID;
    }

    public long getId() {
        return id;
    }

    public void set_id(long _id) {
        this.id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEstablishTime() {
        return establishTime;
    }

    public void setEstablishTime(Date establishTime) {
        this.establishTime = establishTime;
    }

    public long getEstablisherID() {
        return establisherID;
    }

    public void setEstablisherID(long establisherID) {
        this.establisherID = establisherID;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }
}
