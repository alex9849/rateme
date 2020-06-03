package de.hskl.rateme.model.exception;

public class ValidationException extends IllegalArgumentException {

    public ValidationException(String message) {
        super(message);
    }

}
