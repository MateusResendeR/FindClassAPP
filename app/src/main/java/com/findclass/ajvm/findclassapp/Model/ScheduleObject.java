package com.findclass.ajvm.findclassapp.Model;

import java.io.Serializable;

public class ScheduleObject implements Serializable {
    private User professor;
    private User student;
    private Subject subject;
    private String rating;
    private String id;

    public ScheduleObject(String rating,String id,User professor, User student, Subject subject) {
        this.professor = professor;
        this.student = student;
        this.subject = subject;
        this.rating = rating;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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
}
