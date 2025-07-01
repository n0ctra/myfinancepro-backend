package com.api.myFinancePro.exception;

/**
 * @author alej0nt
 */
public class EmailAlreadyExistsException extends Exception {
    public EmailAlreadyExistsException() {
        super("The email already exists.");
    }
}
