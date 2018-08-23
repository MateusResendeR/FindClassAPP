package com.findclass.ajvm.findclassapp.Model;

import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduleObject implements Serializable {
    private User professor;
    private User student;
    private Subject subject;
    private Time time;
    private Date_Status date;
    private String rating;
    private String id;

    public ScheduleObject(User professor, User student, Subject subject, Time time, Date_Status date, String id) {
        this.professor = professor;
        this.student = student;
        this.subject = subject;
        this.time = time;
        this.date = date;
        this.id = id;
    }

    public ScheduleObject(String rating,String id,User professor, User student, Subject subject, Time time, Date_Status date) {
        this.professor = professor;
        this.student = student;
        this.subject = subject;
        this.rating = rating;
        this.id = id;
        this.date =date;
        this.time = time;
    }

    public Date dateTimeUnion(String dateString,String time){
        Date data = new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            data = new Date();
            try {
                data = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String oldData = dateFormat.format(data)+"-"+time;
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy-HH:mm");
            Date dataTime = new Date();
            try {
                dataTime = sdf2.parse(oldData);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        catch (Exception e){
            e.getStackTrace();
        }
        return data;
    }



    public User getProfessor() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor = professor;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Date_Status getDate() {
        return date;
    }

    public void setDate(Date_Status date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
