package org.bitman.ay27.module.form;

public class CommentForm {

	public static final int COMMENT_ANSWER = 0x1;
	public static final int COMMENT_NOTE = 0x2;
	public static final int COMMENT_ATTACHMENT = 0x3;

	private long commentedId;
	private int type;
	private String content;

	public long getCommentedId() {
		return commentedId;
	}

	public void setCommentedId(long commentedId) {
		this.commentedId = commentedId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public CommentForm(long commentedId, int type, String content) {
		this.commentedId = commentedId;
		this.type = type;
		this.content = content;
	}

	public boolean checkFieldValidation() {
		if ((this.commentedId > 0) && (this.content != null)
				&& (!this.content.equals(""))) {
			return true;
		}
		return false;
	}

}
