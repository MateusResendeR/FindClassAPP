package com.findclass.ajvm.findclassapp.Exception;

public class CanNotCancelException extends Exception {
    @Override
    public String getMessage() {
        return "Impossivel cancelar uma aula com menos de 48h para a hora marca!";
    }
}
