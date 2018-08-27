package com.findclass.ajvm.findclassapp.Exception;

public class AlreadyRegisteredTimeException extends Exception {
    @Override
    public String getMessage() {
        return "Este horário contém ou está presente em algum intervalo de horários já cadastrado!";
    }
}
