package com.findclass.ajvm.findclassapp.Exception;

public class PhoneLenghtException extends Exception {
    @Override
    public String getMessage(){
        return "Telefone inválido!";
    }
}
