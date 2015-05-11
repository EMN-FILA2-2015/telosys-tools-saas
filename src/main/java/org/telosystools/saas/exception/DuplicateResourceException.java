package org.telosystools.saas.exception;

/**
 * @author Adrian
 */
public class DuplicateResourceException extends Exception {

    public DuplicateResourceException(String message) {
        super("A resource with the path : " + message + " already exists");
    }
}
