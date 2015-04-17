package org.telosystools.saas.service;


import org.telosystools.saas.domain.Project;
import org.telosystools.saas.domain.ProjectConfiguration;
import org.telosystools.saas.domain.User;

import java.util.List;

/**
 * Created by Adrian on 29/01/15.
 */
public interface ProjectService {

    List<Project> list();

    Project loadProject(String id);

    void delete(String id);

    Project createProject(Project project, String userId);

    void updateProjectConfig(String projectId, ProjectConfiguration projectConfig);
}
