package com.planning.college.model;

/**
 * Created by KUIKUI on 2018-05-23.
 */

public class Profession {
    private String p_no;
    private String name;

    public String getP_no() {
        return p_no;
    }

    public void setP_no(String p_no) {
        this.p_no = p_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Profession{" +
                "p_no='" + p_no + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
