package com.findclass.ajvm.findclassapp.Exception;

public class DateLenghtException extends Exception {
    @Override
    public String getMessage(){
        return "Data de nascimento inválida!";
    }
}
