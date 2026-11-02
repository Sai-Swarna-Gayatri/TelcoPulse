package com.ssg.telcopulse.exception;

public class InvalidSearchParameterException extends RuntimeException {

    public InvalidSearchParameterException(String unknownParams) {
        super(unknownParams);
    }

}
