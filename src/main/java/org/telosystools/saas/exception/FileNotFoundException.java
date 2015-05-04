package org.telosystools.saas.exception;

/**
 * @author Marion Bechennec
 */
public class FileNotFoundException extends Exception {
    public FileNotFoundException(String gridFSId) {
        super("The file " + gridFSId + " cannot be found in GridFS");
    }
}
