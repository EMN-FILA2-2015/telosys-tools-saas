package org.telosystools.saas.domain;

import org.springframework.data.annotation.Id;

/**
 * Created by Adrian on 29/01/15.
 */
public class Project {

    @Id
    private String id;

    private String name;

    public Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Project[id=%s, name='%s']", id, name);
    }
}
