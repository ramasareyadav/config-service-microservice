package com.example.addressservice.exception;

/** Thrown when an address (or other resource) cannot be found -> HTTP 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, String field, Object value) {
        super(resource + " not found with " + field + " : " + value);
    }
}
