package com.findclass.ajvm.findclassapp.Exception;

public class MaxAgeException extends Exception {
    @Override
    public String getMessage() {
        return "Se você realmente nasceu nessa data favor, entrar em contato por: findclass.contato@gmail.com";
    }
}
