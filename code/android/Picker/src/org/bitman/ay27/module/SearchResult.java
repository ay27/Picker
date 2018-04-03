package org.bitman.ay27.module;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-10-3.
 */
public class SearchResult extends BaseModule {

    public static final int RESULT_USER = 0;
    public static final int RESULT_CIRCLE = 1;
    public static final int RESULT_BOOK = 2;
    public static final int RESULT_QUESTION = 3;
    public static final int RESULT_NOTE = 4;
    public static final int RESULT_AFEED = 5;

    private long id = -1;
    private String imageUrl;
    private String content;
    private String name;
    private int type;

    @Override
    public long getId() {
        return (id==-1)? id=System.currentTimeMillis() : id ;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
