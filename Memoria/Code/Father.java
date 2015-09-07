package com.example.gonzalo.schoolapp.clases;
//...
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
    //*****Parte de la interfaz Parcelable*****//
	//...
    }
