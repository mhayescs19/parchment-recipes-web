package com.mhayes.parchment_recipes_web.exception;

public class DuplicateUserException extends Exception {
    public DuplicateUserException(String email) {
        super("user exists for email" + email);
    }
}
