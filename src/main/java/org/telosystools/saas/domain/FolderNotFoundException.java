package org.telosystools.saas.domain;

/**
 * @author Marion Bechennec
 */
public class FolderNotFoundException extends Exception {

    public FolderNotFoundException(String folderPath, String project) {
        super("The folder "+folderPath+" cannot be found in the project "+project);
    }
}
