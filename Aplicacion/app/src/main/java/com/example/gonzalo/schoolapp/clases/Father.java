package com.example.gonzalo.schoolapp.clases;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gonzalo on 07/04/2015.
 */
public class Father implements Parcelable {
    private String name;
    private String lastname;
    private String mail;
    private String telephone;
    private String dni;
    private ArrayList<Student> childrens;
    private String rol = "Padre";

    public Father(Map<String, Object> values) {
        childrens = new ArrayList<>();
        setName((String) values.get("nombre"));
        setLastname((String) values.get("apellido"));
        setMail((String) values.get("mail"));
        //Log.i("Father", "clase Telefono:"+values.get("telefono").getClass());
        setTelephone((String) values.get("telefono"));
        setDNI((String) values.get("dni"));
        Map<String, Object> childs = (Map<String, Object>) values.get("hijos");
        setChildrens(childs);
    }

    public String getDNI () { return dni; }

    public void setDNI (String dni) {
        this.dni = dni;
    }

    public ArrayList<Student> getChildrens() {
        return childrens;
    }

    public void setChildrens(Map<String, Object> childs){
        Set<String> keys = childs.keySet();
        for (String key: keys) {
            Student student = new Student((Map<String, Object>) childs.get(key));
            if (!childrens.contains(student)) {
                childrens.add(student);
            }//if
        }//for key
    }

    public ArrayList<String> getClassrooms() {
        ArrayList<String> classrooms = new ArrayList<>();
        for (Student child: childrens) {
            if (!classrooms.contains(child.getClassroom())) {
                classrooms.add(child.getClassroom());
            }
        }//for childrens
        return classrooms;
    }

    public ArrayList<String> getSchools() {
        ArrayList<String> schools = new ArrayList<>();
        for (Student child: childrens) {
            if (!schools.contains(child.getSchool())) {
                schools.add(child.getSchool());
            }
        }//for childrens
        return schools;
    }

    public ArrayList<String> getDniChildrens () {
        ArrayList<String> dnis = new ArrayList<>();
        for (Student child: childrens) {
            if (!dnis.contains(child.getDNI())) {
                dnis.add(child.getDNI());
            }
        }
        return dnis;
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

    public String getRol() {
        return rol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountChildrens () {
        return childrens.size();
    }

    @Override
    public String toString() {
        return name + " " + lastname;
    }

    /*public boolean equals (Father father) {
        boolean same = false;
        if ((name.equals(father.getName())) && (lastname.equals(father.getLastname())) &&
                (mail.equals(father.getMail())) && (telephone.equals(father.getTelephone()))) {
            same = true;
        }
        return same;
    }//*/

    public boolean equals (Father father) {
        boolean same = false;
        if (dni.equals(father.getDNI())) {
            same = true;
        }
        return same;
    }

    //*****Parte de la interfaz Parcelable*****//
    public Father(Parcel in) {
        childrens = new ArrayList<Student> ();
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
        dest.writeString(mail);
        dest.writeString(telephone);
        dest.writeString(dni);
        dest.writeTypedList(childrens);
        dest.writeString(rol);
    }

    public void readFromParcel(Parcel in) {
        name = in.readString();
        lastname = in.readString();
        mail = in.readString();
        telephone = in.readString();
        dni = in.readString();
        in.readTypedList(childrens, Student.CREATOR);
        rol = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator () {
        @Override
        public Father createFromParcel (Parcel in) {
            return new Father(in);
        }

        @Override
        public Father[] newArray(int size) {
            return new Father[size];
        }
    };//Parcelable.creator*/

}
