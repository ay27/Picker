package org.bitman.ay27.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-29.
 */
public enum DataTable implements BaseColumns {
    user, book, question, note,
    answer, comment, circle, circleMemberDP,
    dialog, follow, favorite, privateLetter,
    commentDP, answerDP, noteDP, privateLetterDP,
    questionDP, userDP, message, pmessageSum,
    search, beFollow, followOther, circleDP, attachmentDp, attachmentFile;

    public static final String KEY_ID = _ID;
    public static final String KEY_JSON = "json";

    public static int getTableCount() {
        return DataTable.values().length;
    }

    public static DataTable getTableByIndex(int index) {
        if (index < 0 || index >= getTableCount())
            return null;

        return DataTable.values()[index];
    }

    public static String sql_createTable(DataTable table) {
        return "create table if not exists " + table.getDisplayName()
                + "( " +
                " _id integer primary key," +
                " json ntext," +
                " page integer," +
                " time timestamp " +
                "  );";
    }

    public static Uri getContentUri(DataTable table) {
        return Uri.parse(DataBase.SCHEME + DataBase.AUTHORITY + "/" + table.getDisplayName());
    }

    public String getDisplayName() {
        return this.name();
    }

}
