package org.bitman.ay27.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.google.gson.Gson;
import org.bitman.ay27.module.BaseModule;

import java.util.List;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-28.
 */
public class DataHelper{

    private ContentResolver cr;
    private Context context;

    public DataHelper(Context context, ContentResolver cr) {
        this.cr = cr;
        this.context = context;
    }


    public Cursor queryOne(Uri uri, String key, Object value) {
        return cr.query(uri, null, key+"=?", new String[]{value.toString()}, null);
    }

    public final Cursor query(Uri uri, String[] projection,
                              String selection, String[] selectionArgs, String sortOrder) {
        return cr.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    public final Cursor queryAll(Uri uri) {
        return cr.query(uri, null, null, null, null);
    }

    public final int delete(Uri url, String where, String[] selectionArgs) {
        int result;
//        synchronized (PickerContentProvider.DBLock) {
            result = cr.delete(url, where, selectionArgs);
//        }
        return result;
    }

    public final int deleteAll(Uri uri) {
        int row;
//        synchronized (PickerContentProvider.DBLock) {
            row = cr.delete(uri, null, null);
//        }
        return row;
    }

    public final Uri insert(Uri url, ContentValues values) {
        return cr.insert(url, values);
    }

    public <T extends BaseModule> Uri insert(Uri uri, T obj) {

        if (obj == null)
            return null;

        ContentValues values = new ContentValues();
        values.put(DataTable.KEY_ID, obj.getId());
        values.put(DataTable.KEY_JSON, new Gson().toJson(obj));

        Uri result;
//        synchronized (PickerContentProvider.DBLock) {
            result = cr.insert(uri, values);
//        }
        return result;
    }

    public <T extends BaseModule> Uri insert(Uri url, String json, Class<T> cls) {

        if (json == null || json.isEmpty())
            return null;

        Uri result;
//        synchronized (PickerContentProvider.DBLock) {
            result = cr.insert(url, generateContentValues(json, cls));
//        }
        return result;
    }

    public <T extends BaseModule> void bulkInsert(Uri uri, List<T> data) {
        if (data == null || data.isEmpty())
            return;
        for (int i = 0; i < data.size(); i++) {
            insert(uri, data.get(i));
        }
//        for (T item : data) {
//            insert(uri, new Gson().toJson(item), cls);
//        }
    }

    /**
     * sqlite_master 是系统表，管理着系统中所有的数据库
     * 只要cursor中存在数据，getInt方法就不会返回0
     * 如果cursor没有数据，那个moveToNext都执行不通过
     */
    public static boolean isTableExist(String tableName) {
        SQLiteOpenHelper dbHelper = PickerContentProvider.getDBHelper();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "select DrawerItemCount(*) from sqlite_master where type='table' and name='"+tableName.trim()+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            if (cursor.getInt(0) > 0) {
                return true;
            }
        }
        return false;
    }


    public static <T extends BaseModule> ContentValues generateContentValues(String json, Class<T> cls) {
        ContentValues contentValues = new ContentValues();

        BaseModule obj = new Gson().fromJson(json, cls);

        contentValues.put(DataTable.KEY_ID, obj.getId());
        contentValues.put(DataTable.KEY_JSON, json);

        return contentValues;
    }
}
