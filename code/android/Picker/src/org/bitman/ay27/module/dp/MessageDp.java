package org.bitman.ay27.module.dp;

import org.bitman.ay27.module.Message;

public class MessageDp extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String producerAvatarUrl;
    private String strDate;

	public String getProducerAvatarUrl() {
		return producerAvatarUrl;
	}

	public void setProducerAvatarUrl(String producerAvatarUrl) {
		this.producerAvatarUrl = producerAvatarUrl;
	}

	public MessageDp() {

	}

	public MessageDp(Message message, String producerAvatarUrl) {
		super(message);
		this.producerAvatarUrl = producerAvatarUrl;
	}
}
