package org.telosystools.saas.domain.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * Created by Adrian on 29/01/15.
 */
@Document(collection = "projects")
public class Project implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String owner;

    @Field("config")
    @JsonIgnore
    private ProjectConfiguration projectConfiguration;

    @JsonIgnore
    private DefaultVariables defaultVariables;

    public Project() {
        defaultVariables = new DefaultVariables();
    }

    public String getId() {
        return id.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
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

    public DefaultVariables getDefaultVariables() {
        return defaultVariables;
    }

    @Override
    public String toString() {
        return String.format("Project[id=%s, name='%s']", id, name);
    }

}