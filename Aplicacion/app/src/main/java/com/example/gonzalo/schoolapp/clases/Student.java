package com.example.gonzalo.schoolapp.clases;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gonzalo on 07/04/2015.
 */
public class Student implements Parcelable {
    private String name;
    private String lastname;
    private String school;
    private String classroom;
    private String mail;
    private String telephone;
    private String dni;
    private String rol = "Alumno";

    public Student(Map<String, Object> values) {
        setName((String) values.get("nombre"));
        setLastname((String) values.get("apellido"));
        setSchool((String) values.get("centro"));
        setClassroom((String) values.get("clase"));
        setMail((String) values.get("mail"));
        setTelephone((String) values.get("telefono"));
        setDNI((String) values.get("dni"));
    }

    public String getDNI () { return dni; }

    public void setDNI (String dni) { this.dni = dni; }

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

    public Map<String, Object> toMap() {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("nombre", getName());
        infoMap.put("apellido", getLastname());
        infoMap.put("centro", getSchool());
        infoMap.put("clase", getClassroom());
        infoMap.put("mail", getMail());
        if (getTelephone() != null){
            infoMap.put("telefono", getTelephone());
        } else{
            infoMap.put("telefono", "000000000");
        }
        infoMap.put("dni", getDNI());
        return infoMap;
    }

    @Override
    public String toString() {
        return name + " " + lastname;
    }

    public boolean equals (Student student) {
        boolean same = false;
        if (dni.equals(student.getDNI())){
            same = true;
        }
        return same;
    }

    //*****Parte de la interfaz Parcelable*****//
    public Student(Parcel in) {
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
        dest.writeString(dni);
        dest.writeString(rol);
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        lastname = in.readString();
        school = in.readString();
        classroom = in.readString();
        mail = in.readString();
        telephone = in.readString();
        dni = in.readString();
        rol = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator () {
        @Override
        public Student createFromParcel (Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };//Parcelable.creator*/
}
