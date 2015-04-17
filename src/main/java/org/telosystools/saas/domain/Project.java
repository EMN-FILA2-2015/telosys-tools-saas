package org.telosystools.saas.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Created by Adrian on 29/01/15.
 */
public class Project implements Serializable{

    @Id
    private ObjectId id;

    private String name;

    private String description;

    private ObjectId ownerId;

    private ProjectConfiguration projectConfiguration;

    public Project() {}

    public Project(String name) {
        this.name = name;
    }

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectConfiguration getProjectConfiguration() {
        return projectConfiguration;
    }

    public void setProjectConfiguration(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }

    @Override
    public String toString() {
        return String.format("Project[id=%s, name='%s']", id, name);
    }
}
