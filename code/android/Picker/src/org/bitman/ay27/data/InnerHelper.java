package org.bitman.ay27.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-27.
 */
class InnerHelper extends SQLiteOpenHelper {
    public InnerHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public InnerHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public InnerHelper(Context context) {
        super(context, DataBase.DATABASE_NAME, null, DataBase.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (DataTable table : DataTable.values()) {
            db.execSQL(DataTable.sql_createTable(table));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP = "drop table if exists ";

        for (DataTable table : DataTable.values()) {
            db.execSQL(DROP+table.getDisplayName());
        }
        onCreate(db);
    }

}
