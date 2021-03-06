package com.planning.college.collegeplanning.guihua;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/10/24 0024.
 */

public class NotesDB_Nengli_zi_item extends SQLiteOpenHelper{

    public static final String ZIITEM = "ziitem";
    public static final String CONTENT = "content";
    public static final String PATH = "path";
    public static final String VIDEO = "video";
    public static final String TITLE = "title";
    public static final String ID = "_id";
    public static final String TIME = "time";

    public NotesDB_Nengli_zi_item(Context context){
        super(context,"ziitem",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + ZIITEM + "(" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTENT
                + " TEXT," + TITLE
                + " TEXT," + PATH
                + " INTEGER," + VIDEO
                + " TEXT," + TIME
                + " TEXT)");
    }
    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
