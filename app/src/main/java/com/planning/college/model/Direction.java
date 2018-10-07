package com.planning.college.model;

/**
 * Created by KUIKUI on 2018-07-25.
 */

public class Direction {

    private String d_no;
    private String name;
    private String type;
    private String intro_link;
    private String vista_link;
    private String update_date;

    public String getD_no() {
        return d_no;
    }

    public void setD_no(String d_no) {
        this.d_no = d_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntro_link() {
        return intro_link;
    }

    public void setIntro_link(String intro_link) {
        this.intro_link = intro_link;
    }

    public String getVista_link() {
        return vista_link;
    }

    public void setVista_link(String vista_link) {
        this.vista_link = vista_link;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    @Override
    public String toString() {
        return "Direction{" +
                "r_no='" + d_no + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", intro_link='" + intro_link + '\'' +
                ", vista_link='" + vista_link + '\'' +
                ", update_date='" + update_date + '\'' +
                '}';
    }
}
