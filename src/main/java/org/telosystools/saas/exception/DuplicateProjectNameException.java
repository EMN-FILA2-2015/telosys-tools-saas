package org.telosystools.saas.exception;

/**
 * @author Marion Bechennec
 */
public class DuplicateProjectNameException extends Exception {
    public DuplicateProjectNameException(String projectName) {
        super("A project with the name \"" + projectName + "\" already exists.");
    }
}
