package org.telosystools.saas.exception;

/**
 * @author Marion Bechennec
 */
public class GridFSFileNotFoundException extends Exception {
    public GridFSFileNotFoundException(String gridFSId) {
        super("The file " + gridFSId + " cannot be found in GridFS");
    }
}
