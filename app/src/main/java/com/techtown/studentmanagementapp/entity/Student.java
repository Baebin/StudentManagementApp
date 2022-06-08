package com.techtown.studentmanagementapp.entity;

import java.io.Serializable;

public class Student implements Serializable {
    private int grade_ = 0;
    private int class_ = 0;
    private int number_ = 0;
    private String name_ = "Error";

    public Student(int grade_, int class_, int number_, String name_) {
        this.grade_ = grade_;
        this.class_ = class_;
        this.number_ = number_;
        this.name_ = name_;
    }

    public Student(String grade_, String class_, String number_, String name_) {
        this.grade_ = Integer.parseInt(grade_);
        this.class_ = Integer.parseInt(class_);
        this.number_ = Integer.parseInt(number_);
        this.name_ = name_;
    }

    public int getGrade_() {
        return grade_;
    }

    public void setGrade_(int grade_) {
        this.grade_ = grade_;
    }

    public int getClass_() {
        return class_;
    }

    public void setClass_(int class_) {
        this.class_ = class_;
    }

    public int getNumber_() {
        return number_;
    }

    public void setNumber_(int number_) {
        this.number_ = number_;
    }

    public String getName_() {
        return name_;
    }

    public void setName(String name_) {
        this.name_ = name_;
    }
}
