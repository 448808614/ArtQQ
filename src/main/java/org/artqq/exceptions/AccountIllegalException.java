package org.artqq.exceptions;

/**
 * @author luoluo
 */
public class AccountIllegalException extends RuntimeException {
    @Override
    public String toString() {
        return "Account number does not conform to specification";
    }

    public AccountIllegalException() {
    }
}

