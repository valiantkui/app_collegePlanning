package com.planning.college.model;

/**
 * Created by KUIKUI on 2018-05-24.
 */

public class Subject {

    private String s_no;
    private String name;
    private String type;
    private String intro;
    private String image_address;

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getImage_address() {
        return image_address;
    }

    public void setImage_address(String image_address) {
        this.image_address = image_address;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "s_no='" + s_no + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", intro='" + intro + '\'' +
                ", image_address='" + image_address + '\'' +
                '}';
    }
}
