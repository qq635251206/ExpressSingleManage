package com.jane.expressSingleManage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jane on 2015/4/4.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    private static final String TAG ="SqliteHelper";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE IF NOT EXISTS "+
                        DBExpressSingle.TB_NAME+ "("+
                        DBExpressSingle.ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        DBExpressSingle.NUMBER+ " text,"+
                        DBExpressSingle.STATUS+ " integer,"+
                        DBExpressSingle.CREATETIME+ " text,"+
                        DBExpressSingle.UPDATETIME+ " text"+
                        ")"
        );
        Log. e(TAG ,"onCreate" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + DBExpressSingle.TB_NAME );
        onCreate(db);
        Log. e(TAG, "onUpgrade");
    }
}
