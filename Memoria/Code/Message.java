package com.example.gonzalo.schoolapp.clases;
//...
public class Message implements Parcelable{
    private String dniRemitter; //Mail Remitente
    private String remitter; //Remitente
    private String message; //Mensaje
    private String rolRemmiter; //Rol Remitente
    private Date date; //Fecha

    public Message() {}

    public Message(String dniRemitter, String remitter, String message, Date date, String rolRemitter) {
        setDniRemitter(dniRemitter);
        setRemitter(remitter);
        setMessage(message);
        setRolRemmiter(rolRemitter);
        setState("No Leido");
        setDate(date);
    }

    public Message(Map<String, Object> values) {
        setDniRemitter((String) values.get("dni_remitente"));
        setRemitter((String) values.get("remitente"));
        setMessage((String) values.get("mensaje"));
        setRolRemmiter((String) values.get("rol_remitente"));
        setState("No Leido");
        Map<String, Object> dateValue = (Map<String, Object>) values.get("fecha");
        date = new Date(dateValue);
    }
	//...
    //*****Parte de la interfaz Parcelable*****//
	//...
}
