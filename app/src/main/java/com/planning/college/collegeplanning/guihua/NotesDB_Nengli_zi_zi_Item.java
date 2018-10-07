package com.planning.college.collegeplanning.guihua;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class NotesDB_Nengli_zi_zi_Item extends SQLiteOpenHelper{

    public static final String NENGLIITEM = "nengliitem";
    public static final String CONTENT = "content";
    public static final String PATH = "path";
    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String TYPE = "type";
    public static final String TIMETOTIME= "timetotime";
    public static final String BEIZHU = "beizhu";
    public static final String NENGLIMING = "nengliming";
    public static final String KAISHITIME = "kaishitime";
    public static final String JIESHUTIME = "jieshutime";
    public static final String KAISHIYEAR = "kaishiyear";
    public static final String JIESHUYEAR = "jieshuyear";
    public static final String SCORE = "score";

    public NotesDB_Nengli_zi_zi_Item(Context context){
        super(context,"nengliitem",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + NENGLIITEM + "(" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTENT
                + " TEXT," + SCORE
                + " TEXT," + TYPE
                + " TEXT," + BEIZHU
                + " TEXT," + TIMETOTIME
                + " TEXT," + NENGLIMING
                + " TEXT," + KAISHITIME
                + " TEXT," + JIESHUTIME
                + " TEXT," + KAISHIYEAR
                + " TEXT," + JIESHUYEAR
                + " TEXT," + PATH
                + " INTEGER," + TIME
                + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
