package org.bitman.ay27.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import org.bitman.ay27.PickerApplication;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-27.
 */
public class PickerContentProvider extends ContentProvider {

    private static final String TAG = "PickerContentProvider";

    private static final UriMatcher sUriMatcher;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        for (int i = 0; i < DataTable.values().length; i++) {
            DataTable table = DataTable.values()[i];
            sUriMatcher.addURI(DataBase.AUTHORITY, table.getDisplayName(), i);
        }
    }

    public static String getMatchTable(Uri uri) {
        int index = sUriMatcher.match(uri);
        DataTable table = DataTable.getTableByIndex(index);
        if (table!=null)
            return table.getDisplayName();
        else
            throw new IllegalArgumentException("Illegal Uri : " + uri);
    }

    private Uri getMatchContentUri(Uri uri) {
       int index = sUriMatcher.match(uri);
        DataTable table = DataTable.getTableByIndex(index);
        if (table!=null)
            return DataTable.getContentUri(table);
        else
            throw new IllegalArgumentException("Illegal Uri: "+uri);

    }


    @Override
    public String getType(Uri uri) {
        int index = sUriMatcher.match(uri);
        if (index>=0 && index<DataTable.getTableCount())
            return DataBase.CONTENT_TYPE;
        else
            throw new IllegalArgumentException("Illegal Uri: "+uri);
    }



    public static InnerHelper mDBHelper;
    public static SQLiteOpenHelper getDBHelper() {
        if (mDBHelper == null)
            mDBHelper = new InnerHelper(PickerApplication.getContext());
        return mDBHelper;
    }

    public final static Object DBLock = new Object();

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = getMatchTable(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(table);

        // TODO : add the other args.

        SQLiteDatabase db = getDBHelper().getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // 这里是关键
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        synchronized (DBLock) {
            String table = getMatchTable(uri);

            SQLiteDatabase db = getDBHelper().getWritableDatabase();

            long rowID = -1;
            db.beginTransaction();
            try {
                rowID = db.insert(table, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);

            if (rowID > 0)
                return ContentUris.withAppendedId(getMatchContentUri(uri), rowID);
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            String table = getMatchTable(uri);
            SQLiteDatabase db = getDBHelper().getWritableDatabase();

            int count = 0;
            db.beginTransaction();
            try {
                count = db.delete(table, selection, selectionArgs);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);

            return count;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            int count;
            String table = getMatchTable(uri);
            db.beginTransaction();
            try {
                count = db.update(table, values, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);

            return count;
        }
    }
}
