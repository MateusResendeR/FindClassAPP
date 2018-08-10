package com.findclass.ajvm.findclassapp.Exception;

public class TimeLenghtException extends Exception {
    @Override
    public String getMessage(){
        return "Algum horário inválido!";
    }
}
