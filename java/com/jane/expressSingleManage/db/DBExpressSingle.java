package com.jane.expressSingleManage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jane.expressSingleManage.doman.ExpressSingle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jane on 2015/4/4.
 */
public class DBExpressSingle {
    public static final String DB_NAME = "expressSingleManage.db";
    public static final int DB_VERSION = 1;
    public static final String TB_NAME = "expressSingle";

    public static final String ID = "id";
    public static final String NUMBER ="numberStr";
    public static final String STATUS ="status";
    public static final String CREATETIME ="createTime";
    public static final String UPDATETIME ="updateTime";

    private static final String TAG = "DBExpressSingle";

    private SqliteHelper dbHelper = null;
    private SQLiteDatabase db = null;

    private static DBExpressSingle dbExpressSingle = null;
    public static DBExpressSingle getInstance(Context context){
        if(dbExpressSingle == null){
            dbExpressSingle = new DBExpressSingle(context);
        }
        return dbExpressSingle;
    }

    private DBExpressSingle(Context context){
        dbHelper = new SqliteHelper(context,DB_NAME,null,DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void Close() {
        db.close();
        dbHelper.close();
    }

    public long insert(ExpressSingle info){
        ContentValues values = new ContentValues();
        values.put(NUMBER, info.getNumber());
        values.put(STATUS, info.getStatus());
        values.put(CREATETIME,info.getCreateTime());
        values.put(UPDATETIME,info.getUpdateTime());
        long id = db.insert(TB_NAME, null, values);
        Log.d(TAG,"insert id:"+id);
        return id;
    }

    public ExpressSingle findByNumber(String number){
        Cursor cursor = db.rawQuery("select * from "+TB_NAME+" where "+NUMBER+"=?",new String[]{number});
        ExpressSingle info = null;
        if(cursor.moveToFirst()) {
            info = createModel(cursor);
        }
        cursor.close();
        return info;
    }

    // 更新ExpressSingle表的记录
    public int update(ExpressSingle info) {
        ContentValues values = new ContentValues();
        values.put(NUMBER, info.getNumber());
        values.put(STATUS, info.getStatus());
        values.put(CREATETIME,info.getCreateTime());
        values.put(UPDATETIME,info.getUpdateTime());
        int id = db.update(TB_NAME, values, NUMBER + "="
                + info.getNumber(), null);
        Log. e(TAG, "update id : "+id);
        return id;
    }

    // 获取users表中的UserID、Access Token、Access Secret的记录
    public List<ExpressSingle> findAll() {
        List<ExpressSingle> list = new ArrayList<ExpressSingle>();
        Cursor cursor = db.query(TB_NAME, null, null , null, null,
                null, UPDATETIME + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null )) {
            ExpressSingle info = createModel(cursor);
            list.add(info);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ExpressSingle createModel(Cursor cursor){
        ExpressSingle info = new ExpressSingle();
        info.setId(cursor.getInt(cursor.getColumnIndex(ID)));
        info.setNumber(cursor.getString(cursor.getColumnIndex(NUMBER)));
        info.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
        info.setCreateTime(cursor.getString(cursor.getColumnIndex(CREATETIME)));
        info.setUpdateTime(cursor.getString(cursor.getColumnIndex(UPDATETIME)));
        return info;
    }


    // 根据条件查询
    public List<ExpressSingle> findBy(String key,String value) {
        Log.d(TAG,"key:"+key+",value:"+value);
        List<ExpressSingle> list = new ArrayList<ExpressSingle>();
        Cursor cursor = db.query(TB_NAME, null, key+" like ?" , new String[]{"%"+value+"%"}, null,
                null, UPDATETIME + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null )) {
            ExpressSingle info = createModel(cursor);
            list.add(info);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
