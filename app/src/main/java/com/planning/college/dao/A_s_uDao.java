package com.planning.college.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.planning.college.model.A_s_u;
import com.planning.college.tools.DBOpenHelper;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by KUIKUI on 2018-08-02.
 */

@EBean
public class A_s_uDao {


    @RootContext
    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean deleteA_s_uByA_no(String a_no){
        SQLiteDatabase sqLiteDatabase = new DBOpenHelper(context,null,null,1).getWritableDatabase();

           try{
            sqLiteDatabase.execSQL("delete from a_s_u where a_no = "+a_no);


           } catch (Exception e){
               e.printStackTrace();
               return false;
           } finally {
               sqLiteDatabase.close();
           }


        return true;
    }

    public boolean deleteA_s_uByA_noS_no(String a_no,String s_no){
        SQLiteDatabase sqLiteDatabase = new DBOpenHelper(context,null,null,1).getWritableDatabase();

           try{
            sqLiteDatabase.execSQL("delete from a_s_u where a_no = "+a_no+"  and s_no = "+s_no);


           } catch (Exception e){
               e.printStackTrace();
               return false;
           } finally {
               sqLiteDatabase.close();
           }


        return true;
    }

    public boolean insertA_s_u(A_s_u asu){

        SQLiteDatabase db = new DBOpenHelper(context,null,null,1).getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("a_no",asu.getA_no());
            values.put("s_no",asu.getS_no());
            values.put("u_id",asu.getU_id());
            db.insertOrThrow("a_s_u",null,values);
        }catch (SQLiteConstraintException e){

            return false;
        }finally {
            db.close();
        }
        return true;
    }

    public List<A_s_u> findA_s_uByA_no(String a_no){
        List<A_s_u> asuList = new ArrayList<>();

        DBOpenHelper dbOpenHelper = new DBOpenHelper(context,null,null,1);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from a_s_u " +
                " where a_no = ? ", new String[]{a_no});

        if(c != null){
            while(c.moveToNext()){
                A_s_u asu = new A_s_u();
                asu.setA_no(c.getString(c.getColumnIndex("a_no")));
                asu.setS_no(c.getString(c.getColumnIndex("s_no")));
                asu.setU_id(c.getString(c.getColumnIndex("u_id")));
                asuList.add(asu);
            }
            c.close();
        }
        return asuList;
    }

    public A_s_u findA_s_uByA_noS_no(String a_no,String s_no){
      //  List<A_s_u> asuList = new ArrayList<>();
        A_s_u asu = null;
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context,null,null,1);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from a_s_u " +
                " where a_no = ? and s_no = ? ", new String[]{a_no,s_no});

        if(c != null){
            if(c.moveToNext()){
                asu = new A_s_u();
                asu.setA_no(c.getString(c.getColumnIndex("a_no")));
                asu.setS_no(c.getString(c.getColumnIndex("s_no")));
                asu.setU_id(c.getString(c.getColumnIndex("u_id")));
                //asuList.add(asu);
            }
            c.close();
        }
        return asu;
    }


}
