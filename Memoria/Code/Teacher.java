package com.example.gonzalo.schoolapp.clases;
//...
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
	//...
    //*****Parte de la interfaz Parcelable*****//
     //...
}
