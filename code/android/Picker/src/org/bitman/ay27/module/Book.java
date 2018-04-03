package org.bitman.ay27.module;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-24.
 */
public class Book extends BaseModule  {

    public static final int LENGTH_OF_ISBN = 13;

    private long id;
    private String isbn;
    private String bookName;
    private String writer;
    private String press;
    private int followNum;
    private int noteNum;
    private int questionNum;
    private String imageUrl;
    private boolean hasInventory;


    public Book() {
    }

    public Book(String ISBN) {
        this.isbn = ISBN;
    }

    public Book(String ISBN, String name, long _id) {
        this.isbn = ISBN;
        this.bookName = name;
        this.id = _id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return bookName;
    }

    public void setName(String name) {
        this.bookName = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getNoteNum() {
        return noteNum;
    }

    public void setNoteNum(int noteNum) {
        this.noteNum = noteNum;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public String getCoverUrl() {
        return imageUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.imageUrl = coverUrl;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

}
