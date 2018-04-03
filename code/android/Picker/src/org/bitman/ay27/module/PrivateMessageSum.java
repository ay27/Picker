package org.bitman.ay27.module;


public class PrivateMessageSum extends PrivateLetter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String senderName;

	private String senderAvatarUrl;

	private String receiverName;

	private String receiverAvatarUrl;

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public PrivateMessageSum(PrivateLetter privateMessage, String senderName,
			String senderAvatarUrl, String receiverName,
			String receiverAvatarUrl, int count) {
		super(privateMessage);
		this.senderName = senderName;
		this.senderAvatarUrl = senderAvatarUrl;
		this.receiverName = receiverName;
		this.receiverAvatarUrl = receiverAvatarUrl;
		this.count = count;
	}

}
