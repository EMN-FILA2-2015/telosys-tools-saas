package org.telosystools.saas.domain.filesystem;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by luchabou on 27/02/2015.
 *
 * The workspace.
 */
public class Workspace implements Serializable {

    public static final String MODEL = "model";
    public static final String TEMPLATES = "templates";
    public static final String GENERATED = "generated";

    /**
     * Root folders
     */
    private final Map<String,RootFolder> rootFoldersByNames = new TreeMap<>();

    public RootFolder getRootFolderByName(String name) {
        return rootFoldersByNames.get(name);
    }

    public RootFolder getModel() {
        return rootFoldersByNames.get(MODEL);
    }

    public void setModel(RootFolder models) {
        rootFoldersByNames.put(MODEL, models);
    }

    public RootFolder getTemplates() {
        return rootFoldersByNames.get(TEMPLATES);
    }

    public void setTemplates(RootFolder templates) {
        this.rootFoldersByNames.put(TEMPLATES, templates);
    }

    public RootFolder getGenerated() {
        return rootFoldersByNames.get(GENERATED);
    }

    public void setGenerated(RootFolder generateds) {
        this.rootFoldersByNames.put(GENERATED, generateds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workspace)) return false;

        Workspace workspace = (Workspace) o;

        return rootFoldersByNames.equals(workspace.rootFoldersByNames);

    }

    @Override
    public int hashCode() {
        return rootFoldersByNames.hashCode();
    }
}
