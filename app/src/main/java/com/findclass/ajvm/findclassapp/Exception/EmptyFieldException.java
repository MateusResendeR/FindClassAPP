package com.findclass.ajvm.findclassapp.Exception;

public class EmptyFieldException extends Exception {
    @Override
    public String getMessage(){
        return "Campos em branco não são permitidos.";
    }
}
