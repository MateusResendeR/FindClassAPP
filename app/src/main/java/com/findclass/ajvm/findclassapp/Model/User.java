package com.findclass.ajvm.findclassapp.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class User {
    private DatabaseReference db;

    private String email;
    private String name;
    private String surname;
    private String cpf;
    private String telephony;
    private String bithdate;
    private String professor;
    private String verified;

    public User(String email, String name, String surname, String cpf,
                String bithdate, String telephony, Boolean professor) {
        this.db = FirebaseDatabase.getInstance().getReference();
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.cpf = cpf;
        this.telephony = telephony;
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
        return this.telephony;
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
