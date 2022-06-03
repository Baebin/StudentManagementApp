package com.techtown.studentmanagementapp.manager;

import com.techtown.studentmanagementapp.entity.Student;

public class StudentManager {
    static public Student guest = new Student(0, 0, 0, "Guest");
    static public Student error = new Student(0, 0, 0, "Error");

    static public String getGC(Student student) {
        String gc = "" + student.getGrade_();
        if (student.getClass_() < 10) gc += "0";

        return gc + student.getClass_();
    }

    static public Student getGCStudent(String gc) {
        int grade_ = Integer.parseInt(String.valueOf(gc.charAt(0)));
        int class_ = Integer.parseInt(String.valueOf(gc.charAt(1))) * 10;
        class_ += Integer.parseInt(String.valueOf(gc.charAt(2))) * 1;

        Student student = new Student(grade_, class_, 0, "");
        return student;
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
