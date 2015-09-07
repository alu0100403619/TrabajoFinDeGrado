package com.example.gonzalo.schoolapp.clases;

//...
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

//...

    //*****Parte de la interfaz Parcelable*****//
    public Student(Parcel in) { readFromParcel(in); }

    @Override
    public int describeContents() { return 0; }

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
        public Student createFromParcel (Parcel in) { return new Student(in); }
        @Override
        public Student[] newArray(int size) { return new Student[size]; }
    };//Parcelable.creator*/
}