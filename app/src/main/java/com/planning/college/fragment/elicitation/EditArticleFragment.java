package com.planning.college.fragment.elicitation;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.planning.college.adapter.ArticleLableAdapter;
import com.planning.college.collegeplanning.R;
import com.planning.college.dao.A_s_uDao;
import com.planning.college.dao.ArticleDao;
import com.planning.college.model.A_s_u;
import com.planning.college.model.Article;
import com.planning.college.model.Subject;
import com.planning.college.tools.Globle;
import com.planning.college.tools.IOUtil;
import com.planning.college.tools.ImageCompress;
import com.planning.college.tools.Tool;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KUIKUI on 2018-05-21.
 */

@EFragment(R.layout.fragment_article_edit)
public class EditArticleFragment extends Fragment {
    //当用户点击了一次保存之后，将isSave设为true,那么下次再次点击的时候就不要再重新创建文件了，也不要向数据库中插入新的记录
    private boolean isSave = false;//记录当前活动中的笔记是否已经被保存过一次(默认未保存)
    private Article article;    //当数据库中有本次article的内容时，article才不为空，这时代表 当前为更新才做
    private String currentFileName;//记录当前正在做的文章的文件名，当用户第一次点击时被赋值
    private Date create_date;

    @Bean
    ArticleDao articleDao;

    @Bean
    A_s_uDao asuDao;

    @ViewById(R.id.title)
    EditText title;

    @ViewById(R.id.intro)
    EditText intro;

    @ViewById(R.id.content)
    EditText editText;

    @ViewById(R.id.gv_label)
    GridView gridView;

    Activity activity;

    private String indexPath;

    private ArticleLableAdapter articleLableAdapter;

    private String url;

    private  boolean isGetAllLabelsFinish = false;

    private boolean isCreateHtml = false;

    List<A_s_u> asuList;//当从修改选项跳进来的时候所需的全局变量

    private Map<String,String> checkBoxMap = new HashMap<>();

    private Map<String,String> asuDeleteMap = new HashMap<>();  //第一个string:学科编号，第二个参数：文章编号

