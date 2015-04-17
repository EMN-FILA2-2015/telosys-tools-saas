package org.telosystools.saas.domain;

import java.util.Map;

/**
 * @author Marion Bechennec
 */
public class ProjectConfiguration {

    private Map<String, String> packages;
    private Map<String, String> folders;
    private Map<String, String> variables;

    public ProjectConfiguration() {
    }

    public Map<String, String> getPackages() {
        return packages;
    }

    public void setPackages(Map<String, String> packages) {
        this.packages = packages;
    }

    public Map<String, String> getFolders() {
        return folders;
    }

    public void setFolders(Map<String, String> folders) {
        this.folders = folders;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }
}
