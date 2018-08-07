package com.findclass.ajvm.findclassapp.Exception;

public class FieldLenghtException extends Exception {
    @Override
    public String getMessage(){
        return "Algum campo está inválido!";
    }
}
