package com.findclass.ajvm.findclassapp.Model;

import java.io.Serializable;

public class Time implements Serializable {
    private String startTime;
    private String endTime;
    private String day;

    public Time() {
    }

    public Time(String startTime, String endTime, String day) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
