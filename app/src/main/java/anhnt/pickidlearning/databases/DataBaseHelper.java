package anhnt.pickidlearning.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HCD-Fresher046 on 1/10/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DB_ANH_VIET_FAV = "anh_viet_fav";
    public static final String DB_TABLE = "anh_viet";
    public static final String COLUNM_ID = "_id";
    public static final String COLUNM_WORD = "word";
    public static final String COLUNM_CONTENT = "content";
    public static final String COLUNM_FAV = "fav";
    public static final String COLUNM_NOTE = "note";
    public static final String DB_NAME_ANH_VIET = "anh_viet.db";

    public DataBaseHelper(Context context, String dataBaseName) {
        super(context, dataBaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + DB_TABLE
                + "(" + COLUNM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUNM_WORD + " TEXT NOT NULL, "
                + COLUNM_CONTENT + " TEXT NOT NULL, "
                + COLUNM_FAV + " INTEGER NOT NULL, "
                + COLUNM_NOTE + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
    }

}
