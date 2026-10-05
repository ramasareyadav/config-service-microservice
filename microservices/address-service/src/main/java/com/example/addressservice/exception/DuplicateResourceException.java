package com.example.addressservice.exception;

/** Thrown when the resource already exists -> HTTP 409. */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
