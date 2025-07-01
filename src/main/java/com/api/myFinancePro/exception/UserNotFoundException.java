package com.api.myFinancePro.exception;

/**
 * @author alej0nt
 */
public class UserNotFoundException extends Exception{
    public UserNotFoundException() {
        super("User not found.");
    }
}
