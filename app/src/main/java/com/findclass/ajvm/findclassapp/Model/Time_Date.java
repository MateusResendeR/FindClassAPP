package com.findclass.ajvm.findclassapp.Model;

public class Time_Date {
    private Date_Status date_status;
    private Time time;
    private Date_Time date_time;

    private User user;

    public Time_Date() { }

    public Time_Date(Date_Status date_status, Time time, Date_Time date_time, User user) {
        this.date_status = date_status;
        this.time = time;
        this.date_time = date_time;
        this.user = user;
    }

    public Date_Status getDate_status() {
        return date_status;
    }

    public void setDate_status(Date_Status date_status) {
        this.date_status = date_status;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date_Time getDate_time() {
        return date_time;
    }

    public void setDate_time(Date_Time date_time) {
        this.date_time = date_time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
