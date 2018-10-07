package com.planning.college.model;

/**
 * Created by KUIKUI on 2018-10-04.
 */

public class User {
    private String u_id;
    private String password;
    private String name;
    private String gender;
    private String grager;
    private String birthday;

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrager() {
        return grager;
    }

    public void setGrager(String grager) {
        this.grager = grager;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "u_id='" + u_id + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", grager='" + grager + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }
}
