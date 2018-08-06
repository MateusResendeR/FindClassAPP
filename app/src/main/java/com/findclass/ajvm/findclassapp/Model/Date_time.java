package com.findclass.ajvm.findclassapp.Model;

import java.io.Serializable;

public class Date_time implements Serializable {
    private Time time;
    private String date;
    private String day;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Date_time() {
    }

    public Date_time(Time time, String date, String day, String status) {
        this.time = time;
        this.date = date;
        this.day = day;
        this.status = status;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
