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
}
