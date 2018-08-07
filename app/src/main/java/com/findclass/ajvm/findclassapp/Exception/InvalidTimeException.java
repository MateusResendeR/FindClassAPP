package com.findclass.ajvm.findclassapp.Exception;

public class InvalidTimeException extends Exception {
    @Override
    public String getMessage(){
        return "Algum horário está inválido!";
    }
}
