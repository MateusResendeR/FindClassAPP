package com.findclass.ajvm.findclassapp.Exception;

public class InvalidTimeException extends Exception {
    @Override
    public String getMessage(){
        return "Você digitou algum horário errado!";
    }
}
