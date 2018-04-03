package org.bitman.ay27.module.dp;

import org.bitman.ay27.module.Circle;
import org.bitman.ay27.module.interfaces.IsJoinable;

public class CircleDP extends Circle implements IsJoinable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean isJoin;
    private String establisherName;
    private String strDate;

    public String getEstablisherName() {
        return establisherName;
    }

    public void setEstablisherName(String establisherName) {
        this.establisherName = establisherName;
    }

    public boolean isJoin() {
		return isJoin;
	}

	public void setJoin(boolean isJoin) {
		this.isJoin = isJoin;
	}

	public CircleDP(Circle circle, boolean isJoin) {
		super(circle);
		this.isJoin = isJoin;
	}

	public CircleDP() {

	}

    @Override
    public boolean isJoined() {
        return isJoin;
    }

    @Override
    public void setJoinable(boolean value) {
        this.isJoin = value;
    }

    @Override
    public int getJoinedNum() {
        return getMemberNum();
    }
}
