package org.telosystools.saas.exception;

/**
 * Created by Adrian on 20/04/15.
 */
public class ProjectNotFoundException extends Exception {
    public ProjectNotFoundException(String projectId) {
        super("The project " + projectId + " does not exist.");
    }
}
