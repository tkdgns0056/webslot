package com.example.slotweb.exception;

public
class RequestNotFoundException extends RuntimeException {

    public RequestNotFoundException() {
        super();
    }

    public RequestNotFoundException(String s) {
        super(s);
    }

    public RequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestNotFoundException(Throwable cause) {
        super(cause);
    }

}