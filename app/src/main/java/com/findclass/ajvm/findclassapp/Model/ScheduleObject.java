package com.findclass.ajvm.findclassapp.Model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduleObject implements Serializable, Comparable<ScheduleObject>{
    private User professor;
    private User student;
    private Subject subject;
    private Time time;
    private Date_Status date;
    private String rating;
    private String id;
    private int cancel;

    public ScheduleObject(User professor, User student, Subject subject, Time time, Date_Status date, String id, int cancel) {
        this.professor = professor;
        this.student = student;
        this.subject = subject;
        this.time = time;
        this.date = date;
        this.id = id;
        this.cancel = cancel;
    }

    public ScheduleObject(String rating,String id,User professor, User student, Subject subject, Time time, Date_Status date,int cancel) {
        this.professor = professor;
        this.student = student;
        this.subject = subject;
        this.rating = rating;
        this.id = id;
        this.date =date;
        this.time = time;
        this.cancel = cancel;
    }

    public int getCancel() { return cancel; }

    public void setCancel(int cancel) { this.cancel = cancel; }

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

    @Override
    public int compareTo(@NonNull ScheduleObject scheduleObject) {
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            if (sdf.parse(getDate().getDate()) == null || sdf.parse(scheduleObject.getDate().getDate()) == null) {
                return 0;
            }
            else if (getDate().getDate().equals(scheduleObject.getDate().getDate())){
                return getTime().getStartTime().compareTo(scheduleObject.getTime().getStartTime());
            }
            return sdf.parse(getDate().getDate()).compareTo(sdf.parse(scheduleObject.getDate().getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
