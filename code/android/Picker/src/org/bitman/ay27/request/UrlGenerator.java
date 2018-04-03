package org.bitman.ay27.request;

import org.bitman.ay27.common.ContentType;

import java.io.UnsupportedEncodingException;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-29.
 */
public class UrlGenerator {

    private UrlGenerator() {
    }

    public static String queryBookByISBN(String isbn) {
        return Urls.baseUrl + "json/book/isbn?isbn=" + isbn;
    }

    public static String addBook(long bookID) {
        return Urls.baseUrl + "json/book/" + bookID + "/add";
    }

    public static String queryCircleByID(long circleID) {
        return Urls.baseUrl + "json/circle/" + circleID + "/dp";
    }

    public static String queryCircleUserList(long circleID) {
        return Urls.baseUrl + "json/circle/" + circleID + "/members";
    }


    public static String removeBook(long currentBookID) {
        return Urls.baseUrl + "json/book/" + currentBookID + "/delete";
    }

    public static String queryUserInfo(long targetUserID) {
        return Urls.baseUrl + "json/user/" + targetUserID;
    }

    public static String queryNote(long noteID) {
        return Urls.baseUrl + "json/note/" + noteID + "/dp";
    }

    public static String queryCommentDPList_Note(long noteID) {
        return Urls.baseUrl + "json/note/" + noteID + "/commentdps";
    }

    public static String queryDialog() {
        return Urls.baseUrl + "json/pmessage";
    }

    public static String sendPrivateLetter() {
        return Urls.baseUrl + "json/pmessage/send";
    }

    public static String queryAnswerList(long questionID) {
        return Urls.baseUrl + "json/question/" + questionID + "/answerdps";
    }

    public static String queryQuestion(long questionID) {
        return Urls.baseUrl + "json/question/" + questionID + "/dp";
    }

    public static String queryAnswer(long answerID) {
        return Urls.baseUrl + "json/answer/" + answerID + "/dp";
    }

    public static String queryCommentDPList_Answer(long answerID) {
        return Urls.baseUrl + "json/answer/" + answerID + "/commentdps";
    }

    public static String queryNoteList(long bookID) {
        return Urls.baseUrl + "json/book/" + bookID + "/notedps";
    }

    public static String queryQuestionList(long bookID) {
        return Urls.baseUrl + "json/book/" + bookID + "/questiondps";
    }

    public static String signIn() {
        return Urls.baseUrl + "json/login";
    }

    public static String signUp() {
        return Urls.baseUrl + "json/register";
    }

    public static String queryMyUserInfo() {
        return Urls.baseUrl + "json/user";
    }

    public static String getResourcesUrl(String coverUrl) {
        return Urls.baseUrl.substring(0, Urls.baseUrl.length()-1) + coverUrl;
    }

    public static String queryUserAnswerList(long targetUserID) {
        return Urls.baseUrl + "json/user/" + targetUserID + "/answerdps";
    }

    public static String queryUserNoteList(long targetUserID) {
        return Urls.baseUrl + "json/user/" + targetUserID + "/notedps";
    }

    public static String queryUserQuestionList(long targetUserID) {
        return Urls.baseUrl + "json/user/" + targetUserID + "/questiondps";
    }

    public static String queryCircleList(long targetUserID) {
        return Urls.baseUrl + "json/user/" + targetUserID + "/circles";
    }

    public static String queryUserBookList(long targetUserID) {
        return Urls.baseUrl + "json/user/" + targetUserID + "/books";
    }

    public static String withdrawFavorite(long targetID, ContentType targetType) {
        switch (targetType) {
            case Answer:
                return Urls.baseUrl + "json/answer/" + targetID + "/withdraw_subscribe";
            case Comment:
                return Urls.baseUrl + "json/comment/" + targetID + "/withdraw_subscribe";
            case Note:
                return Urls.baseUrl + "json/note/" + targetID + "/withdraw_subscribe";
            case Question:
                return Urls.baseUrl + "json/question/" + targetID + "/withdraw_subscribe";
            default:
                return null;
        }
    }

    public static String favorite(long targetID, ContentType targetType) {
        switch (targetType) {
            case Answer:
                return Urls.baseUrl + "json/answer/" + targetID + "/subscribe";
            case Comment:
                return Urls.baseUrl + "json/comment/" + targetID + "/subscribe";
            case Note:
                return Urls.baseUrl + "json/note/" + targetID + "/subscribe";
            case Question:
                return Urls.baseUrl + "json/question/" + targetID + "/subscribe";
            default:
                return null;
        }
    }


