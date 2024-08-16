package com.mhayes.parchment_recipes_web.exception;

public class GoogleIdTokenValidationException extends RuntimeException {

    public GoogleIdTokenValidationException() {
        super("Google ID token validation failed");
    }
    public GoogleIdTokenValidationException(Exception trace) {
        super("Google ID token validation failed", trace);
    }
}
