package com.planning.college.collegeplanning.guihua;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.planning.college.collegeplanning.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2018/9/26.
 */

public class Zi_mubiao extends Activity implements View.OnClickListener{

    private static List<SecondaryListAdapter.DataTree<String, String>> datas;
    private ImageView add;
    private RecyclerView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zijihua);
        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                Intent i = new Intent(this,Add_Zijihua.class);
                String name = getIntent().getStringExtra("mubiao");
                i.putExtra("title",name);
                startActivity(i);
                break;
        }
    }

    public void getData(){
        datas = new ArrayList<>();
        NotesDB_Nengli_zi_item notesDB = new NotesDB_Nengli_zi_item(getApplicationContext());
        SQLiteDatabase dbReader = notesDB.getReadableDatabase();
        String selection = "title=?" ;
        String name = getIntent().getStringExtra("mubiao");
        String[] selectionArgs = new  String[]{name};
        Cursor cursor = dbReader.query(NotesDB_Nengli_zi_item.ZIITEM,null,selection,selectionArgs,null,null,null);
        int max = cursor.getCount();
        for (int i = 0; i < max; i ++){
            List<String> group = new ArrayList<>();
            group.add("sub 0");group.add("sub 0");group.add("sub 1");group.add("sub 2");
            if(cursor.moveToNext()) {
                String content = cursor.getString(cursor.getColumnIndex(NotesDB_Nengli_zi_item.CONTENT));
                datas.add(i,new SecondaryListAdapter.DataTree<String, String>(content, group));
            }
        }
    }

    public void selectDB(){
        getData();
        lv = (RecyclerView) findViewById(R.id.list);
        RecyclerAdapter readapter = new RecyclerAdapter(this);
        readapter.setData(datas);
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setAdapter(readapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        selectDB();
    }

}
