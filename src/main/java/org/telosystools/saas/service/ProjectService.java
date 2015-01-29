package org.telosystools.saas.service;


import org.telosystools.saas.domain.Project;
import java.util.List;

/**
 * Created by Adrian on 29/01/15.
 */
public interface ProjectService {

    List<Project> list();

    Project find(String id);

    void delete(String id);

    Project insert(Project project);
}
