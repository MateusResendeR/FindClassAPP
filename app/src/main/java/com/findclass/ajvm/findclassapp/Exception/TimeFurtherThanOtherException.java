package com.findclass.ajvm.findclassapp.Exception;

public class TimeFurtherThanOtherException extends Exception {
    @Override
    public String getMessage(){
        return "Horário final maior que horário de início!";
    }
}
