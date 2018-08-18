package com.findclass.ajvm.findclassapp.Model;

import android.support.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.io.Serializable;

public class User implements Serializable,Comparable<User> {
    private String email;
    private String name;
    private String surname;
    private String cpf;
    private String telephone;
    private String bithdate;
    private String professor;
    private String verified;
    private int score;
    private String id;

    public User() {
    }

    public User(String id, String email, String name, String surname,
                String cpf, String bithdate, String telephone, Boolean professor) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.cpf = cpf;
        this.telephone = telephone;
        this.bithdate = bithdate;
        if (professor==true){
            this.professor = "true";
        }else{
            this.professor = "false";
        }
        this.verified = "false";
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = this.score + score;
    }

    public String getEmail(){
        return this.email;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public String getTelephony(){
        return this.telephone;
    }

    public String getBirthdate(){
        return this.bithdate;
    }

    public String getProfessor(){
        return this.professor;
    }

    public String getVerified(){
        return this.verified;
    }

    public String getCpf(){
        return this.cpf;
    }

    public String getId() {
        return this.id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setBithdate(String bithdate) {
        this.bithdate = bithdate;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isNotNull(){
        if(this.getName() == null){
            return false;
        }
        return true;
    }

    public void setUser(User user){
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setName(user.getName());
        this.setSurname(user.getSurname());
        this.setCpf(user.getCpf());
        this.setTelephone(user.getTelephony());
        this.setBithdate(user.getBirthdate());
        this.setProfessor(user.getProfessor());
        this.setVerified(user.getVerified());
    }

    public int compareTo(@NonNull User user) {
        if (this.getScore() > user.getScore()) {
            return -1;
        }
        if (this.getScore() < user.getScore()) {
            return 1;
        }
        return 0;
    }
}
