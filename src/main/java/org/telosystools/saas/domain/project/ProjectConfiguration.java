package org.telosystools.saas.domain.project;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marion Bechennec
 */
public class ProjectConfiguration {

    private ProjectConfigPkg packages;
    private ProjectConfigFolders folders;
    private Map<String, String> variables;

    public ProjectConfiguration() {
        packages = new ProjectConfigPkg();
        folders = new ProjectConfigFolders();
        variables = new HashMap<>();
    }

    public ProjectConfigPkg getPackages() {
        return packages;
    }

    public void setPackages(ProjectConfigPkg packages) {
        this.packages = packages;
    }

    public ProjectConfigFolders getFolders() {
        return folders;
    }

    public void setFolders(ProjectConfigFolders folders) {
        this.folders = folders;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

}
