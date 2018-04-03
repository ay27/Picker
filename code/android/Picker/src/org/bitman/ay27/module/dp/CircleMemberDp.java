package org.bitman.ay27.module.dp;

import org.bitman.ay27.module.User;

import java.util.Date;

public class CircleMemberDp extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date memberJoinTime;
    private String strDate;

	public Date getMemberJoinTime() {
		return memberJoinTime;
	}

	public void setMemberJoinTime(Date memberJoinTime) {
		this.memberJoinTime = memberJoinTime;
	}

	public CircleMemberDp() {

	}
//
//	public CircleMemberDp(User user, Date memberJoinTime) {
//		/*
//		 * remove password
//		 */
//        super(user.getId(), user.getName(), user.getEmail(), user
//                        .getLastVisit(), user.getRegisterTime(), user.getFavoriteNum(),
//                user.getFollowNum(), user.getFollowNum(), user
//                        .getQuestionNum(), user.getAnswerNum(), user
//                        .getNoteNum(), user.getCircleNum(), user.getBookNum(),
//                user.getAvatarUrl(), user.getSignature());
//		this.memberJoinTime = memberJoinTime;
//	}

}
