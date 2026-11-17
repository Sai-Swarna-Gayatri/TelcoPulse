package com.ssg.telcopulse.exception;

public class AddonPackageNotFoundException extends RuntimeException{

    public AddonPackageNotFoundException(Long id)
    {
        super("Add-on package not found with ID: " + id);
    }

    public AddonPackageNotFoundException(String message)
    {
        super(message);
    }

}
