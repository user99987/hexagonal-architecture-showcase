package com.cp.ecommerce.adapter.common.exception;

/**
 * Exception that should be thrown once business rule is violated.
 */
public class BusinessRuleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BusinessRuleException(final String message) {

        super(message);
    }

}
