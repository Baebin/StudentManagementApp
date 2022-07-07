package com.techtown.studentmanagementapp.entity;

import java.io.Serializable;

public class AdminInfo implements Serializable {
    private String id = "";
    private String pw = "";

    public AdminInfo(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getPW() {
        return pw;
    }

    public void setPW(String pw) {
        this.pw = pw;
    }
}
