package org.telosystools.saas.service;


import org.telosystools.saas.domain.Project;
import org.telosystools.saas.domain.ProjectConfiguration;
import org.telosystools.saas.exception.DuplicateProjectNameException;
import org.telosystools.saas.exception.UserNotFoundException;
import org.telosystools.saas.exception.ProjectNotFoundException;

import java.util.List;

/**
 * Created by Adrian on 29/01/15.
 */
public interface ProjectService {

    /**
     * Find all the projects related to the connected user,
     * i.e. the projects he owns and those he contributes to.
     *
     * @return a list of projects
     */
    List<Project> findAllByUser() throws UserNotFoundException;

    /**
     * Return the project with the specified id
     *
     * @param id project's mongo id
     * @return Project if exists, null otherwise
     */
    Project loadProject(String id) throws ProjectNotFoundException;

    /**
     * Delete the project and its workspace.
     *
     * @param id the project's mongo id
     */
    void deleteProject(String id);

    /**
     * Create a new Project for the connected user.
     *
     * @param project the new Project
     * @return a Project instance with its unique id.
     */
    Project createProject(Project project) throws DuplicateProjectNameException;

    /**
     * Set the project configuration.
     *
     * @param projectId projectid
     * @param projectConfig the new configuration
     */
    void updateProjectConfig(String projectId, ProjectConfiguration projectConfig) throws ProjectNotFoundException;
}
