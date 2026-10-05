package com.example.userservice.exception;

/** Thrown when the resource already exists (e.g. email) -> HTTP 409. */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
