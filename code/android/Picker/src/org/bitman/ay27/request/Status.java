package org.bitman.ay27.request;

public class Status {
    private Status() {
    }

    public static final int SUCCESS = 0;
    public static final int ERROR = 1;
    public static final int EXISTS = 2;
    public static final int NOT_EXISTS = 3;
    public static final int UNCHANGED = 4;
    public static final int NULLPOINTER = 5;
    public static final int UNKNOWN = 6;
    public static final int INVALID = 7;
}
