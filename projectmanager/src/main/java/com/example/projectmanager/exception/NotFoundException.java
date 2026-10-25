package com.example.projectmanager.exception;

/** Thrown when an id in the URL or the body points at nothing; answered with 404. */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String what, Long id) {
        super(what + " " + id + " was not found");
    }
}
