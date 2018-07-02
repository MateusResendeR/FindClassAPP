package com.findclass.ajvm.findclassapp.Exception;

public class CPFLenghtException extends Exception {
    @Override
    public String getMessage() {
        return "CPF inválido";
    }
}
