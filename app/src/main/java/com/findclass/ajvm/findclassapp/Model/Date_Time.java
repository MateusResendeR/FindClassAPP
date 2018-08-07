package com.findclass.ajvm.findclassapp.Model;

import java.io.Serializable;

public class Date_Time {
    private String time_id;
    private String date_id;
    private String day;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Date_Time() {
    }

    public Date_Time(String time_id, String date_id, String day, String status) {
        this.time_id = time_id;
        this.date_id = date_id;
        this.day = day;
        this.status = status;
    }

    public String getTime_id() {
        return time_id;
    }

    public void setTime_id(String time_id) {
        this.time_id = time_id;
    }

    public String getDate_id() {
        return date_id;
    }

    public void setDate_id(String date_id) {
        this.date_id = date_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
