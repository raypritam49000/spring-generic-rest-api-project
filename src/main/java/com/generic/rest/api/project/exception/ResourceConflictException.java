package com.generic.rest.api.project.exception;

public class ResourceConflictException extends RuntimeException{
    public ResourceConflictException() {
    }

    public ResourceConflictException(String message) {
        super(message);
    }
}
