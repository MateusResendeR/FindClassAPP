package com.findclass.ajvm.findclassapp.Exception;

public class CanNotCancelACancelScheduleException extends Exception {
    @Override
    public String getMessage() {
        return "Essa aula já foi cancelada, mas você pode finaliza-la.";
    }
}

