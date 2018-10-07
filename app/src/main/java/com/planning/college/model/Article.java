package com.planning.college.model;

/**
 * Created by KUIKUI on 2018-05-20.
 * 对应数据库中文章的实体
 */


public class Article {
    private String a_no;
    private String title;
    private String intro;
    private String content_link;
    private String type;
    private String status;//status有两种：local,cloud

    private String update_date;

    public String getA_no() {
        return a_no;
    }

    public void setA_no(String a_no) {
        this.a_no = a_no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getContent_link() {
        return content_link;
    }

    public void setContent_link(String content_link) {
        this.content_link = content_link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }


    @Override
    public String toString() {
        return "Article{" +
                "a_no='" + a_no + '\'' +
                ", title='" + title + '\'' +
                ", intro='" + intro + '\'' +
                ", content_link='" + content_link + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", update_date='" + update_date + '\'' +
                '}';
    }
}
