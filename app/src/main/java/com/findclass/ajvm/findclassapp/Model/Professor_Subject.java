package com.findclass.ajvm.findclassapp.Model;

public class Professor_Subject {
    private String professorUid;
    private String subjectId;

    public Professor_Subject() { }

    public Professor_Subject(String professorUid, String subjectId) {
        this.professorUid = professorUid;
        this.subjectId = subjectId;
    }

    public String getProfessorUid() {
        return professorUid;
    }

    public void setProfessorUid(String professorUid) {
        this.professorUid = professorUid;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
