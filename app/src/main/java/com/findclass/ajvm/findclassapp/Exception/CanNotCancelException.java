package com.findclass.ajvm.findclassapp.Exception;

public class CanNotCancelException extends Exception {
    @Override
    public String getMessage() {
        return "Impossível cancelar este agendamento!";
    }
}
