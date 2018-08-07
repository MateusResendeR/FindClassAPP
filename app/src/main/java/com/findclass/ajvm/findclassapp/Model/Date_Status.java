package com.findclass.ajvm.findclassapp.Model;


public class Date_Status {
    private String date;
    private String status;

    public Date_Status(String date, String status) {
        this.date = date;
        this.status = status;
    }

    public Date_Status(Date_Status date_status) {
        this.date = date_status.getDate();
        this.status = date_status.getStatus();
    }

    public Date_Status(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
