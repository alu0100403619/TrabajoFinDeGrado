package com.example.gonzalo.schoolapp;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gonzalo on 03/04/2015.
 */
public class Teacher {

    private String telephone;
    private String name;
    private String lastname;
    private String school;
    private String mail;
    private ArrayList<String> classRooms;

    Teacher (Map<String, Object> values) {
        classRooms = new ArrayList<>();
        setTelephone((String) values.get("telefono"));
        setName((String) values.get("nombre"));
        setLastname((String) values.get("apellido"));
        setSchool((String) values.get("centro"));
        setMail((String) values.get("mail"));
        //ClassRooms
        Map<String, Object> classes = (Map<String, Object>) values.get("clases");
        setClassRooms(classes);
    }

    public ArrayList<String> getClassRooms() {
        return classRooms;
    }

    public void setClassRooms(Map<String, Object> classRooms) {
        Set<String> keys = classRooms.keySet();
        for (String key : keys) {
            this.classRooms.add((String) classRooms.get(key));
        }//for
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "Teacher-->{" +
                "telephone='" + telephone + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", school='" + school + '\'' +
                ", mail='" + mail + '\'' +
                ", classRooms='" + classRooms + '\'' +
                '}';
    }
}
