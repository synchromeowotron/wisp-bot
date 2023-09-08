package org.mogwai.wisp.exceptions;

public class ModelLoadFailureException extends Exception {
    public ModelLoadFailureException(String errorMessage) {
        super(errorMessage);
    }

    public ModelLoadFailureException(Exception exception) {
        super(exception);
    }
}
