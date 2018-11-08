package com.elementarynl;

public class NotRunningException extends RuntimeException {
    public NotRunningException () {

    }

    public NotRunningException (String message) {
        super (message);
    }

    public NotRunningException (Throwable cause) {
        super (cause);
    }

    public NotRunningException (String message, Throwable cause) {
        super (message, cause);
    }
}
