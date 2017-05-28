package anhnt.pickidlearning.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import anhnt.pickidlearning.models.TuDien;

/**
 * Created by AnhNT on 5/28/2017.
 */

public class MyAssetsDatabase extends SQLiteAssetHelper {
    public static final String DB_NAME = "anh_viet";
    SQLiteDatabase database;

    public MyAssetsDatabase(Context context) {
        super(context, DB_NAME, null, 1);
    }

    public void open() {
        database = getWritableDatabase();
    }

    public void close() {
        database.close();
    }


    public List<TuDien> getDictionary(String word1) {
        List<TuDien> tuDiens = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT  * From " + word1, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            TuDien tuDien = new TuDien(id, word, content, 0);
            tuDiens.add(tuDien);
            cursor.moveToNext();
        }
        return tuDiens;
    }

    public List<TuDien> getAnhVietsByWord(String word1) {
        Cursor cursor = database.rawQuery("select * from " + word1 + " limit 200", null);
        cursor.moveToFirst();
        List<TuDien> tuDiens = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            TuDien tuDien = new TuDien(id, word, content, 0);
            tuDiens.add(tuDien);
            cursor.moveToNext();
        }
        cursor.close();
        return tuDiens;
    }

    public List<TuDien> getTuDienByWord(char dbTable, char[] word1) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < word1.length; i++) {
            buffer.append(word1[i]);
        }
        Cursor cursor = database.rawQuery("select * from " + dbTable + " where " + DataBaseHelper.COLUNM_WORD + " like '" + buffer + "%'", null);
        cursor.moveToFirst();
        List<TuDien> tuDiens = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            TuDien tuDien = new TuDien(id, word, content, 0);
            tuDiens.add(tuDien);
            cursor.moveToNext();
        }
        cursor.close();
        return tuDiens;
    }

    public String getContentByWord(char tableName, String word) {
        Cursor cursor = database.rawQuery("SELECT content FROM " + tableName + " where word = '" + word + "'", null);
        cursor.moveToFirst();
        String htmlContent = cursor.getString(cursor.getColumnIndex("content"));
        return htmlContent;
    }

    public TuDien getTuDien(String word) {
        Cursor cursor = database.rawQuery("select * from " + word.charAt(0) + " where " + DataBaseHelper.COLUNM_WORD + " = '" + word + "'", null);
        cursor.moveToFirst();
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String word1 = cursor.getString(cursor.getColumnIndex("word"));
        String content = cursor.getString(cursor.getColumnIndex("content"));
        TuDien tuDien = new TuDien(id, word1, content, 0);
        return tuDien;
    }

}
