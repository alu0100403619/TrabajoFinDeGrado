package com.example.gonzalo.schoolapp.clases;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gonzalo on 03/04/2015.
 */
public class Teacher implements Parcelable {

    private String name;
    private String lastname;
    private String school;
    private String mail;
    private String telephone;
    private String dni;
    private ArrayList<String> classRooms;
    private String rol = "Profesor";

    public Teacher (Map<String, Object> values) {
        classRooms = new ArrayList<>();
        setTelephone((String) values.get("telefono"));
        setName((String) values.get("nombre"));
        setLastname((String) values.get("apellido"));
        setSchool((String) values.get("centro"));
        setMail((String) values.get("mail"));
        setDNI((String) values.get("dni"));
        //ClassRooms
        Map<String, Object> classes = (Map<String, Object>) values.get("clases");
        setClassRooms(classes);
    }

    public String getDNI () {return dni;}

    public void setDNI (String dni) {
        this.dni = dni;
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

    public String getRol() {
        return rol;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return name + " " + lastname;
    }

    /*public boolean equals (Teacher teacher) {
        boolean same = false;
        if ((name.equals(teacher.getName())) && (lastname.equals(teacher.getLastname())) &&
                (school.equals(teacher.getSchool())) && (mail.equals(teacher.getMail())) &&
                (telephone.equals(teacher.getTelephone())) &&
                (classRooms.containsAll(teacher.getClassRooms()))) {
            same = true;
        }
        return same;
    }//*/

    public boolean equals (Teacher teacher) {
        boolean same = false;
        if (dni.equals(teacher.getDNI())) {
            same = true;
        }
        return same;
    }

    //*****Parte de la interfaz Parcelable*****//
    public Teacher (Parcel in) {
        classRooms = new ArrayList<>();
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(lastname);
        dest.writeString(school);
        dest.writeString(mail);
        dest.writeString(telephone);
        dest.writeString(dni);
        dest.writeStringList(classRooms);
        dest.writeString(rol);
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        lastname = in.readString();
        school = in.readString();
        mail = in.readString();
        telephone = in.readString();
        dni = in.readString();
        in.readStringList(classRooms);
        rol = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator () {
        @Override
        public Teacher createFromParcel (Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };//Parcelable.creator
}
