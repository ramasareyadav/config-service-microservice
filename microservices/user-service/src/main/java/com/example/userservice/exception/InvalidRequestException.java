package com.example.userservice.exception;

/** Thrown when the request is syntactically valid but semantically wrong -> HTTP 400. */
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
