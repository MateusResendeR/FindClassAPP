package com.findclass.ajvm.findclassapp.Model;

public class ScheduleObject {
    private User professor;
    private User student;
    private Subject subject;

    public ScheduleObject(User professor, User student, Subject subject) {
        this.professor = professor;
        this.student = student;
        this.subject = subject;
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
