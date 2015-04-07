package com.example.gonzalo.schoolapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Gonzalo on 07/04/2015.
 */
public class Father {
    String name;
    String lastname;
    String mail;
    String telephone;
    ArrayList<Alumno> childrens;

    public Father(Map<String, Object> values) {
        childrens = new ArrayList<>();
        setName((String) values.get("nombre"));
        setLastname((String) values.get("apellido"));
        setMail((String) values.get("mail"));
        setTelephone((String) values.get("telefono"));
        Map<String, Object> childs = (Map<String, Object>) values.get("hijos");
        setChildrens(childs);
    }

    public ArrayList<Alumno> getChildrens() {
        return childrens;
    }

    public void setChildrens(Map<String, Object> childs){
        Set<String> keys = childs.keySet();
        for (String key: keys) {
            Alumno alumno = new Alumno((Map<String, Object>) childs.get(key));
            if (!childrens.contains(alumno)) {
                childrens.add(alumno);
            }//if
        }//for key
    }

    public ArrayList<String> getClassrooms() {
        ArrayList<String> classrooms = new ArrayList<>();
        for (Alumno child: childrens) {
            if (!classrooms.contains(child.getClassroom())) {
                classrooms.add(child.getClassroom());
            }
        }//for childrens
        return classrooms;
    }

    public ArrayList<String> getSchools() {
        ArrayList<String> schools = new ArrayList<>();
        for (Alumno child: childrens) {
            if (!schools.contains(child.getSchool())) {
                schools.add(child.getSchool());
            }
        }//for childrens
        return schools;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Father--->{" +
                "name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", mail='" + mail + '\'' +
                ", telephone='" + telephone + '\'' +
                ", childrens=" + childrens +
                '}';
    }
}
