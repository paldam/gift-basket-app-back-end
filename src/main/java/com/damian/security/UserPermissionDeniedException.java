package com.damian.security;

public class UserPermissionDeniedException extends Exception {

    public UserPermissionDeniedException(String message) {
        super(message);
    }
}