    public static String withdrawFollow(long targetID, ContentType targetType) {
        switch (targetType) {
            case User:
                return Urls.baseUrl + "json/user/" + targetID + "/withdraw_follow";
            case Question:
                return Urls.baseUrl + "json/question/" + targetID + "/withdraw_follow";
            default:
                return null;
        }
    }

    public static String follow(long targetID, ContentType targetType) {
        switch (targetType) {
            case User:
                return Urls.baseUrl + "json/user/" + targetID + "/follow";
            case Question:
                return Urls.baseUrl + "json/question/" + targetID + "/follow";
            default:
                return null;
        }
    }

    public static String queryBookList(long myUserId) {
        return Urls.baseUrl + "json/user/" + myUserId + "/books";
    }


    public static String uploadImage() {
        return Urls.baseUrl + "json/image_upload";
    }

    public static String uploadQuestion() {
        return Urls.baseUrl + "json/question/add";
    }

    public static String uploadNote() {
        return Urls.baseUrl + "json/note/add";
    }

    public static String uploadAnswer(long questionId) {
        return Urls.baseUrl + "json/answer/add";
    }

    public static String queryUserDynamic() {
        return Urls.baseUrl + "json/user/dynamic";
    }

    public static String queryUserRelativeMessage() {
        return Urls.baseUrl + "json/user/related_message";
    }

    public static String search(ContentType type, String searchText) {
        StringBuilder sb = new StringBuilder();
        sb.append(Urls.baseUrl + "json/search/");
        switch (type) {
            case User:
                sb.append("user/");
                break;
            case Circle:
                sb.append("circle/");
                break;
            case Book:
                sb.append("book/");
                break;
            case Question:
                sb.append("question/");
                break;
            case Note:
                sb.append("note/");
                break;
            default:
                return null;
        }
        sb.append(searchText);

        try {
            byte[] bytes = sb.toString().getBytes("utf-8");
            return new String(bytes, "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String queryDialogByDialogId(long targetUserID) {
        return Urls.baseUrl + "json/pmessage/" + targetUserID + "/dp";
    }

    public static String queryUserBeFollowList(long targetUserID) {
        return Urls.baseUrl + "json/user/" + targetUserID + "/followers";
    }

    public static String queryUserFollowOtherList(long targetUserID) {
        return Urls.baseUrl + "json/user/" + targetUserID + "/followees";
    }

    public static String queryUserDynamic(long targetUserId) {
        return null;
    }

    public static String queryBookById(long bookId) {
        return Urls.baseUrl + "json/book/" + bookId;
    }

    public static String queryDialogByUserId(long targetUserId) {
        return Urls.baseUrl + "json/pmessage/user/" + targetUserId + "/dp";
    }

    public static String joinCircle(long circleID) {
        return Urls.baseUrl + "json/circle/" + circleID + "/join";
    }

    public static String withdrawJoin(long id) {
        return Urls.baseUrl + "json/circle/" + id + "/withdraw_join";
    }

    public static String queryUserFootPrint(long targetUserID) {
        return Urls.baseUrl + "json/user/" + targetUserID + "/footprint";
    }

    public static String queryAttachmentList(long bookID) {
        return Urls.baseUrl + "json/book/" + bookID + "/attachmentdps";
    }

    public static String queryAttachment(long attachmentId) {
        return Urls.baseUrl + "json/attachment_feed/" + attachmentId;
    }

    public static String queryAttachmentFileList(long attachmentId) {
        return Urls.baseUrl + "json/attachment/" + attachmentId + "/files";
    }

    public static String uploadFile() {
        return Urls.baseUrl + "json/attachment_upload";
    }

    public static String uploadAttachment() {
        return Urls.baseUrl + "json/attachment_feed/add";
    }

    public static String comment() {
        return Urls.baseUrl+"json/comment/add";
    }

    public static String searchPage(long bookId, int bookPage) {
        return Urls.baseUrl+"json/search/book/"+bookId+"/"+bookPage;
    }

    public static String queryQuestionListPart(long bookID) {
        return Urls.baseUrl+"json/book/"+bookID+"/questiondps/";
    }

    public static String queryAttachmentListPart(long bookID) {
        return Urls.baseUrl+"json/book/"+bookID+"/attachmentdps/";
    }

    public static String queryNoteListPart(long bookID) {
        return Urls.baseUrl+"json/book/"+bookID+"/notedps/";
    }

    public static String queryBookSectionList(long bookID) {
        return Urls.baseUrl+"json/book/"+bookID+"/sections";
    }

    public static String queryQuestionListInPage(long bookId) {
        return queryQuestionList(bookId)+"?type=page";
    }
}
