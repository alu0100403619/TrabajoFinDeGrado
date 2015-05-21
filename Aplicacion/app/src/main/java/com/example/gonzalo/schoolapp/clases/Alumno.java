package com.example.gonzalo.schoolapp.clases;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by Gonzalo on 07/04/2015.
 */
public class Alumno implements Parcelable {
    private String name;
    private String lastname;
    private String school;
    private String classroom;
    private String mail;
    private String telephone;
    private String rol = "Alumno";

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

    public String getRol() {
        return rol;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return name + " " + lastname;
    }

    public boolean equals (Alumno alumno) {
        boolean same = false;
        if ((name.equals(alumno.getName())) && (lastname.equals(alumno.getLastname())) &&
                (school.equals(alumno.getSchool())) && (classroom.equals(alumno.classroom)) &&
                (mail.equals(alumno.getMail())) && (telephone.equals(alumno.getTelephone()))) {
            same = true;
        }
        return same;
    }

    //*****Parte de la interfaz Parcelable*****//
    public Alumno(Parcel in) {
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
        dest.writeString(classroom);
        dest.writeString(mail);
        dest.writeString(telephone);
        dest.writeString(rol);
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        lastname = in.readString();
        school = in.readString();
        classroom = in.readString();
        mail = in.readString();
        telephone = in.readString();
        rol = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator () {
        @Override
        public Alumno createFromParcel (Parcel in) {
            return new Alumno(in);
        }

        @Override
        public Alumno[] newArray(int size) {
            return new Alumno[size];
        }
    };//Parcelable.creator*/
}
