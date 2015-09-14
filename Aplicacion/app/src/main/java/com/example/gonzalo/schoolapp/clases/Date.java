package com.example.gonzalo.schoolapp.clases;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Map;

/**
 * Created by Gonzalo on 21/05/2015.
 */
public class Date implements Parcelable, Comparable<Date>{

    private long day;
    private long month;
    private long year;
    private long hour;
    private long minutes;

    public Date() {}

    public Date (Map<String, Object> values) {
        setDay((long) values.get("dia"));
        setMonth((long) values.get("mes"));
        setYear((long) values.get("anno"));
        setHour((long) values.get("hora"));
        setMinutes((long) values.get("minutos"));//*/
    }

    public Date (long day, long month, long year, long hour, long minutes) {
        setDay(day);
        setMonth(month);
        setYear(year);
        setHour(hour);
        setMinutes(minutes);
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    public String toString () {
        return (getDay()+"/"+getMonth()+"/"+getYear()+" - "+getHour()+":"+getMinutes());
    }

    public boolean equals(Date date) {
        boolean same = false;
        if ((day == date.getDay()) && (month == date.getMonth()) && (year == date.getYear())) {
            if ((hour == date.hour) && (minutes == date.minutes)) {
                same = true;
            }//hora
        }//fecha
        return same;
    }

    //*****Parte de la interfaz Comparable*****//
    @Override
    public int compareTo (Date date) {
        int result = 0;
        if (this.year < date.getYear()) { result = -1; }
        else if (this.year > date.getYear()) { result = 1; } else {
            if (this.month < date.getMonth()) { result = -1; }
            else if (this.month > date.getMonth()) { result = 1; } else {
                if (this.day < date.getDay()) { result = -1; }
                else if (this.day > date.getDay()) { result = 1; } else {
                    if (this.hour < date.getHour()) { result = -1; }
                    else if (this.hour > date.getHour()) { result = 1; } else {
                        if (this.minutes < date.getMinutes()) { result = -1; }
                        else if (this.minutes > date.getMinutes()) { result = 1; } else {
                            result = 0;
                        }//if else minute
                    }//else if hour
                }//if else day
            }//if else month
        }//if else year
        return result;
    }

    //*****Parte de la interfaz Parcelable*****//
    public Date(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(day);
        dest.writeLong(month);
        dest.writeLong(year);
        dest.writeLong(hour);
        dest.writeLong(minutes);
    }

    public void readFromParcel(Parcel in) {
        day = in.readLong();
        month = in.readLong();
        year = in.readLong();
        hour = in.readLong();
        minutes = in.readLong();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator () {
        @Override
        public Date createFromParcel (Parcel in) {
            return new Date(in);
        }

        @Override
        public Date[] newArray(int size) {
            return new Date[size];
        }
    };//Parcelable.creator*/
}
