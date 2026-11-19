package com.ssg.telcopulse.exception;

public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String userName) {
        super("UserName "+ userName +" already exists. Please select a different user name.");
    }

}
