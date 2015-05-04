package org.telosystools.saas.exception;

/**
 * @author Adrian
 */
public class InvalidPathException extends Exception {
    public InvalidPathException(String path) {
        super("The path " + path + " contains invalid characters.");
    }
}
