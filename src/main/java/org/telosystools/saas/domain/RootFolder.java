package org.telosystools.saas.domain;

import org.springframework.data.annotation.Id;

/**
 * Created by luchabou on 27/02/2015.
 */
public class RootFolder extends Folder {

    public static final String ID_PREFIX = "rootFolder:";

    @Id
    private final String id;

    public RootFolder(String name) {
        super(name);
        this.id = ID_PREFIX + this.getName();
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RootFolder)) return false;

        RootFolder that = (RootFolder) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
