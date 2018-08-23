package com.findclass.ajvm.findclassapp.Exception;

public class MinAgeException extends Exception {
    @Override
    public String getMessage() {
        return "É preciso ter ao menos 10 anos para pode se cadastrar.";
    }
}
