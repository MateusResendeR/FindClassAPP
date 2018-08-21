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
    private String telephony;
    private String bithdate;
    private String professor;
    private String verified;
    private int score;
    private String id;
    private Address address;

    public User() {
    }

    public User(String id, String email, String name, String surname,
                String cpf, String bithdate, String telephone, Boolean professor) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.cpf = cpf;
        this.telephony = telephone;
        this.bithdate = bithdate;
        if (professor==true){
            this.professor = "true";
        }else{
            this.professor = "false";
        }
        this.verified = "false";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelephony() {
        return telephony;
    }

    public void setTelephone(String telephone) {
        this.telephony = telephone;
    }

    public String getBirthdate() {
        return bithdate;
    }

    public void setBithdate(String bithdate) {
        this.bithdate = bithdate;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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
