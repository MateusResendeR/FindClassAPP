package com.findclass.ajvm.findclassapp.Model;

import java.io.Serializable;

public class Schedule implements Serializable {
    String datetime_id;
    String professor_id;
    String subject_id;
    String student_id;

    public Schedule(String datetime_id, String professor_id, String subject_id, String student_id) {
        this.datetime_id = datetime_id;
        this.professor_id = professor_id;
        this.subject_id = subject_id;
        this.student_id = student_id;
    }
    public Schedule() {
    }

    public String getDatetime_id() {
        return datetime_id;
    }

    public void setDatetime_id(String datetime_id) {
        this.datetime_id = datetime_id;
    }

    public String getProfessor_id() {
        return professor_id;
    }

    public void setProfessor_id(String professor_id) {
        this.professor_id = professor_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }
}
