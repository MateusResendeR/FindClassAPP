package com.findclass.ajvm.findclassapp.Model;

import java.io.Serializable;

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

    public ScheduleObject(User professor, User student, Subject subject, String rating, String id) {
        this.professor = professor;
        this.student = student;
        this.subject = subject;
        this.rating = rating;
        this.id = id;
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
