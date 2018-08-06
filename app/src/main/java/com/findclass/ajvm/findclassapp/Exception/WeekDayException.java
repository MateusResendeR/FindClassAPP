package com.findclass.ajvm.findclassapp.Exception;

public class WeekDayException extends Exception {
    @Override
    public String getMessage(){
        return "Dia da semana inválido!";
    }
}
