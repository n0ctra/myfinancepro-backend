package com.api.myFinancePro.exception;

/**
 * @author alej0nt
 */
public class UsernameAlreadyExistsException extends Exception {
    public UsernameAlreadyExistsException() {
        super("The username already exists.");
    }
}
