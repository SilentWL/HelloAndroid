package com.example.administrator.contentprovider;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
/**
 * 这个类继承SQLiteOpenHelper抽象类，用于创建数据库和表。创建数据库是调用它的父类构造方法创建。
 * @author HB
 */
public class DBOpenHelper extends SQLiteOpenHelper {


    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    public DBOpenHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }
    /**
     * 只有当数据库执行创建 的时候，才会执行这个方法。如果更改表名，也不会创建，只有当创建数据库的时候，才会创建改表名之后 的数据表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ConstData.UserTableData.TABLE_NAME
                + "(" + ConstData.UserTableData._ID
                + " INTEGER PRIMARY KEY autoincrement,"
                + ConstData.UserTableData.NAME + " varchar(20),"
                + ConstData.UserTableData.TITLE + " varchar(20),"
                + ConstData.UserTableData.DATE_ADDED + " long,"
                + ConstData.UserTableData.SEX + " boolean)" + ";");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
