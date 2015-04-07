package com.example.gonzalo.schoolapp;

import android.util.Log;

import java.util.Map;

/**
 * Created by Gonzalo on 07/04/2015.
 */
public class Alumno {
    String name;
    String Lastname;
    String school;
    String classroom;
    String mail;
    String telephone;

    public Alumno(Map<String, Object> values) {
        setName((String) values.get("nombre"));
        setLastname((String) values.get("apellido"));
        setSchool((String) values.get("centro"));
        setClassroom((String) values.get("clase"));
        setMail((String) values.get("mail"));
        setTelephone((String) values.get("telefono"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "name='" + name + '\'' +
                ", Lastname='" + Lastname + '\'' +
                ", school='" + school + '\'' +
                ", classroom='" + classroom + '\'' +
                ", mail='" + mail + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
