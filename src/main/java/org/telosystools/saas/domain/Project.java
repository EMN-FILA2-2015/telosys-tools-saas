package org.telosystools.saas.domain;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Created by Adrian on 29/01/15.
 */
public class Project implements Serializable{

    public static final String ID = "project";

    @Id
    private final String id = ID;

    private String name;

    public Project() {}

    public Project(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Project[id=%s, name='%s']", id, name);
    }
}
