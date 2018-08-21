package com.findclass.ajvm.findclassapp.Exception;

public class LenghtException extends Exception {
    @Override
    public String getMessage() {
        return "Campo/campos com tamanhos inválidos!";
    }
}
