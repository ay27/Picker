package org.bitman.ay27.module;

/**
 * Created by ay27 on 14-11-5.
 */
public class AttachmentFile extends BaseModule {

    protected long id;
    protected String name;
    protected String path;
    protected long aFeedId;

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return name;
    }

    public void setFileName(String fileName) {
        this.name = fileName;
    }

    public String getFilePath() {
        return path;
    }

    public void setFilePath(String filePath) {
        this.path = filePath;
    }

    public long getaFeedId() {
        return aFeedId;
    }

    public void setaFeedId(long aFeedId) {
        this.aFeedId = aFeedId;
    }

    @Override
    public long getId() {
        return id;
    }
}
