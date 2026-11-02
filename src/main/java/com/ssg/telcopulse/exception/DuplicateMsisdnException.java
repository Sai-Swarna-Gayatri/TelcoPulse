package com.ssg.telcopulse.exception;

public class DuplicateMsisdnException extends RuntimeException{

    public DuplicateMsisdnException(String msisdn) {
        super("Msisdn " + msisdn +" already exists for another subscriber. plz provide another msisdn");
    }  
    
}
