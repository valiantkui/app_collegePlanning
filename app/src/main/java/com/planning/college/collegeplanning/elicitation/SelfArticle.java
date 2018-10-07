package com.planning.college.collegeplanning.elicitation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.planning.college.collegeplanning.Elicitation;
import com.planning.college.collegeplanning.Knowledge;
import com.planning.college.collegeplanning.R;
import com.planning.college.collegeplanning.Think;
import com.planning.college.adapter.SelfArticleAdapter;
import com.planning.college.collegeplanning.elicitation.selfArticle.ArticalInfoActivity;
import com.planning.college.collegeplanning.elicitation.selfArticle.EditArticalActivity;
import com.planning.college.model.Article;
import com.planning.college.tools.DBOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KUIKUI on 2018-05-19.
 * 抛弃的代码
 */
public class SelfArticle extends Activity {

    private ListView selfNoteListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_self_article);

        selfNoteListView = findViewById(R.id.self_note_listView);

        /**
         * 随机生成若干条数据
         */

      List<Article> articleList = getAllArticle();


      /*  List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("a_no", "" + i);
            map.put("title", "title" + i);
            map.put("intro", "intro" + i);
            list.add(map);
        }
        String[] from = {"a_no", "title", "intro"};
        int[] to = {R.id.a_no, R.id.title, R.id.intro};

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.item_article, from, to);*/


        //创建适配器，并向指定适配器添加资源

        Log.i("tag",articleList.toString());
        SelfArticleAdapter selfArticleAdapter = new SelfArticleAdapter(SelfArticle.this, articleList,"");

        //将适配器与视图进行绑定

        selfNoteListView.setAdapter(selfArticleAdapter);
        /**
         * 为ListView的item绑定监听事件
         * 1.当用户点击了某个item时，我们先获取该item的a_no
         * 2.然后查找数据库，从数据库中查找到对应资料。
         * 3.跳转到显示该文章的  Activity页面
         */
        selfNoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView a_noTextView = view.findViewById(R.id.a_no);
                String a_no = a_noTextView.getText().toString();
                Article article = findArticleByid(a_no);//获取对应Artical


                Intent intent_articalInfo = new Intent(SelfArticle.this, ArticalInfoActivity.class);

                intent_articalInfo.putExtra("a_no",article.getA_no());
                intent_articalInfo.putExtra("title",article.getTitle());
                intent_articalInfo.putExtra("intro",article.getIntro());
                intent_articalInfo.putExtra("content_link",article.getContent_link());
                startActivity(intent_articalInfo);


            }
        });

    }

    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.elicitation:
                //跳转到启发页面
                Intent intent = new Intent(this, Elicitation.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.think:
                //跳转到思考页面
                Intent intent2 = new Intent(this, Think.class);
                startActivity(intent2);
                overridePendingTransition(0, 0);
                break;
            case R.id.knowledge:
                //跳转到知识页面
                Intent intent3 = new Intent(this, Knowledge.class);
                startActivity(intent3);
                overridePendingTransition(0, 0);
                break;
            case R.id.self_article:
                //跳转到心得体会页面
                Intent intent4 = new Intent(this, SelfArticle.class);
                startActivity(intent4);
                break;

            case R.id.route:
                //跳转到推荐路线页面

                break;


        }
    }


    public void doMenuClick(View v){

        Log.i("click","doMenuClick()");
        Intent intentEditArticle = new Intent(this, EditArticalActivity.class);
        startActivity(intentEditArticle);
    }


    private List<Article> getAllArticle(){
        List<Article> articleList = new ArrayList<>();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this,null,null,1);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
       Cursor c = db.rawQuery("select * from article order by update_date desc",null);

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

    private Article findArticleByid(String a_no){
        Article article = new Article();
        DBOpenHelper dbOpenHelper = new DBOpenHelper(this,null,null,1);
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c = db.rawQuery("select * from article where _id = ?",new String[]{a_no});
        if(c != null){

            if(c.moveToNext()){
                article.setA_no(c.getString(c.getColumnIndex("_id")));
                article.setTitle(c.getString(c.getColumnIndex("title")));
                article.setIntro(c.getString(c.getColumnIndex("intro")));
                article.setContent_link(c.getString(c.getColumnIndex("content_link")));
                article.setType(c.getString(c.getColumnIndex("type")));
                article.setStatus(c.getString(c.getColumnIndex("status")));
                article.setUpdate_date(c.getString(c.getColumnIndex("update_date")));
            }

        }

        return article;
    }
}
