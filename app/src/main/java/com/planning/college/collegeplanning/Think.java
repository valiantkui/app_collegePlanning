package com.planning.college.collegeplanning;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.planning.college.collegeplanning.guihua.Add_Jihua;
import com.planning.college.collegeplanning.guihua.ItemClickSupport;
import com.planning.college.collegeplanning.guihua.Myadapter;
import com.planning.college.collegeplanning.guihua.NotesDB;
import com.planning.college.collegeplanning.guihua.Zi_mubiao;

import java.util.ArrayList;


/**
 * 1.给底部导航的三个按钮添加事件
 * 2.实现每个事件的跳转功能
 */
public class Think extends Activity implements View.OnClickListener{
    private ImageView add;
    public static ArrayList<String> mData;
    private RecyclerView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.think);
        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                Intent i = new Intent(this,Add_Jihua.class);
                startActivity(i);
                break;
        }
    }

    public ArrayList<String> getData(){
        mData = new ArrayList<>();
        NotesDB notesDB = new NotesDB(getApplicationContext());
        SQLiteDatabase dbReader = notesDB.getReadableDatabase();
        Cursor cursor = dbReader.query(NotesDB.TABLE_NAMEO,null,null,null,null,null,null);
        int max = cursor.getCount();
        for (int i = 0; i < max; i ++){
            if(cursor.moveToNext()) {
                mData.add(i, cursor.getString(cursor.getColumnIndex(NotesDB.CONTENT)));
            }
        }
        return mData;
    }

    public void selectDB(){
        mData = getData();
        lv = (RecyclerView) findViewById(R.id.list);
        Myadapter readapter = new Myadapter(this,mData);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        lv.setLayoutManager(layoutManager);
        lv.setAdapter(readapter);
        ItemClickSupport.addTo(lv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                NotesDB notesDB = new NotesDB(v.getContext());
                SQLiteDatabase dbReader = notesDB.getReadableDatabase();
                Cursor cursor = dbReader.query(NotesDB.TABLE_NAMEO,null,null,null,null,null,null);
                cursor.moveToPosition(position);
                Intent i = new Intent(v.getContext(),Zi_mubiao.class);
                i.putExtra("mubiao",cursor.getString(cursor.getColumnIndex("content")));
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        selectDB();
    }
}
