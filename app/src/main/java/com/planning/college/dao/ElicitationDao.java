package com.planning.college.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.planning.college.model.Article;
import com.planning.college.tools.DBOpenHelper;

/**
 * Created by KUIKUI on 2018-05-21.
 */

public class ElicitationDao {


    public boolean saveArticle(Article article){
       // SQLiteDatabase db = new DBOpenHelper(this,null,null,1).getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("title",article.getTitle());
        values.put("intro",article.getIntro());
        values.put("content_link",article.getContent_link());
        values.put("type",article.getType());
        values.put("status",article.getStatus());
        values.put("update_date",article.getUpdate_date());




        return false;
    }
}
