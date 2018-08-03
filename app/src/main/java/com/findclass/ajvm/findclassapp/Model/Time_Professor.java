package com.findclass.ajvm.findclassapp.Model;

public class Time_Professor {
    private User user;
    private Time time;
    private Professor_Time professorTime;

    public Time_Professor() { }

    public Time_Professor(User user, Time time, Professor_Time professorTime) {
        this.user = user;
        this.time = time;
        this.professorTime = professorTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Professor_Time getProfessorTime() {
        return professorTime;
    }

    public void setProfessorTime(Professor_Time professorTime) {
        this.professorTime = professorTime;
    }
}
