package com.findclass.ajvm.findclassapp.Model;

import java.io.Serializable;

public class Subject implements Serializable {
    private String id;
    private String name;
    private String level;

    public Subject() {
    }

    public Subject(String id, String name, String level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setSubject(Subject subject){
        this.setId(subject.getId());
        this.setLevel(subject.getLevel());
        this.setName(subject.getName());
    }
}
