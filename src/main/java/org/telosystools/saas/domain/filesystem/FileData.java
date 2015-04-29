package org.telosystools.saas.domain.filesystem;

import java.io.Serializable;

/**
 * Created by Adrian on 20/04/15.
 *
 * Represent a File as seen from the front end : its path and its content.
 */
public class FileData implements Serializable {

    private String path;

    private String content;

    public FileData() {}

    public FileData(String path, String content) {
        this.path = path;
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
