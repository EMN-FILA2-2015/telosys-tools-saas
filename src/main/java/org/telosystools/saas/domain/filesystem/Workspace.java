package org.telosystools.saas.domain.filesystem;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by luchabou on 27/02/2015.
 */
public class Workspace implements Serializable {

    public static final String MODELS = "models";
    public static final String TEMPLATES = "templates";
    public static final String GENERATEDS = "generateds";
    public static final String SETTINGS = "settings";

    /**
     * Root folders
     */
    private final Map<String,RootFolder> rootFoldersByNames = new TreeMap<>();

    public RootFolder getRootFolderByName(String name) {
        return rootFoldersByNames.get(name);
    }

    public RootFolder getModels() {
        return rootFoldersByNames.get(MODELS);
    }

    public void setModels(RootFolder models) {
        rootFoldersByNames.put(MODELS, models);
    }

    public RootFolder getTemplates() {
        return rootFoldersByNames.get(TEMPLATES);
    }

    public void setTemplates(RootFolder templates) {
        this.rootFoldersByNames.put(TEMPLATES, templates);
    }

    public RootFolder getGenerateds() {
        return rootFoldersByNames.get(GENERATEDS);
    }

    public void setGenerateds(RootFolder generateds) {
        this.rootFoldersByNames.put(GENERATEDS, generateds);
    }

    public RootFolder getSettings() {
        return rootFoldersByNames.get(SETTINGS);
    }

    public void setSettings(RootFolder settings) {
        this.rootFoldersByNames.put(SETTINGS, settings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workspace)) return false;

        Workspace workspace = (Workspace) o;

        if (!rootFoldersByNames.equals(workspace.rootFoldersByNames)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return rootFoldersByNames.hashCode();
    }
}
