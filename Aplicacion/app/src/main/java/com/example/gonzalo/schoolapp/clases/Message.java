package com.example.gonzalo.schoolapp.clases;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Map;

/**
 * Created by Gonzalo on 21/05/2015.
 */
public class Message implements Parcelable{
    private String mailRemitter; //Mail Remitente
    private String remitter; //Remitente
    private String message; //Mensaje
    private String state; //Estado: leido--> true, no leido --> false
    private Date date; //Fecha

    public Message() {}

    public Message(String mailRemitter, String remitter, String message, Date date) {
        setMailRemitter(mailRemitter);
        setRemitter(remitter);
        setMessage(message);
        setState("No Leido");
        setDate(date);
    }

    public Message(Map<String, Object> values) {
        setMailRemitter((String) values.get("mail_remitente"));
        setRemitter((String) values.get("remitente"));
        setMessage((String) values.get("mensaje"));
        setState("No Leido");
        Map<String, Object> dateValue = (Map<String, Object>) values.get("fecha");
        date = new Date(dateValue);
    }

    public String getMailRemitter() {
        return mailRemitter;
    }

    public void setMailRemitter(String mailRemitter) {
        this.mailRemitter = mailRemitter;
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

    public String show () {
        return getMailRemitter() + ", " + getRemitter() + ", " + getMessage() + ", " + getState() +
                ", " + getDate();
    }

    @Override
    public String toString() {
        return remitter;
    }

    public boolean equals (Message message) {
        boolean same = false;
        if ((mailRemitter.equals(message.getMailRemitter())) &&
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
        dest.writeString(mailRemitter);
        dest.writeString(remitter);
        dest.writeString(message);
        dest.writeString(state);
        dest.writeValue(date);
    }

    public void readFromParcel(Parcel in) {
        mailRemitter = in.readString();
        remitter = in.readString();
        message = in.readString();
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
