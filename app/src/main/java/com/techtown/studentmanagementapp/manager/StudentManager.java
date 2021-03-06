package com.techtown.studentmanagementapp.manager;

import com.techtown.studentmanagementapp.entity.Student;

public class StudentManager {
    static public Student admin = new Student(0, 0, 0, "Admin");
    static public Student developer = new Student(0, 0, 0, "Developer");

    static public Student guest = new Student(0, 0, 0, "Guest");
    static public Student error = new Student(0, 0, 0, "Error");

    static public String getGC(Student student) {
        String gc = "" + student.getGrade_();
        if (student.getClass_() < 10) gc += "0";

        return gc + student.getClass_();
    }

    static public String getGCN(Student student) {
        String gc = getGC(student);
        if (student.getNumber_() < 10) gc += "0";

        return gc + student.getNumber_();
    }

    static public Student getGCStudent(String gc) {
        int grade_ = Integer.parseInt(String.valueOf(gc.charAt(0)));
        int class_ = Integer.parseInt(String.valueOf(gc.charAt(1))) * 10;
        class_ += Integer.parseInt(String.valueOf(gc.charAt(2))) * 1;

        Student student = new Student(grade_, class_, 0, "");
        return student;
    }

    public static Student getAdmin() {
        return admin;
    }

    public static Student getDeveloper() {
        return developer;
    }

    public static Student getGuest() {
        return guest;
    }

    public static Student getError() {
        return error;
    }

    static public boolean checkAdmin(Student student) {
        if (checkDeveloper(student)) return true;
        if (student.getClass_() == 0 && student.getName_().equals(admin.getName_())) {
            return true;
        }
        return false;
    }

    static public boolean checkDeveloper(Student student) {
        if (student.getClass_() == 0 && student.getName_().equals(developer.getName_())) {
            return true;
        }
        return false;
    }

    static public boolean checkGuest(Student student) {
        if (student.getClass_() == 0 && student.getName_().equals(guest.getName_())) {
            return true;
        }
        return false;
    }

    static public boolean checkError(Student student) {
        if (student.getClass_() == 0 && student.getName_().equals(error.getName_())) {
            return true;
        }
        return false;
    }
}
