package org.telosystools.saas.exception;

/**
 * @author Marion Bechennec
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(String login) {
        super("The user " + login + " cannot be found.");
    }
}
