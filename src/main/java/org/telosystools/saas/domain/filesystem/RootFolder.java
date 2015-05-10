package org.telosystools.saas.domain.filesystem;

import org.springframework.data.annotation.Id;

/**
 * Created by luchabou on 27/02/2015.
 *
 * Updated by adrian : add readOnly parameter.
 */
public class RootFolder extends Folder {

    public static final String ID_PREFIX = "rootFolder:";

    @Id
    private String id;

    private boolean readOnly;

    public RootFolder(String name) {
        super(name);
        this.id = ID_PREFIX + this.getName();
    }

    public RootFolder(String name, boolean readOnly) {
        super(name);
        this.id = ID_PREFIX + this.getName();
        this.readOnly = readOnly;
    }

    public RootFolder() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RootFolder)) return false;

        RootFolder that = (RootFolder) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
