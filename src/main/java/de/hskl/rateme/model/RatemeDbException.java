package de.hskl.rateme.model;

public class RatemeDbException extends RuntimeException {
    public RatemeDbException(String message, Throwable e) {
        super(message, e);
    }

    public RatemeDbException(String message) {
        super(message);
    }
}
