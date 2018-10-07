package com.planning.college.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.planning.college.model.Article;
import com.planning.college.tools.DBOpenHelper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KUIKUI on 2018-08-02.
 */

@EBean
public class ArticleDao {


    @RootContext
    Context context;


    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 根据文章编号删除，注意：
     * 在sqlite中，a_no是int型，但在服务器端数据库中，a_no是字符串型
     * @param a_no
     * @return
     */
    public boolean deleteArticleByA_no(String a_no){
        Log.i("tag",context+"");
        SQLiteDatabase sqLiteDatabase = new DBOpenHelper(context,null,null,1).getWritableDatabase();
        try{

            sqLiteDatabase.execSQL("delete from article where _id = "+a_no);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            sqLiteDatabase.close();
        }


        return true;
    }
    /**
     * 向数据库中保存article
     * @param article
     */
    public long insertArticle(Article article){
        SQLiteDatabase db = new DBOpenHelper(context,null,null,1).getWritableDatabase();

        long rowId = -1;
        try{
            ContentValues values = new ContentValues();
            values.put("title",article.getTitle());
            values.put("intro",article.getIntro());
            values.put("content_link",article.getContent_link());
            values.put("type",article.getType());
            values.put("status",article.getStatus());
            values.put("update_date",article.getUpdate_date());

            rowId = db.insertOrThrow("article", null, values);
            //db.setTransactionSuccessful();

            Log.i("id",rowId+"");

        }catch (SQLiteConstraintException e){

            e.printStackTrace();
        }finally {
            db.close();
        }

        return rowId;
    }



    public Boolean updateArticleForUpdateDate(Article article){

        try{
            String contentLink = article.getContent_link();
            SQLiteDatabase db = new DBOpenHelper(context,null,null,1).getWritableDatabase();
            db.execSQL("update article set title = ?, intro = ?, update_date = ? where content_link = ?"
                    ,new String[]{article.getTitle(),article.getIntro(),article.getUpdate_date()
                            ,article.getContent_link()});
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
public Boolean updateArticle(Article article){

        try{
            String contentLink = article.getContent_link();
            SQLiteDatabase db = new DBOpenHelper(context,null,null,1).getWritableDatabase();
            db.execSQL("update article set title = ?, intro = ?, content_link = ?,type = ?, status = ?,update_date = ? "
                            + " where _id = ?"
                    ,new String[]{article.getTitle(),article.getIntro(),article.getContent_link(),
                            article.getType(),article.getStatus(),article.getUpdate_date(),article.getA_no()});
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }



    public List<Article> getAllArticle(){
        List<Article> articleList = new ArrayList<>();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context,null,null,1);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from article order by update_date desc",null);
        int coun = c.getCount();
        Article article = null;
        while(c.moveToNext()){
            article = new Article();
            article.setA_no(c.getString(c.getColumnIndex("_id")));
            article.setTitle(c.getString(c.getColumnIndex("title")));
            article.setIntro(c.getString(c.getColumnIndex("intro")));
            article.setContent_link(c.getString(c.getColumnIndex("content_link")));
            article.setType(c.getString(c.getColumnIndex("type")));
            article.setStatus(c.getString(c.getColumnIndex("status")));
            article.setUpdate_date(c.getString(c.getColumnIndex("update_date")));

            articleList.add(article);
        }
        c.close();
        db.close();
        dbOpenHelper.close();

        return articleList;
    }

    public Article findArticleByid(String a_no){
        Article article = new Article();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context,null,null,1);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        //Integer[] arr = {Integer.parseInt(a_no)};
        Cursor c = db.rawQuery("select * from article where _id = ?",new String[]{a_no});


        if(c != null){
            int count = c.getCount();
            if(c.moveToNext()){
                article.setA_no(c.getString(c.getColumnIndex("_id")));
                article.setTitle(c.getString(c.getColumnIndex("title")));
                article.setIntro(c.getString(c.getColumnIndex("intro")));
                article.setContent_link(c.getString(c.getColumnIndex("content_link")));
                article.setType(c.getString(c.getColumnIndex("type")));
                article.setStatus(c.getString(c.getColumnIndex("status")));
              String test =  c.getString(c.getColumnIndex("update_date"));
                article.setUpdate_date(c.getString(c.getColumnIndex("update_date")));
            }

        }

        return article;
    }

}
