package com.example.gonzalo.schoolapp.clases;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Map;

/**
 * Created by Gonzalo on 21/05/2015.
 */
public class Message implements Parcelable{
    private String dniRemitter; //Mail Remitente
    private String remitter; //Remitente
    private String message; //Mensaje
    private String rolRemmiter; //Rol Remitente
    private String state; //Estado: leido--> true, no leido --> false
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

    public String getDniRemitter() {
        return dniRemitter;
    }

    public void setDniRemitter(String dniRemitter) {
        this.dniRemitter = dniRemitter;
    }

    public String getRemitter() {
        return remitter;
    }

    public void setRemitter(String remitter) {
        this.remitter = remitter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRolRemmiter() {
        return rolRemmiter;
    }

    public void setRolRemmiter(String rolRemmiter) {
        this.rolRemmiter = rolRemmiter;
    }

    public String show () {
        return getDniRemitter() + ", " + getRemitter() + ", " + getMessage() + ", " +
                getRolRemmiter() + ", " +getState() + ", " + getDate();
    }

    @Override
    public String toString() {
        String string = "";
        string += "De " + remitter + ": " + message;
        return string;
        //return remitter;
    }

    public boolean equals (Message message) {
        boolean same = false;
        if ((dniRemitter.equals(message.getDniRemitter())) &&
                (this.message.equals(message.getMessage())) && (date.equals(message.getDate()))) {
            same = true;
        }
        return same;
    }//*/

    //*****Parte de la interfaz Parcelable*****//
    public Message(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dniRemitter);
        dest.writeString(remitter);
        dest.writeString(message);
        dest.writeString(rolRemmiter);
        dest.writeString(state);
        dest.writeValue(date);
    }

    public void readFromParcel(Parcel in) {
        dniRemitter = in.readString();
        remitter = in.readString();
        message = in.readString();
        rolRemmiter = in.readString();
        state = in.readString();
        date = (Date) in.readValue(Date.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator () {
        @Override
        public Message createFromParcel (Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };//Parcelable.creator*/
}
