package com.findclass.ajvm.findclassapp.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    private DatabaseReference db;
    private String email;
    private String name;
    private String surname;
    private String cpf;
    private String telephone;
    private String bithdate;
    private String professor;
    private String verified;

    public User() {
    }

    public User(String email, String name, String surname, String cpf,
                String bithdate, String telephone, Boolean professor) {
        this.db = FirebaseDatabase.getInstance().getReference();
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

    public boolean isNotNull(){
        if(this.getName() == null){
            return false;
        }
        return true;
    }

    public void setUser(User user){
        this.setEmail(user.getEmail());
        this.setName(user.getName());
        this.setSurname(user.getSurname());
        this.setCpf(user.getCpf());
        this.setTelephone(user.getTelephony());
        this.setBithdate(user.getBirthdate());
        this.setProfessor(user.getProfessor());
        this.setVerified(user.getVerified());
    }
}