    private List<File> fileList = new ArrayList<>();

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {

            Manifest.permission.READ_EXTERNAL_STORAGE,

            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    /**
     * 从别的activity读取参数：如果参数为：
     * 1.article: 表示这是一个新建笔记的页面，则从服务器获取所有学科的标签
     * 2.diary : 表示这是一个新建个人日记的页面，则不从服务器获得标签
     *
     */

    private String tag;//接受从EditArticleActivity传来的新建类型



    @AfterViews
    public void afterViews()  {
        activity = getActivity();
        Bundle bun = getArguments();
        if(bun != null) {
            tag = bun.getString("tag");
            String a_no = bun.getString("a_no");
            indexPath = bun.getString("indexPath");

            if (a_no != null && indexPath != null) {//说明这是从ArticleInfoActivity点击修改跳转而来的
                //接下来进行一些设置
                modifyAction(a_no, indexPath);


            }

            switch (tag) {
                case "article":
                    url = "/cpServer/subject/findAll";
                    getAllLabels();
                    break;
                case "diary":
                    break;

                default:
                    break;
            }
            //getAllLabels();
            //  gridView.setChoiceMode(gridView.CHOICE_MODE_MULTIPLE);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CheckBox checkBox = view.findViewById(R.id.article_label);
                    TextView textView = view.findViewById(R.id.s_no);
                    String s_no = textView.getText().toString();
                    checkBox.toggle();

                    if (checkBox.isChecked()) {
                        checkBoxMap.put(s_no, checkBox.getText().toString());

                        if(article != null){
                            asuDeleteMap.remove(s_no);
                        }

                    } else {
                        checkBoxMap.remove(s_no);
                        if(article != null){

                            asuDeleteMap.put(s_no,article.getA_no());
                        }
                    }
                }
            });


        }
    }

    /**
     * 如果是ArticleInfoActivity中跳转过来的，则要执行本方法
     * @throws ParseException
     */

    public void modifyAction(String a_no,String indexPath){
        //1.从数据库中根据文章编号获取articl,a_s_u信息
        article = articleDao.findArticleByid(a_no);
        asuList = asuDao.findA_s_uByA_no(a_no);

        //2. 将asuList存到checkBoxMap当中，
        for(A_s_u asu: asuList){
            checkBoxMap.put(asu.getS_no(),"");//暂时获取不到学科名（也用不到），
        }

        //3. 将isSave设置为已经保存
        isSave = true;

        //4. 将isCreateHtml设置为已经创建
        isCreateHtml = true;

        //5. 将create_date设置为不为空

        Log.i("tag","取得的日期为:"+article.getUpdate_date());
        // create_date = article.getUpdate_date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            create_date = sdf.parse(article.getUpdate_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //6.修改相应的视图




    }

    @Background
    public void modifyViews(final Article article,final  List<A_s_u> asuList,final String indexPath) {


        activity.runOnUiThread(new Runnable() {



            @Override
            public void run() {


        String title_str = article.getTitle();
        String intro_str = article.getIntro();
        String content_str = "";
        File file = new File(activity.getFilesDir()+"/article/"+article.getA_no()+".cp");
        if(!file.exists()){
            Toast.makeText(activity,"资源定位失败",Toast.LENGTH_SHORT).show();
        } else{
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);




                byte[] bytes = new byte[1024*10];
                int i = -1;
                while((i=fis.read(bytes)) != -1){
                    String s = new String(bytes, 0, i);

                    content_str += s;
                }

              editText.setText(content_str);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(fis != null) fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        title.setText(title_str);
        intro.setText(intro_str);

        editText.setText(content_str);



         /*   ArticleLableAdapter adapter = (ArticleLableAdapter) gridView.getAdapter();

            int c = adapter.getCount();
            //遍历每个checkBox
            for(int j = 0;j<adapter.getCount();j++){
                LinearLayout linearLayout =
                        (LinearLayout) adapter.getView(j, null, null);

                CheckBox cb = linearLayout.findViewById(R.id.article_label);
                TextView tv = linearLayout.findViewById(R.id.s_no);
                Log.i("tag",cb.isChecked()+",对应学科名："+cb.getText().toString());
          second:  for(A_s_u a:asuList){

                    if(a.getS_no().equals(tv.getText().toString())){

                        cb.toggle();


                        break second;
                    }
                }

            }
*/
            //gridView.setAdapter(adapter);

         //   adapter.notifyDataSetChanged();









            }
        });




    }


    @Click(R.id.preview)
    public void doPreview(){

        createHtml();
       Intent intent = new Intent(activity,PreviewArticleActivity_.class);
       startActivity(intent);


    }


    private void saveContent(String fileName){

        File file = new File(activity.getFilesDir()+"/article/"+fileName);
        String content = editText.getText().toString();
        FileOutputStream fos = null;
        try {

            if(!file.exists()){
                if(!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();


            }

            fos = new FileOutputStream(file);

            fos.write(content.getBytes());
            fos.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void createHtml(){
        String filePath = activity.getCacheDir()+"/index.html";

        String content = Globle.htmlHead+ editText.getText().toString() +Globle.htmltail;
        File file = new File(filePath);

        if(!file.exists()){
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte [] bytes = content.getBytes();

            fos.write(bytes);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }   finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(!fileList.contains(file)){

            fileList.add(file);
        }
        //将图片和html文件都压缩成压缩文件

        try {
            IOUtil.getZipFile(fileList,activity.getCacheDir()+"/index.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }

        isCreateHtml = true;

    }
    private   void verifyStoragePermissions() {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            // We don't have permission so prompt the user

            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );

        }

    }

    /**
     * 当用户点击add_imamge图标时，要跳转到增加图片的activity
     */
    @Click(R.id.add_image)
    public void addImage(){
        verifyStoragePermissions();
        Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
        getImage.addCategory(Intent.CATEGORY_OPENABLE);
        getImage.setType("image/*");
        startActivityForResult(getImage, 1);
    }



    @Click(R.id.add_title)
    public void addTile(){
        String content = editText.getText().toString();
        content += "<br/><h1></h1>";
        editText.setText(content);


    }


    @TextChange(R.id.content)
    public void checkTextChanged(CharSequence s){

        //Toast.makeText(activity,"内容改变了:"+s,Toast.LENGTH_SHORT).show();

        isCreateHtml = false;


    }


    /**
     * 当用户选择完图片时，会执行此方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String content = editText.getText().toString();
        if (resultCode == activity.RESULT_OK && requestCode == 1) {
            Uri uri = data.getData();
            String test =  uri.toString();
            String path = Tool.getPath(activity, uri);
            String path2 = test.substring(test.indexOf(":")+2);

            String fileName = path.substring(path.lastIndexOf("/")+1);

            ImageCompress.saveBitmapFile(path,activity.getCacheDir()+"/"+fileName);

            File file2 = new File(activity.getCacheDir()+"/"+fileName) ;

            if(file2.exists()){

                fileList.add(file2);
            }
            content += "<br/><img src='" + fileName + "' />";

            editText.setText(content);
        }


        //添加完一次图片需要声明 editText又有了新的图片添加，如果点击保存按钮时，会重新更新html文件
        isCreateHtml = false;

    }


    @Click({R.id.save,R.id.back})
    public void doClick(View v){

        switch (v.getId()){

            //当用户点击返回时，使用intent跳转到启发界面，并将content内容设为心得笔记
            case R.id.back:
                activity.finish();
                break;

            /**
             * 用户可能会点击多次保存按钮
             * 第一次需要创建新文件，并向数据库当中插入记录
             * 第一次之后，不再创建新文件，仅仅更改文件的内容，只修改数据的更新日期
             */
            case R.id.save:
                //取出输入内容
                Log.i("click","doClick()");
                String titleStr = title.getText().toString();
                String introStr = intro.getText().toString();
                String contentStr = editText.getText().toString();


                if(create_date == null){
                    create_date = new Date();
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yy_MM_dd_HH_mm_ss");

                String fileName = "art_"+sdf.format(create_date)+".zip";//文件名以art_开头，以.cp结尾


                if(isSave == true && article != null ){
                    //表示多次点击了保存
                    Log.i("click","2ci点击");
                    article.setTitle(titleStr);
                    article.setIntro(introStr);

                    boolean isInsert = true;
                    boolean isDelete = true;
                    if(!isCreateHtml){//如果editText中的内容未保存，则更新文件

                        fileMove(fileName);
                        saveContent(article.getA_no()+".cp");
                    }

                    for (Map.Entry entry: checkBoxMap.entrySet()){
                        String s_no = (String) entry.getKey();
                        String nm = (String) entry.getValue();

                        String a_no = article.getA_no();

                        SharedPreferences preferences = activity.getSharedPreferences("user_info",activity.getChangingConfigurations());

                        String id = preferences.getString("id","yuankui");

                        A_s_u asu = new A_s_u();
                        asu.setA_no(a_no);
                        asu.setS_no(s_no);
                        asu.setU_id(id);

                        if(asuDao.findA_s_uByA_noS_no(a_no,s_no) == null){

                          isInsert =  asuDao.insertA_s_u(asu);
                        }


                    }

                    for(Map.Entry entry: asuDeleteMap.entrySet()){
                        String  key = (String)entry.getKey();
                        String  value = (String)entry.getValue();
                       if(!asuDao.deleteA_s_uByA_noS_no(value,key)){
                           isDelete = false;
                       }


                    }
                    if( articleDao.updateArticleForUpdateDate(article) && isInsert && isDelete){
                        Toast.makeText(activity,"更新成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(activity,"更新失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }
                Log.i("click","break之后");
                //设置文件名（一旦设定之后不可更改，文件名代表的含义是  第一次创建该文件的日期）

                //将currentFileName赋值

                currentFileName = fileName;

                sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
                String update_date = sdf.format(create_date);
                article = new Article();
                article.setTitle(titleStr);
                article.setIntro(introStr);
                article.setContent_link(fileName);
                article.setType("note");
                article.setStatus("local");
                article.setUpdate_date(update_date);


                //从sharedPreference中获取用户id
                SharedPreferences preference;
                preference = activity.getSharedPreferences("user_info",activity.getChangingConfigurations());
              /*  SharedPreferences.Editor editor = preference.edit();
                editor.putString("id","yuankui");
                editor.putString("password","123456");
                editor.commit();//每次修改preference都要提交*/
                String id = preference.getString("id","");
              //  String password = preference.getString("password","123456");

                boolean isSaveA_s_u = true;


                //获取到文章编号后，再将文章编号存到article对象
                long rowId = articleDao.insertArticle(article);
                article.setA_no(String.valueOf(rowId));

                Log.i("tag","插入的文章的编号为："+rowId);
                for(Map.Entry<String,String> entry:checkBoxMap.entrySet()){
                    String s_no = entry.getKey();
                    A_s_u asu = new A_s_u();
                    asu.setA_no(String.valueOf(rowId));
                    asu.setS_no(s_no);
                    asu.setU_id(id);
                    if(! asuDao.insertA_s_u(asu)){
                        isSaveA_s_u = false;
                    }
                }

                if( rowId != -1 && isSaveA_s_u){
                    isSave = true;
                    //表明存储成功
                    Toast.makeText(activity,"保存成功",Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(activity,"保存失败",Toast.LENGTH_SHORT).show();
                }


                fileMove(fileName);
                saveContent(article.getA_no()+".cp");

                break;
        }

    }

    private void fileMove(String fileName){

        if(!isCreateHtml)   {
            createHtml();
        }

        File file = new File(activity.getCacheDir()+"/index.zip");
        File file2 = new File(activity.getFilesDir()+"/article/"+fileName);
        if(!file2.exists()){

            if(!file2.getParentFile().exists()){
                file2.getParentFile().mkdirs();
            }

            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        file.renameTo(file2);
    }

    /**
     * 获取所有的学科标签
     */
    public  void getAllLabels(){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request
                        .Builder()
                        .url(Globle.HOST_PORT+url).build();
        Call call = okHttpClient.newCall(request);


        //异步
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("error","请求失败");
            }
            @Override
            public void onResponse(Response response) throws IOException {
                String resultJson = response.body().string();
                Log.i("info",resultJson);
                Gson gson = new Gson();
                final List<Subject> subjectList = gson.fromJson(resultJson, new TypeToken<List<Subject>>() {
                }.getType());

                activity.runOnUiThread(new Runnable() {
                    List<Map<String, String>> list = new ArrayList<>();
                    @Override
                    public void run() {
                        for (Subject s: subjectList) {
                            Map<String, String> map = new HashMap<>();
                            // map.put("p_no", s.getS_no());

                            map.put("s_name",s.getName());
                            list.add(map);
                        }
                        String[] from = {"s_name"};
                        int[] to = { R.id.article_label};
//                        SimpleAdapter simpleAdapter = new SimpleAdapter
//                                (EditArticalActivity.this, list, R.layout.item_article_label, from, to);
                        articleLableAdapter  = new ArticleLableAdapter(activity,subjectList,asuList);
                        gridView.setAdapter(articleLableAdapter);
                        isGetAllLabelsFinish = true;


                        //修改相应的视图

                        if(indexPath != null){

                            modifyViews(article,asuList,indexPath);
                        }
                    }
                });
            }
        });
    }







}
