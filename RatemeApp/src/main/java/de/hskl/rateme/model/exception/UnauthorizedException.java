package de.hskl.rateme.model.exception;

public class UnauthorizedException extends IllegalAccessException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
