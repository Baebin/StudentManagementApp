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
