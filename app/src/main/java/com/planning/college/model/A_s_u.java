package com.planning.college.model;

/**
 * Created by KUIKUI on 2018-06-29.
 */

public class A_s_u {
    private String a_no;
    private String s_no;
    private String u_id;

    public String getA_no() {
        return a_no;
    }

    public void setA_no(String a_no) {
        this.a_no = a_no;
    }

    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    @Override
    public String toString() {
        return "A_s_u{" +
                "a_no='" + a_no + '\'' +
                ", s_no='" + s_no + '\'' +
                ", u_id='" + u_id + '\'' +
                '}';
    }
}
