package com.planning.college.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.planning.college.dao.ElicitationDao;

/**
 * Created by KUIKUI on 2018-05-21.
 *
 */

public class DBOpenHelper extends SQLiteOpenHelper {



    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "cp.db", factory, version);//初次执行时会创建cp.db数据库文件


    }

    @Override
    /**
     * 首次创建数据库的时候调用  一般可以把建库  建表的操作放到此处
     */
    public void onCreate(SQLiteDatabase db) {


        /**
         * 建立文章的表
         */
        db.execSQL("create table if not exists article(_id integer primary key autoincrement," +
                " title text not null, " +
                " intro text not null, " +
                " content_link text not null, " +
                " type text not null, " +
                " status text not null, " +
                " update_date text not null)");

        db.execSQL("create table if not exists a_s_u(" +
                "a_no text not null," +
                "s_no text not null," +
                "u_id text not null )");

    }
    /**
     * 当数据库的版本发生变化的时候会自动执行
     */
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
