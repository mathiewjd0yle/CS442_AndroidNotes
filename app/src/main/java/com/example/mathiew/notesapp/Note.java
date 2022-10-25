package com.example.mathiew.notesapp;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Note implements Serializable {

    private String name;
    private String department;

    private static int ctr = 1;

    Note() {
        this.name = "Employee Name " + ctr;
        this.department = "Department " + ctr;
        ctr++;
    }

    public Note(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" + "title = " + name + '\'' + "body = " + department + '\'' + '}';
    }


}