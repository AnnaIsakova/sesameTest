package com.sesame.test.exception;

import lombok.Data;

import java.util.List;


/**
 * Contains violations that returned by Model validation process
 */
@Data
public class ConstraintException extends Exception {

    private List<String> violations;

    public ConstraintException(List<String> violations) {
        super();
        this.violations = violations;
    }

    public ConstraintException(String message, List<String> violations) {
        super(message);
        this.violations = violations;
    }

    public ConstraintException(String message, Throwable cause, List<String> violations) {
        super(message, cause);
        this.violations = violations;
    }

    public ConstraintException(Throwable cause, List<String> violations) {
        super(cause);
        this.violations = violations;
    }
}
