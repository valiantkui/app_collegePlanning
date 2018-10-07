package com.planning.college.collegeplanning.guihua;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.planning.college.collegeplanning.R;

/**
 * Created by Lenovo on 2018/9/26.
 */

public class Add_Zijihua extends Activity implements View.OnClickListener{

    private static EditText ettext;
    private Button tianjia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_jihua);
        ettext = (EditText) findViewById(R.id.ettext);
        tianjia = (Button) findViewById(R.id.zengjia);
        tianjia.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zengjia:
                addDB();
                finish();
                break;
        }
    }

    public void addDB() {
        NotesDB_Nengli_zi_item notesDB = new NotesDB_Nengli_zi_item(getApplicationContext());
        SQLiteDatabase dbwriter = notesDB.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String Real_textcontent = ettext.getText().toString();
        cv.put(NotesDB_Nengli_zi_item.CONTENT,Real_textcontent);//带有图片uri的真实地址
        cv.put(NotesDB_Nengli_zi_item.TITLE,getIntent().getStringExtra("title"));//带有图片uri的真实地址
        dbwriter.insert(NotesDB_Nengli_zi_item.ZIITEM, null, cv);
    }
}
