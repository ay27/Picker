package org.bitman.ay27.module.form;

import java.util.List;

public class AttachmentFeedForm {
	private long bookId;

	private int page;

	private String content;

	private String title;

	private List<Integer> attachmentIds;

	public long getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Integer> getAttachmentIds() {
		return attachmentIds;
	}

	public void setAttachmentIds(List<Integer> attachmentIds) {
		this.attachmentIds = attachmentIds;
	}

	public AttachmentFeedForm(long bookId, int page, String title, String content, List<Integer> attachmentIds) {
		this.bookId = bookId;
		this.page = page;
		this.title = title;
		this.content = content;
		this.attachmentIds = attachmentIds;
	}

	@Override
	public String toString() {
		return "AttachmentFeedForm [bookId=" + bookId + ", page=" + page
				+ ", content=" + content + ", attachmentIds=" + attachmentIds
				+ "]";
	}

	public boolean chechValidation() {
		if ((this.bookId > 0) && (this.page >= 0) && (this.content != null)
				&& (!this.content.equals("")) && (this.attachmentIds != null)) {
			return true;
		}
		return false;
	}

}
