package org.bitman.ay27.upload_download;

import java.io.File;
import java.util.ArrayList;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14/11/14.
 */
public class BroadcastNode {

    private static ArrayList<APublisher> publishers;
    private static BroadcastNode instance;

    public static class APublisher {
        public long id;
        public String fileName;
        public Integer status;
        public int process;
        public int fileSize, currentFileSize;
        public Exception exception;
        public S_P_Callback callback;
        public S_P_Callback subscribe;
        public static final int STATUS_WAITING = 0x0;
        public static final int STATUS_RUNNING = 0x1;
        public static final int STATUS_FINISHED = 0x2;
        public static final int STATUS_ERROR = 0x3;
        public static final int STATUS_PAUSE = 0x4;
    }

    private BroadcastNode() {
        publishers = new ArrayList<APublisher>();
    }

    public static BroadcastNode getInstance() {
        if (instance == null)
            instance = new BroadcastNode();
        return instance;
    }

    public static ArrayList<APublisher> getPublishers() {
        return publishers;
    }

    public boolean registerSubscribe(long id, S_P_Callback subscribe) {
        for (APublisher publisher : publishers) {
            if (publisher.id == id) {
                // 避免并发问题，这里需要对status加锁
                synchronized (publisher.status) {
                    if (publisher.status == APublisher.STATUS_FINISHED || publisher.status == APublisher.STATUS_ERROR)
                        return false;
                    publisher.subscribe = subscribe;
                    return true;
                }
            }
        }
        return false;
    }

    public APublisher getPublisherInfo(long id) {
        for (APublisher publisher : publishers) {
            if (publisher.id == id)
                return publisher;
        }
        return null;
    }

    public boolean unregisterSuscribe(long id) {
        for (APublisher publisher : publishers) {
            if (publisher.id == id) {
                publisher.subscribe = null;
                return true;
            }
        }
        return false;
    }

    public S_P_Callback generatePublisher() {
        final APublisher publisher = new APublisher();
        publisher.callback = new S_P_Callback() {
            @Override
            public void onProgress(int progress) {
                publisher.process = progress;
                publisher.status = APublisher.STATUS_RUNNING;
                if (publisher.subscribe != null) {
                    publisher.subscribe.onProgress(progress);
                }
            }

            @Override
            public void onError(Exception e) {
                publisher.exception = e;
                publisher.status = APublisher.STATUS_ERROR;
                if (publisher.subscribe != null) {
                    publisher.subscribe.onError(e);
                }
            }

            @Override
            public void onFinished(String serverResponseMessage, File file) {
                // 避免在获取status时就跑完了这一段，所以需要对status加锁
                // 用以保证，注册进来的subscribe至少会获得一次的callback反馈
                synchronized (publisher.status) {
                    publisher.process = 100;
                    publisher.status = APublisher.STATUS_FINISHED;
                    if (publisher.subscribe != null) {
                        publisher.subscribe.onFinished(serverResponseMessage, file);
                    }
                }
            }

            @Override
            public void onStart(long id, String fileName) {
                publisher.id = id;
                publisher.fileName = fileName;
                publisher.status = APublisher.STATUS_WAITING;
                if (publisher.subscribe != null) {
                    publisher.subscribe.onStart(id, fileName);
                }
            }

            @Override
            public void onPause(int currentFileSize, int fileSize) {
                publisher.fileSize = fileSize; publisher.currentFileSize = currentFileSize;
                publisher.status = APublisher.STATUS_PAUSE;
                if (publisher.subscribe != null) {
                    publisher.subscribe.onPause(currentFileSize, fileSize);
                }
            }
        };
        publishers.add(publisher);
        return publisher.callback;
    }

    public void destroy() {
        publishers = null;
        instance = null;
    }

}
