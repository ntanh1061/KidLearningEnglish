package anhnt.pickidlearning.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import anhnt.pickidlearning.models.TuDien;

/**
 * Created by AnhNT on 1/11/2017.
 */

public class MyDatabaseFav {
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase database;

    public MyDatabaseFav(Context context) {
        dataBaseHelper = new DataBaseHelper(context, dataBaseHelper.DB_ANH_VIET_FAV);
        database = dataBaseHelper.getWritableDatabase();
    }

    public long insertDatabase(TuDien tuDien) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dataBaseHelper.COLUNM_ID, tuDien.getId());
        contentValues.put(dataBaseHelper.COLUNM_WORD, tuDien.getWord());
        contentValues.put(dataBaseHelper.COLUNM_CONTENT, tuDien.getContent());
        contentValues.put(dataBaseHelper.COLUNM_FAV, tuDien.getFav());
        contentValues.put(dataBaseHelper.COLUNM_NOTE, tuDien.getNote());
        return database.insert(dataBaseHelper.DB_TABLE, null, contentValues);
    }

    public long insertNewWord(TuDien tuDien) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dataBaseHelper.COLUNM_WORD, tuDien.getWord());
        contentValues.put(dataBaseHelper.COLUNM_CONTENT, tuDien.getContent());
        contentValues.put(dataBaseHelper.COLUNM_FAV, tuDien.getFav());
        contentValues.put(dataBaseHelper.COLUNM_NOTE, tuDien.getNote());
        return database.insert(dataBaseHelper.DB_TABLE, null, contentValues);
    }


    public List<TuDien> getTuDienFav() {
        List<TuDien> tuDiens = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " + DataBaseHelper.DB_TABLE, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUNM_ID));
            String word = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUNM_WORD));
            String htmlContent = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUNM_CONTENT));
            int fav = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUNM_FAV));
            String note = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUNM_NOTE));
            TuDien tuDien = new TuDien(note, id, word, htmlContent, fav);
            tuDiens.add(tuDien);
            cursor.moveToNext();
        }
        cursor.close();
        return tuDiens;
    }

    public void updateTuDienFav(int fav, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.COLUNM_FAV, fav);
        database.update(DataBaseHelper.DB_TABLE, contentValues, DataBaseHelper.COLUNM_ID + " = '" + id + "'", null);
    }

    public List<TuDien> getTuDienByWord(String word1) {
        Cursor cursor = database.rawQuery("select * from " + DataBaseHelper.DB_TABLE + " where " + DataBaseHelper.COLUNM_WORD + " like '" + word1 + "'", null);
        cursor.moveToFirst();
        List<TuDien> tuDiens = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            int fav = cursor.getInt(cursor.getColumnIndex("fav"));
            String note = cursor.getString(cursor.getColumnIndex("note"));
            TuDien tuDien = new TuDien(note, id, word, content, fav);
            tuDiens.add(tuDien);
            cursor.moveToNext();
        }
        cursor.close();
        return tuDiens;
    }

    public void updateNode(String note, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.COLUNM_NOTE, note);
        database.update(DataBaseHelper.DB_TABLE, contentValues, DataBaseHelper.COLUNM_ID + " = '" + id + "'", null);
    }

    public String getNote(int id) {
        Cursor cursor = database.rawQuery("select note from anh_viet where _id = '" + id + "'", null);
        cursor.moveToFirst();
        String note = cursor.getString(cursor.getColumnIndex("note"));
        return note;
    }

    public List<TuDien> getFav() {
        List<TuDien> tuDiens = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " + DataBaseHelper.DB_TABLE + " where " + DataBaseHelper.COLUNM_ID + " = '1'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String note = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUNM_NOTE));
            String word = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUNM_WORD));
            String content = cursor.getString(cursor.getColumnIndex(DataBaseHelper.COLUNM_CONTENT));
            int id = cursor.getInt(cursor.getColumnIndex(DataBaseHelper.COLUNM_ID));
            TuDien tuDien = new TuDien(note, id, word, content, 1);
            tuDiens.add(tuDien);
            cursor.moveToNext();
        }
        cursor.close();
        return tuDiens;
    }

    public long updateTuDien(TuDien tuDien) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.COLUNM_NOTE, tuDien.getNote());
        contentValues.put(DataBaseHelper.COLUNM_WORD, tuDien.getWord());
        contentValues.put(DataBaseHelper.COLUNM_CONTENT, tuDien.getContent());
        return database.update(DataBaseHelper.DB_TABLE, contentValues,DataBaseHelper.COLUNM_ID + " = '" + tuDien.getId() + "'", null);
    }

    public int deleteYourWord(TuDien tuDien) {
        return database.delete(DataBaseHelper.DB_TABLE, DataBaseHelper.COLUNM_ID + " = '" + tuDien.getId() + "'", null);
    }

    public void close() {
        database.close();
    }
}