package org.telosystools.saas.service;


import org.springframework.web.client.HttpServerErrorException;
import org.telosystools.saas.domain.Project;
import org.telosystools.saas.domain.ProjectConfiguration;
import org.telosystools.saas.domain.User;

import java.util.List;

/**
 * Created by Adrian on 29/01/15.
 */
public interface ProjectService {

    List<Project> list();

    List<Project> listByUser();

    Project loadProject(String id);

    void delete(String id);

    Project createProject(Project project) throws HttpServerErrorException;

    void updateProjectConfig(String projectId, ProjectConfiguration projectConfig);
}
