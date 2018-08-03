package com.findclass.ajvm.findclassapp.Model;

public class Professor_Time {
    private String professorUid;
    private String timeId;

    public Professor_Time() { }

    public Professor_Time(String professorUid, String timeId) {
        this.professorUid = professorUid;
        this.timeId = timeId;
    }

    public String getProfessorUid() {
        return professorUid;
    }

    public void setProfessorUid(String professorUid) {
        this.professorUid = professorUid;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }
}
