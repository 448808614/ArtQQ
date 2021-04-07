package org.artqq.exceptions;

/**
 * @author luoluo
 */
public class UnknownException extends RuntimeException {
    public UnknownException(String str) {
        super(str);
    }

    @Override
    public String toString() {
        return "UnknownException: " + super.getMessage();
    }
}

