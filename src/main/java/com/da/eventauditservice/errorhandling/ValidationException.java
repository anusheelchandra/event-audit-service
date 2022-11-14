package com.da.eventauditservice.errorhandling;

public class ValidationException extends Exception{

    public ValidationException(String message) {
        super(message);
    }
}
