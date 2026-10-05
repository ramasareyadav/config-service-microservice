package com.example.userservice.exception;

/** Thrown when a downstream service (address-service) cannot be reached -> HTTP 503. */
public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
