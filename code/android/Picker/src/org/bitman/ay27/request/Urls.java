package org.bitman.ay27.request;

public class Urls {

    public static final String KEY_IMAGE_ID = "imageId";
    public static final String KEY_IMAGE_URL = "imageUrl";
    public static final String KEY_OVERSIZE = "overSize";
    public static final String KEY_USER_MESSAGE_LIST = "messageList";
    public static final String KEY_SEARCH_RESULT = "searchResultList";
    public static final String KEY_ATTACHMENT_LIST = "attachmentList";
    public static final String KEY_ATTACHMENT_FILE_LIST = "attachmentFileList";
    public static final String KEY_ATTACHMENTFEED = "attachmentFeed";
	public static final String KEY_ATTACHMENT_ID = "attachmentId";
	public static final String KEY_SECTION_LIST = "sectionList";

	private Urls() {

	}

	public static void setIp(String ip) {
		baseUrl = "http://"+ip+"/";
	}

	public static String baseUrl = "http://picker.bitmen.org/";

	public static final String KEY_STATUS = "status";
	public static final String KEY_QUESTION_LIST = "questionList";
	public static final String KEY_COMMENT_LIST = "commentList";
	public static final String KEY_ANSWER_LIST = "answerList";
	public static final String KEY_CIRCLE_LIST = "circleList";
	public static final String KEY_FOLLOW_LIST = "followList";
	public static final String KEY_NOTE_LIST = "noteList";
	public static final String KEY_BOOK_LIST = "bookList";
	public static final String KEY_USER_LIST = "userList";
	public static final String KEY_QUESTION = "question";
	public static final String KEY_COMMENT = "comment";
	public static final String KEY_ANSWER = "answer";
	public static final String KEY_CIRCLE = "circle";
	public static final String KEY_FOLLOW = "follow";
	public static final String KEY_NOTE = "note";
	public static final String KEY_BOOK = "book";
	public static final String KEY_USER = "user";
    public static final String KEY_PRIVATEMESSAGE_LIST = "privateMessageList";
    public static final String KEY_FAVORITENUM = "favoriteNum";

}
