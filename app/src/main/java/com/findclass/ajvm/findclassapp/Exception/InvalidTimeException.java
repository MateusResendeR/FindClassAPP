package com.findclass.ajvm.findclassapp.Exception;

public class InvalidTimeException extends Exception {
    @Override
    public String getMessage(){
        return "Alguma data está inválida!";
    }
}
