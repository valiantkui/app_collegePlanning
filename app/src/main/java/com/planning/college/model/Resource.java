package com.planning.college.model;

/**
 * Created by KUIKUI on 2018-07-27.
 */

public class Resource {
    private String r_no;
    private String title;
    private String intro;
    private String type;
    private String path;
    private String update_date;


    public String getR_no() {
        return r_no;
    }

    public void setR_no(String r_no) {
        this.r_no = r_no;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }
    @Override
    public String toString() {
        return "Resource{" +
                "r_no='" + r_no + '\'' +
                ", title='" + title + '\'' +
                ", intro='" + intro + '\'' +
                ", type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", update_date='" + update_date + '\'' +
                '}';
    }
}
