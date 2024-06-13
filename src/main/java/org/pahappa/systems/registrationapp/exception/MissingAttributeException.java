package org.pahappa.systems.registrationapp.exception;

public class MissingAttributeException extends Exception{
    public MissingAttributeException(String failedToFetchUsers, Exception e) {
        super(failedToFetchUsers, e);

    }
}
