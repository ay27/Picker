package org.bitman.ay27.module;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Section extends BaseModule implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final List<Section> NULL_SUB_SECTIONS = new ArrayList<Section>();
    private long id;

    private int bookId;

    // 1.1 1.1.2 ...
    private String num;

    private String name;

    private int startPage;

    private int endPage;

    private List<Section> subSections;

    private int type;

    public Section() {

    }

    public Section(long id, int bookId, String num, String name, int startPage, int endPage,
                   List<Section> subSections) {
        super();
        this.id = id;
        this.bookId = bookId;
        this.num = num;
        this.name = name;
        this.startPage = startPage;
        this.endPage = endPage;
        this.subSections = subSections;
    }

    public Section(int bookId, String num, String name, int startPage, int endPage,
                   List<Section> subSections) {
        this.bookId = bookId;
        this.num = num;
        this.name = name;
        this.startPage = startPage;
        this.endPage = endPage;
        this.subSections = subSections;
    }

    public Section(int bookId, String num, String name, int startPage, int endPage) {
        this.bookId = bookId;
        this.num = num;
        this.name = name;
        this.startPage = startPage;
        this.endPage = endPage;
        this.subSections = NULL_SUB_SECTIONS;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public List<Section> getSubSections() {
        return subSections;
    }

    public void setSubSections(List<Section> subSections) {
        this.subSections = subSections;
    }

    public boolean hasSubSections() {
        return this.subSections!=null && this.subSections.size() != 0;
    }

    @Override
    public String toString() {
        return "Section [id=" + id + ", bookId=" + bookId + ", num=" + num
                + ", name=" + name + ", startPage=" + startPage + ", endPage="
                + endPage + ", subSections=" + subSections + "]";
    }


}
